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
 * - Giulio gavardi (giulio.gavardi@eng.it)
 */

Ext.ns("Sbi.browser.mexport");

Sbi.browser.mexport.MassiveExportWizardParametersPage = function(config) {

	var defaultSettings = {
			//title: LN('sbi.browser.mexport.massiveExportWizardParametersPage.title')  //LN('Sbi.browser.mexport.massiveExportWizardParametersPage.title')
			layout: 'fit'
			//, width: 500
			//, height: 300   
			, frame: true
			, closable: true
			, constrain: true
			, hasBuddy: false
			, resizable: true
	};
	if (Sbi.settings && Sbi.settings.browser 
			&& Sbi.settings.browser.mexport && Sbi.settings.browser.mexport.massiveExportWizardParametersPage) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.browser.mexport.massiveExportWizardParametersPage);
	}
	
	var c = Ext.apply(defaultSettings, config || {});	
	Ext.apply(this, c);

	this.services = this.services || new Array();

	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null, TYPE: 'WORKSHEET'};
	this.services['StartMassiveExportExecutionProcessAction'] = this.services['StartMassiveExportExecutionProcessAction'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'START_MASSIVE_EXPORT_EXECUTION_PROCESS_ACTION'
		, baseParams: new Object()
	});	
	
	this.initMainPanel(c);	
	c = Ext.apply(c, {
		layout: 'fit'
		, items: [this.mainPanel]	
	});

	// constructor
	Sbi.browser.mexport.MassiveExportWizardParametersPage.superclass.constructor.call(this, c);
	
	this.addEvents('select', 'unselect');
	
	this.on('select', this.onSelection, this);
	this.on('unselect', this.onDeselection, this);	
};

Ext.extend(Sbi.browser.mexport.MassiveExportWizardParametersPage, Ext.Panel, {

	services: null
    , mainPanel: null
    , currentPage: null
    
    
	// ----------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------

	, onSelection: function() {
		this.currentPage = true;
		this.wizard.setPageTitle('Parameters', 'Setup parameters\' configuration');
		
		// clear the fields in case you are coming to panel for the second time
		this.mainPanel.clear();

		// create ExecutionInstances and  get parameters 
		var selectedRole = this.getPreviousPage().getSelectedRole();	
	
		var params = {
			selectedRole : selectedRole
			, functId : this.functId
			, type : 'WORKSHEET'						
		}				
		this.createExecutionInstances(params);
	}
	
	, onDeselection: function() {
		this.currentPage = false;
	}
	
	, isTheCurrentPage: function() {
		return this.currentPage;
	}
	
	, getPageIndex: function() {
		var i;		
		for(i = 0; i < this.wizard.pages.length; i++) {
			if(this.wizard.pages[i] == this) break;
		}		
		return i;
	}
	
	, getPreviousPage: function() {
		var pages = this.wizard.pages;
		var i = this.getPageIndex();
		return (i != 0)? this.wizard.pages[i-1]: null;
	}
	
	, getNextPage: function() {
		var pages = this.wizard.pages;
		var i = this.getPageIndex();
		return (i != (pages.length-1))? this.wizard.pages[i+1]: null;
	}
	
	, getName: function(){
		return 'Sbi.browser.mexport.MassiveExportWizardParametersPage';
	}
	
	/**
	 * returns the value selected of the parameters in parametersPanel,
	 * and for each also the objparameterId (for label rinomination: name  => nameB)
	 */
	, getContent: function() {
		var state;
		
		state = {};
		for(p in this.mainPanel.fields) {
			var field = this.mainPanel.fields[p];
			var value = field.getValue();
			state[field.name] = value;
			var rawValue = field.getRawValue();
			if(value == "" && rawValue != ""){
				state[field.name] = rawValue;
			}
			
			// add objParsId information if present (massive export case)
			if(field.objParameterIds){
				for(pr=0;pr < field.objParameterIds.length;pr++){
					val = field.objParameterIds[pr];
					state[val+ '_objParameterId']=field.name;
				}
			}
		}
		return state;
	}
	
    // ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------

    , initMainPanel: function() {
    	var services = new Array();
		var params = {
				LIGHT_NAVIGATOR_DISABLED: 'TRUE'
				, SBI_EXECUTION_ID: null
					   
		};
		
		services['getParametersForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'GET_ANALYTICAL_DRIVER_FROM_DOCS_IN_FOLDER_ACTION'
			, baseParams: params
		});
	
		var config = {
			services : services	
			, contest : 'massiveExport'
			, drawHelpMessage : false	
			, columnNo: 2
			, columnWidth: 240
			, labelAlign: 'top'
			, fieldWidth: 215
			//, fieldLabelWidth: 100
		};
		
		this.mainPanel = new Sbi.execution.ParametersPanel(config);
    }
    
    , createExecutionInstances: function(params) {
    	
		params = Ext.apply(params, {modality: 'CREATE_EXEC_CONTEST_ID_MODALITY'});
		
		Ext.Ajax.request({
	        url: this.services['StartMassiveExportExecutionProcessAction'],
	        params: params,	        	 
	        success : function(response, options) {
	        	if(response !== undefined) {   
	        		
	        		if(response.responseText !== undefined) {
	        			var content = Ext.util.JSON.decode( response.responseText );
	        			if(content !== undefined) {
		      				this.executionInstances = {
		      					SBI_EXECUTION_ID: content.execContextId
		      				};
		      		  		for(p in this.mainPanel.fields){
		      		  			var field = this.mainPanel.fields[p];
		      		  			field.enable();
		      		  		}
		      		  		this.wizard.btnFinish.enable();
		      				params = Ext.apply(params, this.executionInstances);
		      				this.mainPanel.loadParametersForExecution(params);
	        			} 
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}	
	        		
	        	} else {
	        		//clear preceding store if error happened
	        		for(p in this.mainPanel.fields){
		  			var field = this.mainPanel.fields[p];
		  			field.disable();
		  		}
		  		this.wizard.btnFinish.disable();
		  	}
	      },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
	   });
	}	
	
});