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
  * - Alberto Ghedin (alberto.ghedin@eng.it)
  */

//================================================================
//CrossTab
//================================================================
//
//The cross tab is a grid with headers and for the x and for the y. 
//it's look like this:
//       ----------------
//       |     k        |
//       ----------------
//       |  y  |  x     |
//       ----------------
//       |y1|y2|x1|x2|x3|
//-----------------------
//| | |x1|  |  |  |  |  |
//| | |------------------
//| |x|x2|  |  |  |  |  |
//| | |------------------
//|k| |x3|  |  |  |  |  |
//| |--------------------
//| | |y1|  |  |  |  |  |
//| |y|------------------
//| | |y2|  |  |  |  |  |
//-----------------------
//
//The grid is structured in 4 panels:
//         -----------------------------------------
//         |emptypanelTopLeft|    columnHeaderPanel|
// table=  -----------------------------------------
//         |rowHeaderPanel   |    datapanel        | 
//         -----------------------------------------

Ext.ns("Sbi.crosstab.core");

Sbi.crosstab.core.CrossTab = function(config) {
    
	var defaultSettings = {
				percentageFontSize: 9,
				columnWidth: 80,
				rowHeight: 25,
				fontSize: 10};
	if(Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.crossTab) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.crossTab);
	}
	
	defaultSettings = Ext.apply(defaultSettings, config);

	Ext.apply(this, defaultSettings);

	//add the percent of the value respect to the total of the row or the column
	if(this.percenton==undefined && this.percenton==null && this.percenton==''){
		this.percenton = 'no';
	}
	if(this.percenton!='no'){
		//if the user whant the percentage than the width of the cells should be bigger//add the percent of the value respect to the total of the row or the column
		this.columnWidth = this.columnWidthPercent;
	}
	
	
	this.calculatedFields = new Array();
	this.manageDegenerateCrosstab(this.rowHeadersDefinition, this.columnHeadersDefinition);
	this.entries = new Sbi.crosstab.core.CrossTabData(this.entries);
    this.rowHeader = new Array();
    this.build(this.rowHeadersDefinition, 0, this.rowHeader, false);
    this.setFathers(this.rowHeader);
    this.setDragAndDrop(this.rowHeader, false, this);
    this.rowHeader[0][0].hidden=true;//hide the fake root header
    this.rowHeaderPanel = this.buildHeaderGroup(this.rowHeader, false);
    
    this.columnHeader = new Array();
    this.build(this.columnHeadersDefinition, 0, this.columnHeader, true);
    this.setFathers(this.columnHeader);
    this.setDragAndDrop(this.columnHeader, true, this);
    this.columnHeader[0][0].hidden=true;//hide the fake root header
    this.columnHeaderPanel = this.buildHeaderGroup(this.columnHeader, true);

    this.addDDArrowsToPage();
    
    var c = {
  	//	layout:'fit',
  		autoHeight: true,
  		border: false,
  		defaults: {autoScroll: true},
		padding : 10
	};

    this.addEvents();   

    if(this.calculatedFields!=null && this.calculatedFields.length>0){
    	this.on('afterrender', function(){
    		var i=0; 
    		for(i=0; i<this.calculatedFields.length; i++){
    			Sbi.crosstab.core.CrossTabCalculatedFields.calculateCF(this.calculatedFields[i].level, this.calculatedFields[i].horizontal, this.calculatedFields[i].operation, this.calculatedFields[i].name, this, true, this.percenton);
    		}
   			this.calculatePartialSum(null, true);
   	    	this.reloadHeadersAndTable();
    		
    	}, this);
    }else{
    	this.on('afterrender', function(){
    		this.calculatePartialSum();
    	}, this);
    }
    
    Sbi.crosstab.core.CrossTab.superclass.constructor.call(this, c);
};
	
Ext.extend(Sbi.crosstab.core.CrossTab, Ext.Panel, {
	entries: null // matrix with the data 
    ,rowHeaderPanelContainer: null //Panel with the header for the rows
    ,columnHeaderPanelContainer: null //Panel with the header for the columns
    ,rowHeader: null // Array. Every entry contains an array of HeaderEntry. At position 0 there is the external headers.
    ,columnHeader: null // Array. Every entry contains an array of HeaderEntry. At position 0 there is the external headers.
    ,emptypanelTopLeft: null // The top-left corner of the table
    ,datapanel: null // The panel with the table of data
    ,rowHeaderPanel:null // An array. Every entry contains a Panel wich items are the rowHeader. i.e: rowHeaderPanel[0]= new Ext.Panel(...items :  rowHeader[0]), rowHeaderPanel[1]= new Ext.Panel(...items :  rowHeader[1])
    ,columnHeaderPanel: null // An array. Every entry contains a Panel wich items are the columnHeader. i.e: columnHeaderPanel[0]= new Ext.Panel(...items :  columnHeader[0]), columnHeaderPanel[1]= new Ext.Panel(...items :  columnHeader[1])
    ,table: null //the external table with 2 rows and 2 columns. It contains emptypanelTopLeft, columnHeaderPanel, rowHeaderPanel, datapanel
    ,checkBoxWindow: null //window with the checkBoxs for hide or show a column/line
	,columnWidth: null
	,rowHeight: null
	,fontSize: null
	,percentageFontSize: null
	,entriesPanel : null
	,crossTabCFWizard: null
	,clickMenu: null
	,withRowsSum: null
	,withColumnsSum: null
	,withRowsPartialSum: null
	,withColumnsPartialSum: null
	,calculatedFields: null
	,misuresOnRow: null
	,visibleColumns: null //the number of visible columns
	,visibleRows: null //the number of visible rows
	,measuresMetadata: null // metadata on measures: it is an Array, each entry is a json object with name, type and (in case of date/timestamp) format of the measure
	,visibleMeasuresMetadataLength: null
	,measuresNames: null
	,measuresPosition: null
	,rowsTotalSumArray: null
	,columnsTotalSumArray: null
	,superSumArray: null //array with the sum of sums (panel in the bottom right if there are the sum either in the rows either in the columns)

	
	, manageDegenerateCrosstab: function(rowHeadersDefinition, columnHeadersDefinition) {
		if (rowHeadersDefinition.length == 1) { // degenerate crosstab (everything on columns)
			var array = ["Data"];
			var wrapper = [array];
			rowHeadersDefinition.push(wrapper);
		}
		if (columnHeadersDefinition.length == 1) { // degenerate crosstab (everything on rows)
			var array = ["Data"];
			var wrapper = [array];
			columnHeadersDefinition.push(wrapper);
		}
	}
	
    //================================================================
    // Loads and prepare the table with the data
    //================================================================
    
    // takes the data definition and prepare an ordered array with a panel for every tab cell
    // (in position 0 there is the cell at position (0,0) in the table, 
    // in positin 1 the cell (0,1) ecc..) 
    ,getEntries : function(){
    	var entries = this.entries.getEntries();
    	var toReturn = new Array();
    	var visiblei=0;//the visible row index. If i=2 and row[0].hidden=true, row[1].hidden=false  then  visiblei=i-1 = 1

    	for(var i=0; i<entries.length; i++){
    		if(!this.rowHeader[this.rowHeader.length-1][i].hidden){
    			var visiblej=0;
    			partialSum =0;
    			for(var j=0; j<entries[i].length; j++){

    				if(!this.columnHeader[this.columnHeader.length-1][j].hidden){
    					// get measure metadata (name, type and format)
    					var measureName = null;
    					if (this.misuresOnRow) {
    						measureName = this.rowHeader[this.rowHeader.length-1][i].name;
    					} else {
    						measureName = this.columnHeader[this.columnHeader.length-1][j].name;
    					}
    					var measureMetadata = this.getMeasureMetadata(measureName);
    					// in case of calculated fields made with measures, measureMetadata is null!!!
    					var datatype = measureMetadata !== null ? measureMetadata.type : 'float';
    					var format = (measureMetadata !== null && measureMetadata.format !== null && measureMetadata.format !== '') ? measureMetadata.format : null;
    					// get also type of the cell (data, CF = calculated fields, partialsum)
    					var celltype = this.getCellType(this.rowHeader[this.rowHeader.length-1][i], this.columnHeader[this.columnHeader.length-1][j]);
    					// put measure value and metadata into an array
    					var a = new Array();
    					a.push(entries[i][j]);
    					a.push('['+visiblei+','+visiblej+']');
    					a.push(datatype);
    					a.push(format);
    					a.push(celltype);
	    				toReturn.push(a);
	    				visiblej++;
    				}   				
    			}
    			visiblei++;
        	}	
    	}
    	this.visibleColumns=visiblej;
    	this.visibleRows=visiblei;
    	return toReturn;
    }

	// returns the type of the cell (data, CF = calculated fields, partialsum) by the cell headers
	, getCellType: function(rowHeader, columnHeader) {
		if (rowHeader.type == 'CF' || columnHeader.type == 'CF') {
			return 'CF';
		}
		if (rowHeader.type == 'partialsum' || columnHeader.type == 'partialsum') {
			return 'partialsum';
		}
		return 'data';
	}

	, getMeasureMetadata: function (measureName) {
		for (var i = 0; i < this.measuresMetadata.length; i++) {
			if (this.measuresMetadata[i].name === measureName) {
				return this.measuresMetadata[i];
			}
		}
		return null;
	}
	    	
	//returns the number of the visible (not hidden) rows		
    ,getRowsForView : function(){
    	var count =0;
    	for(var i=0; i<this.rowHeader[this.rowHeader.length-1].length; i++){
    		if(!this.rowHeader[this.rowHeader.length-1][i].hidden){
    			count++
    		}
    	}
    	return count;
    }
    
    //returns the number of the visible (not hidden) columns
    , getColumnsForView : function(){
    	var count =0;
    	for(var i=0; i<this.columnHeader[this.columnHeader.length-1].length; i++){
    		if(!this.columnHeader[this.columnHeader.length-1][i].hidden){
    			count++
    		}
    	}
    	return count;
    }
    
    //highlight a row of the table by adding a class to the cell elements (the additional class sets a background color)
    //i: the number of the row (visible)
    ,highlightRow: function(i){
		for(var y = 0; ; y++){
			//var el = Ext.get('['+i+','+y+']');
			//if (el == null) return;
			//el.addClass('crosstab-table-cells-highlight');
	   		var cel = document.getElementById('['+i+','+y+']');
	   		if (cel == null) return;
	   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
		}
    }

    //highlight a column of the table by adding a class to the cell elements (the additional class sets a background color)
    //j: the number of the column (visible)
    ,highlightColumn: function(j){
		for (var y = 0; ; y++) {
			//var el = Ext.get('['+y+','+j+']');
			//if (el == null) return;
			//Ext.get('['+y+','+j+']').addClass('crosstab-table-cells-highlight');
	   		var cel = document.getElementById('['+y+','+j+']');
	   		if (cel == null) return;
	   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
		}
    }

//    //highlight a row of the table by adding a class to the cell elements (the additional class sets a background color)
//    //i: the number of the row (visible)
//    ,highlightRowWithTimeOut: function(i){
//    	if(this.selectedRowId!=null){
//    		this.removeHighlightOnRow(this.selectedRowId);
//    	}
//    	this.selectedRowId = i;
//    	for(var y = 0; ; y++){
//	   		var cel = document.getElementById('['+i+','+y+']');
//	   		if (cel == null) return;
//	   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
//		}
//    }
    
//    //highlight a column of the table by adding a class to the cell elements (the additional class sets a background color)
//    //j: the number of the column (visible)
//    ,highlightColumnWithTimeOut: function(j){
//    	if(this.selectedColumnId!=null){
//    		this.removeHighlightOnColumn(this.selectedColumnId);
//    	}
//    	this.selectedColumnId = j;        
//		for (var y = 0; ; y++) {
//	   		var cel = document.getElementById('['+y+','+j+']');
//	   		if (cel == null) return;
//	   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
//		}
//    }
    
    //remove highlight of a row of the table by removing an additional class 
    //i: the number of the row (visible)
    ,removeHighlightOnRow: function(i){
		for(var y = 0; ; y++){
	   		var cel = document.getElementById('['+i+','+y+']');
	   		if (cel == null) return;
	   		cel.className = cel.className.replace(/\bcrosstab-table-cells-highlight\b/,''); // removing class crosstab-table-cells-highlight
		}
    }
         
    //remove highlight of a column of the table by removing an additional class 
    //j: the number of the column (visible)
    ,removeHighlightOnColumn: function(j){
 		for (var y = 0; ; y++) {
	   		var cel = document.getElementById('['+y+','+j+']');
	   		if (cel == null) return;
	   		cel.className = cel.className.replace(/\bcrosstab-table-cells-highlight\b/,''); // removing class crosstab-table-cells-highlight
 		}
    }
     
     //remove highlight from all the cell of the table by removing an additional class 
     ,removeHighlightOnTable: function(){
    	var entries = this.entries.getEntries();
		for(var i = 0; i<entries.length; i++){
			for(var y = 0; y<entries[0].length; y++){
		   		var cel = document.getElementById('['+i+','+y+']');
		   		if (cel == null) break;
		   		cel.className = cel.className.replace(/\bcrosstab-table-cells-highlight\b/,''); // removing class crosstab-table-cells-highlight
			}
		}
     }
     
     ,buildTotalSubTree: function(headers, misuresOnLine){
		 var node = headers[0][0];
		 var horizontal = headers[0][0].horizontal;
    	 if(misuresOnLine){
	    	 for(var y=1; y<headers.length;y++){
				if(y<headers.length-1){
					var freshNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name: LN('sbi.crosstab.header.total.text'), thisDimension: this.measuresMetadata.length, horizontal: horizontal, level: y});
					freshNode.father = node;
						node.childs.push(freshNode);	
						node = freshNode;
				}else{
					for(var k=0; k<this.measuresMetadata.length; k++){
						var freshNode =(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:this.measuresMetadata[k].name, thisDimension: 1, horizontal: horizontal, level: headers.length-1}));
						freshNode.father = node;
						node.childs.push(freshNode);
					}	
				}
			}
	     }else{
				for(var y=1; y<headers.length;y++){
					var freshNode =(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: 1, horizontal: horizontal, level: y}));
					freshNode.father = node;
					node.childs.push(freshNode);	
					node = freshNode;
				}
	     }
     }
     
     //serialize the crossTab: 
     //Create a JSONObject with the properties: data, columns, rows
     ,serializeCrossTab: function(){

    	 var columnsum = null;
    	 var rowsum = null;
    	 
    	 if(this.withColumnsSum || this.withRowsSum){
			 var columnHeaderBK = this.cloneHeader(this.columnHeader);
			 var rowHeaderBK = this.cloneHeader(this.rowHeader);
    	 }
    	 
    	 if(this.withColumnsSum){
    		 //save the column headers because buildTotalSubTree change the structure 
    		 //of the headers

    		 this.buildTotalSubTree(this.rowHeader, this.misuresOnRow);
    		
    		 if(this.misuresOnRow){	
    			columnsum = this.calculateTotalSum(true);
    		 }else{
    			columnsum = new Array();
    			columnsum.push(this.columnsSum(true, true));
    		 }   		 
    	 }
    	 
    	 if(this.withRowsSum){
    		 //save the row headers because buildTotalSubTree change the structure 
    		 //of the headers
 
    	  	this.buildTotalSubTree(this.columnHeader, !this.misuresOnRow);
    		 if(!this.misuresOnRow){
    			 rowsum = this.calculateTotalSum(true);
    		 }else{
    			 rowsum = new Array();
    			 rowsum.push(this.rowsSum(true, true));
    		 }
    	 }

    	 var serializedCrossTab = {}; 
    	 serializedCrossTab.data= this.entries.serializeEntries(rowsum, columnsum,this.misuresOnRow, this.percenton);
    	 serializedCrossTab.columns=  this.serializeHeader(this.columnHeader[0][0]);
    	 serializedCrossTab.rows=  this.serializeHeader(this.rowHeader[0][0]);
    	 if(this.withColumnsSum || this.withRowsSum){
    		 this.columnHeader = columnHeaderBK;
    		 this.rowHeader=rowHeaderBK;
    	 }
    	 return serializedCrossTab;
     }
     
     //serialize a header and all his the subtree
 	 ,serializeHeader: function(header){
  		var node = {};
  		node.node_key =  header.name;
 		if(header.childs.length>0){
 			var nodeChilds = new Array();
 			for(var i=0; i<header.childs.length; i++){
 				nodeChilds.push(this.serializeHeader(header.childs[i]));
 			}
 			node.node_childs = nodeChilds;
 		}
 		
 		return node;
 	}
 	    
    
    //================================================================
    // Build the headers
    //================================================================
	//    ----------------
	//    |     k        |
	//    ----------------
	//    |  y  |  x     |
	//    ----------------
	//    |y1|y2|x1|x2|x3|
	//    ----------------
    //Recursive function that builds the header panels (i.e. columnHeader and rowHeader)  
    //line: the definition of a subtree (for example ["x",[["x1"],["x2"],["x3"]] or ["y",[["y1"],["y2"]]])
    //level: the header level. For example the level of x is 1, for x1 is 2
    //headers: Or columnHeader or rowHeader
    //horizontal: true for columnHeader and false for rowHeader 
     , build : function(line, level, headers, horizontal){
		var name = line[0];
		var thisDimension;
    	if(line.length==1){
    		thisDimension = 1;
    	}else{
    		var t=0;
    		var items = line[1];
    		for(var i=0; i<items.length; i++){
    			t= t+this.build(items[i], level+1, headers, horizontal);
    		}
    		thisDimension =t;
    	}
    	p = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:name, thisDimension:thisDimension, horizontal:horizontal, level:level});
    	this.setHeaderListener(p);

    	if(headers[level]==null){
    		headers[level]= new Array();
    	}
    	headers[level].push(p);
    	return thisDimension;
    }

     //Adds the listeners to the header
    , setHeaderListener: function(header, noMenu){
    	

    	if(noMenu){
	    	header.addListener({render: function(theHeader) {
				//color the rows/columns when the mouse enter in the header
	    		theHeader.el.on('mouseenter', this.headerMouseenterHandler.createDelegate(this, [theHeader], true), this);
	    		theHeader.el.on('mouseleave', this.headerMouseleaveHandler.createDelegate(this, [theHeader], true), this);
				}, scope: this
		  	});
    	}else{
	    	header.addListener({render: function(theHeader) {
				// open the show/hide dialog
	    		theHeader.el.on('click', this.headerClickHandler.createDelegate(this, [theHeader], true), this);
				//color the rows/columns when the mouse enter in the header
	    		theHeader.el.on('mouseenter', this.headerMouseenterHandler.createDelegate(this, [theHeader], true), this);
	    		theHeader.el.on('mouseleave', this.headerMouseleaveHandler.createDelegate(this, [theHeader], true), this);
				}, scope: this
		  	});
    	}
    	
    }
    
    , headerClickHandler: function(event, element, object, theHeader) {
    	var theHandler = function(event, element, object, theHeader) {
			if(this.crossTabCFWizard!=null && this.crossTabCFWizard.isVisible()){
				this.crossTabCFWizard.addField("field["+theHeader.name+"]", theHeader.level, theHeader.horizontal);
			}else{
				if(this.clickMenu!=null){
					this.clickMenu.destroy();
				}
				this.clickMenu = new Sbi.crosstab.core.CrossTabContextualMenu(theHeader, this);
				this.clickMenu.showAt([event.getPageX(), event.getPageY()]);
			}
		}
    	theHandler.defer(100, this, [event, element, object, theHeader]); 
    	// This is a work-around (workaround, work around): without deferring the function call, if you open the calculated fields wizard, 
    	// then close it, and click quickly on a header, the context menu appears for a moment but it immediately disappears.
	}
    
    , headerMouseenterHandler: function(event, htmlElement, o, theHeader) {
		if(this.crossTabCFWizard!=null && this.crossTabCFWizard.isVisible() && this.crossTabCFWizard.isActiveLevel(theHeader.level, theHeader.horizontal)){
			theHeader.setWidth(theHeader.getWidth()-2);
			theHeader.setHeight(theHeader.getHeight()-2); 
			theHeader.addClass("crosstab-borderd");
		}

		var start=0;
		var i=0;
		var headers;
		
		if(theHeader.horizontal){
			headers = this.columnHeader;
		}else{
			headers = this.rowHeader
		}
		
		while(!this.isTheSameHeader(headers[theHeader.level][i],theHeader) && i<headers[theHeader.level].length){
			if(!headers[theHeader.level][i].hidden){
				start = start + headers[theHeader.level][i].thisDimension;
			}
			i++;
		}
		
		if( i<headers[theHeader.level].length){
			var end = start+headers[theHeader.level][i].thisDimension-1;
			if(theHeader.horizontal){
				for(i=start; i<=end; i++){
					this.highlightColumn(i);
				}
			}else{
				for(i=start; i<=end; i++){
					this.highlightRow(i);
				}
			}
		}
	}
    
    , headerMouseleaveHandler: function(event, htmlElement, o, theHeader) {
		
		if(this.crossTabCFWizard!=null && this.crossTabCFWizard.isVisible() && this.crossTabCFWizard.isActiveLevel(theHeader.level, theHeader.horizontal)){
			theHeader.removeClass("crosstab-borderd");
			theHeader.setWidth(theHeader.getWidth()+2);
			theHeader.setHeight(theHeader.getHeight()+2); 
		}
		this.removeHighlightOnTable();
			
	}
     
     //Sets the father of every HeaderEntry
    , setFathers : function(headers){
    	var heigth;	
    	for(var k=0; k<headers.length-1; k++){
    		var pannels = headers[k];
    		//index of the first child in the headers array
    		var heigthCount=0;
    		var i=0; 
	    	for(var y=0; y<pannels.length; y++){
	    		//index of the last child in the headers array
	    		heigth = pannels[y].thisDimension+heigthCount;
	    		pannels[y].childs = new Array();
	    		while(heigthCount<heigth){
	    			pannels[y].childs.push(headers[k+1][i]);
//	    			if(headers[k+1][i]==null){
//	    				alert((k+1)+" "+i+" "+pannels[y].name);
//	    			}
	    			headers[k+1][i].father = pannels[y];
	    			heigthCount = heigthCount+headers[k+1][i].thisDimension;
	    			i++;
	    		}
	    	}
    	}
    }
    
    //Sets the drag and drop function of every HeaderEntry
    , setDragAndDrop : function(headers, horizontal, myPanel){
	    for(var k=1; k<headers.length; k++){
			var pannels = headers[k];
	    	for(var y=0; y<pannels.length; y++){
	    		pannels[y].draggable = {

	    			// for the shadow of the panel to drag
	    			// draw the arrows
		    	    onDrag : function(e){
	    	            var pel = this.proxy.getEl();
	    	            this.x = pel.getLeft(true);
	    	            this.y = pel.getTop(true);
	    	            var s = this.panel.getEl().shadow;
	    	            if (s) {
	    	                s.realign(this.x, this.y, pel.getWidth(), pel.getHeight());
	    	            }
	    	            
	    	            var arrowTop = document.getElementById('ext-arrow-dd-top');  	            
	    	            arrowTop.style.visibility = "visible";
	    	            
	    	            var brothers =this.getDragData().panel.father.childs;
	    	            var i=0;
	    	        	var father = this.getDragData().panel.father;
	    	        	if(father==undefined || father==null){
	    	        		return;
	    	        	}
	    	            if(father.getEl()==undefined || father.getEl()==null){
	    	            	return;
	    	            }
	    	            if(horizontal){
	    	            	var arrowBottom = document.getElementById('ext-arrow-dd-bottom');
		    	            arrowBottom.style.visibility = "visible";
		    	            
		    	            //try to find the panel the cursor is over
		    	            for(i=0; i<brothers.length; i++){
			    	            if((brothers[i].getEl().getLeft(false)<e.xy[0] && brothers[i].getEl().getRight(false)>e.xy[0])){ 
			    	            	 //the cursor is over a brother panel
		    	            		arrowTop.style.left = brothers[i].getEl().getLeft(false)-5;
			    	           		arrowTop.style.top =  brothers[i].getEl().getTop(false)-10;
			    	           		arrowBottom.style.left = brothers[i].getEl().getLeft(false)-5;
			    	           		arrowBottom.style.top =  brothers[i].getEl().getBottom(false);
			    	            	return;
			    	            }
		    	            }  
		    	            //the cursor is out of the list of panels
		    	            if (father.getEl().getRight(false)<e.xy[0] ){
	    	            		arrowTop.style.left = father.getEl().getRight(false)-5;
		    	           		arrowTop.style.top =  father.getEl().getBottom(false)-10;
		    	           		arrowBottom.style.left = father.getEl().getRight(false)-5;
		    	           		arrowBottom.style.top =  father.getEl().getBottom(false)+pel.getHeight();
		    	            }else if (father.getEl().getLeft(false)>e.xy[0]){
	    	            		arrowTop.style.left = father.getEl().getLeft(false)-5;
		    	           		arrowTop.style.top =  father.getEl().getBottom(false)-10;
		    	           		arrowBottom.style.left = father.getEl().getLeft(false)-5;
		    	           		arrowBottom.style.top =  father.getEl().getBottom(false)+pel.getHeight();
		    	            }else{//the cursor is over the panel we try to dd
		    	            	
		    	            	var visibleBrother = this.getDragData().panel.getPreviousSibling(true);
		    	            	if (visibleBrother.name != this.getDragData().panel.name){
		    	            		arrowTop.style.left = visibleBrother.getEl().getRight(false)-5;
			    	           		arrowTop.style.top =  visibleBrother.getEl().getTop(false)-10;	
			    	           		arrowBottom.style.left = visibleBrother.getEl().getRight(false)-5;
		    	            		arrowBottom.style.top =  visibleBrother.getEl().getBottom(false);	
		    	            	}else{//there are no previous visible pannel
		    	            	
		    	            		if(!father.hidden){
		    	            			//not father is not the root, so we can take the father cordinates
		    	            			arrowTop.style.left = father.getEl().getLeft(false)-5;
		    	            			arrowTop.style.top =  father.getEl().getBottom(false)-10;
		    	            			arrowBottom.style.left = father.getEl().getLeft(false)-5;
		    	            			arrowBottom.style.top =  father.getEl().getBottom(false)+pel.getHeight();
		    	            		}else{
		    	            			//the father is the root, so we have to take the cordinates of the next brother
		    	            			visibleBrother = this.getDragData().panel.getNextSibling(true);
		    	            			if (visibleBrother.name != this.getDragData().panel.name){
				    	            		arrowTop.style.left = visibleBrother.getEl().getLeft(false)-5-pel.getWidth();
					    	           		arrowTop.style.top =  visibleBrother.getEl().getTop(false)-10;	
					    	           		arrowBottom.style.left = visibleBrother.getEl().getLeft(false)-5-pel.getWidth();
				    	            		arrowBottom.style.top =  visibleBrother.getEl().getBottom(false);	
		    	            			}else{//there is only one panel, the one we dd 
		    	            				arrowBottom.style.visibility = "hidden";
				    	    	            arrowTop.style.visibility = "hidden";	
				    	            	}
		    	            		}
		    	            	}
		    	            }
	    	            }else{
	    	            	var arrowTop2 = document.getElementById('ext-arrow-dd-top2');
		    	            arrowTop2.style.visibility = "visible";
		    	            for(i=0; i<brothers.length; i++){
			    	            //the cursor is over another panel
		    	            	if((brothers[i].getEl().getTop(false)<e.xy[1] && brothers[i].getEl().getBottom(false)>e.xy[1])){
		    	            		arrowTop.style.left = brothers[i].getEl().getLeft(false)-5;
			    	           		arrowTop.style.top =  brothers[i].getEl().getTop(false)-10;
			    	           		arrowTop2.style.left = brothers[i].getEl().getRight(false)-5;
			    	           		arrowTop2.style.top =  brothers[i].getEl().getTop(false)-10;
		    	    	            return;
		    	            	}
		    	            }
		    	            //the cursor is out of the list of panels
		    	            if (father.getEl().getBottom(false)<e.xy[1] ){
	    	            		arrowTop.style.left = father.getEl().getRight(false)-5;
		    	           		arrowTop.style.top =  father.getEl().getBottom(false)-10-pel.getHeight();
		    	           		arrowTop2.style.left = father.getEl().getRight(false)-5+pel.getWidth();
		    	           		arrowTop2.style.top =  father.getEl().getBottom(false)-10-pel.getHeight();
		    	            }else if (father.getEl().getTop(false)>e.xy[1]){
	    	            		arrowTop.style.left = father.getEl().getRight(false)-5;
		    	           		arrowTop.style.top =  father.getEl().getTop(false)-10;
		    	           		arrowTop2.style.left = father.getEl().getRight(false)-5+pel.getWidth();
		    	           		arrowTop2.style.top =  father.getEl().getTop(false)-10;
		    	            }else{//the cursor is over the panel we try to dd
		    	            	var visibleBrother = this.getDragData().panel.getPreviousSibling(true);
		    	            	if (visibleBrother.name != this.getDragData().panel.name){
		    	            		arrowTop.style.left = visibleBrother.getEl().getLeft(false)-5;
			    	           		arrowTop.style.top =  visibleBrother.getEl().getTop(false)-10+brothers[i-1].getEl().getHeight();
			    	           		arrowTop2.style.left = visibleBrother.getEl().getRight(false)-5;
			    	           		arrowTop2.style.top =  visibleBrother.getEl().getTop(false)-10+brothers[i-1].getEl().getHeight()
		    	            	}else{//there are no previous visible pannel
		    	            		if(!father.hidden){
		    	            			//not father is not the root, so we can take the father cordinates
		    	            			arrowTop.style.left = father.getEl().getRight(false)-5;
		    	            			arrowTop.style.top =  father.getEl().getTop(false)-10;
		    	            			arrowTop2.style.left = father.getEl().getRight(false)-5+pel.getWidth();
		    	            			arrowTop2.style.top =  father.getEl().getTop(false)-10;
		    	            		}else{
		    	            			//the father is the root, so we have to take the cordinates of the next brother
		    	            			visibleBrother = this.getDragData().panel.getNextSibling(true);
		    	            			if (visibleBrother.name != this.getDragData().panel.name){
				    	            		arrowTop.style.left = visibleBrother.getEl().getLeft(false)-5;
				    	            		arrowTop.style.top =  visibleBrother.getEl().getTop(false)-10-pel.getHeight();	
				    	            		arrowTop2.style.left = visibleBrother.getEl().getRight(false)-5;
				    	            		arrowTop2.style.top =  visibleBrother.getEl().getTop(false)-10-pel.getHeight();	
		    	            			}else{//there is only one panel, the one we dd 
		    	            				arrowBottom.style.visibility = "hidden";
				    	    	            arrowTop.style.visibility = "hidden";	
				    	            	}
		    	            		}
		    	            	}
		    	            }		    	            
	    	            }
			        },	    	       
			        //Upadate the positions of the entries in the line with the element dropped
			        endDrag : function(e){  
			        	var myXY;
			        	var exy;
			        	if(horizontal){
			        		myXY = this.getDragData().panel.getEl().getX();
			        		exy = 0;
			        	}else{
			        		myXY = this.getDragData().panel.getEl().getY();
			        		exy = 1;
			        	}
			        	
			        	var father =this.getDragData().panel.father;
			        	
	    	        	if(father==undefined || father==null){
	    	        		return;
	    	        	}
			        	
			        	var tempChilds = new Array();
			        	var xy;
			        	
			        	var hiddenPanels = new Array();
			        	var notHiddenPanels = new Array();
			        	for(var j=0; j<father.childs.length; j++){
			        		if(father.childs[j].hidden){
			        			hiddenPanels.push(father.childs[j]);
			        		}else{
			        			notHiddenPanels.push(father.childs[j]);
			        		}
			        	}

			        	father.childs =notHiddenPanels.concat(hiddenPanels);
			        	
			        	if(myXY<e.xy[exy]){
	    	        		var startPosition = father.childs.length-1;
	    	        		var endPosition = father.childs.length-1;
	    	        		
	    	        		for(var j=0; j<father.childs.length; j++){
	    	        			if(horizontal){
	    	        				xy = (father.childs[j]).getEl().getX();
	    	        			}else{
	    	        				xy = (father.childs[j]).getEl().getY();
	    	        			}
	    	        			if(xy>myXY || xy==0){
	    	        				startPosition = j-1;
	    	        				break;
	    	        			}
	    	        		}
	    	        		for(var j=startPosition; j<father.childs.length; j++){
	    	        			if(horizontal){
	    	        				xy = (father.childs[j]).getEl().getX();
	    	        			}else{
	    	        				xy = (father.childs[j]).getEl().getY();
	    	        			}

	    	        			if(xy>e.xy[exy] || xy==0){
	    	        				endPosition = j-1;
	    	        				break;
	    	        			}
	    	        		}

	    	        		var me = father.childs[startPosition];
	    	        		for(var j=startPosition; j<endPosition; j++){
	    	        			father.childs[j]=father.childs[j+1];
	    	        		}
	    	        		father.childs[endPosition] = me;
	    	        	}else{
	    	        		var startPosition = father.childs.length-1;
	    	        		var endPosition = 0;
	    	        		
	    	        		for(var j=0; j<father.childs.length; j++){
	    	        			if(horizontal){
	    	        				xy = (father.childs[j]).getEl().getX();
	    	        			}else{
	    	        				xy = (father.childs[j]).getEl().getY();
	    	        			}
	    	        			
	    	        			if(xy>e.xy[exy]){
	    	        				endPosition = j-1;
	    	        				break;
	    	        			}
	    	        		}
	    	        		if(endPosition <0){
	    	        			endPosition = 0;
	    	        		}
	    	        		
	    	        		for(var j=endPosition; j<father.childs.length; j++){
	    	        			if(horizontal){
	    	        				xy = (father.childs[j]).getEl().getX();
	    	        			}else{
	    	        				xy = (father.childs[j]).getEl().getY();
	    	        			}
	    	        			if(xy>myXY || xy==0){
	    	        				startPosition = j-1;
	    	        				break;
	    	        			}
	    	        		}

	    	        		var me = father.childs[startPosition];
	    	        		for(var j=startPosition; j>endPosition; j--){
	    	        			father.childs[j]=father.childs[j-1];
	    	        		}
	    	        		father.childs[endPosition] = me;
	    	        	}
			        	var arrowBottom = document.getElementById('ext-arrow-dd-bottom');
	    	            var arrowTop = document.getElementById('ext-arrow-dd-top');
	    	            var arrowTop2 = document.getElementById('ext-arrow-dd-top2');
	    	            arrowBottom.style.visibility = "hidden";
	    	            arrowTop.style.visibility = "hidden";
	    	            arrowTop2.style.visibility = "hidden";
	    	            myPanel.reload(headers, horizontal);
	    	        }
	    	    }
	    	}
		}
    }
    
    //Adds the arrows for the drag and drop to the page
    , addDDArrowsToPage : function(){
		var dh = Ext.DomHelper; 
		var bottomArrowDOM = {
			id: 'ext-arrow-dd-bottom',
			tag: 'div',
			cls: 'col-move-bottom',
			html: '&nbsp;',
			style: 'left: -100px; top: -100px; visibility: hidden;'
		};
		
		var topArrowDOM = {
			id: 'ext-arrow-dd-top',
			tag: 'div',
			cls: 'col-move-top',
			html: '&nbsp;',
			style: 'left: -100px; top: -100px; visibility: hidden;'
		};
		
		var topArrowDOM2 = {
			id: 'ext-arrow-dd-top2',
			tag: 'div',
			cls: 'col-move-top',
			html: '&nbsp;',
			style: 'left: -100px; top: -100px; visibility: hidden;'
		};
		
		var bodyElement = document.getElementsByTagName('body');

		dh.append(bodyElement[0].id, bottomArrowDOM);
		dh.append(bodyElement[0].id, topArrowDOM);
		dh.append(bodyElement[0].id, topArrowDOM2);
		
    }

    //reload the panels after a change (for example DD)
    , reload : function(headers, horizontal){
    	
    	var headersFresh=new Array();
    	headersFresh.push(headers[0]);
    	for(var k=1; k<headers.length; k++){
	    	var rowHeaderFresh=new Array();
	    	for(var y=0; y<headersFresh[k-1].length; y++){
	    		for(var i=0; i<headersFresh[k-1][y].childs.length; i++){
	    			rowHeaderFresh.push(headersFresh[k-1][y].childs[i]);
	    		}
	    	}
	    	headersFresh.push(rowHeaderFresh);   
    	}
    	
    	this.upadateAndReloadTable(headersFresh, horizontal);
    }
    
    // Build the columnHeaderPanel or the rowHeaderPanel
	, buildHeaderGroup : function(headers, horizontal) {
	
		var headerGroup = new Array();
		this.headersPanelHidden = new Array();
		var resizeHeandles = 'e';
		if (horizontal){
			resizeHeandles = 's';
		}
		var c;
		if(horizontal){
			c = {
					boxMinHeight: this.rowHeight
				};
		}else{
			c = {
					boxMinWidth: this.columnWidth
				};	
		}
		
		for(var y=1; y<headers.length; y++){
			var layoutConfig;
			if(horizontal){
				layoutConfig= {rows: 1};
			}else{
				layoutConfig= {columns: 1};
			}
			
			var headersPanel = new Array();
			var headersPanelHidden = new Array();
			for(var i=0; i<headers[y].length; i++){
				if(!headers[y][i].hidden){
					headersPanel.push(headers[y][i]);
				}else{
					//we put the hidden panels in the tail of the table.
					//this because a bug of ie
					headersPanelHidden.push(headers[y][i]);
				}
			}
			
			
			if((!horizontal && this.withColumnsSum && this.misuresOnRow) || (horizontal && this.withRowsSum && !this.misuresOnRow)){
				this.getVisibleMeasures(headers);
				if(y<headers.length-1){
					if(horizontal){
						headersPanel.push(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: this.visibleMeasuresMetadataLength, horizontal: horizontal, level: y, width: null , height: headers[y][0].height}));	
					}else{
						headersPanel.push(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: this.visibleMeasuresMetadataLength, horizontal: horizontal, level: y, width: headers[y][0].width,  height: null}));
					}
				}else{
					for(var k=0; k<this.measuresMetadata.length; k++){
						if(this.measuresMetadata[k].visible){
							headersPanel.push(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:this.measuresMetadata[k].name, thisDimension: 1, horizontal: horizontal, level: headers.length-1, width: headers[y][headers[y].length-1].width, height: headers[y][headers[y].length-1].height}));
						}
					}	
				}
			}
			
			if((!horizontal && this.withColumnsSum && !this.misuresOnRow) || (horizontal && this.withRowsSum && this.misuresOnRow)){
				if(horizontal){
					headersPanel.push(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: 1, horizontal: horizontal,level:  y, width: null , height: headers[y][0].height}));	
				}else{
					headersPanel.push(new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: 1, horizontal: horizontal, level: y, width: headers[y][0].width, height:  null}));
				}
			}

			headersPanel = headersPanel.concat(headersPanelHidden);
			c = Ext.apply(c,{
		    	cellCls: 'crosstab-header-panel-cell',
		    	layout:'table',
		    	border: false,
	            layoutConfig: layoutConfig,
				items:  headersPanel
		    });
		    var p = new Ext.Panel(c);  
		   	    
		    this.createResizable(p,resizeHeandles,headersPanel, horizontal);
		    headerGroup.push(p);

		}
	//	alert('finito headerGroup');
		return headerGroup;
	}
	
	//Upadate the container table 
    , upadateAndReloadTable : function(headerGroup, horizontal){
    	if(horizontal){
    		this.updateTableY(this.getNewPositions(this.columnHeader[this.columnHeader.length-1], headerGroup[this.columnHeader.length-1]));
    		this.columnHeader = headerGroup;
    	}else{
    		this.updateTableX(this.getNewPositions(this.rowHeader[this.rowHeader.length-1], headerGroup[this.rowHeader.length-1]));
    		this.rowHeader = headerGroup;
    	}
    	
    	this.reloadHeadersAndTable(horizontal);
    }

    //Calculate the translation vector after a transformation (for example DD)
    //headerLine2: the old headerLine
    //headerLine1: the new headerLine
    //For example:
    //headerLine2 = x y
    //headerLine1 = y x
    //returns 1 0 
    , getNewPositions : function(headerLine2, headerLine1){
    	var newPositions = new Array();
    	for(var y=0; y<headerLine1.length; y++){
        	for(var i=0; i<headerLine2.length; i++){
        		if(this.isTheSameHeader(headerLine2[i], headerLine1[y])){
        			if(headerLine2[i].hidden){
        				newPositions.push(i);
        			}else{
        				newPositions.push(i);
        			}
        			break;
        		}
        	}
    	}
    	return newPositions;
    }
    
    //check if header1.equals(header2)
    ,isTheSameHeader : function(header1, header2, debug){
    	var loop1 = header1; 
    	var loop2 = header2;
    	do{
    		if(debug){
    			alert(loop1.name+" "+loop2.name );
    		}
    		if(loop1.name!= loop2.name){
    			return false;
    		}
    		loop1 = loop1.father;
    		loop2 = loop2.father;
    	} while(loop1!=null);
    	return loop2==null;
    }
    
    //Update the order of the cells after a change in the column headers (Dd or hide/show)
    , updateTableY : function(newPositions){
    	var newEntries = new Array();
    	var entries = this.entries.getEntries();
    	for(var i=0; i<entries.length; i++){
    		var templine = new Array();
    		for(var y=0; y<entries[i].length; y++){
    		//	if(newPositions[y]!=null){
	        		templine.push(entries[i][newPositions[y]]);
	       // 	}
    		}
    		newEntries.push(templine);
    	}
    	this.entries.setEntries(newEntries);
    }

    //Update the order of the cells after a change in the row headers (Dd or hide/show)
    , updateTableX : function(newPositions){
    	var entries = this.entries.getEntries();
    	var newEntries = new Array();
    	for(var i=0; i<entries.length; i++){
    		//if(newPositions[i]!=null){
    		newEntries.push(entries[newPositions[i]]);
    		//}
    	}
    	this.entries.setEntries(newEntries);
    }
    
    //reload the container table
    , reloadTable : function(lazy){

    	var d1 = new Date();
    	
    	var tableRows = 2;
    	var tableColumns = 2;
    	var dataPanelStyle = "crosstab-table-data-panel";
    	var classEmptyBottomRight = 'crosstab-table-empty-bottom-right-panel';
    	
    	if(this.table!=null && this.datapanel!=null){
    		this.datapanel.destroy();
    		if(this.withRowsSum && this.datapanelRowSum!=null){
    			this.datapanelRowSum.destroy();
    		}
    		if(this.withColumnsSum  && this.datapanelColumnSum!=null){
    			this.datapanelColumnSum.destroy();
    		}
    		this.table.hide();
    		this.remove(this.table, false);
    	}
    	
		if(this.withRowsSum){
			tableColumns = 3;
			dataPanelStyle = dataPanelStyle+ " crosstab-none-right-border-panel";
		}else{
			classEmptyBottomRight = classEmptyBottomRight+' crosstab-none-top-border-panel';
		}
    	if(this.withColumnsSum){
    		tableRows = 3;
    		dataPanelStyle = dataPanelStyle+" crosstab-none-bottom-border-panel";
    	}else{
    		classEmptyBottomRight = classEmptyBottomRight+' crosstab-none-left-border-panel';
    	}
    	   	
    	this.table = new Ext.Panel({  
    		cls: 'centered-panel',
            layout:'table',
            border: false,
            layoutConfig: {
                columns: tableColumns,
                rows: tableRows
            }
        });
    	
   	    this.entriesPanel = this.getEntries(true, true);
   		var rowForView = this.getRowsForView();
   		var columnsForView = this.getColumnsForView();
   		
   		if(this.emptypanelTopLeft==null){
	   		this.emptypanelTopLeft = new Ext.Panel({
	   			//height: (this.columnHeader.length-1)*this.rowHeight,
	   	        //width: (this.rowHeader.length-1)*this.columnWidth,
	   	        cellCls: 'crosstab-table-empty-top-left-panel',
	   	        border: false,
	   	        html: ""
	   	    });
   		} 	
   		
   		if(this.withRowsSum && this.emptypanelTopRight==null){
	   		this.emptypanelTopRight = new Ext.Panel({
	   			//height: (this.columnHeader.length-1)*this.rowHeight,
	   	        //width: this.columnWidth,
	   	        cellCls: 'crosstab-table-empty-top-right-panel',
	   	        border: false,
	   	        html: ""
	   	    });
   		} 
   		
   		if(this.withColumnsSum && this.emptypanelBottomLeft==null){
	   		this.emptypanelBottomLeft = new Ext.Panel({
	   			//height: this.rowHeight,
	   	        //width: this.columnWidth,
	   	        cellCls: 'crosstab-table-empty-bottom-left-panel',
	   	        border: false,
	            layout:'table'
	   		});
   		} 
   		
   		if((this.withColumnsSum || this.withRowsSum) && this.emptypanelBottomRight==null){
	   		this.emptypanelBottomRight = new Ext.Panel({
	   			//height: this.rowHeight,
	   	        //width: this.columnWidth,
	   	        cellCls: classEmptyBottomRight,
	   	        border: false,
	   	        html: ""
	   	    });
   		} 
   		   		
    	var store = new Ext.data.ArrayStore({
    	    autoDestroy: true,
    	    storeId: 'myStore',
    	    fields: [
    	             {name: 'name'},
    	             'divId',
    	             {name: 'datatype'},
    	             {name: 'format'},
    	             {name: 'celltype'},
    	             {name: 'percent'}
    	    ]
    	});

		if(this.misuresOnRow){
			if(this.withColumnsSum || this.percenton=='column'){
				this.columnsTotalSumArray = this.calculateTotalSum();
			}
			if(this.withRowsSum || this.percenton=='row'){
				this.rowsTotalSumArray = this.rowsSum(true);
			}
		}else{
			if(this.withColumnsSum || this.percenton=='column'){
				this.columnsTotalSumArray = this.columnsSum(true);
			}
			if(this.withRowsSum || this.percenton=='row'){
				this.rowsTotalSumArray = this.calculateTotalSum();
			}	
		}
		
		//Add the percentage to the entries
		if(this.percenton=='column'){
			if(!this.misuresOnRow){
				for(var i=0; i< this.visibleRows; i++){
		        	for(var j=0; j< this.visibleColumns; j++){
		        		this.entriesPanel[i*this.visibleColumns+j].push(100*parseFloat(this.entriesPanel[i*this.visibleColumns+j][0])/parseFloat(this.columnsTotalSumArray[j]));
		        	}
		    	}
			}else{
		    	for(var i=0; i< this.visibleRows; i++){
		        	for(var j=0; j< this.visibleColumns; j++){
		        		this.entriesPanel[i*this.visibleColumns+j].push(100*parseFloat(this.entriesPanel[i*this.visibleColumns+j][0])/parseFloat(this.columnsTotalSumArray[i%this.columnsTotalSumArray.length][j]));
		        	}
		    	}	
			}
		} else if(this.percenton=='row'){
			if(this.misuresOnRow){
		    	for(var i=0; i< this.visibleRows; i++){
		        	for(var j=0; j< this.visibleColumns; j++){
		        		this.entriesPanel[i*this.visibleColumns+j].push(100*parseFloat(this.entriesPanel[i*this.visibleColumns+j][0])/parseFloat(this.rowsTotalSumArray[i]));
		        	}
		    	}	
			}else{
		    	for(var i=0; i< this.visibleRows; i++){
		        	for(var j=0; j< this.visibleColumns; j++){
		        		this.entriesPanel[i*this.visibleColumns+j].push(100*parseFloat(this.entriesPanel[i*this.visibleColumns+j][0])/parseFloat(this.rowsTotalSumArray[j%this.rowsTotalSumArray.length][i]));
		        	}
		    	}
			}
		}
		   	
    	store.loadData(this.entriesPanel);
    	var columnsForView = this.getColumnsForView();
    	
    	var ieOffset =0;
    	if(Ext.isIE){
    		ieOffset = 2;
    	}
    	
    	var tpl = new Ext.XTemplate(
    	    '<tpl for=".">'
    	    , '<div id="{divId}" class="x-panel crosstab-table-cells crosstab-table-cells-{celltype}" ' // the crosstab-table-cells class is needed as itemSelector
    	    , ' style="height: '+(this.rowHeight-2+ieOffset)+'px; width:'+(this.columnWidth-2)+'px; float:left;" >'
    	    , '  <div class="x-panel-bwrap"> '
    	    , '    <div style="width:'+(this.columnWidth-2)+'px; overflow:hidden; padding-top:'+(this.rowHeight-4-this.fontSize)/2+'px;font-size:'+this.fontSize+'px;">'
    	    , '    {[this.format(values.name, values.datatype, values.format, values.percent,'+this.percentageFontSize+' )]}'
    	    , '    </div> '
    	    , '  </div>'
    	    , '</div>'
    	    , '</tpl>'
    	    , {
    	    	format: this.format
    	    }
    	);
    	
    	var dataView = new Ext.DataView({
	        store: store,
	        tpl: tpl,
	        itemSelector: 'div.crosstab-table-cells',
	        trackOver:true
	    });
    	dataView.on('mouseleave', function(dataView, index, htmlNode, event) {
            clearTimeout(this.selectedColumnTimeout);
            clearTimeout(this.selectedRowTimeout);
            var divId = eval(htmlNode.id);
           	var row = divId[0];
           	var column = divId[1];
          	this.removeHighlightOnColumn(column);
          	this.removeHighlightOnRow(row);
//            this.selectedColumnExitTimeout=(this.removeHighlightOnColumn).defer(200,this,[column]);
//            this.selectedRowExitTimeout=(this.removeHighlightOnRow).defer(200,this,[row]);
        }, this);
    	dataView.on('mouseenter', function(dataView, index, htmlNode, event) {
//    		clearTimeout(this.selectedColumnExitTimeout);
//    		clearTimeout(this.selectedRowExitTimeout);
    		var divId = eval(htmlNode.id);
           	var row = divId[0];
           	var column = divId[1];
//            this.selectedColumnTimeout=(this.highlightColumnWithTimeOut).defer(200,this,[column]);
//            this.selectedRowTimeout=(this.highlightRowWithTimeOut).defer(200,this,[row]);
           	
           	//defer the highlight: during the navigation tipically the mouse
           	// passes over some cells with out stop in them. Deferring the highlightColumn
           	// and eith the clearTimeout in the mouseleve event we try to not highlight the transit cells
            this.selectedColumnTimeout=(this.highlightColumn).defer(200,this,[column]);
            this.selectedRowTimeout=(this.highlightRow).defer(200,this,[row]);
        }, this);
    	
    	this.datapanel = new Ext.Panel({
            width: (columnsForView)*(this.columnWidth),
            height: (rowForView)*(this.rowHeight)+1,
            cellCls: dataPanelStyle,
            border: false,
    	    layout:'fit',
    	    items: dataView
    	});

   		this.table.add(this.emptypanelTopLeft);
   		this.table.add(this.columnHeaderPanelContainer);
   		this.table.add(this.rowHeaderPanelContainer);
   		this.table.add(this.datapanel);
		if(this.withRowsSum){
			this.datapanelRowSum = this.getRowsSumPanel(tpl, rowForView, this.withColumnsSum);
			this.table.add(this.datapanelRowSum);
		}
		
    	if(this.withColumnsSum){
	   		this.datapanelColumnSum = this.getColumnsSumPanel(tpl, columnsForView, this.withRowsSum);
	   		this.table.add(this.datapanelColumnSum);
	   		// add the total of totals
	   		if(this.withRowsSum && this.withColumnsSum){
	   			this.table.add(this.superSumPanel);	
	   		}
    	}
    	
    	
   		this.add(this.table);
   		if(!lazy){
	   		var d22 = new Date();
	   		this.rowHeaderPanelContainer.doLayout();
	   		var d3 = new Date();
	   		this.columnHeaderPanelContainer.doLayout();
	   		var d4 = new Date();
	   		this.datapanel.doLayout();
	   		var d5 = new Date();
	   		this.emptypanelTopLeft.doLayout();
	   		var d6 = new Date();
	
	   		this.table.doLayout(false);
	   		this.doLayout(false);
   		}
   		var d7 = new Date();

//   		alert("B: "+(d3-d22));
//   		alert("C: "+(d4-d3));
//   		alert("D: "+(d5-d4));
//   		alert("E: "+(d6-d5));
//   		alert("F: "+(d7-d1));
   		
   		
    }
    
    
    , format: function(value, type, format, percent, percentFontSize) {
    	if(value=='NA'){
    		return value;
    	}
		var str;
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
			
			//if the tipe is a number and the percenton variable is not null in the configuration
			//we add the percent in the cell
			if(percent!=undefined && percent!=null && percent!='' && (type == 'float' || type == 'int')){
				str = '<div style=\'text-align: right;\'>' + Sbi.qbe.commons.Format.number(valueObj, type) +'<span style="font-size:'+percentFontSize+'px;"> (' + Sbi.qbe.commons.Format.number(percent, 'int') +'%)</span></div>';
			}else{
				str = Sbi.locale.formatters[type].call(this, valueObj); // formats the value
			}
			
			return str;
		} catch (err) {
			return value;
		}
	}
    
    
    , reloadHeadersAndTable: function(horizontal, lazy){
   // 	if(horizontal || horizontal==null){
    	
    		var span=1;
    		if(this.withRowsSum){
    			span=2;
    		}
        	this.columnHeaderPanel = this.buildHeaderGroup(this.columnHeader, true);
        	if(this.columnHeaderPanelContainer!=null){
        		this.columnHeaderPanelContainer.destroy();
        	}
        	this.columnHeaderPanelContainer = new Ext.Panel({
	   			style: 'margin: 0px; padding: 0px;',
	   			cellCls: 'crosstab-column-header-panel-container',
	   			height: 'auto',
	   	        width: 'auto',
	   	        border: false,
	   	        layout:'table',
	   	        colspan: span,
	   	        layoutConfig: {
	   	            columns: 1
	   	        },
	   	        items: this.columnHeaderPanel
	   	    });
//    	}
//    	
//    	if(!horizontal || horizontal==null){
    		if(this.withColumnsSum){
    			span=2;
    		}else{
    			span=1;
    		}
	    	this.rowHeaderPanel = this.buildHeaderGroup(this.rowHeader, false);
	    	if(this.rowHeaderPanelContainer!=null){
	    		this.rowHeaderPanelContainer.destroy();
	    	}
	   		this.rowHeaderPanelContainer = new Ext.Panel({
	   			style: 'margin: 0px; padding: 0px; ',
	   			cellCls: 'crosstab-row-header-panel-container',
	   			height: 'auto',
	   	        width: 'auto',
	   	        layout:'table',
	   	        border: false,
	   	        rowspan: span,
	   	        layoutConfig: {
	   	    		
	   	            columns: this.rowHeader.length
	   	        },
	   	        items: this.rowHeaderPanel,
	   	        colspan: 1
	   	    });
 //   	}
		this.reloadTable(lazy);
    }
    
	, createResizable: function(aPanel, heandles, items, horizontal) {
		
    	var iePinned = false;
    	if(Ext.isIE){
    		iePinned = true;
    	}
		
		aPanel.on('render', function() {
			var resizer = new Ext.Resizable(this.id, {
			    handles: heandles,
			    pinned: iePinned
			});
			resizer.on('resize', function(resizable, width, height, event) {
				
				for(var i=0; i<items.length; i++){
					if(horizontal){
						items[i].updateStaticDimension(height);
					}else{
						items[i].updateStaticDimension(width);
					}
				}
			}, this);
		}, aPanel);
	}
 
    
    //Add a new block in the table (a subtree with the headhers and a set of columns or rows)
    //level: the level in witch put the root of the subtree 
    //node: the root of the subtree of headers to add
    //headers: the headers (columnHeader or rowheader)
    //entries: the rows or columns with the data to add
    //horizontal: true if the entries are columns, false otherwise
    //lazy: true if we only want to insert the row/columns in the data structures, but not in spread the data in the GUI.
    //      it's usefull if we call this method more than one time: we call it with lazy=true far all the iteration and with lazy=false in the last one +
    //       (take a look at the method calculateCF)
    , addNewEntries: function(level,node,headers,entries, horizontal, lazy){
    	
    	var father = node.father;
    	var dimensionToAdd= node.thisDimension;
    	//update the father
    	father.childs.push(node);
    	
      	//update the fathers dimension of the subtree of headers where we put the node..
	   	while(father!=null){
	   		father.thisDimension= father.thisDimension+dimensionToAdd;
	   		father.leafsNumber = father.leafsNumber+dimensionToAdd;
	   		father=father.father;
	   	}
    	var nodeToAddList = new Array();
    	var freshNodeToAddList;
    	var nodePosition;
    	var startPos;
    	var endPos;
    	var nodeS;
    	nodeToAddList.push(node);

    	//Find the index in the headers[level] where put the node
    	var startDimension=0;
		for(var i=0; i<headers[level].length; i++){
			nodeS =  headers[level][i];
			if(nodeS.father == node.father){
				startDimension=startDimension+this.getLeafsNumber(node.father)-this.getLeafsNumber(node);
				startPos = i+nodeS.father.childs.length-1;
				break;
			}
			startDimension = startDimension+this.getLeafsNumber(nodeS);
		}
		nodePosition=startDimension;
		
		//when we add a new node, we have to move forward all the
		//pannels that live after the nodePosition, and we have
		//to add the node and all its childs
    	for(var y=level; y<headers.length;y++){

			//move the pannels that live after the position of the pannels
			for(var j=headers[y].length-1; j>=startPos; j--){
				headers[y][j+nodeToAddList.length] = headers[y][j];
			}

			//add the node and all its childs
			for(var j=0; j<nodeToAddList.length; j++){
				headers[y][startPos+j] = nodeToAddList[j];
			}
			
			if(y<headers.length-1){
				//prepares the fresh variable for the next iteration with all the childs 
				freshNodeToAddList = new Array();
				for(var j=0; j<nodeToAddList.length; j++){
					freshNodeToAddList = freshNodeToAddList.concat(nodeToAddList[j].childs);
				}
				nodeToAddList = freshNodeToAddList;
				var freshStartDimension=0;
				
				for(var i=0; i<=headers[y+1].length; i++){
					if(startDimension == freshStartDimension){
						startPos = i;
						break;
					}
					if(headers[y+1][i]!=null){
						freshStartDimension=freshStartDimension+this.getLeafsNumber(headers[y+1][i]);
					}else{
						startPos = i;
						break;
					}
				}
			}
    	}
    	
    	//add the columns or the rows
    	if(horizontal){
    		this.entries.addColumns(nodePosition,entries);
    	}else{
    		this.entries.addRows(nodePosition,entries);
    	}
    	
    	if(horizontal){
    		this.columnHeader = headers;
    	}else{
    		this.rowHeader = headers;
    	}
    	
    	if(!lazy){
	    	this.setFathers(headers);
			this.setDragAndDrop(headers, horizontal, this);
	    	this.reloadTable();
    	}
    }   

    , addCalculatedField: function(level, horizontal, op, CFName){
    	var calculatedField = new Sbi.crosstab.core.CrossTabCalculatedField(CFName, level, horizontal, op); 
    	if(this.calculatedFields==null){
    		this.calculatedFields = new Array();
    	}
    	this.calculatedFields.push(calculatedField);
    }
    
    , modifyCalculatedField: function(level, horizontal, op, CFName){
    	//alert(this.calculatedFields.toSource());
    	if(this.calculatedFields!=null){
    		for(var i=0; i<this.calculatedFields.length; i++){
    			if(this.calculatedFields[i].name == CFName){
    				this.calculatedFields[i] = new Sbi.crosstab.core.CrossTabCalculatedField(CFName, level, horizontal, op);
    			}
    		}
    	}
    	//alert(this.calculatedFields.toSource());
    }

    , getCalculatedFields: function() {
    	return this.calculatedFields;
    }
    
    // Returns an array with the lower and upper bounds of a header:
    // The lower bound is the id(position of the leaf inside the header[header.lenght-1] array) of the firs leaf, the upper is the id of the last one
//    , getHeaderBounds: function(header, horizontal, level){
//    	var headers;
//    	var bounds = new Array();
//    	var dimension=0;
//    	
//    	if(header != null){
//    		horizontal = header.horizontal;
//    		level = header.level;
//    	}
//    	if(level == null){
//    		level =0;
//    	}
//    	if(horizontal){
//    		headers = this.columnHeader[level];
//    	}else{
//    		headers = this.rowHeader[level];
//    	}
//    	if(header == null){
//    		header = headers[0];
//    	}
//    	
//    	for(var i=0; i<headers.length; i++){
//    		if(headers[i]==header){
//    			bounds[0] = dimension;
//    			bounds[1] = dimension+headers[i].thisDimension-1;
//    			break; 
//    		}else{
//    			dimension = dimension+headers[i].thisDimension;
//    		}
//    	}
//    	return bounds;
//    } 
    
    // Returns an array with the lower and upper bounds of a header:
    // The lower bound is the id(position of the leaf inside the header[header.lenght-1] array) of the firs leaf, the upper is the id of the last one
    , getHeaderBounds: function(header, horizontal, level){
    	var headers;
    	var bounds = new Array();
    	var dimension=0;
    	
    	if(header != null){
    		horizontal = header.horizontal;
    		level = header.level;
    	}
    	if(level == null){
    		level =0;
    	}
    	if(horizontal){
    		headers = this.columnHeader[level];
    	}else{
    		headers = this.rowHeader[level];
    	}
    	if(header == null){
    		header = headers[0];
    	}
    	
    	for(var i=0; i<headers.length; i++){
    		if(headers[i]==header){
    			bounds[0] = dimension;
    			bounds[1] = dimension+this.getLeafsNumber(headers[i])-1;
    			break; 
    		}else{
    			dimension = dimension+this.getLeafsNumber(headers[i]);
    		}
    	}
    	
    	return bounds;
    } 
    
    
    
    , getLeafsNumber: function(header){
    	
    //	return header.leafsNumber;
    	
    	if(header.childs==0){
    		return 1;
    	}else{
    		var leafs=0;
    		for(var i=0; i<header.childs.length; i++){
    			leafs = leafs+this.getLeafsNumber(header.childs[i]);
    		}
    		return leafs;
    	}
    }
    
    
    //remove a header and all its childs from the headers structure
    , removeHeader: function(header, updateFathers){
    	
    	if(header.horizontal){
    		headers = this.columnHeader;
    	}else{
    		headers = this.rowHeader;
    	}
    	var array;
    	var value;
    	var freshArray;
    	if(updateFathers){
	    	var father = header.father;
	    	var leafsToRemove= this.getLeafsNumber(header);
	    	var dimensionToRemove= header.thisDimension;
	    	
	    	while(father!=null){
	    		
	    		if((this.getLeafsNumber(father)-leafsToRemove)==0){
	    			this.removeHeader(father, true);//se il padre =0 allora lo rimuovo
	    			return;
	    		}
	    		father.thisDimension= father.thisDimension-dimensionToRemove;
	    		father.leafsNumber= father.leafsNumber-leafsToRemove;
	    		father=father.father;
	    	}  
	    	
	    	for(var i=0; i<header.father.childs.length; i++){
	    		if(this.isTheSameHeader(header.father.childs[i],header)){
	    			for(var j=i; j<header.father.childs.length-1; j++){
	    				header.father.childs[j]=header.father.childs[j+1];
	    			}
	    			header.father.childs.length = header.father.childs.length-1;
	    			i=header.father.childs.length;
	    		}
	    	} 	
    	}
    	
    	for(var i=0; i<headers[header.level].length; i++){
    		if(this.isTheSameHeader(headers[header.level][i],header)){
    			for(var j=i; j<headers[header.level].length-1; j++){
    				headers[header.level][j]=headers[header.level][j+1];
    			}
    			headers[header.level].length = headers[header.level].length-1;
    			i=headers[header.level].length;
    		}
    	}

    	if(header.horizontal){
    		this.columnHeader[header.level] = headers[header.level];
    	}else{
    		this.rowHeader[header.level] = headers[header.level];
    	}

    	
    	for(var i=0; i<header.childs.length; i++){
    		this.removeHeader(header.childs[i], false);
    	}
    }   

    //remove the header and all its entries
    , removeEntries: function(header, lazy){
		var bounds = this.getHeaderBounds(header);
		if(header.horizontal){
			this.entries.removeColumns(bounds[0],bounds[1]);
		}else{
			this.entries.removeRows(bounds[0],bounds[1]);
		}
		
		this.removeHeader(header, true);

		if(!lazy){
			this.reloadHeadersAndTable();
		}
    }
	
    
  //============================
  //Partial Sum
  //============================
    
    //Calculate the partial sum of the rows
    , rowsSum : function(isATotalSum, withHidden){
    	return this.rowsHeaderSum(0, this.entries.getEntries()[0].length-1, isATotalSum, withHidden);
    }
    
    //Calculate the partial sum of the columns
    , columnsSum : function(isATotalSum, withHidden){
    	return this.columnsHeaderSum(0, this.entries.getEntries().length-1, isATotalSum, withHidden);
    }  

    , addHeaderSum: function(header, type){
    	if(header.type=='data'){
	    	var bounds = this.getHeaderBounds(header);
	    	var sum; 
	    	var headers;
	    	if(header.horizontal){
	    		sum = this.rowsHeaderSum(bounds[0],bounds[1]);
	    		headers = this.columnHeader;
	    	}else{
	    		sum = this.columnsHeaderSum(bounds[0],bounds[1]);
	    		headers = this.rowHeader;
	    	}
	    	var sums = new Array();
	    	
	    	
	    	if(header.horizontal){
	    		var totalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name: LN('sbi.crosstab.header.total.text'), thisDimension:1, horizontal:header.horizontal, level:header.level+1, width:null, height:header.childs[0].height});
	    	}else{
	    		var totalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name: LN('sbi.crosstab.header.total.text'), thisDimension:1, horizontal:header.horizontal, level:header.level+1, width:header.childs[0].width, height:null});
	    	}

	    	totalNode.hidden = header.hidden;
	    	if(totalNode.hidden){
	    		totalNode.thisDimension=0;
	    	}
	    	
	    	totalNode.type=type;
	    	totalNode.father = header;
	    	this.setHeaderListener(totalNode, true);
	    	
	    	if (header.childs[0].childs.length>0){
	    	
		    	var cousinNode = header.childs[0];
		    	var freshTotalChildNode;
		    	var freshFatherNode = totalNode;
		    	
		    	while(cousinNode.childs.length>0){
		    		freshTotalChildNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name: LN('sbi.crosstab.header.total.text'), thisDimension:1, horizontal:freshFatherNode.horizontal, level:freshFatherNode.level+1, width:cousinNode.childs[0].width, height:cousinNode.childs[0].height});
		    		freshTotalChildNode.type=type;
		    		freshTotalChildNode.father = freshFatherNode;
		    		freshTotalChildNode.hidden = freshFatherNode.hidden;
		    		freshFatherNode.childs.push(freshTotalChildNode);
		    		freshFatherNode = freshTotalChildNode;
		    		cousinNode = cousinNode.childs[0];
		    		this.setHeaderListener(freshTotalChildNode, true);
			    	if(totalNode.hidden){
			    		freshTotalChildNode.thisDimension=0;
			    	}
		    	}
	    	}
	    	sums.push(sum);
	    	this.addNewEntries(header.level+1,totalNode,headers,sums, header.horizontal, true);
    	}
    }
    
      //Calculate the partial sum of the rows
    , rowsHeaderSum : function(start, end, isATotalSum, withHidden){
    	var entries = this.entries.getEntries();
    	var sum = new Array();
    	var partialSum;
    	var number;
    	for(var i=0; i<entries.length; i++){
    		partialSum =0;
    		if(withHidden || !this.rowHeader[this.rowHeader.length-1][i].hidden){
	        	for(var j=start; j<entries[0].length && j<=end; j++){
	        		if((withHidden || !this.columnHeader[this.columnHeader.length-1][j].hidden) && this.columnHeader[this.columnHeader.length-1][j].type=='data'){
	        			number = parseFloat(entries[i][j]);
	        			if(!isNaN(number)){
	        				partialSum = partialSum + number;
	        			}
	        		}
	        	}
	    		if(isATotalSum){
	    			sum.push(''+partialSum);
	    		}
    		}
    		if(!isATotalSum){
    			sum.push(''+partialSum);
    		}
    	}
    	return sum;
    }
    
    //Calculate the partial sum of the columns
    , columnsHeaderSum : function(start, end, isATotalSum, withHidden){
    	var entries = this.entries.getEntries();
    	var sum = new Array();
    	var partialSum;
    	var number;
       	for(var j=0; j<entries[0].length; j++){
	       	partialSum =0;
	       	if(withHidden || !this.columnHeader[this.columnHeader.length-1][j].hidden){
	        	for(var i=start; i<entries.length && i<=end; i++){
	        		if((withHidden || !this.rowHeader[this.rowHeader.length-1][i].hidden) && this.rowHeader[this.rowHeader.length-1][i].type=='data'){
	        			number = parseFloat(entries[i][j]);
	        			if(!isNaN(number)){
	        				partialSum = partialSum + number;
	        			}
	        		}
	        	}
	    		if(isATotalSum){
	    			sum.push(''+partialSum);
	    		}
    		}
    		if(!isATotalSum){
    			sum.push(''+partialSum);
    		}
    	}
    	return sum;
    } 

    //Calculate the sum of the rows
    , rowsHeaderListSum : function(lines, isATotalSum, withHidden){
    	var entries = this.entries.getEntries();
    	var sum = new Array();
    	var partialSum;
    	var number;
    	for(var i=0; i<entries.length; i++){
    		partialSum =0;
    		if(withHidden || !this.rowHeader[this.rowHeader.length-1][i].hidden){
	        	for(var j=0; j<lines.length; j++){
	        		
	        		if(withHidden || !this.columnHeader[this.columnHeader.length-1][lines[j]].hidden ){//}&& this.columnHeader[this.columnHeader.length-1][lines[j]].type=='data'){
	        			number = parseFloat(entries[i][lines[j]]);
	        			if(!isNaN(number)){
	        				partialSum = partialSum + number;
	        			}
	        		}
	        	}
	    		if(isATotalSum){
	    			sum.push(''+partialSum);
	    		}
    		}
    		if(!isATotalSum){
    			sum.push(''+partialSum);
    		}
    	}
    	return sum;
    }
    
    //Calculate the sum of the columns
    , columnsHeaderListSum : function(lines, isATotalSum, withHidden){
    	
    	var entries = this.entries.getEntries();
    	var sum = new Array();
    	var partialSum;
    	var number;
       	for(var j=0; j<entries[0].length; j++){
       		partialSum =0;
	       	if(withHidden || !this.columnHeader[this.columnHeader.length-1][j].hidden){
	        	for(var i=0; i<lines.length; i++){
	        		if(withHidden || !this.rowHeader[this.rowHeader.length-1][lines[i]].hidden ){//&& this.rowHeader[this.rowHeader.length-1][lines[i]].type=='data'){
	        			number = parseFloat(entries[lines[i]][j]);
	        			if(!isNaN(number)){
	        				partialSum = partialSum + number;
	        			}
	        		}
	        	}
	    		if(isATotalSum){
	    			sum.push(''+partialSum);
	    		}
    		}
    		if(!isATotalSum){
    			sum.push(''+partialSum);
    		}
    	}
    	return sum;
    } 
  
  
    , removePartialSum: function(lazy){
    	for(var j=0; j<2; j++){
			var headers;
			var horizontal = j==0;
			if(horizontal){
				if(this.misuresOnRow){
					headers = this.columnHeader[this.columnHeader.length-1];
				}else{
					headers = this.columnHeader[this.columnHeader.length-2];
				}
			}else{
				if(this.misuresOnRow){
					headers = this.rowHeader[this.rowHeader.length-2];
				}else{
					headers = this.rowHeader[this.rowHeader.length-1];
				}
			}  		
			for(var i=headers.length-1; i>=0; i--){
				if(headers[i].type == 'partialsum'){
					this.removeEntries(headers[i], true);
				}
			}
    	}
    	if(!lazy){
    		this.reloadHeadersAndTable();
    	}

    }
    
    
    , calculatePartialSum: function(horizontal, lazy){
    	var withRowsPartialSumLocal = this.withRowsPartialSum;
    	var withColumnsPartialSumLocal = this.withColumnsPartialSum;
    	
    	if(horizontal!=null){
    		withRowsPartialSumLocal = withRowsPartialSumLocal && !horizontal;
    		withColumnsPartialSumLocal = withColumnsPartialSumLocal && horizontal;
    	}
    	
		if(withRowsPartialSumLocal && !this.misuresOnRow){
		    for(var i=0; i<this.rowHeader.length-1; i++){
		        for(var j=0; j<this.rowHeader[i].length; j++){
		        	this.addHeaderSum(this.rowHeader[i][j], 'partialsum');
		        }
		    }
		}
		if(withColumnsPartialSumLocal && this.misuresOnRow){
			for(var i=0; i<this.columnHeader.length-1; i++){
		        for(var j=0; j<this.columnHeader[i].length; j++){
		        	this.addHeaderSum(this.columnHeader[i][j], 'partialsum');
		        }
		    }
		}
		
	//	this.reloadHeadersAndTable();

	    if((withRowsPartialSumLocal && this.misuresOnRow) || (withColumnsPartialSumLocal && !this.misuresOnRow)){
		
    		if(!this.misuresOnRow){
    			headers = this.columnHeader;
    		}else{
    			headers = this.rowHeader;
    		}
    		
		    var check = false;
		    var bounds;
		    var sums;
		    var measuresNames;
		    var measuresPosition;
		    var partialTotalNode;
		    var freshChild;
		    var measurePosition;
		    if(headers.length>=3){//if there are more than one level
		    	//calculate the partial sum and prepare the header of the level above the measures
		    	var i = headers.length-3;
		        for(var j=0; j<headers[i].length; j++){

		        	bounds = this.getHeaderBounds(headers[i][j]);
		        	//alert(headers[i][j].name+" "+bounds.toSource());
			    	measuresNames = new Array();
			    	measuresPosition = new Array();

				    for(var k=bounds[0]; k<=bounds[1]; k++){
				    	if(headers[headers.length-1][k].type=='data' && !headers[headers.length-1][k].hidden){
				    		measurePosition = measuresNames.indexOf(headers[headers.length-1][k].name);
				    		if(measurePosition<0){
				    			measuresNames.push(headers[headers.length-1][k].name);
				    			measurePosition = measuresNames.length-1;
				    			measuresPosition.push(new Array());
				    		}
				    		measuresPosition[measurePosition].push(k);
					    }
				    }
				    
				    if(measuresNames.length>0){
				    
					    sums = new Array(); 
					    for(var x=0; x<measuresNames.length;x++){
					    	if(this.misuresOnRow){
			 		    		sums.push(this.columnsHeaderListSum(measuresPosition[x]));
					    	}else{
					    		sums.push(this.rowsHeaderListSum(measuresPosition[x]));
					    	}
					    }
					    
					    if(headers[i][0].horizontal){
					    	partialTotalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: measuresNames.length, horizontal: headers[i][0].horizontal, level: headers.length-2, width: null , height: headers[headers.length-2][0].height});
					    }else{
					    	partialTotalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: measuresNames.length, horizontal: headers[i][0].horizontal, level: headers.length-2, width: headers[headers.length-2][0].width, height: null});	
					    }
					    partialTotalNode.type = 'partialsum';
					    partialTotalNode.father = headers[i][j];
					    this.setHeaderListener(partialTotalNode, true);
					    
						for(var k=0; k<measuresNames.length; k++){
							freshChild = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:measuresNames[k], thisDimension: 1, horizontal: headers[i][0].horizontal, level: headers.length-1, width: headers[headers.length-1][0].width, height: headers[headers.length-1][0].height});
							partialTotalNode.childs.push(freshChild);
							freshChild.father = partialTotalNode;
							freshChild.type = 'partialsum';
							this.setHeaderListener(freshChild, true);
						}	

						this.addNewEntries(partialTotalNode.level,partialTotalNode,headers,sums, partialTotalNode.horizontal, true);
						
				    }
		        }
		        
		        //build the headres of all the others levels
		        var measuresSum = new Array();
		        var lineCount=0;
		        for(var k=i; k>1; k--){
		        	
		        	if(!this.misuresOnRow){
	        			headers = this.columnHeader;
	        		}else{
	        			headers = this.rowHeader;
	        		}
		        	
		        	lineCount=0;
		        	var partialSumNode;
		        	var grandFather = headers[k][0].father;
		        	var j=0;
		        	var headers_k1_length = headers[k+1].length;
		        	while(j<headers[k+1].length){
		        		var measuresPosition = new Array();
	    	        	while( j<headers[k+1].length && grandFather.name == headers[k+1][j].father.father.name){
		        	       	if(headers[k+1][j].type=='partialsum'){
		        	       		measuresPosition.push(lineCount);
		        	       		partialSumNode=headers[k+1][j];
		        	       	}
		        	       	lineCount = lineCount+ this.getLeafsNumber(headers[k+1][j]);
		        	       	j++;
	        	        }
	
	    	        	if(partialSumNode!=null){
	    	        		//prepare the total node
	    	        		if(partialSumNode.horizontal){
	    	        			var totalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: partialSumNode.thisDimension, horizontal: partialSumNode.horizontal, level: k, width: null, height: headers[k][0].height});	
	    	        		}else{
	    	        			var totalNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:LN('sbi.crosstab.header.total.text'), thisDimension: partialSumNode.thisDimension, horizontal: partialSumNode.horizontal, level: k, width: headers[k][0].width, height: null});	
	    	        		}
		        	        
		        	        totalNode.father = partialSumNode.father.father;
		        	        totalNode.type = 'partialsum';
		        	        var totalNodeChild = this.cloneNode(partialSumNode,totalNode,true);
		        	        totalNode.childs.push(totalNodeChild);
		        	        this.setHeaderListener(totalNode, true);

		        	        var totalSum = new Array();
		        	        for(var y=0; y<this.getLeafsNumber(partialSumNode); y++){//the number of the leafs(measures)
		        	        	var lineToSum = new Array();
		        	        	var sum;
			        	        for(var x=0; x<measuresPosition.length; x++){
			        	        	lineToSum.push(measuresPosition[x]+y);
			        	        }	
			        	        if(!totalNode.horizontal){
			        	        	totalSum.push(this.columnsHeaderListSum(lineToSum));
			        	        }else{
			        	        	totalSum.push(this.rowsHeaderListSum(lineToSum));
			        	        }
		        	        }

		        	        this.addNewEntries( k,totalNode,headers,totalSum, totalNode.horizontal, true);
		        	        //prepare for the next step
		        	        if(j+2<headers[k+1].length){//2 perch� bisogna aggiunge i per il subtotale del livello k+1 piu il totale che sto aggiungendo ora
	        	        		j=j+1; //the totalNode entry
	        	        		grandFather = headers[k+1][j].father.father;
	        	        		lineCount = lineCount+this.getLeafsNumber(partialSumNode);
	        	        		partialSumNode = null;
	        	        	}else{
	        	        		break;
	        	        	}
	    	        	}else{
	    	        		break;
	    	        	}
		        	}
		        }
		    }
	    }
	    
	    if((withRowsPartialSumLocal || withColumnsPartialSumLocal) &&!lazy){
	    	this.reloadHeadersAndTable();
	    }
	    
    }
    
    //calculate the totals
    , calculateTotalSum: function(withHidden){
	    if((this.withColumnsSum && this.misuresOnRow) || (this.withRowsSum && !this.misuresOnRow)){
    		if(!this.misuresOnRow){
    			headers = this.columnHeader;
    		}else{
    			headers = this.rowHeader;
    		}
		    var sums = new Array();
		    var sumsLine;
		    var measuresNumber;
		    var measuresPosition;
		    
		    if(!withHidden){
		    	measuresNumber = this.measuresNames.length
		    	measuresPosition = this.measuresPosition;
		    }else{
		    	measuresNumber = this.measuresNumber;
		    	measuresPosition = this.allMeasuresPosition;
		    }
		    
		    if(headers.length>=2){//if there are more than zero level
		    	
		        for(var j=0; j<measuresNumber;j++){
		        	//if(measuresPosition[j].length>0){
			    	if(this.misuresOnRow){
			    		sumsLine = this.columnsHeaderListSum(measuresPosition[j], true, withHidden);
			    	}else{
			    		sumsLine = this.rowsHeaderListSum(measuresPosition[j], true, withHidden);
			    	}
			    	//if we add rows and there is the sum also for the rows, so we have to
			    	//remove the last line (the total line)
//			    	if((this.withColumnsSum && this.misuresOnRow && this.withRowsSum) || (this.withRowsSum && !this.misuresOnRow && this.withColumnsSum)){
//			    		sumsLine.length = sumsLine.length-1;
//			    	}
			    	sums.push(sumsLine);
			    	
		        }
		    }
		    return sums;
		   
	    }
	   
    }
    
    , getVisibleMeasures: function(headers){
    	var i = headers.length-1;
    	
    	//not hidden
    	this.measuresNames = new Array();
    	this.measuresPosition = new Array();

    	//also hidden
    	this.allMeasuresPosition = new Array();
    	this.allMeasuresNames = new Array();
    	this.measuresNumber = 0;
        for(var j=0; j<headers[i].length; j++){
        	if(headers[i][j].type=='data'){
        		var measurePositionAll = this.allMeasuresNames.indexOf(headers[i][j].name);
        		if(measurePositionAll<0){
        			this.measuresNumber++;
	        		this.allMeasuresNames.push(headers[i][j].name);
	        		measurePositionAll = this.allMeasuresNames.length-1;
	        		this.allMeasuresPosition.push(new Array());
	        	}
	        	this.allMeasuresPosition[measurePositionAll].push(j)
        		if(!headers[i][j].hidden){
    	        	var measurePosition = this.measuresNames.indexOf(headers[i][j].name);
    	        	if(measurePosition<0){
    	        		this.measuresNames.push(headers[i][j].name);
    	        		measurePosition = this.measuresNames.length-1;
    	        		this.measuresPosition.push(new Array());
    	        	}
    	        	this.measuresPosition[measurePosition].push(j);
        		}
	        }
        }
        
        this.visibleMeasuresMetadataLength=0;
        
        for(var j=0; j<this.measuresMetadata.length; j++){
        	if(this.measuresNames.indexOf(this.measuresMetadata[j].name)>=0){
        		this.measuresMetadata[j].visible = true;
        		this.visibleMeasuresMetadataLength++;
        	}else{
        		this.measuresMetadata[j].visible = false;
        	}
        }
    }
    
    
    
    
    //Build the panel with the sum of the columns
    , getColumnsSumPanel : function(tpl, columnsForView, withRowsSum){
    	var storeColumns = new Ext.data.ArrayStore({
    	    autoDestroy: true,
    	    storeId: 'myStore',
    	    fields: [
    	             {name: 'name'},
    	             'divId',
    	             {name: 'datatype'},
    	             {name: 'format'},
    	             {name: 'celltype'}
    	    ]
    	});
    	var sumColumnsStore = new Array();
    	var dataViewWidth;    	
		var sumColumns = this.columnsTotalSumArray;
		
		if(this.misuresOnRow){
			
	    	var superSumColumnsArray= new Array();
	    	var superSumColumns=0;			
	   		//we use superSumColumnsArray, superSumColumns for the total of totals
	   		
	   		for(var j=0; j<sumColumns.length; j++){
	   			superSumColumns=0;
	   			for(var i=0; i<sumColumns[j].length; i++){
	   				//in the total sum we not consider calculated fields and partial sums
	   				if(!this.rowHeader[this.rowHeader.length-1][i].type!='CF' &&
	   				   !this.rowHeader[this.rowHeader.length-1][i].type!='partialsum'){
	   						superSumColumns = superSumColumns+parseFloat(sumColumns[j][i]);
	   				}
	   				var a = new Array();
					a.push(sumColumns[j][i]);
					a.push('[sumC'+j+','+i+']');
					a.push('float');
					a.push(null);
					a.push('totals');
					sumColumnsStore.push(a);
	   			}
	   			superSumColumnsArray.push(superSumColumns);
	   		}
	   		dataViewHeight = (this.rowHeight)*sumColumns.length;
	   		this.buildSuperTotalPanel(superSumColumnsArray, tpl, dataViewHeight, this.columnWidth);
		}else{
	   		
	   		for(var j=0; j<sumColumns.length; j++){
	   			var a = new Array();
	   			a.push(sumColumns[j]);
	   			a.push('[sumC'+j+']');
				a.push('float');
				a.push(null);
				a.push('totals');
	   			sumColumnsStore.push(a);
	   		}
	   		dataViewHeight = (this.rowHeight);
		}

    	storeColumns.loadData(sumColumnsStore);
    	
		var cellCls = 'crosstab-column-sum-panel-container';
		if(withRowsSum){
			cellCls = cellCls + ' crosstab-none-right-border-panel';
		}
    	var datapanelColumnSum = new Ext.Panel({
    		cellCls: cellCls,
            width: (columnsForView)*(this.columnWidth),
            height: dataViewHeight,
            border: false,
    	    layout:'fit',
    	    items: new Ext.DataView({
    	        store: storeColumns,
    	        tpl: tpl,
    	        itemSelector: 'div.crosstab-table-cells'
    	    })
    	});
    	return datapanelColumnSum;
    }
    
    //Build the panel with the partial sum of the rows
    , getRowsSumPanel : function(tpl,rowForView, withColumnsSum){
    	var storeRows = new Ext.data.ArrayStore({
    	    autoDestroy: true,
    	    storeId: 'myStore',
    	    fields: [
    	             {name: 'name'},
    	             'divId',
    	             {name: 'datatype'},
    	             {name: 'format'},
    	             {name: 'celltype'}
    	    ]
    	});
		var sumRowsStore = new Array();
   		var dataViewWidth;

		var sumRows = this.rowsTotalSumArray;
		
		if(!this.misuresOnRow){//there is one column for each measure
			
	    	//we use superSumColumnsArray for the total of totals
	    	var superSumColumnsArray= new Array();
   			for(var i=0; i<sumRows.length; i++){
   				superSumColumnsArray[i] =0;
   			}
	   		for(var j=0; j<sumRows[0].length; j++){
	   			for(var i=0; i<sumRows.length; i++){
		   			//in the total sum we not consider calculated fields and partial sums
	   				if( !this.columnHeader[this.columnHeader.length-1][j].type!='CF' &&
	   					!this.columnHeader[this.columnHeader.length-1][j].type!='partialsum'){
	   						superSumColumnsArray[i] = (superSumColumnsArray[i]+parseFloat(sumRows[i][j]));
	   				}
					var a = new Array();
					a.push(sumRows[i][j]);
					a.push('[sumR'+i+','+j+']');
					a.push('float');
					a.push(null);
					a.push('totals');
					sumRowsStore.push(a);
	   			}
	   		}
	   		dataViewWidth=this.columnWidth*sumRows.length;
	   		this.buildSuperTotalPanel(superSumColumnsArray, tpl, this.rowHeight, dataViewWidth);
		}else{
	   		
	   		for(var j=0; j<sumRows.length; j++){
				var a = new Array();
				a.push(sumRows[j]);
				a.push('[sumR'+j+']');
				a.push('float');
				a.push(null);
				a.push('totals');
				sumRowsStore.push(a);
	   		}
	   		dataViewWidth=this.columnWidth;
		}	
		storeRows.loadData(sumRowsStore);	
		
		var cellCls = 'crosstab-row-sum-panel-container';
		if(withColumnsSum){
			cellCls = cellCls + ' crosstab-none-bottom-border-panel';
		}
		
    	var datapanelRowSum = new Ext.Panel({
    		cellCls: cellCls,
            width: dataViewWidth,
            height: (rowForView)*(this.rowHeight),
            border: false,
    	    layout:'fit',
    	    items: new Ext.DataView({
    	        store: storeRows,
    	        tpl: tpl,
    	        itemSelector: 'div.crosstab-table-cells'
    	    })
    	});
    	return datapanelRowSum;
    }
    
    /**
	* Build the pannel of total of totals
	*/
    , buildSuperTotalPanel: function(superSumColumnsArray, tpl, dataViewHeight, dataViewWidth){
    	
    	//with the if not commented the total sum is computed only at the biginning..
    	//in this way the partial sums and the calculated fields are not counted
    	//if(this.superSumArray == null){
    		this.superSumArray = new Array();
			if(this.withRowsSum && this.withColumnsSum){
	    	
		    	
		    	var storeSumColumns = new Ext.data.ArrayStore({
		    	    autoDestroy: true,
		    	    storeId: 'myStore3',
		    	    fields: [
		    	             {name: 'name'},
		    	             'divId',
		    	             {name: 'datatype'},
		    	             {name: 'format'},
		    	             {name: 'celltype'}
		    	    ]
		    	});
	
		    	var sumSuperColumnsStore = new Array();
		   		for(var j=0; j<superSumColumnsArray.length; j++){
		   			a = new Array();
		   			a.push(superSumColumnsArray[j]);
		   			a.push('[sumCT'+j+']');
					a.push('float');
					a.push(null);
					a.push('supertotals');
					sumSuperColumnsStore.push(a);
					this.superSumArray.push(""+superSumColumnsArray[j]);
		   		}
		
				var cellCls = 'crosstab-row-sum-panel-container  crosstab-none-top-border-panel';
		   		
		   		storeSumColumns.loadData(sumSuperColumnsStore);
		   		
		   		this.superSumPanel = new Ext.Panel({
		    		cellCls: cellCls,
		            width: dataViewWidth,
		            height: dataViewHeight,
		            border: false,
		    	    layout:'fit',
		    	    items: new Ext.DataView({
		    	        store: storeSumColumns,
		    	        tpl: tpl,
		    	        itemSelector: 'div.crosstab-table-cells'
		    	    })
		    	});
			}    	
       	//}
    }
    
    , showCFWizard: function(node, modality) {
   		this.crossTabCFWizard = new Sbi.crosstab.core.CrossTabCFWizard({
   			'baseNode' : node, 
   			'modality' : modality
   		}); 
   		this.crossTabCFWizard.show(this);  
   		this.crossTabCFWizard.on('applyCalculatedField', function(theNode, level, horizontal, op, CFName){
   			
    		this.removePartialSum(true);
    		Sbi.crosstab.core.CrossTabCalculatedFields.calculateCF(level, horizontal, op, CFName, this.percenton);	
    		this.addCalculatedField(level, horizontal, op, CFName);
     		this.calculatePartialSum();
    		
   		}, this); 
   		this.crossTabCFWizard.on('modifyCalculatedField', function(theNode, level, horizontal, op, CFName){
   			
   			//remove the old CF
   			var headers;
   	    	if(horizontal){
   	    		headers = this.columnHeader[level];
   	    	}else{
   	    		headers = this.rowHeader[level];
   	    	}  			
   			for(var i=0; i<headers.length; i++){
   				if(headers[i].name == theNode.name){
   					this.removeEntries(headers[i], true);
   				}
   			}
   			this.removePartialSum(true);
   			//add the new CF
   			Sbi.crosstab.core.CrossTabCalculatedFields.calculateCF(level, horizontal, op, CFName, this, this.percenton);
    		this.modifyCalculatedField(level, horizontal, op, CFName);
    		this.calculatePartialSum();
   		}, this); 
    }
    
    //===========================================
    //                    UTILITY
    //===========================================    
	, cloneNode: function(node,father, noHeaderMenu){
		
		if(node.horizontal){
			var clonedNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:node.name, thisDimension: node.thisDimension, horizontal: node.horizontal, level: node.level, width: null, height: node.height});
		}else{
			var clonedNode = new Sbi.crosstab.core.HeaderEntry({percenton: this.percenton, name:node.name, thisDimension: node.thisDimension, horizontal: node.horizontal, level: node.level, width: node.width, height: null});
		}
		clonedNode.type = node.type;
		clonedNode.father = father;
		for(var i=0; i<node.childs.length;i++){
			clonedNode.childs.push(this.cloneNode(node.childs[i],clonedNode,noHeaderMenu));
		}
		this.setHeaderListener(clonedNode, noHeaderMenu);
		return clonedNode;
	}
	
	,cloneHeader: function(header){
		var clonedHeader = new Array();
		for(var i=0; i<header.length; i++){
			var line = new Array();
			for(var j=0; j<header[i].length; j++){
				var node = header[i][j];
				var clonedFather = null;
				if(node.father!=null && i>0){
					for(var jj=0; jj<header[i-1].length; jj++){
						if(this.isTheSameHeader(clonedHeader[i-1][jj],node.father)){
							clonedFather = clonedHeader[i-1][jj];
							break;
						}
					}
				}
				line.push(this.cloneNode(node, clonedFather, true));
			}
			clonedHeader.push(line);
		}
		return clonedHeader;
	}
	
	
    
    ,printHeader: function(header){
    	var printed = new Array();
    	var length;
    	if(header.horizontal){
    		length = this.columnHeader.length;
    	}else{
    		length = this.rowHeader.length;
    	}
    	
    	for(var i= header.level; i<length; i++){
    		printed.push(this.findLevelHeaders(header, i));
    	}
    	return printed;
    }
    
    ,findLevelHeaders: function(header, level){
    	var a = new Array();
    	if (header.level == level){
    		a.push(header.name);
    		return a;
    	}else if (header.childs.length==0){
    		return a;
    	}else{
    		for(var i=0; i<header.childs.length; i++){
    			a = a.concat(this.findLevelHeaders(header.childs[i],level));
    		}
    		return a;
    	}
    }
    
});

/*
function cruxBackground(id,rowsNumber,columnsNumber){

   	var row = id[0];
   	var column = id[1];
   	
   	var cel;
   	for(var i=0; i<rowsNumber; i++){
   		cel = document.getElementById("["+i+","+column+"]");
   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
   	}
   	for(var i=0; i<columnsNumber; i++){
   		cel = document.getElementById("["+row+","+i+"]");
   		cel.className += ' crosstab-table-cells-highlight'; // adding class crosstab-table-cells-highlight
   	}
}

function clearBackground(id,rowsNumber,columnsNumber){

   	var row = id[0];
   	var column = id[1];

   	var cel;
   	for(var i=0; i<rowsNumber; i++){
   		cel = document.getElementById("["+i+","+column+"]");
   		cel.className = cel.className.replace(/\bcrosstab-table-cells-highlight\b/,''); // removing class crosstab-table-cells-highlight
   	}
   	for(var i=0; i<columnsNumber; i++){
   		cel = document.getElementById("["+row+","+i+"]");
   		cel.className = cel.className.replace(/\bcrosstab-table-cells-highlight\b/,''); // removing class crosstab-table-cells-highlight
   	}
}
*/