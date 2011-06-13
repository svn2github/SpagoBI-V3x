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
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Antonella Giachino(antonella.giachino@eng.it)
  */

Ext.ns("Sbi.engines.chart");

Sbi.engines.chart.GenericChartPanel  = function(config) { 
	var defaultSettings = {
			border: false
	};

	var c = Ext.apply(defaultSettings, config || {});
	c.storeId = c.dsLabel;
	
	Ext.apply(this, c);

	//constructor
	Sbi.engines.chart.GenericChartPanel.superclass.constructor.call(this, c);	 	
};

Ext.extend(Sbi.engines.chart.GenericChartPanel, Ext.Panel, {
	loadMask: null,
	storeManager: null,				//the store manager
	store: null,					//the store
	categoryAliasX: [],
	categoryAliasY: [],
	serieAlias: []
	
	/**
	 * Loads the data for the chart.. Call the action which loads the data 
	 * (uses the test method of the manageDataset class) 
	 */
	, loadChartData: function(dataConfig){
		//this.showMask();
		this.setCategoryAliasX(dataConfig);
		this.setCategoryAliasY(dataConfig);
		this.setSerieAlias(dataConfig);
		
		
		var requestParameters = {
			    id: dataConfig.dsId
			  , label: dataConfig.dsLabel
			  , refreshTime: dataConfig.refreshTime || 0
			  , dsTypeCd: dataConfig.dsTypeCd
			  , pars: dataConfig.dsPars
			  , trasfTypeCd: dataConfig.dsTransformerType
		}
		var datasets = [];
		datasets.push(requestParameters);	
		this.initStore(datasets, dataConfig.dsId);
	}
	

	, setCategoryAliasX: function(dataConfig) {
		//alert("setCategoryAliasX.dataConfig: " +dataConfig.toSource());
		if(dataConfig.xAxis != undefined){
			if(dataConfig.xAxis.length != undefined){
				for(var i=0; i< dataConfig.xAxis.length; i++){
					var alias = dataConfig.xAxis[i].alias;
					if(alias != undefined){
						this.categoryAliasX[i]=alias;
					}
				}
			}else{
				//single axis
				var alias = dataConfig.xAxis.alias;
				if(alias != undefined){
					this.categoryAliasX[0]=alias;
				}
			}	
		}
	}
		
	, setCategoryAliasY: function(dataConfig) {
		//alert("setCategoryAliasY.dataConfig: " +dataConfig.toSource());
		if(dataConfig.yAxis != undefined){
			if(dataConfig.yAxis.length != undefined){
				for(var i=0; i< dataConfig.yAxis.length; i++){//it's an array
					var alias = dataConfig.yAxis[i].alias;
					if(alias != undefined){
						this.categoryAliasY[i]=alias;
					}
				}
			}else{
				//single axis
				var alias = dataConfig.yAxis.alias;
				if(alias != undefined){
					this.categoryAliasY[0]=alias;
				}
			}

		}	
	}
	
	, setSerieAlias: function(dataConfig){
		//alert("setSerieAlias.dataConfig: " +dataConfig.toSource());
		//checks series configuration
		if (dataConfig.series){
			var strValue = dataConfig.series;
			if (Ext.isArray(strValue)){
				var str = "";
				for(var i = 0; i < strValue.length; i++) {
					str += strValue[i].alias;
					if (i < (strValue.length-1)) str += ",";
				}
				if (str) {
					this.serieAlias = str.split(",");
				}
			}
		}
		
		//checks plotOptions.series configuration			
		if(this.serieAlias .length == 0 && dataConfig.plotOptions && dataConfig.plotOptions.series){
			var str = dataConfig.plotOptions.series.alias;
			if (str) {
				this.serieAlias = str.split(",");
			}
		}
	}
		
	/**
	 * Load the categories for the chart
	 */
	, getCategoriesX: function(){
		
		if(this.store!=null){
		   	var categories = [];
		   	for(var j =0; j< this.categoryAliasX.length; j++){
		    	var catColumn = this.store.getFieldNameByAlias(this.categoryAliasX[j]);
				var records = this.store.getRange();
				var categoriesPerColumn = [];
				var cont = 0;
		    	for (var i = 0; i < records.length; i++) {
		    		var rec = records[i];
					if(rec ) {
						var posValue = categoriesPerColumn.indexOf(rec.get(catColumn));
						if (posValue == -1){
							categoriesPerColumn[cont]= rec.get(catColumn);
							cont++;	
						}
					}
		        }
		    	categories[j] = categoriesPerColumn;
		   	}


			return  categories;
		}
	}
	 , getCategoriesY: function(){
			
			if(this.store!=null){
			   	var categories = [];
			   	for(var j =0; j< this.categoryAliasY.length; j++){
			    	var catColumn = this.store.getFieldNameByAlias(this.categoryAliasY[j]);
					var records = this.store.getRange();
					var categoriesPerColumn = [];
					var cont = 0;
			    	for (var i = 0; i < records.length; i++) {
			    		var rec = records[i];
						if(rec) {
							var posValue = categoriesPerColumn.indexOf(rec.get(catColumn));
							if (posValue == -1){
								categoriesPerColumn[cont]= rec.get(catColumn);
								cont++;	
							}
						}
			        }
			    	categories[j] = categoriesPerColumn;
			   	}


				return  categories;
			}
		}
	/**
	 * Loads the series for the chart
	 */
	, getSeries: function(alias){
		if(this.store!=null){
			//single serie
		   	var series = [];

			//coordinates or multiple columns for 1 value

		   	if (alias != undefined && alias != null){
		   		this.serieAlias = alias.trim().split(",");
		   	}
			if(this.serieAlias.length != 1){
				var records = this.store.getRange();
		    	for (var j = 0; j < records.length; j++) {
		    		var rec = records[j].data;
					if(rec) {
						var recArray = [];
						for(i = 0; i<this.serieAlias.length; i++){			
					    	var serieColumn = this.store.getFieldNameByAlias(this.serieAlias[i]);
					    	var posValue = recArray.indexOf(recArray[serieColumn]);
							if (posValue == -1){
								recArray.push(rec[serieColumn]);
							}
						}
						series.push(recArray);
					}
		    	}
			}else{
		    	var serieColumn = this.store.getFieldNameByAlias(this.serieAlias);
				var records = this.store.getRange();
		    	for (var i = 0; i < records.length; i++) {
		    		var rec = records[i].data;
					if(rec) {
						var posValue = series.indexOf(rec[serieColumn]);
						if (posValue == -1){
							series.push(rec[serieColumn]);
						}
					}
		        }
			}
			return  series;
		}
	}
	
	/**
	 * Opens the loading mask 
	 */
    , showMask : function(){
    	if (this.loadMask == null) {
    		this.loadMask = new Ext.LoadMask(this.getId(), {msg: "Loading.."});
    	}
    	this.loadMask.show();
    }
	
	/**
	 * Closes the loading mask
	 */
	, hideMask: function() {
    	if (this.loadMask != null) {
    		this.loadMask.hide();
    	}
	}
	
    , format: function(value, type, format) {
    	if(value==null){
    		return value;
    	}
		try {
			var valueObj = value;
			if (type == 'int') {
				valueObj = parseInt(value);
			} else if (type == 'float') {
				valueObj = parseFloat(value);
			} else if (type == 'date') {
				valueObj = Date.parseDate(value, format);
			} else if (type == 'timestamp') {
				valueObj = Date.parseDate(value, format);
			}
			return valueObj;
		} catch (err) {
			return value;
		}
	}
    
    , initStore: function(config, dsId) {
    	this.storeManager = new Sbi.engines.chart.data.StoreManager({datasetsConfig: config});	
		this.store = this.storeManager.getStore(dsId);
		this.store.loadStore();
		if (this.store === undefined) {
			Sbi.exception.ExceptionHandler.showErrorMessage('Dataset with identifier [' + this.storeId + '] is not correctly configurated');			
		}else{		
			this.store.on('load', this.onLoad, this);
			this.store.on('exception', Sbi.exception.ExceptionHandler.onStoreLoadException, this);
			//this.store.on('exception', Sbi.exception.ExceptionHandler.handleFailure, this);
			this.store.on('metachange', this.onMetaChange, this);
		}
	}
    
    , onMetaChange: function( store, meta ) {
		var i;
	    var fieldsMap = {};

		var tmpMeta =  Ext.apply({}, meta); // meta;
		var fields = tmpMeta.fields;
		tmpMeta.fields = new Array(fields.length);
		
		for(i = 0; i < fields.length; i++) {
			if( (typeof fields[i]) === 'string') {
				fields[i] = {name: fields[i]};
			}
			
			if (this.columnId !== undefined && this.columnId === fields[i].header ){
				fields[i].hidden = true;
			}
			tmpMeta.fields[i] = Ext.apply({}, fields[i]);
			fieldsMap[fields[i].name] = i;
		}
	   
		//adds numeration column    
		tmpMeta.fields[0] = new Ext.grid.RowNumberer();
	}
    , enableDrillEvents: function(dataConfig){
    	var drill = dataConfig.drill;
    	if(drill != null && drill !== undefined){
    		var doc = drill.document;

    		var event = {
    				click: function(ev){
		        		var params = "";
		        		for(var i = 0; i< drill.param.length; i++){
		        			if(drill.param[i].type == 'ABSOLUTE'){
		        				params+= drill.param[i].name +"="+drill.param[i].value;
		        			
		    	    			if(i != drill.param.length -1 ){
		    	    				params+="&";
		    	    			}
		        			}
		        		}
		        		var relParams = dataConfig.dsPars;
		        		for(var i = 0; i< drill.param.length; i++){
		        			if(drill.param[i].type == 'RELATIVE'){
		        				for(var y =0; y<relParams.length; y++){
		        					if(relParams[y].name == drill.param[i].name){
				        				params+= drill.param[i].name +"="+relParams[y].value+"";
			    	    				params+="&";
		        					}
		        				}
		        			}
		        		}
    					//alert(this.name+" "+ev.point.x +" " +ev.point.y);
    		    		for(var i = 0; i< drill.param.length; i++){
    		    			if(drill.param[i].type == 'CATEGORY'){
    		    				params+= drill.param[i].name +"="+ev.point.category;
    		    			
    			    			if(i != drill.param.length -1 ){
    			    				params+="&";
    			    			}
    		    			}
    		    			
    		    		}
    		    		for(var i = 0; i< drill.param.length; i++){
    		    			if(drill.param[i].type == 'SERIE'){
    		    				params+= drill.param[i].name +"="+ev.point.y;
    		    			
    			    			if(i != drill.param.length -1 ){
    			    				params+="&";
    			    			}
    		    			}
    		    			
    		    		}
    		    		if(params.length != 0){
	    		    		var atpos = params.lastIndexOf("&", params.length-1);
	    		    		if(params.lastIndexOf("&", params.length-1) != -1){
	    		    			params = params.substring(0, atpos);
	    		    		}
    		    		}
    					parent.execCrossNavigation("iframe_"+dataConfig.docLabel, doc, params);
    				}
    		};
    		//depending on chart type enables click navigation events
    		if(doc !== undefined && doc != null ){
				if(dataConfig.series !== undefined){
    				if(dataConfig.series.length !== undefined){
    					for(var i =0; i< dataConfig.series.length; i++){
    						dataConfig.series[i].events = event;
    					}
    				}else{
    					dataConfig.series.events = event;
    				}
				}    			
    		}
    	}
    	
    	
    }
    
    ,onLoad: function(){
    	this.getCategoriesX();
    	this.getCategoriesY();
    	this.getSeries();
    	this.createChart();
    }
});



