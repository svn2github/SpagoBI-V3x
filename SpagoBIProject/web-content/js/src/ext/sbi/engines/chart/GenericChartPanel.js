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
	//	this.showMask();
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
	//	this.hideMask();
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
		//checks series configuration; since SpagoBI 3.2 all series can be filtered through an input parameter defined 
		//into the 'paramFilterSeries' attribute.
		var finalSeries = [];
		if (dataConfig.series){
			var strValue = dataConfig.series;
			var filterSeries = [];
			if (dataConfig.chart.paramFilterSeries){
				filterSeries = this.getSeriesByParam(dataConfig.chart.paramFilterSeries, dataConfig.dsPars);
			}
			if (Ext.isArray(strValue)){
				var str = "";
				for(var i = 0; i < strValue.length; i++) {
					if (dataConfig.chart.paramFilterSeries == undefined || this.isFilteredSerie(strValue[i].alias, filterSeries )){
						finalSeries.push(strValue[i]);
						str += strValue[i].alias;
						if (i < (strValue.length-1)) str += ",";
					}
				}
				if (str) {
					this.serieAlias = str.split(",");
				}
				dataConfig.series = finalSeries;
			}			
		} 
		
		//checks plotOptions.series configuration			
		if(this.serieAlias.length == 0 && dataConfig.plotOptions && dataConfig.series !== undefined){
			var str = dataConfig.series.alias;
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
    	//alert("showMAs: " + this.loadMask);
    	if (this.loadMask == null) {
    		this.loadMask = new Ext.LoadMask(this.id, {msg: "Loading.."});
    	}
    	this.loadMask.show();
    }
	
	/**
	 * Closes the loading mask
	 */
	, hideMask: function() {
		alert("hideMAsk");
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
    		    				if(ev.point.category !== undefined){
    		    					params+= drill.param[i].name +"="+ev.point.category;
    		    				}else{
    		    					params+= drill.param[i].name +"="+ev.point.name;
    		    				}
    		    				
    		    			
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
    
    , defineSeriesData: function(config){
		//gets series values and adds theme to the config
		var seriesNode = [];

		if (config.series !== undefined ){
			var serieValue = config.series;
			if (Ext.isArray(serieValue)){
				var seriesData =  {};
				var str = "";
				for(var i = 0; i < serieValue.length; i++) {
					seriesData = serieValue[i];					
					seriesData.data = this.getSeries(serieValue[i].alias);//values from dataset
					seriesNode.push(seriesData);
				}
			}
		}else if (config.plotOptions){ 
			seriesData = config.series;//other attributes too
			seriesData.data = this.getSeries();//values from dataset
			seriesNode.push(seriesData);
		}

		config.series = seriesNode;
	}
	
	, definesCategoriesX: function(config){
		if(config.xAxis != undefined){
			//if multiple X axis
			if(config.xAxis.length != undefined){
				//gets categories values and adds theme to the config	
				var categoriesX = this.getCategoriesX();
				if(categoriesX == undefined || categoriesX.length == 0){
					delete this.chartConfig.xAxis;
					for(var j =0; j< this.categoryAliasX.length; j++){
						config.xAxis[j].categories = categoriesX[j];
					}					
				}
				//else keep templates ones

			}else{
				//single axis
				var categoriesX = this.getCategoriesX();
				if(categoriesX != undefined && categoriesX.length != 0){
					config.xAxis.categories = categoriesX[0];
				}				
			}
		}
	}
	
	, definesCategoriesY: function(config){
		if(config.yAxis != undefined){
			//if multiple Y axis
			if(config.yAxis.length != undefined){
				//gets categories values and adds theme to the config	
				var categoriesY = this.getCategoriesY();
				if(categoriesY == undefined || categoriesY.length == 0){
					delete this.chartConfig.yAxis;
					for(var j =0; j< this.categoryAliasY.length; j++){
						config.yAxis[j].categories = categoriesY[j];
					}
					
				}
				//else keep templates ones
			}else{
				//single axis
				var categoriesY = this.getCategoriesY();
				if(categoriesY != undefined && categoriesY.length != 0){
					config.yAxis.categories = categoriesY[0];
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
   
    //checks if there are some $F{<field>} to substitute into template (title, subtitle, x_axis, y_axis)
    , setFieldValuesIntoTemplate: function(config){    	
    	//checks title text   
    	if (config.title && config.title.text)
    		config.title.text = this.checkTextFields(config.title.text);
    	//checks subtitle text  
    	if (config.subtitle && config.subtitle.text)
    		config.subtitle.text = this.checkTextFields(config.subtitle.text);
    	//checks xAxis text  
    	if (config.xAxis && config.xAxis.title && config.xAxis.title.text)
    		config.xAxis.title.text = this.checkTextFields(config.xAxis.title.text);
    	//checks yAxis text  
    	if (config.yAxis && config.yAxis.title && config.yAxis.title.text)
    		config.yAxis.title.text = this.checkTextFields(config.yAxis.title.text);
		
    }

    , checkTextFields: function(text){
    	var aliasFields = [];
    	var prefix = "$F{";
    	var suffix = "}";
    	
    	aliasFields = this.getFieldLabels(text);
		for (var i=0, l=aliasFields.length; i<l; i++){
			var alias = aliasFields[i];
			var fieldValue = this.getFieldValue(alias);
			if (fieldValue !== null){
				var tmpText = text.replace(prefix + alias + suffix, fieldValue);
				text = tmpText;
			}
		}
		return text;
    }
    
	,getFieldLabels: function(text){
    	var prefix = "$F{";
    	var suffix = "}";
    	var fieldLabels = [];
    	var tmpText = text;
    	var doGetLabels = true;
    	
    	while (doGetLabels){
			if (tmpText.indexOf(prefix) >= 0){			
				var tmpLabel = tmpText.substring( tmpText.indexOf(prefix)+3, tmpText.indexOf(suffix));
				var tmpLen = tmpLabel.length + 1;
				fieldLabels.push(tmpLabel);
				tmpText = tmpText.substring(tmpText.indexOf(tmpLabel)+tmpLen);
			}else{
				doGetLabels = false;
			}
    	}
		return fieldLabels;
	}

    , getFieldValue: function(alias){
		var fieldValues = null;	
		
		if (alias === undefined || alias === null){
			return  fieldValues;
	   	}
		//gets the field value from the first record 
		//if(this.store!=null && this.store.getCount() > 0){
		if(this.store!=null ){
	    	var fieldColumn = this.store.getFieldNameByAlias(alias);
	    	    	
    		var rec = this.store.getAt(0);
			if(rec) fieldValues = rec.get(fieldColumn);						
		}
		return  fieldValues;
	}
    
    , getColors : function (config) {
		var colors = [];
		var retColors = [];
		
		if (config.colors !== undefined && config.colors[0] !== undefined && config.colors[0].color !== undefined) {
			var tmpColors =  config.colors[0].color;
			colors = tmpColors.split(",");
		}else{
			colors = Highcharts.getOptions().colors;
		}
		//adaptes the colors array to the real number of the serie (necessary for force the same color for each serie in double pie)
	    var lenColors = 0;
	    if(config.series[0] !== undefined){
		    for (var i = 0, l = config.series[0].data.length; i < l ; i++) {				
				if (i == colors.length){
					lenColors = 0;
				}
				retColors.push(colors[lenColors]);
				lenColors ++;
			}
	    }
	    
		return retColors;
	}
    
    , getSeriesByParam: function(filterParam, params){
    	var toReturn = [];
    	for(var i=0, l=params.length; i<l; i++){
    		var tmpPar = params[i];
    		if (tmpPar.name == filterParam){
    			toReturn = tmpPar.value.split(",");
    			break;
    		}
    	}
    	return toReturn;
    }
    
    , isFilteredSerie: function(serie, filterSeries){
    	var toReturn = false;
    	
    	for(var i=0, l=filterSeries.length; i<l; i++){
    		if (serie.indexOf(filterSeries[i]) >= 0) {
    			toReturn = true;
    			break;
    		}
    	}
    	return toReturn;
    }
    
});



