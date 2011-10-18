/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.ZONE
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * CalculatedFieldEditorPanel - short description
  * 
  * Object documentation ...
  * 
  * by Monica Franceschini
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.CalculatedFieldEditorPanel = function(config) {	
	
	var c = Ext.apply({}, config || {}, {
		layout: 'border',
	    frame: false, 
	    border: false,
	    bodyStyle:'background:#E8E8E8;',
	    style:'padding:3px;'
	});

	Ext.apply(this, c);
	
	this.expertDisable = c.expertDisable;
	
	this.initNorthRegionPanel(c.northRegionConfig || {});
	this.initWestRegionPanel(c.westRegionConfig || {});
	this.initCenterRegionPanel(c.centerRegionConfig || {});
	
	Ext.apply(c, {
		items:  [this.westRegionPanel, this.centerRegionPanel, this.northRegionPanel]
	});	
	
	// constructor
	Sbi.qbe.CalculatedFieldEditorPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.qbe.CalculatedFieldEditorPanel, Ext.Panel, {
	hasBuddy: null
    , buddy: null
    
    , expertDisable: false
   
    , westRegionPanel: null 
    , centerRegionPanel: null
    , northRegionPanel: null
    
    , detailsFormPanel: null
    , expItemsTreeRootNode: null
	, expItemsTree: null
	, expItemsPanel: null
		
	, baseExpression: ''
	, expressionEditor: null
	, expressionEditorPanel: null
	
	, inputFields: null
	, expertCheckBox: null
	
	, target: null
	
	, validationService:  null
	, expertMode:  false
	
	, expItemGroups: null
	, groupRootNodes: null
	, scopeComboBoxData: null	
	
	, opWin: null
	

    // --------------------------------------------------------------------------------------------
    // public methods
    // --------------------------------------------------------------------------------------------
	
	
	, getExpression: function() {
		
		var expression;
		if(this.expressionEditor) {
	  		expression = this.expressionEditor.getValue();
	  		expression = Ext.util.Format.stripTags( expression );
	  		expression = expression.replace(/&nbsp;/g," ");
	  		expression = expression.replace(/\u200B/g,"");
	  		expression = expression.replace(/&gt;/g,">");
	  		expression = expression.replace(/&lt;/g,"<");
	  		
	  		if(!this.expertMode){
	  			expression = this.replaceFieldAliasesWithFieldUniqueNames(expression);
	  		}
	  	}
		
		//alert('getExpression: [' + expression + ']');
		
		return expression;
	}

	


	, setExpression: function(expression) {
		if(this.expressionEditor) {
			if(this.expertMode === undefined || this.expertMode === null || this.expertMode === false){
	  			expression = this.replaceFieldUniqueNamesWithFieldAliases(expression);
	  		}
			
			expression = expression.replace(/ /g,"&nbsp;");
	  		expression = expression.replace(/</g,"&lt;");
	  		expression = expression.replace(/>/g,"&gt;");
	  		
	  		//alert('setExpression: [' + expression + ']');
	  		
	  		this.baseExpression = expression;
	  		if(this.expressionEditor.initialized) {
	  			this.expressionEditor.reset();
  				this.expressionEditor.insertAtCursor( expression );
	  		} 
		}
	}
	
	, replaceFieldAliasesWithFieldUniqueNames: function(expression) {
		var newExpression;
				
		newExpression = expression;
		var fieldNodes = this.groupRootNodes['fields'];
		var childNodes = fieldNodes.childNodes;
		var aliasToUniqueNameMap = new Object();
		var aliasOrderedByLengthList = new Array();
		for(var i = 0; i < childNodes.length; i++) {
			var childNode = childNodes[i];
			var alias = childNode.attributes['alias'];
			var uniqueName = childNode.attributes['uniqueName'];
			aliasToUniqueNameMap[alias] = uniqueName;
			aliasOrderedByLengthList.push(alias);
		}
		
		// we need to replace first longest aliases in order to avoid replacing confilcts error that occur when one alias is the prefix of another one 
		// ex. 'Profit' & 'Profit per Unit'
		aliasOrderedByLengthList.sort(function(a,b){
			return b.length - a.length;
		});
		
		var uniqueNamesUsedInExpression = [];
		for(var i = 0; i < aliasOrderedByLengthList.length; i++) {
			var alias = aliasOrderedByLengthList[i];
			var uniqueName = aliasToUniqueNameMap[alias];
			
			if(newExpression.indexOf(alias) >= 0) {
				uniqueNamesUsedInExpression.push(uniqueName);
				//alert('replacing all occurrences of [' + alias + '](' + alias.length + ') with [' + uniqueName+ '] in expression [' + newExpression + ']');
				newExpression = newExpression.replace(new RegExp(alias, 'g'), '#' + (uniqueNamesUsedInExpression.length - 1));
				//alert('replacing [' + alias + '] with [' + '#' + (uniqueNamesUsedInExpression.length - 1) +']: ' + newExpression);
				//alert('Results: ' + newExpression);
			}
		}		
		
		for(var i = 0; i < uniqueNamesUsedInExpression.length; i++) {
			var uniqueNameEncoded = uniqueNamesUsedInExpression[i];
			uniqueNameEncoded = uniqueNameEncoded.replace(new RegExp('\\(' , 'g'), '[');
			uniqueNameEncoded = uniqueNameEncoded.replace(new RegExp('\\)' , 'g'), ']');
			newExpression = newExpression.replace(new RegExp('#' + i , 'g'), uniqueNameEncoded);
			//alert('replacing [' + '#' + i + '] with [' + uniqueNameEncoded +']: ' + newExpression);
		}
		
		return newExpression;
	}
	
	, replaceFieldUniqueNamesWithFieldAliases: function(expression) {
		var newExpression;
		
		newExpression = expression;
		var fieldNodes = this.groupRootNodes['fields'];
		var childNodes = fieldNodes.childNodes;
		var uniqueNameToAliasMap = new Object();
		var uniqueNameOrderedByLengthList = new Array();
		
		for(var i = 0; i < childNodes.length; i++) {
			var childNode = childNodes[i];
			var alias = childNode.attributes['alias'];
			var uniqueName = childNode.attributes['uniqueName'];
			uniqueNameToAliasMap[uniqueName] = alias;
			uniqueNameOrderedByLengthList.push(uniqueName);
		}
		
		// we need to replace first longest aliases in order to avoid replacing confilcts error that occur when one alias is the prefix of another one 
		// ex. 'Profit' & 'Profit per Unit'
		uniqueNameOrderedByLengthList.sort(function(a,b){
			return b.length - a.length;
		});
		
		var aliasesUsedInExpression = [];
		for(var i = 0; i < uniqueNameOrderedByLengthList.length; i++) {
			var uniqueName = uniqueNameOrderedByLengthList[i];
			var alias = uniqueNameToAliasMap[uniqueName];
			
			var uniqueNameRegEx = uniqueName;
			uniqueNameRegEx = uniqueNameRegEx.replace(new RegExp('\\(' , 'g'), '\\[');
			uniqueNameRegEx = uniqueNameRegEx.replace(new RegExp('\\)' , 'g'), '\\]');
			
			var uniqueNameEncoded = uniqueName;
			uniqueNameEncoded = uniqueNameEncoded.replace(new RegExp('\\(' , 'g'), '[');
			uniqueNameEncoded = uniqueNameEncoded.replace(new RegExp('\\)' , 'g'), ']');
			
			
			//alert('unique name [' + uniqueName +'] has been decoded [' + uniqueNameEncoded + ']');
			
			if(newExpression.indexOf(uniqueNameEncoded) >= 0) {
				aliasesUsedInExpression.push(alias);
				//alert('replacing all occurrences of [' + alias + '](' + alias.length + ') with [' + uniqueName+ '] in expression [' + newExpression + ']');
				newExpression = newExpression.replace(new RegExp(uniqueNameRegEx, 'g'), '#' + (aliasesUsedInExpression.length - 1));
				//alert('REPLACING [' + uniqueNameRegEx + '] with [' + '#' + (aliasesUsedInExpression.length - 1) +']: ' + newExpression);
				//alert('Results: ' + newExpression);
			} else {
				//alert('No match for [' + uniqueNameEncoded + '] in expression [' + newExpression + ']');
			}
		}	
		
		for(var i = 0; i < aliasesUsedInExpression.length; i++) {
			var alias = aliasesUsedInExpression[i];
			newExpression = newExpression.replace(new RegExp('#' + i , 'g'), alias);
			//alert('replacing [' + '#' + i + '] with [' + alias +']: ' + newExpression);
		}
		
		return newExpression;
	}
	
	, getFormState : function() {      
    	
      	var formState = {};
      	
      	for(p in this.inputFields) {
      		formState[p] = this.inputFields[p].getValue();
      	}
      	
      	if(this.expressionEditor) {
      		formState.expression = this.getExpression();
      	}
      	
      	return formState;
    }
	
	, setTargetRecord: function(record) {
		this.target = record;
		if(this.target) {
			this.inputFields.alias.setValue( record.data.alias );
			this.inputFields.type.setValue( record.data.id.type );
			//this.setExpression(record.data.id.expression);
			this.setExpression.defer(100,this,[record.data.id.expression]);
		} else {
			this.inputFields.alias.reset();
			this.inputFields.type.reset();
			this.expressionEditor.reset();
		}
	}
	
	, setTargetNode: function(node) {
		this.target = node;
		if(this.target) {
			var alias;
			var nodeType;
			
			alias =  node.text || node.attributes.text;
			nodeType = node.attributes.type || node.attributes.attributes.type;
			if(nodeType === Sbi.settings.qbe.constants.NODE_TYPE_ENTITY) {
				this.inputFields.alias.reset();
				this.inputFields.type.reset();
				this.expressionEditor.reset();
			} else if(nodeType === Sbi.settings.qbe.constants.NODE_TYPE_SIMPLE_FIELD) {
				Sbi.qbe.commons.unimplementedFunction('handle [field] target');
			} else if(nodeType === Sbi.settings.qbe.constants.NODE_TYPE_CALCULATED_FIELD) {
				this.inputFields.alias.setValue( node.attributes.attributes.formState.alias );
				this.inputFields.type.setValue( node.attributes.attributes.formState.type );
				this.setExpression.defer(100,this, [node.attributes.attributes.formState.expression] );
			} else if(nodeType === Sbi.settings.qbe.constants.NODE_TYPE_INLINE_CALCULATED_FIELD) {
				this.inputFields.alias.setValue( node.attributes.attributes.formState.alias );
				this.inputFields.type.setValue( node.attributes.attributes.formState.type );
				this.setExpression.defer(100,this,[node.attributes.attributes.formState.expression] );
			} else {
				alert('Impossible to edit node of type [' + nodeType +']');
			} 
		} 	
	}
	
	, setCFAlias: function(alias) {
		this.inputFields.alias.setValue(alias);
	}

	, validate: function() {
		if(this.expertMode) {
			var serviceUrl;
			var params;
			if(typeof this.validationService === 'object') {
				serviceUrl = Sbi.config.serviceRegistry.getServiceUrl(this.validationService);
				params = this.validationService.params || {};
			} else {
				serviceUrl = this.validationService;
				params = {};
			}
			
			params.expression = this.getExpression();
			
			Ext.Ajax.request({
			    url: serviceUrl,
			    success: this.onValidationSuccess,
			    failure: Sbi.exception.ExceptionHandler.handleFailure,	
			    scope: this,
			    params: params
			}); 
			
		}else{
			var error = SQLExpressionParser.module.validateInLineCalculatedField(this.getExpression());
			if(error==""){
				Sbi.exception.ExceptionHandler.showInfoMessage(LN('sbi.qbe.calculatedFields.validationwindow.success.text'), LN('sbi.qbe.calculatedFields.validationwindow.success.title'));
			}else{
				Sbi.exception.ExceptionHandler.showWarningMessage(error, LN('sbi.qbe.calculatedFields.validationwindow.fail.title'));
			}
		}		
	}
	
	, onValidationSuccess: function(response) {
		Sbi.exception.ExceptionHandler.showInfoMessage(LN('sbi.qbe.calculatedFields.validationwindow.success.text'), LN('sbi.qbe.calculatedFields.validationwindow.success.title'));
	}
	
	, onValidationFailure: function(response) {
		
	}
	//--------------------------------------------------------------------------------------------
	//private methods
	//--------------------------------------------------------------------------------------------

	, initNorthRegionPanel: function(c) {
		this.initDetailsFormPanel(Ext.apply({}, {
			region:'north',
    		split: false,
    		frame:false,
    		border:false,
    		height: 70,
    	    bodyStyle:'padding:5px;background:#E8E8E8;border-width:1px;border-color:#D0D0D0;',
    	    style: 'padding-bottom:3px'
		}, c || {}));
		
		this.northRegionPanel = this.detailsFormPanel;
	}
	
	, initWestRegionPanel: function(c) {		
	  
	    this.initExpItemsPanel(Ext.apply({}, {
	    	region:'west',
	    	title: 'Items',
			layout: 'fit',
			split: true,
			collapsible: true,
			autoScroll: true,
		    frame: false, 
		    border: true,
		    width: 120,
		    minWidth: 120
		}, c || {}));
		
		this.westRegionPanel = this.expItemsPanel;
	}
	
	, initCenterRegionPanel: function(c) {
		this.initExpressionEditorPanel(Ext.apply({}, {
			region:'center',
		    frame: false, 
		    border: false
		}, c || {}));
		
		this.centerRegionPanel = this.expressionEditorPanel;
	}
	

	// details form
	, initDetailsFormPanel: function(c) {
	
		if(this.inputFields === null) {
			this.inputFields = new Object();
		}
		
		this.inputFields['alias'] = new Ext.form.TextField({
    		name:'alias',
    		allowBlank: false, 
    		inputType:'text',
    		maxLength:50,
    		width:250,
    		fieldLabel:'Alias' 
    	});
    	var aliasPanel = new  Ext.form.FormPanel({
    		bodyStyle: "background-color: transparent; border-color: transparent",
    		items: [this.inputFields['alias'] ]
    	});
    	
    	var scopeComboBoxStore = new Ext.data.SimpleStore({
    		fields: ['value', 'field', 'description'],
    		data : this.scopeComboBoxData 
    	});  
    	
    	this.expertCheckBox = new Ext.form.Checkbox({
			checked: this.expertMode,
			fieldLabel: LN('sbi.qbe.selectgridpanel.buttons.text.expert'),
		    listeners: {
		    	 check: function(checkbox , checked) {
			    	 if (!checked) {
			    		  this.fireEvent('expert');
			    		  this.hide();
			    	  }else{
			    		  this.fireEvent('notexpert');
			    		  this.hide();
			    	  }
    		
			     },scope: this
		     }

		});	

    	this.inputFields['type'] = new Ext.form.ComboBox({
    		tpl: '<tpl for="."><div ext:qtip="{field}: {description}" class="x-combo-list-item">{field}</div></tpl>',	
    		editable  : false,
    		fieldLabel : 'Type',
    		forceSelection : true,
    		allowBlank :false,
    		mode : 'local',
    		name : 'scope',
    		store : scopeComboBoxStore,
    		displayField:'field',
    		valueField:'value',
    		emptyText:'Select type...',
    		typeAhead: true,
    		triggerAction: 'all',
    		width:150,
    		selectOnFocus:true
    	});
    	
    	var typePanel = new  Ext.form.FormPanel({
    		bodyStyle: "background-color: transparent; border-color: transparent",
    		items: [this.inputFields['type'] ]
    	});
    	
    	var expertCheckPanel = new  Ext.form.FormPanel({
    		bodyStyle: "background-color: transparent; border-color: transparent; padding-left: 10px;",
    		items: [this.expertCheckBox]
    	});
    	if(this.expertDisable){
    		expertCheckPanel.hide();
    	}

    	this.detailsFormPanel = new Ext.Panel(
	    	 Ext.apply({
		    	 layout: 'table',
		    	 layoutConfig: {
			        columns: 2
			     },
	    		 items: [
	    		     aliasPanel,
	    		     expertCheckPanel ,
	    		     typePanel
	    		 ]
	    	 }, c || {})

    	);

	}
	
	
	// items tree
	

	
	, setExpItems: function(itemGroupName, items) {
		
		var groupRootNode = this.groupRootNodes[itemGroupName];
		var oldChildren = new Array();
		groupRootNode.eachChild(function(n){
			oldChildren.push(n);
		}, this);
		
		for(var j = 0, t = oldChildren.length; j < t; j++) {
			groupRootNode.removeChild(oldChildren[j]);
		}
		
		this[itemGroupName] = items;
		for(var j = 0, t = items.length; j < t; j++) {
			var node = new Ext.tree.TreeNode(items[j]);
			Ext.apply(node.attributes, items[j]);
			groupRootNode.appendChild( node );
		}
	}

	, initExpItemsPanel: function(c) {
		
		this.expItemsTreeRootNode = new Ext.tree.TreeNode({text:'Exp. Items', iconCls:'database',expanded:true});
		
		if(this.expItemGroups) {
			this.groupRootNodes = new Object();
			for(var i = 0, l = this.expItemGroups.length; i < l; i++) {
				var groupName = this.expItemGroups[i].name;
				if(this.expItemGroups[i].loader === undefined) {
					this.groupRootNodes[groupName] = new Ext.tree.TreeNode(Ext.apply({}, {leaf: false}, this.expItemGroups[i]));
				} else {
					this.groupRootNodes[groupName] = new Ext.tree.AsyncTreeNode(Ext.apply({}, {leaf: false}, this.expItemGroups[i]));
				}
				this.expItemsTreeRootNode.appendChild( this.groupRootNodes[groupName] );
				if(this[groupName] != null) {
					this.setExpItems(groupName, this[groupName]);
				}
			}
		}
		
	    this.expItemsTree = new Ext.tree.TreePanel({
	        root: this.expItemsTreeRootNode,
	        enableDD:false,
	        expandable:true,
	       // collapsible:true,
	        autoHeight:true ,
	        bodyBorder:false ,
	        leaf:false,
	        lines:true,
	        layout: 'fit',
	        animate:true
	     });
	    
		this.expItemsPanel = new Ext.Panel(
		 Ext.apply({
			 title: 'Items',
		 	 layout: 'fit',
		 	 items: [this.expItemsTree]
		  }, c || {}) 
		 );
		
		this.expItemsTree.addListener('click', this.expItemsTreeClick, this);		
	}
	
	, expItemsTreeClick: function(node, e) {
		//checks if the wizard should ask some operands selection by the user
		if (node.attributes.operands && this.expItemsTreeRootNode && this.expItemsTreeRootNode.childNodes){			
			this.opWin = new Sbi.qbe.OperandsWindow({
				operands: node.attributes.operands
			,   fields: this.expItemsTreeRootNode.childNodes[0]	
			,   text: node.attributes.value
			});
			this.opWin.on('click', function(win, text) {
				this.expressionEditor.insertAtCursor(text) ;
			}, this);
			this.opWin.show();			
		}else{			
			if(node.attributes.value) {
				var text;
				if(node.attributes.alias !== undefined && !this.expertMode ){
					text= node.attributes.alias + ' ';
				}else{
					text= node.attributes.value + ' ';	
				}
		    	this.expressionEditor.insertAtCursor(text) ;
			}
		}
	}
	
	
	// expression editor
	, initExpressionEditorPanel: function(c) {
		
		var buttons = {};
		buttons.clear = new Ext.Toolbar.Button({
		    text:'Clear All',
		    tooltip:'Clear all selected fields',
		    iconCls:'remove'
		});
		buttons.clear.addListener('click', function(){this.expressionEditor.reset();}, this);
		
		/*
		buttons.debug = new Ext.Toolbar.Button({
		    text:'Debug',
		    tooltip:'Shows expression string as passed to the server',
		    iconCls:'option'
		});
		buttons.debug.addListener('click', function(){alert(this.getExpression());}, this);*/
		
		buttons.validate = new Ext.Toolbar.Button({
		    text:'Validate',
		    tooltip:'Syntatic validation of the expression',
		    iconCls:'option'
		});
		buttons.validate.addListener('click', function(){this.validate();}, this);

		this.expressionEditor = new Ext.form.HtmlEditor({
    		name:'expression',
    	    frame: true,
    	    enableAlignments : false,
    	    enableColors : false,
    	    enableFont :  false,
    	    enableFontSize : false, 
    	    enableFormat : false,
    	    enableLinks :  false,
    	    enableLists : false,
    	    enableSourceEdit : false,
    	    fieldLabel:'Expression' ,
    	    	
    	    listeners:{
    	    	'render': function(editor){
    	          var tb = editor.getToolbar();
    	          tb.add(buttons.clear);
    	          //tb.add(buttons.debug);
    	          tb.add(buttons.validate);
    	        },
    	        'activate': function(){
    	          //active = true;
    	        },
    	        'initialize': {
    	        	fn: function(){
	    	          //init = true;
	    	          this.expressionEditor.onFirstFocus();
	    	          this.expressionEditor.insertAtCursor(this.baseExpression) ; 
	    	        } , scope: this
    	        },
    	        'beforesync': function(){
    	          // do nothings
    	        }
  	        
    	    }
    	});

		this.expressionEditorPanel = new Ext.Panel(
			Ext.apply({
				layout: 'fit',
			    items: [this.expressionEditor]
			}, c || {}) 
		);
	}
});
