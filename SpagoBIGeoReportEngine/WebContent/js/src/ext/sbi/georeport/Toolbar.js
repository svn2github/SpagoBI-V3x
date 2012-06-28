/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
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
  * -  Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.georeport");

Sbi.georeport.Toolbar = function(config) {
	
	var defaultSettings = {
		//title: LN('sbi.qbe.queryeditor.title'),
	};
		
	if(Sbi.settings && Sbi.settings.georeport && Sbi.settings.georeport.toolbar) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.georeport.toolbar);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
		
	this.initBar();
	if(false) {
		this.initButtons();
	}
	
	c = Ext.apply(c, {
		map: this.map, 
		configurable: false,      	
	   	items: this.tbButtons
	});

	// constructor
	Sbi.georeport.Toolbar.superclass.constructor.call(this, c);

};

Ext.extend(Sbi.georeport.Toolbar, mapfish.widgets.toolbar.Toolbar, {
    
    tbButtons: null
    , importWMSLayerWin: null
   
   
    // public methods
    
    // private methods
    , initBar: function() {
		this.loadingButton = new Ext.Toolbar.Button({
			tooltip: 'Please wait',
        	iconCls: "x-tbar-loading"
		});
		this.tbButtons = [this.loadingButton, ' Loading toolbar...'];
		this.loadingButton.disable();
	}

	, addSeparator: function(){
	    this.add(new Ext.Toolbar.Spacer());
	    this.add(new Ext.Toolbar.Separator());
	    this.add(new Ext.Toolbar.Spacer());
	} 
	
	, initButtons: function() {
		this.items.each( function(item) {
			this.items.remove(item);
            item.destroy();           
        }, this); 
		
		
		this.initZoomButtonGroup();
		this.initHistoryButtonGroup();
		this.initDrawButtonGroup();
		this.initMeasureButtonGroup();
		this.initImportButtonGroup();
				
	    // test stampa
	  
		/*
	    var printConfigUrl = mapfish.SERVER_BASE_URL + 'pdf/info.json';
	    this.add(
	    	new mapfish.widgets.print.PrintAction({
	            map: this.map,
	            overrides: this.layers,
	            configUrl: printConfigUrl	            
	    	})
		);
		*/
	          
	    this.activate();
	}
	
	, initZoomButtonGroup: function() {
		if(this.zoomToMaxButtonEnabled === true) {
			this.addControl(
				new OpenLayers.Control.ZoomToMaxExtent({
			       	map: this.map,
			        title: 'Zoom to maximum map extent'
			    }), {
		            iconCls: 'zoomfull', 
		            toggleGroup: 'map'
		        }
		    );
				
		    this.addSeparator();
		}
		    
		if(this.mouseButtonGroupEnabled === true) {
						
			this.addControl(
				new OpenLayers.Control.ZoomBox({
					title: 'Zoom in: click in the map or use the left mouse button and drag to create a rectangle'
		        }), {
			    	iconCls: 'zoomin', 
		            toggleGroup: 'map'
				}
		    );
		      
			this.addControl(
				new OpenLayers.Control.ZoomBox({
					out: true,
		            title: 'Zoom out: click in the map or use the left mouse button and drag to create a rectangle'
		        }), {
					iconCls: 'zoomout', 
		            toggleGroup: 'map'
		        }
		    );
			this.featureHandler.activate();
		          
			this.addControl(
				new OpenLayers.Control.DragPan({
					isDefault: true,
		            title: 'Pan map: keep the left mouse button pressed and drag the map'
		        }), {
		            iconCls: 'pan', 
		            toggleGroup: 'map'
		        }
		    );
			
			var btn = this.addControl(
				this.analysisLayerSelectControl
				, {
					iconCls: 'select', 
					toggleGroup: 'map',
					tooltip: "Select fetaure: keep the right mouse button pressed in order to draw the selctiona area"
			});
			
			btn.on('toggle', function(btn, pressed ) {
				this.selectMode = pressed;
				if(!pressed) {
					this.featureHandler.activate();
				} else {
					this.featureHandler.deactivate();
				}
			}, this);
		         
			this.addSeparator();
		}
	}
	
	, initDrawButtonGroup: function() {		          
	    if(this.drawButtonGroupEnabled === true) {
	    	
	    	var vectorLayer = new OpenLayers.Layer.Vector("vector", { 
		    	displayInLayerSwitcher: false
		    });
		    this.map.addLayer(vectorLayer);
		    
	    	this.addControl(
	    		new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Point, {
	    			title: 'Draw a point on the map'
	            }), {
	    			iconCls: 'drawpoint', 
	                toggleGroup: 'map'
	            }
	        );
	          
			this.addControl(
				new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Path, {
					title: 'Draw a linestring on the map'
				}), {
	                iconCls: 'drawline', 
	                toggleGroup: 'map'
				}
	        );
	          
			this.addControl(
				new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
					title: 'Draw a polygon on the map'
	            }), {
	                iconCls: 'drawpolygon', 
	                toggleGroup: 'map'
	            }
	        );
	          
			this.addSeparator();
	    }
	}
	
	, initHistoryButtonGroup: function() {
		if(this.historyButtonGroupEnabled === true) {
	    	var nav = new OpenLayers.Control.NavigationHistory();
	        this.map.addControl(nav);
	        nav.activate();
	          
	        this.add(
	        	new Ext.Toolbar.Button({ 
	        		iconCls: 'back',
	                tooltip: 'Previous view', 
	                handler: nav.previous.trigger
	            })
	         );
	          
	         this.add(
	        	new Ext.Toolbar.Button({
	        		iconCls: 'next',
	                tooltip: 'Next view', 
	                handler: nav.next.trigger
	            })
	         );
	          
	         this.addSeparator();
	    }
	}
	
	, initImportButtonGroup: function() {
		if(this.wmsGroupEnabled === true) {
			this.addControl(
				new OpenLayers.Control.Button({
					isDefault: false,
					title: 'Add WMS Layer'
				})
				, {
					iconCls: 'wmsAdd', 
					toggleGroup: 'map',
					handler: this.showImportWMSLayerWin,
					scope: this
				}
			);   
		
			this.addSeparator();
		}    
	}
	
	, initMeasureButtonGroup: function() {
		
		if(this.measureButtonGroupEnabled === true) {
		
			// SPECIFICHE GRAFICISMI PER MISURAZIONI
		    var style = new OpenLayers.Style();

		    var styleMap = new OpenLayers.StyleMap({"default": style});
		    
		    function handleMeasurements(event) {
		            var geometry = event.geometry;
		            var units = event.units;
		            var order = event.order;
		            var measure = event.measure;
		            var element = Ext.getCmp('mapOutput'); 
		            var out = "";
		            if(order == 1) {
		                out += "Distanza: " + measure.toFixed(3) + " " + units;
		                if (this.map.getProjection() == "EPSG:4326") {
		                    out += ", Great Circle Distance: " + 
		                        calcVincenty(geometry).toFixed(3) + " km"; 
		                }        
		            } else {
		                out += "<span class='mapAreaOutput'>Area: " + measure.toFixed(3) + " " + units + "<sup style='font-size:6px'>2</" + "sup></span>";
		            }
		            element.body.dom.innerHTML = out;
		    };
		        
		    function calcVincenty(geometry) {
		        	/*Note: this function assumes geographic coordinates and  will fail otherwise.  OpenLayers.Util.distVincenty takes two objects representing points with geographic coordinates
			  		and returns the geodesic distance between them (shortest  distance between the two points on an ellipsoid) in *kilometers*.It is important to realize that the segments drawn on the map
			  		are *not* geodesics (or "great circle" segments).  This means that in general, the measure returned by this function  will not represent the length of segments drawn on the map. */
			        var dist = 0;
		            for (var i = 1; i < geometry.components.length; i++) {
		                var first = geometry.components[i-1];
		                var second = geometry.components[i];
		                dist += OpenLayers.Util.distVincenty(
		                    {lon: first.x, lat: first.y},
		                    {lon: second.x, lat: second.y}
		                );
		            }
		            return dist;
		    };  
		        
		    var optionsLine = {
		       	handlerOptions: {
		    		style: "default", // this forces default render intent
		       		layerOptions: {styleMap: styleMap},
		        	persist: true
		        },
		        displayClass: "olControlMeasureDistance"          
		    };

		    var optionsPolygon = {
		       	handlerOptions: {
		       		style: "default", // this forces default render intent
		       		layerOptions: {styleMap: styleMap},
		       		persist: true
		       	},
		       	displayClass: "olControlMeasureArea"     
		    };

	        measureControls = {
	        	line: new OpenLayers.Control.Measure(
	        		OpenLayers.Handler.Path, 
		        	optionsLine
		        ),
		        polygon: new OpenLayers.Control.Measure(
		        	OpenLayers.Handler.Polygon, 
		        	optionsPolygon
		        )
		    };
		                
		    for(var key in measureControls) {
		       	control = measureControls[key];
		       	control.events.on({
		       		"measure": handleMeasurements,
		       		"measurepartial": handleMeasurements
		       	});
		    }  
		   
		    this.addControl(            
		       	measureControls.line,{
		       		tooltip: 'Misura Distanze',
		       		iconCls: 'meaLinee', 
		       		toggleGroup: 'map'    
		       	}
		    );
		  
		    this.addControl(            
		       	measureControls.polygon,{                
		        	tooltip: 'Misura Area',
		        	iconCls: 'meaArea', 
		        	toggleGroup: 'map'
		        }
		    );
		   
		    this.addSeparator();  
		}
	}
	
	
	, showImportWMSLayerWin: function() {
		if(this.importWMSLayerWin === null) {
			var importWMSLayerForm = new Sbi.georeport.ImportWMSLayerForm();
			this.importWMSLayerWin = new Ext.Window({
			 	title		: 'Aggiunge un layer WMS (REFACTORED)',
			  	layout      : 'fit',
	            width       : 610,
	            height      : 300,
	            closeAction : 'hide',
	            plain       : true,
	            buttons		: [
	            {
	            	
	            	text: 'Add to map' ,
			    	handler: function(){
	            		var f = this.importWMSLayerWin.innerForm;
	            		var layers = f.selectionModel.getSelections(); 
	            		for(i = 0; i < layers.length; i++){
	            			var layer = layers[i];
	            			var layerName = layer.get('layername');
			    	  		var layerUrl = f.wmsLayerUrl.getValue();
			    	  		this.addWMSLayer(layerName, layerUrl);
	            		}
	            		
	            		this.importWMSLayerWin.hide();
			      	},
			      	scope: this
			    
	            },{      		   
	            	
	            	text: 'Close',
			    	handler: function(){
	            		this.importWMSLayerWin.hide();
			      	},
			      	scope: this
			    
			    }],
			    items		: [importWMSLayerForm]
			});
			this.importWMSLayerWin.innerForm = importWMSLayerForm;
			
			
		}
		this.importWMSLayerWin.show();
	}
	
	, addWMSLayer: function(layerName, layerUrl) {

		var newLayer = new OpenLayers.Layer.WMS(
				layerName, 
				layerUrl, 
				{
					layers: layerName,                    
			   		srs: 'EPSG:4326',
			   		format: 'image/png',
			   		transparent: true
				}, {
			   		singleTile: true, 
			   		ratio: 1,
			   		visibility: true, 
			   		'isBaseLayer': false, 
			   		opacity: 0.5
				} 
		);
			  
		this.map.addLayer(newLayer);
		
		if(this.map.layerTree) {
			
			var layerNode = {
				layerName: layerName,
	            text: layerName,
	            leaf: true,
	            checked: true,
	            layerNames: [layerName]
	        };
			this.map.layerTree.model.push(layerNode);
			this.map.layerTree._updateCachedObjects();
			
			var rootNode = this.map.layerTree.getRootNode();
			rootNode.appendChild( layerNode );
			//rootNode.attributes.layerNames.push(layerName);
			
		} else {
			alert('no model in map');
		}
		
	}
	 
});