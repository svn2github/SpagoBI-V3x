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
  * - Andrea Gioia (andrea.gioia@eng.it)
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.SummaryPanel = function(config) {
	
		var defaultSettings = {
			layout: 'fit'
			, region: 'north'
			, height: 410
			, split: true
			//, collapseMode: 'mini'
			, collapsible: true
	        , collapseFirst: false
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.summaryPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.summaryPanel);
		}
		var widgetPanelConfig = config.layoutManagerConfig || {};
		
		var c = Ext.apply(defaultSettings, config || {});
		widgetPanelConfig.executionContext = c.executionContext;
		Ext.apply(this, c);
		
		widgetPanelConfig.storeManager = this.storeManager;
		widgetPanelConfig.items = [];
		
		var datasets = [];
		var isComposite = false;
		
		//test
		/*
		var d = new Date();
		var start = d.getTime();
		alert('start: ' + start);
		*/
		for(var i = 0, l1 = config.charts.length ; i < l1; i++) {
			if(config.charts[i].widgetConfig.type === 'chart.composite') {
				//composite widget
				isComposite = true;
				
				//sets the general style of the table 
				widgetPanelConfig.layoutConfig = config.layoutManagerConfig.layoutConfig || {};
				widgetPanelConfig.layoutConfig.tableAttrs = config.layoutManagerConfig.layoutConfig.tableAttrs || {style: {width: '100%'}};
				
				var compositeWidgetPanelConfig = {};
				//******** DEFAULTS configuration applied to each contained panel **********
				
				//dataset 			
				var defaultDataset = widgetPanelConfig.defaults.dataset;						
				//width 
				var defaultWidth =  (widgetPanelConfig.defaults.columnWidthDefault === undefined) ? .1 : widgetPanelConfig.defaults.columnWidthDefault;				
				//heigth 
				var defaultHeight =  (widgetPanelConfig.defaults.columnHeightDefault === undefined) ? .1: widgetPanelConfig.defaults.columnHeightDefault;

			
				//**************** configuration about the SINGLE contained panel *************
				 
				//title
				if (config.charts[i].widgetConfig.linkableDoc !== undefined){
					compositeWidgetPanelConfig.title = (config.charts[i].widgetConfig.linkableDoc.text === undefined) ? "": config.charts[i].widgetConfig.linkableDoc.text;
				}else {
					compositeWidgetPanelConfig.title = (config.charts[i].widgetConfig.title === undefined) ? "" :  config.charts[i].widgetConfig.title;			
				}			
				//colspan
				compositeWidgetPanelConfig.colspan = (config.charts[i].widgetConfig.colspan === undefined) ? 1 : config.charts[i].widgetConfig.colspan;				
				//rowspan
				compositeWidgetPanelConfig.rowspan = (config.charts[i].widgetConfig.rowspan === undefined) ? 1 : config.charts[i].widgetConfig.rowspan;
				//dataset 
				componentDataset = config.charts[i].widgetConfig.dataset;
				//width 
				var componentWidth = config.charts[i].widgetConfig.width;								
				//height 
				var componentHeight = config.charts[i].widgetConfig.height;	
				
				compositeWidgetPanelConfig.storeManager = this.storeManager;
				compositeWidgetPanelConfig.items = [];
								
				for(var j = 0, l2 = config.charts[i].widgetConfig.subcharts.length ; j < l2; j++) {
					var configSubChart = {};
					configSubChart = config.charts[i].widgetConfig.subcharts[j];									
					
					//sets the DATASET; the order for getting values are: single widget, single panel, table
					if (configSubChart.dataset === undefined){
						if (componentDataset !== undefined){
							configSubChart.dataset = componentDataset;
						}
						else{
							configSubChart.dataset = defaultDataset;
						}
					}
					datasets[configSubChart.dataset] = configSubChart.dataset ;
					
					//sets the WIDTH of single element; the order for getting values are: single widget, single panel, table
					if (configSubChart.width === undefined){
						if (componentWidth !== undefined){
							configSubChart.width = componentWidth/l2; //divides total space by the number of elements
						}
						else{
							configSubChart.width = defaultWidth/l2; //divides default total space by the number of elements
						}
					}					
					//apply the colspan
					configSubChart.width = (configSubChart.width * compositeWidgetPanelConfig.colspan);				
										
					//percentage dimensions: the single widget occupies the total space; otherwise if the value is not a valid number,
					//deletes the property from configuration (for IE problem) 
					if (configSubChart.width <= 1){						
						configSubChart.width = "100%";								
					}else if (configSubChart.width === undefined || isNaN(configSubChart.width) ){
						delete configSubChart.width;						
					}
					
					//sets the HEIGHT of single element; the order for getting values are: single widget, single panel, table
					if (configSubChart.height === undefined){
						if (componentHeight !== undefined){
							configSubChart.height = componentHeight; 
						}
						else{
							configSubChart.height = defaultHeight;
						}
					}					
					//apply the rowspan
					configSubChart.height = (configSubChart.height * compositeWidgetPanelConfig.rowspan);
				
					//percentage dimensions: the single widget occupies the total space; otherwise if the value is not a valid number,
					//deletes the property from configuration (for IE problem) 
					if (configSubChart.height <= 1){						
						configSubChart.height = "100%";			
					}else if (configSubChart.height === undefined || isNaN(configSubChart.height) ){
						delete configSubChart.height;							
					}
					configSubChart.executionContext = widgetPanelConfig.executionContext;
					
					//sets the dimensions on the parent panel
					compositeWidgetPanelConfig.width = (componentWidth > configSubChart.width)? componentWidth : configSubChart.width;
					compositeWidgetPanelConfig.height = (componentHeight > configSubChart.height)? componentHeight : configSubChart.height;	
					compositeWidgetPanelConfig.linkableDoc = config.charts[i].linkableDoc;
					compositeWidgetPanelConfig.executionContext = widgetPanelConfig.executionContext;
					compositeWidgetPanelConfig.items.push(new Sbi.console.ChartWidget(configSubChart));
				}
				var compositeWidgetPanel = new Sbi.console.WidgetPanel(compositeWidgetPanelConfig);

				widgetPanelConfig.items.push(compositeWidgetPanel);

			} else {
				//simple widget
				datasets[config.charts[i].dataset] = config.charts[i].dataset;
				widgetPanelConfig.items.push(new Sbi.console.ChartWidget(config.charts[i]));

			}
		}		
		
		widgetPanelConfig.autoScroll = {};
		widgetPanelConfig.autoScroll = true;
		var widgetPanel = new Sbi.console.WidgetPanel(widgetPanelConfig);
		
		c = Ext.apply(c, {  	
			items: [widgetPanel]
		});
			
		// constructor
		Sbi.console.SummaryPanel.superclass.constructor.call(this, c);			
		 
		//add task: checks if all widgets have the isSWFReady setted to true. In this case force the refresh of datasets
		var task = {};
		 
			task = {
					run: function(){
				//		alert('run function');
						var allWidgetsReady = false;
						if (!isComposite){							
							//each panel could contains more charts					
							for (var k=0, l3 = widgetPanelConfig.items.length; k < l3; k++){
								var tmpWidget = widgetPanelConfig.items[k].chart;
								if (tmpWidget === undefined) break;
								for (var x=0, l4 = tmpWidget.items.length; x < l4; x++){
									var tmpChart = tmpWidget.items.get(x);
									
									if (tmpChart !== null && tmpChart !== undefined && !tmpChart.isSwfReady ){							
										allWidgetsReady = false;
										break;
									}else {								
										allWidgetsReady = true;								
									}
									
								}
								
								if (!allWidgetsReady){
									break;
								}
							}
						} else{								
							//each panel could contains more charts 
							for (var k=0, l3 = widgetPanelConfig.items.length; k < l3; k++){
								
								for (p = 0; p < widgetPanelConfig.items[k].items.length; p++){
									var tmpWidget = widgetPanelConfig.items[k].items.get(p).chart;
									if (tmpWidget === undefined || tmpWidget === null) break;
									for (var x=0, l4 = tmpWidget.items.length; x < l4; x++){
										var tmpChart = tmpWidget.items.get(x);
										
										if (tmpChart !== null && tmpChart !== undefined && !tmpChart.isSwfReady ){							
											allWidgetsReady = false;
											break;
										}else {								
											allWidgetsReady = true;								
										}										
									}
									
									if (!allWidgetsReady){
										break;
									}
								}
								if (!allWidgetsReady){
									break;
								}
							}								
						} 
						if (allWidgetsReady && k == l3  ) {
							/*
							var d2 = new Date();
							var end = d2.getTime();
							alert('end: ' + end);
							var diff = end - start;
							alert(' - diff: ' + diff);
							*/
							if ( widgetPanelConfig.storeManager !== null && widgetPanelConfig.storeManager !== undefined){	
								for (ds in datasets){
									//sets the single store as refreshable 
									var tmpStore = widgetPanelConfig.storeManager.getStore(ds);
									
									if (tmpStore !== undefined){										
										tmpStore.stopped = false;
									}
								}								
								widgetPanelConfig.storeManager.forceRefresh();
								//stops the task
								Ext.TaskMgr.stop(task);
							}
						}
							
					},
					interval: 10 //milliseconds
				};
			
				//starts the task
				Ext.TaskMgr.start(task);
};
		

Ext.extend(Sbi.console.SummaryPanel, Ext.Panel, {
    
    services: null
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
	
    
});