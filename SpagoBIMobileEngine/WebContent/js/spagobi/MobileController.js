app.controllers.MobileController = Ext.extend(Ext.Controller,
	{

	login: function(options) 
	  {
		console.log('MobileController: Received event of login successfull');
		var viewport = app.views.viewport;
		viewport.setActiveItem(app.views.main, { type: 'slide', direction: 'left' });

	  }
	, showDetail: function(record) 
	{
		var id = record.record.id;
		//determines if it is a document available for mobile presentetation
		var rec = record.record.attributes.record.data;
		var engine = rec.engine;
		var name= rec.name;
		var descr= rec.description;
		var date= rec.creationDate;
		if(engine != null && engine !== undefined && engine == 'Mobile Engine'){
			
			//onClick="executeDocument();"
			var documentTpl = '<div class="preview-item" id="preview-'+id+'" '+
			'onClick="javascript: executeDocument('+id+');">' +
			'<div class="document-item-icon">' +			
			'<img src="' + Ext.BLANK_IMAGE_URL + '" ></img>' +
			'</div>' +
		    '<div class="item-desc">' +name+ '</div>'+
		    '<div class="item-desc"><b>engine: </b>' +engine+ '</div>'+
		    '<div class="item-desc"><b>description: </b>' +descr+ '</div>'+
		    '<div class="item-desc">' +date+ '</div>'+
		    '</div>';
			app.views.preview.showPreview( documentTpl);

		}else{
			app.views.preview.showPreview( '');
		}
		
	}
	, executeDocument: function(options) 
	  {
		alert(options.id);
		
		var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
		this.services = new Array();
		this.services['executeDocumentService'] = Sbi.config.serviceRegistry.getServiceUrl({
			serviceName: 'EXECUTE_ACTION'
			, baseParams: params
		});
		/*
		Ext.Ajax.request({
            url: this.services['executeDocumentService'],
            scope: this,
            method: 'post',
            //params: {folderId: id},
            success: function(response, opts) {
          	  if(response.responseText.indexOf('<') == -1){
              	  var content = Ext.util.JSON.decode( response.responseText );	              		      			 
              	  var items = content.folderContent[0];
              	  this.data = items;
              	  
                  app.views.browser.store .getRootNode().removeAll();
                  
                  app.views.browser.store.root =this.data;
                  app.views.browser.store .sync();
                  app.views.browser.store .load();


                  
          	  }else{
          		  alert('cannot produce tree!');
              	  return;
          	  }
            }
	    }); 
	    */ 
	  }

});
