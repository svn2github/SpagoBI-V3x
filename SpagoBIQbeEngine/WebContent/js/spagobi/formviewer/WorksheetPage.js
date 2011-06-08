/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.formviewer");

Sbi.formviewer.WorksheetPage = function(config) {	
	var defaultSettings = {
		title: LN('sbi.worksheet.title')
		, layout: 'fit'
		, autoScroll: true
	};
		
	if(Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.worksheetPage) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formviewer.worksheetPage);
	}
		
	this.services={};
	this.services['getWorkSheetState'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_WORKSHEET_PREVIEW_ACTION'
		, baseParams: params
	});
	this.services['setWorkSheetState'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SET_WORKSHEET_DEFINITION_ACTION'
		, baseParams: params
	});
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
		
	this.toolbar = new Ext.Toolbar({
		items: [
		    '->'
		    , {
				text: LN('sbi.formviewer.resultspage.backtoform'),
				handler: function() {this.fireEvent('edit');},
				scope: this
		    }
		  ]
	});
	
	this.init(c);
	
	c = Ext.apply(c, {
	    tbar: this.toolbar,
	    style: 'padding:3px;',
	    items: [this.tabs]
	});
		
	// constructor
	Sbi.formviewer.WorksheetPage.superclass.constructor.call(this, c);
	
	this.addEvents('edit');
};

Ext.extend(Sbi.formviewer.WorksheetPage, Ext.Panel, {

	formState: null
	
    // -- public methods -----------------------------------------------------------------------

    , getFormState: function() {
		return this.formState;
	}

	, setFormState: function(formState) {
		this.formState = formState;
	}
    
    // -- private methods -----------------------------------------------------------------------

	, init: function (c) {
		
		var items = [];
	
		this.worksheetDesignerPanel = new Sbi.worksheet.designer.WorksheetDesignerPanel(Ext.apply(c||{},{smartFilter: true}));
	
		items.push(this.worksheetDesignerPanel);
		
		this.worksheetPreviewPanel = new Sbi.worksheet.runtime.WorkSheetPreviewPage({closable: false});
			
		this.worksheetPreviewPanel.on('activate', function() {
			this.setWorksheetState(this.refreshWorksheetPreview, Sbi.exception.ExceptionHandler.handleFailure, this);
		}, this);
			
		items.push(this.worksheetPreviewPanel);

		
		this.tabs = new Ext.TabPanel({
			border: false,
	  		activeTab: 0,
	  		items: items
		});
	}
	
	, setWorksheetState : function (successFn, failureFn, scope) {
		var state = this.worksheetDesignerPanel.sheetsContainerPanel.getSheetsState();
		var params = {
				'worksheetdefinition':  Ext.encode(state)
		};
		
		params.formstate = Ext.util.JSON.encode(this.getFormState());
		
		Ext.Ajax.request({
		    url: this.services['setWorkSheetState'],
		    success: successFn,
		    failure: failureFn,
		    scope: scope,
		    params: params
		});   
	}
	
	, getWorksheetDefinition : function () {
		return this.worksheetDesignerPanel.getWorksheetDefinition();   
	}
	
	, isValid : function () {
		return this.worksheetDesignerPanel.isValid();
	}

  	, showCrosstabPreview: function(crosstabDefinition) {
  		this.crosstabPreviewPanel.on('beforeload', this.addFormStateParameter, this);
  		this.tabs.activate(this.crosstabPreviewPanel);
  		this.crosstabPreviewPanel.load(crosstabDefinition);
  	}
  	
  	, addFormStateParameter: function(crosstabPreviewPanel, requestParameters) {
  		requestParameters.formstate = Ext.util.JSON.encode(this.getFormState());
  	}
  	
	, refreshWorksheetPreview : function () {
		this.worksheetPreviewPanel.getFrame().setSrc(this.services['getWorkSheetState']);
	}
	


});