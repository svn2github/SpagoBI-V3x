app.views.ParametersView = Ext.extend(
		Ext.Panel,
		{
			fullscreen: true,
			style: 'background-color: #747474;',


			initComponent : function() {
				this.html = '  ';
				app.views.ParametersView.superclass.initComponent.apply(this, arguments);

			}

			,refresh : function(items) {

				this.removeAll();
	
				var fieldset = new Ext.form.FieldSet({
						title : 'Document Parameters',
						xtype : 'fieldset',

						items : items
				});


				
				var formPanel = new Ext.form.FormPanel({
						
					autoRender : true,
					floating : true,
					modal : false,
					centered : true,
					height : 500,
					width : 600,
					scroll: 'vertical',
					hideOnMaskTap : false,
					items: [fieldset],
					dockedItems : [ {
						xtype : 'toolbar',
						dock : 'bottom',
						defaults : {
							ui : 'plain',
							iconMask : true
						},
						scroll : 'horizontal',
						layout : {
							pack : 'center'
						},
						items : [
						         {
						        	 title : 'Home',
						        	 iconCls : 'reply',
						        	 text : 'Home',
						        	 handler : function() {
						        		 Ext
						        		 .dispatch({
						        			 controller : app.controllers.mobileController,
						        			 action : 'backToBrowser'
						        		 });

						        	 }
						         },
						         {
						        	 title : 'Execute',
						        	 iconCls : 'settings',
						        	 text : 'Execute',
						        	 handler : function() {
						        		 var executionInstance = app.controllers.parametersController.executionInstance;
						        		 executionInstance.PARAMETERS = app.controllers.parametersController.getFormState();
						        		 Ext.dispatch({
						        			 controller : app.controllers.executionController,
						        			 action : 'executeTemplate',
						        			 executionInstance : executionInstance
						        		 });
						        	 }
						         } ]

					} ]
				});
				formPanel.show();
				this.insert(0,formPanel);

				this.doLayout();

				

			}
});