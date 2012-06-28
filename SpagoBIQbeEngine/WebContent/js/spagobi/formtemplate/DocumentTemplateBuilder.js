/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.formtemplate");

Sbi.formtemplate.DocumentTemplateBuilder = function(config) {
	
	var defaultSettings = {
		
	};
	
	if (Sbi.settings && Sbi.settings.formbuilder && Sbi.settings.formtemplate.documentTemplateBuilder) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formtemplate.documentTemplateBuilder);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.services = this.services || new Array();	
	this.services['getDocuments'] = this.services['getDocuments'] || Sbi.config.remoteServiceRegistry.getServiceUrl({
		serviceName: 'FILTER_FOLDER_CONTENT_ACTION'
		, baseParams: {
			typeFilter: 'EQUALS_TO'
			, valueFilter: 'DATAMART'
			, columnFilter: 'TYPE'
		}
	});
	
	this.services['execFormBuilder'] = this.services['execFormBuilder'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'FORM_ENGINE_START_ACTION'
		, baseParams: {
			MODALITY: 'EDIT'
			, NEW_SESSION: 'TRUE'
			, formDocumentId: this.formDocumentId
		}
	});
	
	
	
	this.init();
	
	Ext.apply(c, {
		closable: false
		, border: false
		, activeItem: 0
		, hideMode: !Ext.isIE ? 'nosize' : 'display'
		, layout: 'card'
		, items: [this.datamartSelectPage, this.formBuilderPage]
	});
	
	// constructor
    Sbi.formtemplate.DocumentTemplateBuilder.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formtemplate.DocumentTemplateBuilder, Ext.Panel, {
    
	services: null
	, datamartSelectPage: null
	, formBuilderPage: null
	
	// --------------------------------------------------------------------------------
	// public methods
	// --------------------------------------------------------------------------------
		
	

	// --------------------------------------------------------------------------------
	// private methods
	// --------------------------------------------------------------------------------
	
	, init: function() {
		this.initDatamartSelectPage();
		this.initFormBuilderPage();
	}	   

	, initDatamartSelectPage: function() {
		
		this.proxy = new Ext.data.ScriptTagProxy({
	        url: this.services['getDocuments'],
	        method: 'GET'
	    });
		
		this.proxy.onRead = function(action, trans, res) {
	        var result;
	        try {
	        	var t, fc = res.folderContent; 
	        	for (var i = 0; i < fc.length; i++) {
	        		if(fc[i].title === 'Documents') {
	        			t = fc[i];
	        			break;
	        		}
	        	}
	        	t = t.samples;	        
	        	result = trans.reader.readRecords(t);
	        }catch(e){
	            // @deprecated: fire loadexception
	            this.fireEvent("loadexception", this, trans, res, e);

	            this.fireEvent('exception', this, 'response', action, trans, res, e);
	            trans.callback.call(trans.scope||window, null, trans.arg, false);
	            return;
	        }
	        if (result.success === false) {
	            // @deprecated: fire old loadexception for backwards-compat.
	            this.fireEvent('loadexception', this, trans, res);

	            this.fireEvent('exception', this, 'remote', action, trans, res, null);
	        } else {
	            this.fireEvent("load", this, res, trans.arg);
	        }
	        trans.callback.call(trans.scope||window, result, trans.arg, result.success);
	    };
	   
	    var store = new Ext.data.Store({
	        autoLoad: false,
	        proxy: this.proxy,
		    reader: new Ext.data.JsonReader({id: 'id'}, [
	            {name:'id'},
	            {name:'name'},
	            {name:'description'}
		    ])
		});
	  
	    this.documentsCombo = new Ext.form.ComboBox({
    	   	tpl: '<tpl for="."><div ext:qtip="{name}: {description}" class="x-combo-list-item">{name}</div></tpl>',	
    	   	editable  : false,
    	   	fieldLabel : LN('sbi.formtemplate.documenttemplatebuilder.documentfield.label'),
    	   	forceSelection : true,
    	   	//mode : 'local',
    	   	name : 'scope',
    	   	store : store,
    	   	displayField:'name',
    	    valueField:'id',
    	    emptyText: LN('sbi.formtemplate.documenttemplatebuilder.documentfield.emptytext'),
    	    typeAhead: true,
    	    triggerAction: 'all',
    	    selectOnFocus:true
    	});
		
		this.submitBtn = new Ext.Button({
			tooltip: 'Submit',
			text: LN('sbi.formtemplate.documenttemplatebuilder.startediting'),
			disabled: false,
	        hidden: false,
	        handler: function() {			
				var docId = this.documentsCombo.getValue();
				this.openFormBuilder(docId);
			}, 
			scope: this
	    });
		
		this.datamartSelectPage = new Ext.Panel({
			bodyStyle: 'padding:10px',
			layout: 'form',
			items: [this.documentsCombo, this.submitBtn]
		});		
		
		//alert('alert');
	}
	
	, initFormBuilderPage: function() {
		this.formBuilderPage = new Ext.Panel({
			html: 'formBuilderPage'
		});
	}
	
	
	, openFormBuilder: function(docId) {
		var form = document.getElementById('submit-form');
		if(!form) {
			var dh = Ext.DomHelper;
			form = dh.append(Ext.getBody(), {
			    id: 'submit-form'
			    , tag: 'form'
			    , method: 'post'
			    , cls: 'submit-form'
			});
		}
		
		form.action = this.services['execFormBuilder'] + '&document=' + docId;
		form.submit();
	}
	
	
  	
});