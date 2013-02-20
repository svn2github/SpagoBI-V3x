/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
/**
  * Object name 
  * 
  * Singleton object that handle all errors generated on the client side
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


Ext.ns("Sbi.exception.ExceptionHandler");

Sbi.exception.ExceptionHandler = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
	var loginUrl = Sbi.config.loginUrl;

	// public space
	return {
	
		init : function() {
			//alert("init");
		},
		
		
        handleFailure : function(response, options) {
        	
        	var errMessage = ''
        	if(response !== undefined) {
        		if (response.responseText !== undefined) {
        			try{
        				var content = Ext.util.JSON.decode( response.responseText );
        			}catch(e){
        				var content =Ext.JSON.decode( response.responseText );
        			}
        			
        			if (content.errors !== undefined  && content.errors.length > 0) {
        				if (content.errors[0].message === 'session-expired') {
        					// session expired
        		        	Sbi.exception.ExceptionHandler.redirectToLoginUrl();
        		        	return;
        				} else {
        					for (var count = 0; count < content.errors.length; count++) {
        						var anError = content.errors[count];
			        			if (anError.localizedMessage !== undefined && anError.localizedMessage !== '') {
			        				errMessage += anError.localizedMessage;
			        			} else if (anError.message !== undefined && anError.message !== '') {
			        				errMessage += anError.message;
			        			}
			        			if (count < content.errors.length - 1) {
			        				errMessage += '<br/>';
			        			}
        					}
        				}
        			}
        		} else {
        			errMessage = 'Generic error';
        		}
        	}
        	
        	Sbi.exception.ExceptionHandler.showErrorMessage(errMessage, 'Service Error');
       	
        },
        
        showErrorMessage : function(errMessage, title) {
        	var m = errMessage || 'Generic error';
        	var t = title || 'Error';
        	
        	Ext.MessageBox.show({
           		title: t
           		, msg: m
           		, buttons: Ext.MessageBox.OK     
           		, icon: Ext.MessageBox.ERROR
           		, modal: false
       		});
        },
        
        showWarningMessage : function(errMessage, title) {
        	var m = errMessage || 'Generic warning';
        	var t = title || 'Warning';
        	
        	Ext.MessageBox.show({
           		title: t
           		, msg: m
           		, buttons: Ext.MessageBox.OK     
           		, icon: Ext.MessageBox. WARNING
           		, modal: false
       		});
        },
        
        showInfoMessage : function(errMessage, title, config) {
        	var m = errMessage || 'Info';
        	var t = title || 'Info';
        	
        	Ext.MessageBox.show(Ext.apply({
           		title: t
           		, msg: m
           		, buttons: Ext.MessageBox.OK     
           		, icon: Ext.MessageBox.INFO
           		, modal: false
       		},config||{}));
        },
        
        redirectToLoginUrl: function() {
        	var sessionExpiredSpagoBIJSFound = false;
        	try {
        		var currentWindow = window;
        		var parentWindow = parent;
        		while (parentWindow != currentWindow) {
        			if (parentWindow.sessionExpiredSpagoBIJS) {
        				parentWindow.location = loginUrl;
        				sessionExpiredSpagoBIJSFound = true;
        				break;
        			} else {
        				currentWindow = parentWindow;
        				parentWindow = currentWindow.parent;
        			}
        		}
        	} catch (err) {}
        	
        	if (!sessionExpiredSpagoBIJSFound) {
        		window.location = loginUrl;
        	}
        }
        
        , onStoreLoadException : function(proxy, type, action, options, response, arg) {
        	Sbi.exception.ExceptionHandler.handleFailure(response, options);
        }

	};
}();