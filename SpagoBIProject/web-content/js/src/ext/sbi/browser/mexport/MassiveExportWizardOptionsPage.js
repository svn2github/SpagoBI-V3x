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
 * Authors
 *
 * - Giulio Gavardi (giulio.gavardi@eng.it)
 */

Ext.ns("Sbi.browser.mexport");

Sbi.browser.mexport.MassiveExportWizardOptionsPage = function(config) {

//	var defaultSettings = {
//			title: LN('sbi.browser.mexport.massiveExportWizardOptionsPage.title')
//	};
	
	var defaultSettings = {
			title: LN('sbi.browser.mexport.massiveExportWizardOptionsPage.title')			//layout: 'fit'
			//, width: 500
			//, height: 300   
			, frame: true
			, closable: true
			, constrain: true
			, hasBuddy: false
			, resizable: true
	};
	

	if (Sbi.settings && Sbi.settings.browser 
			&& Sbi.settings.browser.mexport && Sbi.settings.browser.mexport.massiveExportWizardOptionsPage) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.browser.mexport.massiveExportWizardOptionsPage);
	}

	var c = Ext.apply(defaultSettings, config || {});
	Ext.apply(this, c);

	this.services = this.services || new Array();
	this.services['StartMassiveExportExecutionProcessAction'] = this.services['StartMassiveExportExecutionProcessAction'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'START_MASSIVE_EXPORT_EXECUTION_PROCESS_ACTION'
			, baseParams: new Object()
	});

	
	this.initFormPanel();
	this.initRolesCombo(Sbi.user.roles);

	c = Ext.apply(c, {
		layout: 'fit', 
		border: false,
		bodyBorder: false,
		items: [this.formPanel]
	});

	// constructor
	Sbi.browser.mexport.MassiveExportWizardOptionsPage.superclass.constructor.call(this, c);

	this.addEvents('select', 'unselect');
	this.on('select', this.onSelection, this);
	this.on('unselect', this.onDeselection, this);	
};

Ext.extend(Sbi.browser.mexport.MassiveExportWizardOptionsPage, Ext.Panel, {

	selectedRole : null
	, selectedOutput : null
	, formPanel : null
	, functId : null
	, rolesCombo : null
	, docsPanel : null
	, checkBoxCycle : null
	, comboBoxType : null

	
	// methods
	, initFormPanel: function(){
		this.formPanel = new Ext.form.FormPanel({
			labelWidth: 130, // label settings here cascade unless overridden
		    frame:true,
		    border: false,
		    bodyBorder: false,
		    bodyStyle:'padding:5px 5px 0',
		    width: 350,
		    autoScroll: true
		});	
	}
	, initRolesCombo: function (rolesArray) {
		//rolesArray (building with multidimensional array)	  
		var multiArray = new Array(); 
		var singleArray;
		for(i = 0; i < rolesArray.length; i ++){
			singleArray = new Array();
			singleArray[0] = rolesArray[i];
			multiArray[i] = singleArray;
		}
		var scopeComboBoxStore = new Ext.data.SimpleStore({
				 fields: ['role']
		         , data : multiArray
		         , autoLoad: true
		}); 
		if(!this.selectedRole){
			this.selectedRole = rolesArray[0];
		}

		// if there is only one role don't draw combo
		if(rolesArray.length==1){
			this.selectedRole = rolesArray[0];
			this.rolesCombo = null;
			this.retrieveDocuments();
		}
		else {
			// in the case of more roles
			this.rolesCombo = new Ext.form.ComboBox({
				  tpl: '<tpl for="."><div ext:qtip="{role}" class="x-combo-list-item">{role}</div></tpl>'
				, editable  : true
				, forceSelection : true
				, fieldLabel : LN('sbi.browser.mexport.massiveExportWizardOptionsPage.field.role.label')
				, labelAlign : 'left'
				, name : 'roles'
				, emptyText: LN('sbi.browser.mexport.massiveExportWizardOptionsPage.field.role.emptyText')
				, mode : 'local'
				, typeAhead: true
				, triggerAction: 'all'
				, store: scopeComboBoxStore
			    , displayField:'role'
				, valueField:'role'
			    , listeners: {
					'select': {
						fn: function(){ 
						this.selectedRole = this.rolesCombo.getValue();
						this.rolesCombo.setValue(this.selectedRole);
						}
				, scope: this
						}
					}
				});	
		
				this.formPanel.add(this.rolesCombo);

				// select first as default
				this.rolesCombo.on('render', function() {
						if(rolesArray.length>0){
							var sel = rolesArray[0];
							this.rolesCombo.setValue(sel);
						}
						this.retrieveDocuments();
					}, this);  
			} // close multi-role case

	}
	, retrieveDocuments: function (rolesArray) {
	
		var params = {
			LIGHT_NAVIGATOR_DISABLED: 'TRUE'
			, SBI_EXECUTION_ID: null
			, TYPE: 'WORKSHEET'
			, MODALITY : 'RETRIEVE_DOCUMENTS_MODALITY'
			, functId: this.functId
		};
		
		Ext.Ajax.request({
			url: this.services['StartMassiveExportExecutionProcessAction'],
			params: params,
			success : function(response, options){
			if(response !== undefined) {   
				if(response.responseText !== undefined) {
					var content = Ext.util.JSON.decode( response.responseText );
					if(content !== undefined) {
						// build documents list
						var docsArray = content.selectedDocuments;
						var list ='<ul>'
						if(docsArray.length==0){
							list = LN('sbi.browser.mexport.massiveExportWizardOptionsPage.msg.noDoc');
							this.wizard.btnNext.disable();
							this.wizard.btnFinish.disable();
						}
						else{
							for(i=0;i<docsArray.length;i++){
								var name = docsArray[i];
								list+='<li>'+name+'</li>';
							}
						}
						list+='</ul>';
						list = '<h1>'+LN('sbi.browser.mexport.massiveExportWizardOptionsPage.field.documents.label')+':</h1>'+list;
						// checkBoxCycle
						this.buildCycleCheck(); 
						// output type combo
						this.buildOutputTypeCombo();
						// draw documents list
						this.buildDocsPanel(list);
						this.doLayout();
					} 
				} else {
					Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
				}
			}
			else{
				//clear preceding store if error happened
				for(p in this.parametersPanel.fields){
					var field = this.parametersPanel.fields[p];
					field.disable();
				}
				this.btnFinish.disable();
			}
		},
		scope: this,
		failure: Sbi.exception.ExceptionHandler.handleFailure      
		});
	
	
	}
	, buildDocsPanel : function(list){
		
		this.docsPanel = new Ext.Panel({
	    	name : 'DocToExport'
			, html : list
		});
		this.formPanel.add(this.docsPanel);
		
	}
	, buildCycleCheck : function(){
		this.checkBoxCycle = new Ext.form.Checkbox({
			name : 'cycle'
			, fieldLabel : LN('sbi.browser.mexport.massiveExportWizardOptionsPage.field.cycle.label')
		});
	
		this.formPanel.add(this.checkBoxCycle);
		this.formPanel.doLayout();
		this.doLayout();
	}
	, buildOutputTypeCombo : function(){ 
		var arrayOutput = [['XLS'], ['XLSX']];
		
		var scopeComboOutputStore = new Ext.data.SimpleStore({
			 fields: ['type']
	         , data : arrayOutput
		}); 
		this.comboBoxOutput = new Ext.form.ComboBox({
			tpl: '<tpl for="."><div ext:qtip="{type}" class="x-combo-list-item">{type}</div></tpl>'
			, editable  : false
			, forceSelection : true
			, fieldLabel : LN('Output type')
			, store: scopeComboOutputStore
		    , displayField:'type'
			, valueField:'type'
			, mode : 'local'
			, value : 'XLS'
			, triggerAction : 'all'
			, listeners: {
				'select': {
					fn: function(){ 
					selectedOutput = this.comboBoxOutput.getValue();
					this.selectedOutput.setValue(selectedOutput);
						}
			, scope: this
					}
				}
			});		
		this.formPanel.add(this.comboBoxOutput);
		this.formPanel.doLayout();
		this.selectedOutput = 'XLS';
	}
	
	, getSelectedRole : function(){
		return this.selectedRole;
	}
	, getSelectedOutput : function(){
		return selectedOutput;
	}
	
	, isCycleOnFilterSelected : function(){
		return this.checkBoxCycle.checked;
	}
	
	, getContent: function() {
		var content = {};
		content.selectedRole = this.getSelectedRole();
		content.splittingFilter = this.isCycleOnFilterSelected();
		content.selectedOutput = this.getSelectedOutput();
		return content;
	}
	
	, onSelection: function() {
		this.currentPage = true;
		this.wizard.setPageTitle('Options', 'Setup massive export options');
	}
	
	, onDeselection: function() {
		this.currentPage = false;
	}
	
	, isTheCurrentPage: function() {
		return this.currentPage;
	}
	
	
	

});