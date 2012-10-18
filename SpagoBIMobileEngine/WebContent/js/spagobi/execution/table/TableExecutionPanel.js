/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
 app.views.TableExecutionPanel = Ext.extend(app.views.WidgetPanel,

		{
	    scroll: 'vertical'
	    , initComponent: function (options)	{

			console.log('init table execution');


			app.views.TableExecutionPanel.superclass.initComponent.apply(this, arguments);
			
			if(this.IS_FROM_COMPOSED){
				if(app.views.execution.loadingMaskForExec != undefined){
					app.views.execution.loadingMaskForExec.hide();
				}
				this.on('afterlayout',this.showLoadingMask,this);
				
			}
			this.addEvents('execCrossNavigation');
			

		}

		,setTableWidget: function(resp, fromcomposition, fromCross){

		      var store = new Ext.data.Store({
		     		root: 'values'
		     		, fields: resp.features.fields
		      		, pageSize: 5
		      		, data: resp
		     		, proxy: {
			              type: 'memory',	              
			              reader: {
			                  type: 'json',
			                  root: 'values',	 
			                  totalProperty: "total",    
			                  totalCount: 'total'
			              }
		          }
		      });

		      store.load();
		      var toolbarForTable = new Ext.Toolbar({
					xtype : "toolbar",
					dock  : "top",						
					title : resp.features.title.value,
					style:  resp.features.title.style,
					layout: 'hbox',
					autoDestroy : true
	                
				});
		      
		      	var tbConfig = {
					store       : store,
					multiSelect : false,
					dockedItems: [toolbarForTable],
					conditions  : resp.features.conditions,
					colModel    : resp.features.columns,
					scope: this,
			    	listeners: { 
			    		tap: { 
			    			element : 'el',
			    			fn : function(e) {
		      		 			var crossParams = new Array();
	      						var target = e.target;
	      						
	      						this.setCrossNavigation(resp, target, crossParams);
	      						if(crossParams.length != 0){
		      						var targetDoc;
		      						if(resp.features != undefined && resp.features.drill != undefined){
			      						targetDoc = this.setTargetDocument(resp);		      						
		      						}
		      						this.fireEvent('execCrossNavigation', this, crossParams, targetDoc);
	      						}
    						},
    						scope : this
  						}
		      		}
		      	};
				if(fromcomposition || fromCross){
					tbConfig.width='100%';
					tbConfig.height='100%';

				}else{
					tbConfig.bodyMargin='2px 2px 2px 2px';
					tbConfig.fullscreen=true;

				}
				
				app.views.table = new Ext.ux.TouchGridPanel(tbConfig);
				if(fromcomposition){
					  this.insert(0, app.views.table);
				      this.doLayout();
				}
				if(fromCross){
					var r = new Ext.Panel({
						style:'z-index:100;',
						height:'100%',
						items: [app.views.table]
						        
					});
					
					this.insert(0, r);
					r.doLayout();
					this.doLayout();
				}
				if(this.IS_FROM_COMPOSED){
					this.loadingMask.hide();
				}
		}

		, setCrossNavigation: function(resp, target, crossParams){
			
			var drill = resp.features.drill;
			if(drill != null && drill != undefined){
				var params = drill.params;
				
				//captures event e on target				
				var textCell = target.innerText;
				var row = target.parentNode;
				var cellsOfRow = row.cells;
				var rowIdx = row.rowIndex;
				var attributes = target.attributes;
				var columns= new Array();
				var colValues = {};
				for(a=0; a<cellsOfRow.length; a++){
					var cell = cellsOfRow[a];
					for(i = 0; i<cell.attributes.length; i++){
						var at = cell.attributes[i];
						if(at.name == 'mapping'){
							var nCol=at.value;
							colValues[nCol] = cell.textContent;
							
						}
					}
				}
				if(params != null && params != undefined){
					for(i=0; i< params.length; i++){
						var param = params[i];
						var name = param.paramName;
						var type = param.paramType;

						/*	RELATIVE AND ABSOLUTE PARAMETERS ARE MANAGED SERVER SIDE */
						if(type == 'SERIE'){
								var col = colValues[name];
								if(col){
									crossParams.push({name : name, value : col});
								}	
						}else if(type == 'CATEGORY'){
							crossParams.push({name : name, value : column});
						}else{
							crossParams.push({name : name, value : param.paramValue});
						}

					}
				}				
			}
			return crossParams;
		}
		
});