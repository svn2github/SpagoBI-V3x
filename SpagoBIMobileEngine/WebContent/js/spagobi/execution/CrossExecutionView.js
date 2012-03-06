﻿app.views.CrossExecutionView = Ext.extend(Ext.Panel,
	{
	fullscreen: true,
	layout: 'card',	
    sortable: false,
	cardSwitchAnimation: 'slide'
	,ui : 'dark'
	,breadCrumbs: new Array()

	,initComponent: function() 	  {
		this.breadCrumbs = new Array();
		this.docHome = {
		    title: 'Home',    		    
		    iconCls: 'reply',			    
		    text: 'Home',
            handler: function () {
        		Ext.dispatch({
                    controller: app.controllers.mobileController,
                    action: 'backToBrowser',
                    fromCross: true
        		});

            }};
		this.toolbarForCross = new Ext.Toolbar({xtype: 'toolbar',
	        dock: 'bottom',
	        defaults: {
	            ui: 'plain',
	            iconMask: true
	        },
	        scroll: 'horizontal',
	        layout: {
	            pack: 'center'
	        }
	        ,items:[this.docHome]
		});
		this.dockedItems=[this.toolbarForCross];
	    app.views.CrossExecutionView.superclass.initComponent.apply(this, arguments);

	}
	, setBreadCrumb: function(objectLabel, 
								objectId,
								typeCode,
								parameters){ 
		var current = this.breadCrumbs.indexOf(objectLabel);
		if(current == -1){

			this.breadCrumbs.push(objectLabel);
			var pos = this.breadCrumbs.length;

			this.toolbarForCross.insert(pos,{
				title: objectLabel,    		    
			    iconCls: 'arrow_left',			    
			    text: objectLabel,
			    disabled: true,
	            handler: function () {
	  			Ext.dispatch({
					  controller: app.controllers.mobileController,
					  action: 'getRoles',
					  label: objectLabel, 
					  id: objectId,
					  typeCode: typeCode,
					  parameters: parameters,
					  isFromCross: true
				});
	
	            }
			});
			//disables current link
			//enables previous
			var idxPrev = pos-1;
			if(idxPrev != 0){
				var prev = this.toolbarForCross.items.items[idxPrev];
				prev.setDisabled(false);
			}
			this.toolbarForCross.doLayout();
			this.doLayout();
		}else{
			//on the way back
			var curr = this.toolbarForCross.items.items[current+1];	
			if(curr == undefined ){
				this.breadCrumbs = new Array();
				return;
			}
			for(i =1; i<this.breadCrumbs.length+1; i++){
				var other = this.toolbarForCross.items.items[i];
				other.setDisabled(false);
			}
			curr.setDisabled(true);
			this.toolbarForCross.doLayout();
			this.doLayout();
		}

	}
	,clearNavigation: function(){
		this.breadCrumbs = new Array();
		this.toolbarForCross = new Ext.Toolbar({xtype: 'toolbar',
	        dock: 'bottom',
	        defaults: {
	            ui: 'plain',
	            iconMask: true
	        },
	        scroll: 'horizontal',
	        layout: {
	            pack: 'center'
	        }
	        ,items:[this.docHome]
		});
	}
});
		