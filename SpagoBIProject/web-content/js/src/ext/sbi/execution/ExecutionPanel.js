/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 

Ext.ns("Sbi.execution");

Sbi.execution.ExecutionPanel = function(config, doc) {
	
	
	// declare exploited services
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getDocumentInfoService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_DOCUMENT_INFO_ACTION'
		, baseParams: params
	});
	
	this.document = doc;
	this.documentsStack = [];
	
	var title = config.title;
	var closable = config.closable !== undefined || true;
	if(config.title !== undefined) delete config.title;
	if(config.closable !== undefined) delete config.closable;
	
	this.activeDocument = new Sbi.execution.ExecutionWizard( config, doc );
	this.documentsStack.push( this.activeDocument );
	
	this.activeDocument.on('beforetoolbarinit', this.setBreadcrumbs, this);
	
	this.activeDocument.parametersSelectionPage.on('collapse3', function() {
		sendMessage({}, 'collapse2'); 
	}, this);
	
	this.activeDocument.parametersSelectionPage.on('crossnavigation', this.loadCrossNavigationTargetDocument , this);
	
	var c = Ext.apply({}, config || {}, {
		title: title
		, closable: closable
		, border: false
		, activeItem: 0
		, hideMode: !Ext.isIE ? 'nosize' : 'display'
		, layout: 'card'
		, items: [this.activeDocument]
	});
	
	// constructor
    Sbi.execution.ExecutionPanel.superclass.constructor.call(this, c);
    
    // Workaround: on IE, it takes a long time to destroy the stacked execution wizards.
    // See Sbi.settings.IE.destroyExecutionWizardWhenClosed on Settings.js for more information
    if (!Ext.isIE || (Sbi.settings.IE.destroyExecutionWizardWhenClosed === undefined || Sbi.settings.IE.destroyExecutionWizardWhenClosed === true)) {
	    this.on('beforedestroy', function() {	    	
	    	this.hide();
			for (var i = 0; i < this.documentsStack.length; i++) {
				var temp = this.documentsStack[i];
				this.remove(temp, false);
			}
	    	return true; // now the execution panel can be destroyed
	    }, this);
    }
};

/**
 * @class Sbi.execution.ExecutionPanel
 * @extends Ext.Panel
 * 
 * Contains a stack of executed documents. The stack contains always at least one element (the original document passed
 * to the constructor). Other elements in the stack are document to which the user have cross navigated to. For example if the stack contains 
 * from the bottom to the top the following documents A,B, C that means that the user have executed document A than have cross 
 * navigated to document B and then from document B have cross navigated to document C. Everytime a new cross navigation starts a 
 * document is pushed on the top of the stack. On the other hand every time the user returns from a cross navigation one or more documents
 * are popped from the top of the stack. Usually the active document (i.e. the one visible to the user) is the one at the top of the stack.
 */

/**
 * @cfg {Boolean} title
 * tbd
 */

/**
 * @cfg {String} closable
 * tbd
 */
Ext.extend(Sbi.execution.ExecutionPanel, Ext.Panel, {

	// =================================================================================================================
	// PROPERTIES
	// =================================================================================================================
	/**
     * @property {Object} document
     * The document to be executed
     */
	document: null
	
	/**
     * @property {Array} documentsStack 
     * The stack of executed documents (Sbi.execution.ExecutionWizard)
     */
	, documentsStack: null
	
	
	/**
     * @property {Object} activeDocument
     * The active document (i.e. the one visible to the user). Usually it is the one at the top of the document stack
     */
	, activeDocument: null
	
	/**
     * @property {Object} origDocumentTarget
     */
	, origDocumentTarget: null
	
	
	// =================================================================================================================
	// METHODS
	// =================================================================================================================
	
	/**
	 * Execute the active document
     * @method
     */
	, execute : function() {
		this.activeDocument.execute();
	}

	/**
	 * @method
	 * 
	 * This method is called when a crossnavigation event is fired by the active document.
	 * @param {Object} config An object describing document and execution preferences:
	 * @param {Object} [config.document=0] The document.
	 * @param {Object} [config.preferences=0] The preferences.
	 */
	, loadCrossNavigationTargetDocument: function( config ) {
		Sbi.trace('[ExecutionPanel.loadCrossNavigationTargetDocument: IN');
		Ext.Ajax.request({
	        url: this.services['getDocumentInfoService'],
	        params: {'OBJECT_LABEL' : config.document.label, 'SUBOBJECT_NAME' : config.preferences.subobject.name},
	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if (content !== undefined) {
	      				if (content.documentFound == false) {
	      					Sbi.exception.ExceptionHandler.showWarningMessage('Required document not found', 'Configuration Error');
	      				} else {
	      					if (content.canSeeDocument == false) {
	      						Sbi.exception.ExceptionHandler.showWarningMessage('User cannot see required document', 'Configuration Error');
	      					} else {
	      						config.document = content.document;
	      						config.preferences.shortcutsHidden = true;
	      						if (content.subobject !== undefined && content.subobject != null) {
	      							config.preferences.subobject = content.subobject;
	      						}
	      						this.executeCrossNavigation(config);
	      					}
	      				}
	      			} else {
	      				Sbi.exception.ExceptionHandler.showErrorMessage('Server response cannot be decoded', 'Service Error');
	      			}
	      		} else {
	      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
	   });
		
		Sbi.trace('[ExecutionPanel.loadCrossNavigationTargetDocument: OUT');
		
	}
	
	/**
	 * This method is used as callback for the ajax call made by method loadCrossNavigationTargetDocument. It delegate the 
	 * execution of the destination document to the proper method according to the value of target property
	 * 
	 * @param {Object} config An object describing document and execution preferences:
	 * @param {Object} [config.document=0] The document.
	 * @param {Object} [config.preferences=0] The preferences.
	 * @param {Object} [config.target=0] The target. Can be update, tab, popup or inline. Default inline.
	 * 
	 * @method 
	 */
	, executeCrossNavigation: function( config ) {
		Sbi.trace('[ExecutionPanel.executeCrossNavigation: IN');
		var destinationDocument  = config.document;
		var sourceDocument = this.activeDocument.document;
		var destinationTarget = config.target;
		
		
		destinationDocument.title = config.title || destinationDocument.title;
		var isSourceDocUpdate = (destinationDocument.label === sourceDocument.label);
		
		// validate destinationTarget
		if(destinationTarget === 'update' && isSourceDocUpdate === false ) destinationTarget = 'inline';
		
		if (destinationTarget === 'update') {
			this.executeCrossNavUpdate(config);
		} else if (destinationTarget === 'tab') {
			if(this.fireEvent('crossnavigationonothertab', config) !== false) {
				this.executeCrossNavInline(config);
			}
		} else if (destinationTarget === 'popup') {
			this.executeCrossNavigationPopup(config);		    
		} else {
			this.executeCrossNavInline(config);
		}
		Sbi.trace('[ExecutionPanel.executeCrossNavigation: OUT');
	}
	
	/**
	 * @method 
	 */
	, executeCrossNavUpdate: function(config) {
		
		var params = config.preferences.parameters;
		var subobjId = config.preferences.subobject.id;
		
		var formState;
				
		var activeDocumentExecutionPage = this.activeDocument.parametersSelectionPage;
		var executionInstance = activeDocumentExecutionPage.executionInstance;
			
		activeDocumentExecutionPage.parametersPanel.clear();
			
		try {
			formState = Ext.urlDecode(params);
		} catch (err) {
			alert('Warning: error while decoding parameters: ' + err);
			formState = null;
		}
		
		if (formState !== null) {
			activeDocumentExecutionPage.parametersPanel.setFormState(formState);
			var formStateStr = Sbi.commons.JSON.encode( formState );
			executionInstance.PARAMETERS = formStateStr;
		}

		if (subobjId !== undefined && subobjId !== null) {
			executionInstance.SBI_SUBOBJECT_ID = subobjId;
		} else {
			delete executionInstance.SBI_SUBOBJECT_ID;
		}
		
		activeDocumentExecutionPage.synchronize(executionInstance, false);
	}
	
	/**
	 * @method 
	 */
	, executeCrossNavigationPopup: function(config) {	
		config.preferences.executionToolbarConfig = {};
		config.preferences.executionToolbarConfig.expandBtnVisible = false;
	
		var activeDocument = new Sbi.execution.ExecutionPanel( {preferences: config.preferences, isFromCross: true}, config.document );  	
		
		var popupWin = new Ext.Window({
			layout: 'fit',                	          
			title: config.title,
			width: config.width || 500,
			height: config.height || 300,	           	
			closable: true,
			constrain: true,
			resizable: true, 
			minimizable :false,
			maximizable : false,
			plain: true,
			buttons: [{
        	  text: 'Close',
              handler: function(){
            	popupWin.destroy();
              }
			}]
		});
	    
		popupWin.add(activeDocument);
		popupWin.show();
		activeDocument.execute();
	    popupWin.doLayout();
	}
	
	/**
	 * @method 
	 */
	, executeCrossNavInline: function(config) {
		//save the original document
		var oldDoc = this.activeDocument;
		
		var formState = Ext.urlDecode(config.preferences.parameters);
		for(p in formState) {
			//alert(p + ' = ' + formState[p]);			
		}
		
		this.activeDocument = new Sbi.execution.ExecutionWizard( {preferences: config.preferences, isFromCross: true}, config.document );
		this.documentsStack.push( this.activeDocument );
			
		this.activeDocument.on('beforetoolbarinit', this.setBreadcrumbs, this);
		
		this.activeDocument.parametersSelectionPage.on('collapse3', function() {
			sendMessage({}, 'collapse2'); 
		}, this);
		this.activeDocument.parametersSelectionPage.on('crossnavigation', this.loadCrossNavigationTargetDocument , this);	
		
		
		
		this.add(this.activeDocument);	
		this.getLayout().setActiveItem(this.documentsStack.length -1);	
		
		this.activeDocument.execute();
		
		//send hide message to the hidden console
		oldDoc.parametersSelectionPage.documentPage.miframe.sendMessage('Disable datastore', 'hide');
	}
	
	/**
	 * @method 
	 */
	, setBreadcrumbs: function(tb) {
		
		tb.addSpacer();
		tb.addDom('<image width="12" height="12" src="../themes/sbi_default/img/analiticalmodel/execution/link16x16.gif"></image>');
		tb.addSpacer();
		
		if (this.documentsStack.length > 1) {
			this.addBreadcrumbsMiddleButtons(tb);
		}
		this.addBreadcrumbsLastButton(tb);
	}
	
	/**
	 * @method 
	 */
	, addBreadcrumbsLastButton: function(tb) {
		var index = this.documentsStack.length-1;
		var text = this.documentsStack[index].document.title || this.documentsStack[index].document.name;
		tb.add({
			text: text
			, stackIndex: index
			, disabled: true
			, cls: 'sbi-last-folder'
		    , listeners: {
    			'click': {
              		fn: this.onBreadCrumbClick,
              		scope: this
            	} 
    		}
		});

		//adds the back button
		if (this.documentsStack.length > 1) {
			tb.addSpacer();
			tb.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back'
				, tooltip: LN('sbi.execution.executionpage.toolbar.breadcrumbback')
			    , scope: this
			    , disabled: false
			    , stackIndex: (this.documentsStack.length-2)
			    , listeners: {
	    			'click': {
	              		fn: this.onBreadCrumbClick,
	              		scope: this
	            	} 
	    		}
			}));			
		}
	}
	
	/**
	 * @method 
	 */
	, addBreadcrumbsMiddleButtons: function(tb) {
		
		var truncateLength = this.getTruncateLength(tb);
		for(var i = 0; i < this.documentsStack.length - 1; i++) {
			this.documentsStack[i].document = this.documentsStack[i].document || {};
			var tooltip = this.documentsStack[i].document.title || this.documentsStack[i].document.name;
			var text = tooltip;
			if (truncateLength > 0) {
				text = Ext.util.Format.ellipsis(text, truncateLength);
			}
			tb.add({
				text: text
				, tooltip: tooltip
				, stackIndex: i
			    , listeners: {
        			'click': {
                  		fn: this.onBreadCrumbClick,
                  		scope: this
                	} 
        		}
			});
			tb.addSpacer();
			tb.addDom('<image width="3" height="6" src="../themes/sbi_default/img/analiticalmodel/execution/c-sep.gif"></image>');
			tb.addSpacer();
		}
	}
	
	/**
	 * @method 
	 */
	, getTruncateLength: function(tb) {
		var toReturn = -1;
		var pixelPerChar = 6;
		var tbWidth = tb.getSize().width;
		var breadcrumbsCharLength = this.getBreadcrumbsCharLength();
		var documentsNo = this.documentsStack.length;
		if ( (breadcrumbsCharLength * pixelPerChar) < (tbWidth - 200 - (documentsNo * 20))) { // 200 is for toolbar buttons, documentsNo * 20 is for spaces between documents
			// no need to truncate
			return toReturn;
		}
		var lastDocument = this.documentsStack[documentsNo - 1].document;
		var lastButtonText = lastDocument.title || lastDocument.name;
		var lastButtonWidth = lastButtonText.length * pixelPerChar;
		var middleWidth = tbWidth - lastButtonWidth - 200 - (documentsNo * 20); // 200 is for toolbar buttons, documentsNo * 20 is for spaces between documents
		var maxElementWidth = middleWidth / ( documentsNo - 1 );
		toReturn = Math.floor(maxElementWidth / pixelPerChar);
		return toReturn;
	}
	
	/**
	 * @method 
	 */
	, getBreadcrumbsCharLength: function() {
		var length = 0;
		for(var i = 0; i < this.documentsStack.length; i++) {
			this.documentsStack[i].document = this.documentsStack[i].document || {};
			var text = this.documentsStack[i].document.title || this.documentsStack[i].document.name;
			length += text.length;
		}
		return length;
	}
	
	/**
	 * @method 
	 */
	, onBreadCrumbClick: function(b, e) {
		//send hide message to the old actived console
		if (this.activeDocument && this.activeDocument.parametersSelectionPage){
			this.activeDocument.parametersSelectionPage.documentPage.miframe.sendMessage('Disable datastore!', 'hide');
		}
		var prevActiveDoc =  this.activeDocument;		
		this.activeDocument = this.documentsStack[b.stackIndex];

		//send show message to the new actived console
		if (this.activeDocument.parametersSelectionPage)
			this.activeDocument.parametersSelectionPage.documentPage.miframe.sendMessage('Enable datastore!', 'show');
				
		//this.swapPanel(prevActiveDoc, this.activeDocument);
		
		for(var i = this.documentsStack.length-1; i > b.stackIndex; i--) {
			var el = this.documentsStack.pop();
			this.remove(el, false);

		    // Workaround: on IE, it takes a long time to destroy the stacked execution wizards.
		    // See Sbi.settings.IE.destroyExecutionWizardWhenClosed on Settings.js for more information
			if (!Ext.isIE || (Sbi.settings.IE.destroyExecutionWizardWhenClosed === undefined || Sbi.settings.IE.destroyExecutionWizardWhenClosed === true)) {
		    	el.destroy();
		    }else{
		    	el.hide(); 
		    }
		}
		
		this.getLayout().setActiveItem(b.stackIndex);

		// if browser is IE, re-inject parent.execCrossNavigation function in order to solve parent variable conflict that occurs when 
		// more iframes are built and the same function in injected: it is a workaround that let cross navigation work properly
		if (Ext.isIE) {
			var scriptFn = 	"parent.execCrossNavigation = function(d,l,p,s,ti,t) {" +
							"	sendMessage({'label': l, parameters: p, windowName: d, subobject: s, target: t, title: ti},'crossnavigation');" +
							"};";
			this.activeDocument.parametersSelectionPage.documentPage.miframe.iframe.execScript(scriptFn, true);
		}
	}
	
	// =================================================================================================================
	// EVENTS
	// =================================================================================================================

});
