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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.DownloadLogsWindow = function(config) {

	/**
	 * example of action calling:
	 * http://localhost:8080/SpagoBI/servlet/AdapterHTTP?ACTION_NAME=DOWNLOAD_ZIP&DIRECTORY=C:/logs&BEGIN_DATE=01/03&END_DATE=30/04&BEGIN_TIME=14:00&END_TIME=15:00
	 */
	
	var defaultSettings = Ext.apply({}, config || {}, {
		title: 'Download windows'
		, width: 500
		, height: 300
		, hasBuddy: false		
	});
	
		
	if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.downloadLogsWindow) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.downloadLogsWindow);
	}
		
	var c = Ext.apply(defaultSettings, config || {});

	Ext.apply(this, c);
		
	this.initFormPanel();	

	this.closeButton = new Ext.Button({
		text: LN('sbi.console.downloadlogs.btnClose'),
		handler: function(){
        	//this.hide();
        	this.close();
        }
        , scope: this
	});
	
	this.downloadButton = new Ext.Button({
		text: LN('sbi.console.downloadlogs.btnDownload')
		, handler: function() {
			//check parameters
			if (!this.checkParameters()) { return; }			
			
			
        	this.fireEvent('checked', this, this.target);
        	//this.hide();
			this.close();
        }
        , scope: this
	});
	

	c = Ext.apply(c, {  	
		layout: 'fit'
	//,	closeAction:'hide'
	,	closeAction:'close'
	,	plain: true
	,	modal:true
	,	title: this.title
	,	buttonAlign : 'center'
	,	buttons: [this.closeButton, this.downloadButton]
	,	items: [this.formPanel]
	});

	// constructor
	Sbi.console.DownloadLogsWindow.superclass.constructor.call(this, c);
	
	this.addEvents('checked');
    
};

Ext.extend(Sbi.console.DownloadLogsWindow, Ext.Window, {

    serviceName: null
    , formPanel: null
    , initialDate: null
    , finalDate: null
    , initialTime: null
    , finalTime: null
    

    // this is the object uppon witch the window has been opened, usually a record
    , target: null
    
   , closeButton: null
   , downloadButton: null
    
    // public methods
   , downloadLogs: function(action, r, index, params) {
		
		//by unique request
		var form = document.getElementById('download-form');
		if(!form) {
			var dh = Ext.DomHelper;
			form = dh.append(Ext.getBody(), {
			    id: 'download-form'
			    , tag: 'form'
			    , method: 'post'
			    , cls: 'download-form'
			});
		}
		//call by ajax for test correct file
		params = Ext.apply(params, {
  			message: action.name, 
        	userId: Sbi.user.userId ,
        	BEGIN_DATE: this.initialDate.value,
        	END_DATE: this.finalDate.value,
        	BEGIN_TIME: this.initialTime.value,
        	END_TIME: this.finalTime.value,
        	PREFIX1: params.PREFIX1,
        	PREFIX2: params.PREFIX2,
        	DIRECTORY: params.DIRECTORY
  		}); 
		 
  		Ext.Ajax.request({
	       	url: params.URL			       
	       	, params: params 			       
	    	, success: function(response, options) {
	    		if(response !== undefined && response.responseText !== undefined) {			    				
						//call by submit to download really 
						form.action = params.URL +  '&PREFIX1=' + params.PREFIX1 + '&PREFIX2=' + params.PREFIX2 +
									  '&DIRECTORY=' + params.DIRECTORY + 
									  '&BEGIN_DATE=' + this.initialDate.value + '&END_DATE=' + this.finalDate.value + 
									  '&BEGIN_TIME=' + this.initialTime.value + '&END_TIME=' + this.finalTime.value;
						form.submit();
										      		
    			} else {
    				Sbi.Msg.showError('Server response is empty', 'Service Error');
    			}
	    	}
	    	, failure: Sbi.exception.ExceptionHandler.onServiceRequestFailure
	    	, scope: this     
	    });
		//call by submit to download really
  		/*
		form.action = params.URL +  '&PREFIX=' + params.PREFIX +  '&DIRECTORY=' + params.DIRECTORY + 
					  '&BEGIN_DATE=' + this.initialDate.value + '&END_DATE=' + this.finalDate.value + 
					  '&BEGIN_TIME=' + this.initialTime.value + '&END_TIME=' + this.finalTime.value;
		form.submit();
	*/
	}

    
    // private methods

    , initFormPanel: function() {
    	
    	this.initialDate = new Ext.form.DateField({
            fieldLabel: LN('sbi.console.downloadlogs.initialDate') 
          , width: 150
          , format: 'd/m'
          , allowBlank: false
        });
    	 
    	this.finalDate = new Ext.form.DateField({
            fieldLabel: LN('sbi.console.downloadlogs.finalDate')            			   
          , width: 150
          , format: 'd/m'
          , allowBlank: false
        });
    	 
    	this.initialTime = new Ext.form.TimeField({
    		 					 fieldLabel: LN('sbi.console.downloadlogs.initialTime') 
    		 				   , width: 150
    						   , increment: 30
    						   , format: 'H:i'
    						});
    	 
    	 
    	this.finalTime = new Ext.form.TimeField({
					    	   fieldLabel: LN('sbi.console.downloadlogs.finalTime') 
					    	 , width: 150
							 , increment: 30
							 , format: 'H:i'
    						});
    	
    	this.formPanel = new  Ext.FormPanel({
    		  title:  LN('sbi.console.downloadlogs.title'),
    		  margins: '50 50 50 50',
	          labelAlign: 'left',
	          bodyStyle:'padding:5px',
	          width: 850,
	          height: 600,
	          layout: 'form',
	          trackResetOnLoad: true,
	          items: [this.initialDate, this.initialTime, this.finalDate, this.finalTime]
	      });
    	 
    }
    
    , checkParameters: function(){
    	if (this.initialDate.getValue() === undefined ||  this.initialDate.getValue() === ''){
			Sbi.Msg.showWarning( LN('sbi.console.downloadlogs.initialDateMandatory'));
			this.initialDate.focus();
			return false;
		}
		if (this.initialTime.getValue() === undefined ||  this.initialTime.getValue() === ''){
			Sbi.Msg.showWarning( LN('sbi.console.downloadlogs.initialTimeMandatory'));
			this.initialTime.focus();
			return false;
		}
		if (this.finalDate.getValue() === undefined ||  this.finalDate.getValue() === ''){
			Sbi.Msg.showWarning( LN('sbi.console.downloadlogs.finalDateMandatory'));
			this.finalDate.focus();
			return false;
		}
		if (this.finalTime.getValue() === undefined ||  this.finalTime.getValue() === ''){
			Sbi.Msg.showWarning( LN('sbi.console.downloadlogs.finalTimeMandatory'));
			this.finalTime.focus();
			return false;
		}
		if (this.initialDate.getValue() > this.finalDate.getValue()){
			Sbi.Msg.showWarning( LN('sbi.console.downloadlogs.rangeInvalid'));
			this.initialDate.focus();
			return false;
		}
		return true;
    }
    
});