app.views.DocumentBrowser = Ext.extend (Ext.NestedList,
		{	
	    scroll: 'vertical',
	    dock : 'left',
	    layout:'card',
	   
		cardSwitchAnimation: 'slide',
	    backText : '&lt;',
	    store: null,
	    data: null,
	    flex:1,
	    displayField: 'name' || 'label',
	    
		initComponent: function(){
			
			var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
			this.services = new Array();
			this.services['loadFolderContentService'] = Sbi.config.serviceRegistry.getServiceUrl({
				serviceName: 'DOCUMENT_BROWSER_ACTION'
				, baseParams: params
			});
			
			this.store = new Ext.data.TreeStore({
			    model: 'browserItems',
			    proxy: {
					type: 'ajax',
					url: this.services['loadFolderContentService'],
			        reader: {
			            type: 'tree',
			            root: 'samples'
			        }
			    }
			});

			this.store.sync();
			
			app.views.DocumentBrowser.superclass.initComponent.apply(this, arguments);


		}			
		
});