app.controllers.ExecutionController = Ext.extend(Ext.Controller,{
	
	init: function()  {
		var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
		
		this.services = new Array();
		this.services['executeMobileTableAction'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'EXECUTE_MOBILE_TABLE_ACTION'
			, baseParams: params
		});
		
		this.services['executeMobileChartAction'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'EXECUTE_MOBILE_CHART_ACTION'
			, baseParams: params
		});
		
		this.services['executeMobileComposedAction'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'EXECUTE_MOBILE_COMPOSED_ACTION'
			, baseParams: params
		});
		this.services['getDocumentInfoAction'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'GET_DOCUMENT_INFO_ACTION'
			, baseParams: params
		});
	}
	, getDocumentInfoForCrossNavExecution: function(options){
		var targetDoc = options.targetDoc;
		var params = options.params;
		Ext.Ajax.request({
	        url: this.services['getDocumentInfoAction'],
	        scope: this,
	        method: 'post',
	        params: {OBJECT_LABEL: targetDoc},
	        success: function(response, opts) {
	        	if(response!=undefined && response!=null && response.responseText!=undefined && response.responseText!=null){
	        		var resp = Ext.decode(response.responseText);
	        		var doc = resp['document'];
	        		var type = doc.typeCode;
		  			Ext.dispatch({
						  controller: app.controllers.mobileController,
						  action: 'getRoles',
						  id: doc.id,
						  label: doc.label, 
						  engine: doc.engine, 
						  typeCode: doc.typeCode,
						  parameters: params,
						  isFromCross: true
					});
	        	}
	        }
	    });
	}
	, executeTemplate: function(option, documentContainerPanel){

		var executionInstance = option.executionInstance;
		var typeCode =  executionInstance.TYPE_CODE;
		var engine =  executionInstance.ENGINE;
		
		var params = Ext.apply({}, executionInstance);
		params.PARAMETERS =  Ext.encode(executionInstance.PARAMETERS);

		if(typeCode != null && typeCode !== undefined && (typeCode == 'MOBILE_TABLE')){

			Ext.Ajax.request({
		        url: this.services['executeMobileTableAction'],
		        scope: this,
		        method: 'post',
		        params: params,
		        success: function(response, opts) {
		        	if(response!=undefined && response!=null && response.responseText!=undefined && response.responseText!=null){
		        		var resp = Ext.decode(response.responseText);
		        		this.createWidgetExecution(resp, 'table', documentContainerPanel, executionInstance);
		        	}
		        }
		    }); 
		}else if(typeCode != null && typeCode !== undefined && (typeCode == 'MOBILE_CHART')){
			Ext.Ajax.request({
		        url: this.services['executeMobileChartAction'],
		        scope: this,
		        method: 'post',
		        params: params,
		        success: function(response, opts) {
		        	if(response!=undefined && response!=null && response.responseText!=undefined && response.responseText!=null){
		        		var resp = Ext.decode(response.responseText);
		        		this.createWidgetExecution(resp, 'chart', documentContainerPanel, executionInstance);
		        	}
		        }
		    }); 
		}else if(typeCode != null && typeCode !== undefined && (typeCode == 'MOBILE_COMPOSED')){
			Ext.Ajax.request({
		        url: this.services['executeMobileComposedAction'],
		        scope: this,
		        method: 'post',
		        params: params,
		        success: function(response, opts) {
		        	if(response!=undefined && response!=null && response.responseText!=undefined && response.responseText!=null){
		        		var resp = Ext.decode(response.responseText);
		        		resp.executionInstance = params;
		        		this.createWidgetExecution(resp,  'composed', null, executionInstance);
		        	}
		        }
		    }); 
		}
	}
	, crossNavigationManagement: function(resp, type, executionInstance){
		var crumb = new app.views.ExecutionView({parameters: executionInstance.PARAMETERS});
		
		if(app.views.crossExecView == undefined || app.views.crossExecView == null){
			//when executing back to home this will be destroyed
			app.views.crossExecView = new app.views.CrossExecutionView();
			app.views.crossExecView.add(app.views.parameters);
		}
		app.views.execView = crumb;
		
		app.views.crossExecView.setBreadCrumb(crumb);
		app.views.crossExecView.add(crumb);
		crumb.setWidget(resp, type);
		crumb.hideBottomToolbar();
		app.views.crossExecView.setActiveItem(crumb, { type: 'fade', direction: 'left' });
		app.views.viewport.add(app.views.crossExecView);
		app.views.viewport.setActiveItem(app.views.crossExecView, { type: 'slide', direction: 'left' });

	}
	, simpleNavigationManagement: function(resp, type, executionInstance){
		app.views.execView = new app.views.ExecutionView({parameters: executionInstance.PARAMETERS});
		app.views.execView.showBottomToolbar();
	    var viewport = app.views.viewport;	    
	    viewport.add(app.views.execView);	    
	    app.views.execView.setWidget(resp, type);
	    viewport.setActiveItem(app.views.execView, { type: 'slide', direction: 'left' });
	}

	, createWidgetExecution: function(resp, type, documentContainerPanel, executionInstance){

		if (documentContainerPanel == undefined || documentContainerPanel == null) {

			if(executionInstance.isFromCross){
				//cross navigation
				this.crossNavigationManagement(resp, type, executionInstance);
			}else{
				//default navigation
				this.simpleNavigationManagement(resp, type, executionInstance);
			}
			app.views.execView.setExecutionInstance(executionInstance);
		} else {
			app.views.execView.setWidgetComposed(resp, type, documentContainerPanel);
			documentContainerPanel.setExecutionInstance(executionInstance);
		}
		
		
	}

});