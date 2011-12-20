/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
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

  */

Ext.ns("Sbi.home");

Sbi.home.DefaultRoleWindow = function(config) {	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE:'GET_USER_ROLES'};
	
	this.services = new Array();
	//this.roleStore = '';
//	this.services['getDefaultRole'] = Sbi.config.serviceRegistry.getServiceUrl({
//		serviceName: 'GET_DEFAULT_ROLE'
//		, baseParams: params
//	});
	this.services['setDefaultRole'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SET_DEFAULT_ROLE_ACTION'
		, baseParams: params
	});		

	this.buddy = undefined;
	this.lookup = {};

	var c = Ext.apply({}, config || {}, {
		hasBuddy: false
	});
		
	this.initFormPanel(c);
	this.init();

 	 	// constructor
    Sbi.home.DefaultRoleWindow.superclass.constructor.call(this, {
		layout:'fit',
		width:350,
		height:150,
		closeAction:'hide',
		plain: true,
		title: LN('sbi.browser.defaultRole.title'),
		items: [this.formPanel]
    });
    
    if(c.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
    
 
 
}; // end CONSTRUCTOR





Ext.extend(Sbi.home.DefaultRoleWindow, Ext.Window, {
    
	scopeField: null
	, hasBuddy: null
    , buddy: null
     , init: function () {
	// fill combo with user roles
	//var arrayRoles = new Array();
	var arrayRoles = Sbi.user.roles;
	var defaultRole = null;

	var defIndex = -1;
 		var store = this.scopeField.store;    	
    	var recId = 100; // provide unique id for the record
 		for (i=0; i<arrayRoles.length; i++){
 			role = arrayRoles[i];

			defaultData = {
    		view: role,
    		role: role
				};

    			r = new store.recordType(defaultData, recId); // create new record
			store.insert(0, r); // insert a new record into the store (also see add)
			if (Sbi.user.defaultRole && Sbi.user.defaultRole == role){
				defIndex = recId;
			}
			recId = recId+1;
		}
		
		// add no default record
		defaultData = {
    		view: LN('sbi.browser.defaultRole.noDefRole'),
    		role: ''
				};
    	r = new store.recordType(defaultData, recId); // create new record
		if(defIndex == -1){				
			store.insert(0, r); // insert a new record into the store (also see add)
			this.scopeField.setValue('')
		}
		else {		
			store.insert(0, r); // insert a new record into the store (also see add)
			this.scopeField.setValue(Sbi.user.defaultRole);		
 		}
 
 }
    // public methods
	,getFormState : function() {      
		var roleSelected = this.scopeField.getValue();
      	return roleSelected;
    }

	//private methods
	, initFormPanel: function(config) {
    	var scopeComboBoxData = [
    		//['-----'],
    	];
    		
    	var scopeComboBoxStore = new Ext.data.SimpleStore({
    		fields: ['view','role'],
    		data : scopeComboBoxData
    	});
    		    
    	this.scopeField = new Ext.form.ComboBox({
    	   	tpl: '<tpl for="."><div ext:qtip="{role}" class="x-combo-list-item">{view}</div></tpl>',	
    	   	editable  : false,
    	   	fieldLabel : LN('sbi.browser.defaultRole.role'),
    	   	forceSelection : true,
    	   	mode : 'local',
    	   	name : 'analysisScope',
    	   	store : scopeComboBoxStore,
    	   	displayField:'view',
    	    valueField:'role',
    	    emptyText:'Select scope...',
    	    typeAhead: true,
    	    triggerAction: 'all',
    	    selectOnFocus:true
    	});
    	
    	this.formPanel = new Ext.form.FormPanel({
    		frame:true,
    	    bodyStyle:'padding:5px 5px 0',
    	    buttonAlign : 'center',
    	    items: [this.scopeField],
    	    buttons: [{
    			text: LN('sbi.browser.defaultRole.save'),
    		    handler: function(){
    	    		//this.fireEvent('save', this, this.getFormState());
                	var selectedRole = this.getFormState();
	         
    		   var params = {
            		SELECTED_ROLE: selectedRole
        			};

// call ajax to action that set as default the selected role                	
        Ext.Ajax.request({
            url: this.services['setDefaultRole'],
            success: function(response, options) {
				if (response !== undefined) {

					// call again the home page
					var urlToCall = Sbi.config.serviceRegistry.getBaseUrlStr({
						//isAbsolute :  true
					});	
					//urlToCall += '?PAGE=LoginPage&NEW_SESSION=TRUE';
					urlToCall += '?PAGE=LoginPage';
					window.location.href=urlToCall;					

				
				
				} else {
					Sbi.exception.ExceptionHandler.showErrorMessage('Error while setting default role', 'Service Error');
				}
            },
            failure: Sbi.exception.ExceptionHandler.handleFailure,    
            scope: this,
            params: params
        });                	
                	
                	//this.hide();
        			this.close();
            	}
           , scope: this
    	    },{
    		    text: LN('sbi.browser.defaultRole.cancel'),
    		    handler: function(){
                	//this.hide();
    	    		this.close();
            	}
            	, scope: this
    		}]
    	 });
    }
});

