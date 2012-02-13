app.controllers.ExecutionController = Ext.extend(Ext.Controller,{
	
	init: function()  {
		var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
		
		this.services = new Array();
		this.services['executeMobileTableAction'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'EXECUTE_MOBILE_TABLE_ACTION'
			, baseParams: params
		});
		
		this.services['prepareDocumentForExecution'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'PREPARE_DOCUMENT_FOR_EXECUTION_ACTION'
			, baseParams: params
		});
	}

	, prepareDocumentForExecution: function(option){
		var params = Ext.apply({PARAMETERS: Ext.encode(option.parameters)},option.executionInstance);
		if(option.parameters!=undefined && option.parameters!=null){
			Ext.Ajax.request({
		        url: this.services['prepareDocumentForExecution'],
		        scope: this,
		        method: 'post',
		        params: params,
		        success: function(response, opts) {
		        	this.executeTemplate(option);
		        }
		    }); 
		}else{
			this.executeTemplate(option);
		}
	}
	
	, executeTemplate: function(option){

		var typeCode =  option.executionInstance.TYPE_CODE;
		var engine =  option.executionInstance.ENGINE;
		
		var params = Ext.apply({PARAMETERS: Ext.encode(option.parameters)},option.executionInstance);
		
		if((engine == 'TableMobileEngine' || engine == 'Table Mobile Engine') && typeCode =='MOBILE'){
			Ext.Ajax.request({
		        url: this.services['executeMobileTableAction'],
		        scope: this,
		        method: 'post',
		        params: params,
		        success: function(response, opts) {
		        	if(response!=undefined && response!=null && response.responseText!=undefined && response.responseText!=null){
		        		var resp = Ext.decode(response.responseText);
		        		this.createTableExecution(resp, params.PARAMETERS);
		        	}
		        }
		    }); 
		}else if((engine == 'ChartMobileEngine' || engine == 'Chart Mobile Engine') && typeCode =='MOBILE'){
			//put code here for chart execution action
		}else if((engine == 'ComposedMobileEngine' || engine == 'Composed Mobile Engine') && typeCode =='MOBILE'){
			//put code here for composed mobile execution action
		}
	}
	
	, createTableExecution: function(resp, parameters){
		
		//these are settings for table object
		app.views.execView = new app.views.ExecutionView({parameters: parameters});
	    //adds execution view directly to viewport
	    var viewport = app.views.viewport;
	    viewport.add(app.views.execView);
	    app.views.execView.setWidget(resp, 'table');

	    viewport.setActiveItem(app.views.execView, { type: 'slide', direction: 'left' });
	}


});