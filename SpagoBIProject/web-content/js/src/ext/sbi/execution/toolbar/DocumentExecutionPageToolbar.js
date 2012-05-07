/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
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
  * Chiara Chiarelli
  */


Ext.ns("Sbi.execution.toolbar");

Sbi.execution.toolbar.DocumentExecutionPageToolbar = function(config) {	
	
	this.toolbarConfig = config.TOOLBAR_CONFIG;
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	
	this.services = new Array();
	this.services['showSendToForm'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SHOW_SEND_TO_FORM'
		, baseParams: params
	});
	
	this.services['saveIntoPersonalFolder'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_PERSONAL_FOLDER'
		, baseParams: params
	});
	
	this.services['toPdf'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_PDF'
		, baseParams: params
	});

	this.services['toDCPdf'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_DOCUMENT_COMPOSITION_PDF'
		, baseParams: params
	});
	
	this.services['toChartPdf'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_CHART_PDF'
		, baseParams: params
	});
	
	this.services['toChartJpg'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_CHART_JPG'
		, baseParams: params
	});
	
	this.services['exportDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_RESULT_ACTION'
		, baseParams: params
	});
	
	this.services['getNotesService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_NOTES_ACTION'
		, baseParams: params
	});
	
	this.services['getMetadataService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName : 'GET_METADATA_ACTION',
		baseParams : params
	});
	
	var updateDocParams = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', MESSAGE_DET: 'DOC_UPDATE'};
	this.services['updateDocumentService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_DOCUMENT_ACTION'
		, baseParams: updateDocParams
	});
	
	Sbi.execution.toolbar.DocumentExecutionPageToolbar.superclass.constructor.call(this, config);
	
	this.addEvents('beforetoolbarinit', 'moveprevrequest', 'beforerefresh','collapse3', 'backToAdmin','refreshexecution','showmask');
};

Ext.extend(Sbi.execution.toolbar.DocumentExecutionPageToolbar, Ext.Toolbar, {
   
	toolbarConfig: null
	, services: null
	, executionInstance: null
	, miframe: null
	, parametersPanel: null
	, shortcutsPanel: null
	, documentMode: 'VIEW'
	
	, getNoteIcon: function () {
  		Ext.Ajax.request({
  	        url: this.services['getNotesService'],
  	        params: {SBI_EXECUTION_ID: this.executionInstance.SBI_EXECUTION_ID, MESSAGE: 'GET_LIST_NOTES'},
  	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );		 
	      			//checks if documents has some note for change icon     			
	      			if (content !== undefined && content.totalCount > 0) {		      		
	      				var el = Ext.getCmp('noteIcon');                
	      				el.setIconClass('icon-notes');
	      			}
	      		} else {
	      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
  	        },
  	        scope: this,
  			failure: Sbi.exception.ExceptionHandler.handleFailure      
  		});
	}
	
	, rateExecution: function() {
		this.win_rating = new Sbi.execution.toolbar.RatingWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_rating.show();
	}
	
	, printExecution: function() {
		this.miframe.getFrame().print();
	}
	
	, sendExecution: function () {
		var sendToIframeUrl = this.services['showSendToForm'] 
		        + '&objlabel=' + this.executionInstance.OBJECT_LABEL
		        + '&objid=' + this.executionInstance.OBJECT_ID
				+ '&' + Sbi.commons.Format.toStringOldSyntax(this.parametersPanel.getFormState());
		this.win_sendTo = new Sbi.execution.toolbar.SendToWindow({'url': sendToIframeUrl});
		this.win_sendTo.show();
	}
	
	, saveExecution: function () {
		Ext.Ajax.request({
	          url: this.services['saveIntoPersonalFolder'],
	          params: {documentId: this.executionInstance.OBJECT_ID},
	          success: function(response, options) {
		      		if (response.responseText !== undefined) {
		      			var responseText = response.responseText;
		      			var iconSaveToPF;
		      			var message;
		      			if (responseText=="sbi.execution.stpf.ok") {
		      				message = LN('sbi.execution.stpf.ok');
		      				iconSaveToPF = Ext.MessageBox.INFO;
		      			}
		      			if (responseText=="sbi.execution.stpf.alreadyPresent") {
		      				message = LN('sbi.execution.stpf.alreadyPresent');
		      				iconSaveToPF = Ext.MessageBox.WARNING;
		      			}
		      			if (responseText=="sbi.execution.stpf.error") {
		      				message = LN('sbi.execution.stpf.error');
		      				iconSaveToPF = Ext.MessageBox.ERROR;
		      			}
	
		      			var messageBox = Ext.MessageBox.show({
		      				title: 'Status',
		      				msg: message,
		      				modal: false,
		      				buttons: Ext.MessageBox.OK,
		      				width:300,
		      				icon: iconSaveToPF,
		      				animEl: 'root-menu'        			
		      			});
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}
	
	, bookmarkExecution: function () {
		this.win_saveRememberMe = new Sbi.execution.toolbar.SaveRememberMeWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_saveRememberMe.show();
	}
	, annotateExecution: function () {
		this.win_notes = new Sbi.execution.toolbar.ListNotesWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_notes.show();
	}
	
	, metaExecution: function () {
		var subObjectId = this.executionInstance.SBI_SUBOBJECT_ID;
		if(subObjectId !== undefined){
			this.win_metadata = new Sbi.execution.toolbar.MetadataWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID, 'SUBOBJECT_ID': subObjectId});
		}else{
			this.win_metadata = new Sbi.execution.toolbar.MetadataWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		}	
		this.win_metadata.show();
	}
	
	, pdfExecution: function () {
		var urlExporter = this.services['toPdf'] + '&OBJECT_ID=' + this.executionInstance.OBJECT_ID;
		window.open(urlExporter,'name','resizable=1,height=750,width=1000');
	}		
	
	, pdfDCExecution: function () {
		// here I have to recover all iframe urls!
		mainFrame=this.miframe.getFrame();
		windowO=mainFrame.getWindow();
		newPars='';
		var isHighchart = false;
		var isExtChart = false;
		var randUUID = Math.random();
		var idxElements = 0;
		for (var i=0; i<windowO.frames.length; i++)
		{
			childFrame=windowO.frames[i];
			//if the iframe contains a console document, it's not exported!
			if (childFrame.Sbi !== undefined && childFrame.Sbi.console !== undefined  ){
				continue;
			}
			fullName=childFrame.name;
			cutName=fullName.substring(7);
			urlNotEncoded=childFrame.location.href;
			// I have to substitute %25 in %
			urlNotEncoded = urlNotEncoded.replace(/%25/g,'%');
			urlNotEncoded = urlNotEncoded.replace(/%20/g,' ');
			urlEncoded=encodeURIComponent(urlNotEncoded);
			newPars+='&TRACE_PAR_'+cutName+'='+urlEncoded;
			//for highcharts and ext charts documents gets the SVG and send it as a hidden form
			if (childFrame.chartPanel !== undefined && childFrame.chartPanel.chart !== undefined){
				var svg = '';
				if (childFrame.chartPanel.chartsArr !== undefined){
					isHighchart = true;
					svg = this.getHighchartSvg(childFrame);
				}else{
					isExtChart = true;
					svg = this.getExtchartSvg(childFrame);
				}
				Ext.DomHelper.useDom = true; // need to use dom because otherwise an html string is composed as a string concatenation,
				 // but, if a value contains a " character, then the html produced is not correct!!!
				 // See source of DomHelper.append and DomHelper.overwrite methods
				 // Must use DomHelper.append method, since DomHelper.overwrite use HTML fragments in any case.
				 var dh = Ext.DomHelper;
				 var form = document.getElementById('export-chart-form__'+ randUUID);
				 if (form === undefined || form === null) {
				     var form = dh.append(Ext.getBody(), { // creating the hidden form
								  id: 'export-chart-form__' + randUUID
								  , tag: 'form'
								  , method: 'post'
							  });
				 }	  
				 
				 dh.append(form, {		// creating the hidden input in form
						tag: 'input'
						, type: 'hidden'
						, name: 'SVG_' + cutName
						, value: ''  // do not put value now since DomHelper.overwrite does not work properly!!
						});
				 
				// putting the chart data into hidden input
				//form.elements[i].value =  Ext.encode(svg);     
				form.elements[idxElements].value = svg;  
				idxElements ++;
				
			}
				
		}//for 
		var urlExporter = this.services['toDCPdf'] + '&OBJECT_ID=' + this.executionInstance.OBJECT_ID;
		urlExporter += newPars;
		window.open(urlExporter,'exportWindow','resizable=1,height=750,width=1000');
		if (isHighchart || isExtChart){
		    form.action = urlExporter;
			//form.target = '_blank'; // result into a new browser tab
			form.target = 'exportWindow'; // result into a popup
			form.submit();
		}
		Ext.DomHelper.useDom = false; //reset configuration for dom management
	}		
	
	, getHighchartSvg: function (childFrame) {
		var svgArr = [],
   	    top = 0,
  	    width = 0,
  	    svg = '';
		//in case of multiple charts redefines the svg object as a global (transforms each single svg in a group tag <g>)
		 for (var c=0; c < childFrame.chartPanel.chartsArr.length; c++){
			var singleChart = childFrame.chartPanel.chartsArr[c];
		    if (singleChart !== undefined && singleChart !== null){
	          	var singleSvg = singleChart.getSVG();
	          	singleSvg = singleSvg.replace('<svg', '<g transform="translate(0,' + top + ')" ');
	          	singleSvg = singleSvg.replace('</svg>', '</g>');
	
	            top += singleChart.chartHeight;
	            width = Math.max(width, singleChart.chartWidth);
	
	            svgArr.push(singleSvg);
	         }
		}
		//defines the global svg (for master/detail chart)
       svg = '<svg height="'+ top +'" width="' + width + '" version="1.1" xmlns="http://www.w3.org/2000/svg">';
       for (var s=0; s < svgArr.length; s++){
       	svg += svgArr[s];
       }
       svg += '</svg>';	   
		
	   return svg;
	}
	
	, getExtchartSvg: function (childFrame) {
		var chartPanel = childFrame.chartPanel;
		var svg = chartPanel.chart.save({type:'image/svg'});	          	
		svg = svg.substring(svg.indexOf("<svg"));

      	var tmpSvg = svg.replace("<svg","<g transform='translate(10,50)'");
		tmpSvg = tmpSvg.replace("</svg>", "</g>");
		
		svg = "<svg height='100%' width='100%' version='1.1' xmlns='http://www.w3.org/2000/svg'>";
		svg += tmpSvg;
      	
      	//adds title and subtitle
      	if (chartPanel.title){
      		var titleStyle = chartPanel.title.style;
      		titleStyle = titleStyle.replace("color","fill");
      		svg += "<text x='10'  y='25' style='" + titleStyle +"'>"+chartPanel.title.text+"</text>";
      	}
      	if (chartPanel.subtitle){
      		var subtitleStyle = chartPanel.subtitle.style;
      		subtitleStyle = subtitleStyle.replace("color","fill");
      		svg += "<text x='10' y='45' style='" + subtitleStyle +"'>"+chartPanel.subtitle.text+"</text>";	          		
      	}
      				
		svg += "</svg>";
		
	    return svg;
	}
	, exportGeoExecution: function (exportType) {	
	    var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	 
	    //var docurlPar = "ACTION_NAME=DRAW_MAP_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&user_id=-1&outputFormat=jpeg&inline=false";
	    var docurlPar = "ACTION_NAME=DRAW_MAP_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&outputFormat="+exportType+"&inline=false";
	    var endUrl = baseUrl + docurlPar;
	   // alert ("endUrl: " + endUrl);
	    
		window.open(endUrl,'name','resizable=1,height=750,width=1000');
	}
	, exportReportExecution: function (exportType) {
	    var endUrl = this.changeDocumentExecutionUrlParameter('outputType', exportType);
		window.open(endUrl, 'name', 'resizable=1,height=750,width=1000');
	}
	
	,exportOlapExecution: function (exportType) {
		var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	    baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf('/')+1) + "Print?";
	 
	    var docurlPar = "cube=01&type=";
	    if (exportType == "PDF") {docurlPar += "1";}
	    else if (exportType == "XLS"){ docurlPar += "0"};
	   
	    var endUrl = baseUrl + docurlPar;
	    
		window.open(endUrl,'name','resizable=1,height=750,width=1000');
	}
	
	, exportChartExecution: function (exportType) {
		this.miframe.getFrame().getWindow().exportChart(exportType);
		/*
		var urlExporter = "";
	    
		if (exportType == "PDF")  {
			urlExporter = this.services['toChartPdf'] + '&OBJECT_ID=' + this.executionInstance.OBJECT_ID ;
			urlExporter+= '&SBI_EXECUTION_ID=' + this.executionInstance.SBI_EXECUTION_ID + "&outputType=PDF";
		}
		window.open(urlExporter,'name','resizable=1,height=750,width=1000');
		*/
	}
	
	, exportQbEExecution: function (exportType) {	
	    var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	 
	    var docurlPar = "ACTION_NAME=EXPORT_RESULT_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&MIME_TYPE="+exportType+"&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT";
	   
	    var endUrl = baseUrl + docurlPar;
	   
	    if(Ext.isIE6) {
		    var form = document.getElementById('export-form');
			if(!form) {
				var dh = Ext.DomHelper;
				form = dh.append(Ext.getBody(), {
				    id: 'export-form'
				    , tag: 'form'
				    , method: 'post'
				    , cls: 'export-form'
				});
			}
			
			form.action = endUrl;
			form.submit();
	    } else {
	    	window.open(endUrl,'name','resizable=1,height=750,width=1000');
	    }
	}
	
	
	
	, exportWorksheetsExecution: function (mimeType, records) {
		try {
			
			this.fireEvent('showmask','Exporting..');
			
			if(!records) {
				
				var urlForMetadata = this.services['getMetadataService'];
				urlForMetadata += "&OBJECT_ID=" + this.executionInstance.OBJECT_ID;
				if (this.executionInstance.SBI_SUBOBJECT_ID) {
					urlForMetadata += "&SUBOBJECT_ID=" + this.executionInstance.SBI_SUBOBJECT_ID;
				}
			
				var metadataStore = new Ext.data.JsonStore({
			        autoLoad: false,
			        fields: [
			           'meta_id'
			           , 'biobject_id'
			           , 'subobject_id'
			           , 'meta_name'
			           , 'meta_type'
			           , 'meta_content'
			           , 'meta_creation_date'
			           , 'meta_change_date'
			        ]
			        , url: urlForMetadata
			    });
			    metadataStore.on('load', function(store, records, options ) {
			    	this.exportWorksheetsExecution(mimeType, records);
		    	}, this);
			    
			    metadataStore.load();
			} else {
				
		
				var metadata = [];
				for(var i = 0; i < records.length; i++) {
					var record = records[i];
					metadata.push(record.data);
				}

				 
				var thePanel = this.miframe.getFrame().getWindow().qbe;
				if(thePanel==null){
					//the worksheet has been constructed starting from a smart filter document
					thePanel = this.miframe.getFrame().getWindow().Sbi.formviewer.formEnginePanel;
				}
				if(thePanel==null){
					//the worksheet is alone with out the qbe
					thePanel = this.miframe.getFrame().getWindow().workSheetPanel;
				}
				
				var parameters = [];
				var formState = this.parametersPanel.getFormState();
				for(f in formState) {
					if(f.indexOf('_field_visible_description') == -1) {
						var p = {name: f, value: formState[f]};
						var description = formState[f + '_field_visible_description'];
						if(description) p.description = description;
						parameters.push(p);
					}
				}
				
				thePanel.exportContent(mimeType, false, metadata, parameters);
			}
		} catch (err) {
			alert('Sorry, cannot perform operation');
			throw err;
		}
	}

	, updateFrame: function(miframe){
		this.miframe = miframe;
	}   
	
   , synchronizeToolbar: function( executionInstance,miframe,southPanel,northPanel,parametersPanel,shortcutsPanel) {
	    this.executionInstance = executionInstance;
	    this.miframe = miframe;
		this.southPanel = southPanel;
		this.northPanel = northPanel;
		this.parametersPanel = parametersPanel;
		this.shortcutsPanel = shortcutsPanel;
		
		// if toolbar is hidden, do nothing
		if (this.toolbarHiddenPreference) 
			return;
		
		this.items.each( function(item) {
			this.items.remove(item);
            item.destroy();           
        }, this); 
		
		this.fireEvent('beforetoolbarinit', this, this);
		
		this.addFill();
		
		if (this.documentMode === 'VIEW') {
			this.addButtonsForViewMode();
		} else {
			this.addButtonsForEditMode();
		}
		
   }
   
   // edit mode buttons (at the moment used by Worksheet documents only)
   , addButtonsForEditMode: function () {

	   this.toolbarConfig.expandBtnVisible = (this.toolbarConfig.expandBtnVisible === undefined)? true: this.toolbarConfig.expandBtnVisible ; 
	   if (Sbi.user.ismodeweb && this.toolbarConfig.expandBtnVisible  === true) {
		   this.addButton(new Ext.Toolbar.Button({
			   iconCls: 'icon-expand' 
				   , tooltip: LN('sbi.execution.executionpage.toolbar.expand')
				   , scope: this
				   , handler : function() {
					   this.fireEvent('collapse3');
				   }			
		   }));
	   }
	   
	   if (this.executionInstance.document && this.executionInstance.document.decorators &&  this.executionInstance.document.decorators.isSavable) {
		   this.addButton(new Ext.Toolbar.Button({
			   iconCls: 'icon-save' 
				   , tooltip: LN('sbi.execution.executionpage.toolbar.save')
				   , scope: this
				   , handler : this.saveWorksheet
		   }));
	   }

	   this.addButton(new Ext.Toolbar.Button({
		   iconCls: 'icon-saveas' 
			   , tooltip: LN('sbi.execution.executionpage.toolbar.saveas')
			   , scope: this
			   , handler : this.saveWorksheetAs	
	   }));

	   if(this.executionInstance.document.exporters.length > 0){
			
		   this.exportMenu = new Ext.menu.Menu({
			   id: 'basicExportMenu_0',
			   items: this.getWorksheetExportMenuItems(),
			   listeners: {	'mouseexit': {fn: function(item) {item.hide();}}}
		   });
	       this.addButton(new Ext.Toolbar.MenuButton({
			   id: Ext.id()
			   , tooltip: 'Exporters'
			   , path: 'Exporters'	
			   , iconCls: 'icon-export' 	
			   , menu: this.exportMenu
			   , width: 15
			   , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
		    }));	
	   }
	   
	   this.addSeparator();
	   this.addButton(new Ext.Toolbar.Button({
		   iconCls: 'icon-view' 
			   , tooltip: LN('sbi.execution.executionpage.toolbar.view')
			   , scope: this
			   , handler : this.stopWorksheetEditing	
	   }));

   }
   
   
   , addButtonsForViewMode: function () {
	   
	   var drawParBack = false;
	   
		if (this.executionInstance.isPossibleToComeBackToParametersPage == undefined || 
				this.executionInstance.isPossibleToComeBackToParametersPage === true)
		 {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.back')
				, scope: this
				, handler : function() {this.fireEvent('moveprevrequest');}
			}));
			drawParBack = true;
		}
		
		// 20100505
		if (this.callFromTreeListDoc == true && drawParBack == false) {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.documentView')
				, scope: this
				, handler : function() {
					this.fireEvent('backToAdmin');
				}
			}));
		}
		
    	this.toolbarConfig.expandBtnVisible = (this.toolbarConfig.expandBtnVisible === undefined)? true: this.toolbarConfig.expandBtnVisible ; 
		if(Sbi.user.ismodeweb && this.toolbarConfig.expandBtnVisible  === true){
				this.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-expand' 
					, tooltip: LN('sbi.execution.executionpage.toolbar.expand')
				    , scope: this
				    , handler : function() {
							this.fireEvent('collapse3');
					}			
				}));
		}
		
		if (Sbi.user.functionalities.contains('EditWorksheetFunctionality') && this.executionInstance.document.typeCode === 'WORKSHEET') {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-edit' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.edit')
			    , scope: this
			    , handler : this.startWorksheetEditing	
			}));
		}
		
		if (this.executionInstance.document.typeCode === 'DATAMART') {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-save' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.save')
			    , scope: this
			    , handler : this.saveQbe	
			}));
		}
		
		if (this.executionInstance.document.typeCode === 'SMART_FILTER') {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-save' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.save')
			    , scope: this
			    , handler : this.saveWorksheetAs	
			}));
		}
		
		this.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-rating' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.rating')
		    , scope: this
		    , handler : this.rateExecution	
		}));
		
		this.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-print' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.print')
		    , scope: this
		    , handler : this.printExecution
		}));
		
		if (Sbi.user.functionalities.contains('SendMailFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID
				&& this.executionInstance.document.typeCode == 'REPORT') {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-sendMail' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.send')
		     	, scope: this
		    	, handler : this.sendExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SaveIntoFolderFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveIntoPersonalFolder' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.saveintopersonalfolder')
		     	, scope: this
		    	, handler : this.saveExecution
			}));
		}

		if (Sbi.user.functionalities.contains('SaveRememberMeFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveRememberMe'
				, tooltip: LN('sbi.execution.executionpage.toolbar.bookmark')
		     	, scope: this
		    	, handler :this.bookmarkExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SeeNotesFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.getNoteIcon();
	    	this.addButton(new Ext.Toolbar.Button({
	  			   id: 'noteIcon'
	  				, tooltip: LN('sbi.execution.executionpage.toolbar.annotate')
	  				, iconCls: 'icon-no-notes'
	  		     	, scope: this
	  		    	, handler : this.annotateExecution
	  			}));    
		}
		
		if (Sbi.user.functionalities.contains('SeeMetadataFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-metadata' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.metadata')
		     	, scope: this
		    	, handler : this.metaExecution
			}));
		}
		
		if(this.executionInstance.document.exporters){
			if ( this.executionInstance.document.typeCode == 'KPI' && this.executionInstance.document.exporters.contains('PDF')) {
				this.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-pdf' 
					, tooltip: LN('sbi.execution.PdfExport')
			     	, scope: this
			    	, handler : this.pdfExecution
				}));
			}
			else if ( this.executionInstance.document.typeCode == 'DOCUMENT_COMPOSITE' ) {
				this.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-pdf' 
					, tooltip: LN('sbi.execution.PdfExport')
			     	, scope: this
			    	, handler : this.pdfDCExecution
				}));
			}
			else if( this.executionInstance.document.typeCode == 'REPORT') {
					var menuItems = new Array();
					
					for(i=0;i<this.executionInstance.document.exporters.length ;i++){
						
						if (this.executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() {this.exportReportExecution('PDF'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(this.executionInstance.document.exporters[i]=='XLS'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XlsExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xls' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportReportExecution('XLS'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(this.executionInstance.document.exporters[i]=='RTF'){
							menuItems.push(   new Ext.menu.Item({
	                            id:  Ext.id()
	                            , text: LN('sbi.execution.rtfExport')
	                            , group: 'group_2'
	                            , iconCls: 'icon-rtf' 
						     	, scope: this
								 , width: 15
						    	, handler : function() { this.exportReportExecution('RTF'); }
								, href: ''   
	                        })	
	                        );
						}else if(this.executionInstance.document.exporters[i]=='DOC'){
							menuItems.push(   new Ext.menu.Item({
	                            id:  Ext.id()
	                            , text: LN('sbi.execution.docExport')
	                            , group: 'group_2'
	                            , iconCls: 'icon-rtf' 
						     	, scope: this
								 , width: 15
						    	, handler : function() { this.exportReportExecution('DOC'); }
								, href: ''   
	                        })	
	                        );
						}else if(this.executionInstance.document.exporters[i]=='CSV'){
							menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.CsvExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-csv' 
									     	, scope: this
									   , width: 15
									    	, handler : function() { this.exportReportExecution('CSV'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(this.executionInstance.document.exporters[i]=='XML'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XmlExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xml' 
									     	, scope: this
									      , width: 15
									    	, handler : function() { this.exportReportExecution('XML'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(this.executionInstance.document.exporters[i]=='JPG'){
						menuItems.push(   new Ext.menu.Item({
				                            id: Ext.id()
				                            , text: LN('sbi.execution.JpgExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-jpg' 
									     	, scope: this
									     , width: 15
									    	, handler : function() { this.exportReportExecution('JPG'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(this.executionInstance.document.exporters[i]=='TXT'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.txtExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-txt' 
									     	, scope: this
									     	 , width: 15
									    	, handler : function() { this.exportReportExecution('TXT'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(this.executionInstance.document.exporters[i]=='PPT'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.pptExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-ppt' 
									     	, scope: this
									      , width: 15
									    	, handler : function() { this.exportReportExecution('PPT'); }
											, href: ''   
				                        })	
				                        ); 
						}
				    }   
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}else if( this.executionInstance.document.typeCode == 'OLAP') {
					var menuItems = new Array();
					
					for(i=0;i<this.executionInstance.document.exporters.length ;i++){
						
						if (this.executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportOlapExecution('PDF'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(this.executionInstance.document.exporters[i]=='XLS'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XlsExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xls' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportOlapExecution('XLS'); }
											, href: ''   
				                        })	
				                        ); 
						}
				    }   
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}
			else if ( this.executionInstance.document.typeCode == 'DASH') {
				this.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-pdf' 
					, tooltip: LN('sbi.execution.PdfExport')
			     	, scope: this
			    	, handler :  function() { this.exportChartExecution('PDF'); }
					, href: ''  
				}));
			}else if ( this.executionInstance.document.typeCode == 'CHART') {
				var menuItems = new Array();
				
				for(i=0;i<this.executionInstance.document.exporters.length ;i++){
					if (this.executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
											  id:  Ext.id()
											, iconCls: 'icon-pdf' 
											, text: LN('sbi.execution.PdfExport')
									     	, scope: this
									    	, handler :  function() { this.exportChartExecution('PDF'); }
											, href: ''  
										}));
					}if (this.executionInstance.document.exporters[i]=='JPG'){
						menuItems.push(	new Ext.menu.Item({
								  id:  Ext.id()
								, iconCls: 'icon-jpg' 
								, text: LN('sbi.execution.JpgExport')
						     	, scope: this
						    	, handler :  function() { this.exportChartExecution('JPG'); }
								, href: ''  
							}));
						}
					}
				
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}				
			}else if ( this.executionInstance.document.typeCode == 'WORKSHEET') {

					var menu0 = new Ext.menu.Menu({
						id: 'basicMenu_0',
						items: this.getWorksheetExportMenuItems()  
					});	

					
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}else if ( this.executionInstance.document.typeCode == 'DATAMART' || 
						this.executionInstance.document.typeCode == 'SMART_FILTER' ) {
			
					
					var menu0 = new Ext.menu.Menu({
						id: 'basicMenu_0',
						listeners: {
							'mouseexit': {fn: function(item) {item.hide();}},
							'beforeshow': {
								fn: function(thisMenu){
									var theWindow = this.miframe.getFrame().getWindow();
									var thePanel = theWindow.qbe;
									var isBuildingWorksheet;
									if(thePanel==null){//smart filter
										thePanel = theWindow.Sbi.formviewer.formEnginePanel;
									}
									isBuildingWorksheet =  thePanel.isWorksheetPageActive();
									var newItems; 
									thisMenu.removeAll(false);
									if (isBuildingWorksheet) {
										newItems = (this.getWorksheetExportMenuItems());
									} else {
										newItems = (this.getQbeExportMenuItems());
									}
									for(var i =0; i<newItems.length; i++){
										thisMenu.add(newItems[i]);
									}

								},
								scope: this
							}
						},
						items: this.getQbeExportMenuItems()     
					});
					

					/*
					if (this.executionInstance.document.typeCode == 'DATAMART') {
						var resultItem = new Ext.menu.Item({
							id:  Ext.id()
							, text: LN('sbi.execution.export.qbe.results')
							, menu: {
								listeners: {'mouseexit': function(item) {item.hide();}},
			        			items: menuItems
			        		}
						});
						
						var crosstabExportItem = new Ext.menu.Item({
                            id:  Ext.id()
                            , text: LN('sbi.execution.XlsExport')
                            , iconCls: 'icon-xls' 
					     	, scope: this
							, width: 15
					    	, handler : function() { this.exportQbECrosstab('application/vnd.ms-excel'); }
							, href: ''   
                        });
						
						var crosstabMenuItem = new Ext.menu.Item({
							id:  Ext.id()
							, text: LN('sbi.execution.export.qbe.crosstab')
							, menu: {
								listeners: {'mouseexit': function(item) {item.hide();}},
				        		items: [crosstabExportItem]
				        	}
						});
						
						menu0 = new Ext.menu.Menu({
							id: 'basicMenu_0',
							listeners: {'mouseexit': function(item) {item.hide();}},
							items: [resultItem, crosstabMenuItem]    
						});
						
					} else {
						menu0 = new Ext.menu.Menu({
							id: 'basicMenu_0',
							items: menuItems    
						});	
					}
					*/
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}	else if ( this.executionInstance.document.typeCode == 'MAP') {
			
					var menuItems = new Array();
					
					for(i=0;i<this.executionInstance.document.exporters.length ;i++){
						
						if (this.executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportGeoExecution('pdf'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(this.executionInstance.document.exporters[i]=='JPG'){
						menuItems.push(   new Ext.menu.Item({
				                            id: Ext.id()
				                            , text: LN('sbi.execution.JpgExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-jpg' 
									     	, scope: this
									     , width: 15
									    	, handler : function() { this.exportGeoExecution('jpeg'); }
											, href: ''   
				                        })	
				                        ); 
						}
			
			  		}   
					var menu0 = new Ext.menu.Menu({
						id: 'basicMenu_0',
						items: menuItems    
					});	
					
					if(this.executionInstance.document.exporters.length > 0){
						this.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}	
		}
		
		this.addSeparator();
		
		this.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-execute' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.refresh')
		    , scope: this
		    , handler : function() {
					// save parameters into session
					// if type is QBE inform user that will lose configurations
					if(this.executionInstance.document.typeCode == 'DATAMART'){
						if(Sbi.user.functionalities.contains('BuildQbeQueriesFunctionality') && Sbi.user.functionalities.contains('SaveSubobjectFunctionality')){
							
							
							Ext.MessageBox.confirm(
    						    LN('sbi.generic.warning'),
            					LN('sbi.execution.executionpage.toolbar.qberefresh'),            
            					function(btn, text) {
                					if (btn=='yes') {
										Sbi.execution.SessionParametersManager.saveStateObject(this.parametersPanel);
										Sbi.execution.SessionParametersManager.updateMementoObject(this.parametersPanel);
										this.southPanel.collapse();
										this.northPanel.collapse();
										this.fireEvent('refreshexecution');
                					}
            					},
            					this
								);
							}else{
								//user who cannot build qbe queries
								Sbi.execution.SessionParametersManager.saveStateObject(this.parametersPanel);
								Sbi.execution.SessionParametersManager.updateMementoObject(this.parametersPanel);
								this.southPanel.collapse();
								this.northPanel.collapse();
								this.fireEvent('refreshexecution');
							}
					} // it 's not a qbe
					else {
						Sbi.execution.SessionParametersManager.saveStateObject(this.parametersPanel);
						Sbi.execution.SessionParametersManager.updateMementoObject(this.parametersPanel);
						this.southPanel.collapse();
						this.northPanel.collapse();
						this.fireEvent('refreshexecution');
				}
			}			
		}));
	   
   }

   , getQbeExportMenuItems: function() {

	   //if (this.qbeExportMenuItems ==undefined || this.qbeExportMenuItems ==null){
		   var menuItems = new Array();

		   for(i=0;i<this.executionInstance.document.exporters.length ;i++){

			   if (this.executionInstance.document.exporters[i]=='PDF'){
				   menuItems.push(	new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.PdfExport')
					   , group: 'group_2'
						   , iconCls: 'icon-pdf' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('application/pdf'); }
				   , href: ''   
				   })	 
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='XLS'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.XlsExport')
					   , group: 'group_2'
						   , iconCls: 'icon-xls' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('application/vnd.ms-excel'); }
				   , href: ''   
				   })	
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='XLSX'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.XlsxExport')
					   , group: 'group_2'
						   , iconCls: 'icon-xlsx' 
							   , scope: this
							   , width: 15
							   , handler : function() { 
								   this.exportQbEExecution('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
							   }
				   , href: ''   
				   })	
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='RTF'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.rtfExport')
					   , group: 'group_2'
						   , iconCls: 'icon-rtf' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('application/rtf'); }
				   , href: ''   
				   })	
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='CSV'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.CsvExport')
					   , group: 'group_2'
						   , iconCls: 'icon-csv' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('text/csv'); }
				   , href: ''   
				   })	
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='JRXML'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.jrxmlExport')
					   , group: 'group_2'
						   , iconCls: 'icon-jrxml' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('text/jrxml'); }
				   , href: ''   
				   })	
				   ); 
			   } else if(this.executionInstance.document.exporters[i]=='JSON'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.jsonExport')
					   , group: 'group_2'
						   , iconCls: 'icon-json' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportQbEExecution('application/json'); }
				   , href: ''   
				   })	
				   ); 
			   }
		   }
	   //}

	   return menuItems;
   }

   , getWorksheetExportMenuItems: function() {
	  // if (this.worksheetExportMenuItems ==undefined || this.worksheetExportMenuItems ==null){
		   var menuItems = new Array();
	   
		   for(i=0;i<this.executionInstance.document.exporters.length ;i++){

			   if (this.executionInstance.document.exporters[i]=='PDF'){
				   menuItems.push(	new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.PdfExport')
					   , group: 'group_2'
						   , iconCls: 'icon-pdf' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportWorksheetsExecution('application/pdf'); }
				   , href: ''   
				   })	 
				   ); 
			   }else if(this.executionInstance.document.exporters[i]=='XLS'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.XlsExport')
					   , group: 'group_2'
						   , iconCls: 'icon-xls' 
							   , scope: this
							   , width: 15
							   , handler : function() { this.exportWorksheetsExecution('application/vnd.ms-excel'); }
				   , href: ''   
				   })	
				   ); 
			   } else if(this.executionInstance.document.exporters[i]=='XLSX'){
				   menuItems.push(   new Ext.menu.Item({
					   id:  Ext.id()
					   , text: LN('sbi.execution.XlsxExport')
					   , group: 'group_2'
						   , iconCls: 'icon-xlsx' 
							   , scope: this
							   , width: 15
							   , handler : function() { 
								   this.exportWorksheetsExecution('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'); 
							   }
				   , href: ''   
				   })	
				   ); 
			   }
		   }
		   //this.worksheetExportMenuItems = menuItems;
	   //}
	   return menuItems;
   }
   
   , startWorksheetEditing: function() {
	   this.documentMode = 'EDIT';
	   this.synchronizeToolbar(this.executionInstance, this.miframe, this.southPanel, this.northPanel, this.parametersPanel, this.shortcutsPanel);
	   var newUrl = this.changeDocumentExecutionUrlParameter('ACTION_NAME', 'WORKSHEET_START_EDIT_ACTION');
	   this.miframe.getFrame().setSrc(newUrl);
   }
   
   , saveWorksheet: function () {
		var templateJSON = this.getWorksheetTemplateAsJSONObject();
		var wkDefinition = templateJSON.OBJECT_WK_DEFINITION;
		var query = templateJSON.OBJECT_QUERY;
		var formValues = templateJSON.OBJECT_FORM_VALUES;
		var params = this.executionInstance;
				
		if(wkDefinition!=null){
			params = Ext.apply(params, {'wk_definition': Ext.util.JSON.encode(wkDefinition)});
		}
		if(query!=null){
			params = Ext.apply(params, {'query': Ext.util.JSON.encode(query)});
		}
		if(formValues!=null){//the values of the smart filter
			params = Ext.apply(params, {'formValues': Ext.util.JSON.encode(formValues)});
		}

		Ext.Ajax.request({
	        url: this.services['updateDocumentService'],
	        params: params,
	        success : function(response, options) {
	      		if(response !== undefined && response.responseText !== undefined) {
	      			var content = Ext.util.JSON.decode( response.responseText );
	      			if (content.text !== 'Operation succeded' && content.responseText !== 'Operation succeded') {
	                    Ext.MessageBox.show({
	                        title: LN('sbi.generic.error'),
	                        msg: content,
	                        width: 150,
	                        buttons: Ext.MessageBox.OK
	                   });              
		      		} else {			      			
		      			Ext.MessageBox.show({
	                        title: LN('sbi.generic.result'),
	                        msg: LN('sbi.generic.resultMsg'),
	                        width: 200,
	                        buttons: Ext.MessageBox.OK
		                });
		      		}  
	      		} else {
	      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
	      		}
	        },
	        scope: this,
			failure: Sbi.exception.ExceptionHandler.handleFailure      
		});
   }
   
   , getWorksheetTemplateAsString: function() {
		try {
			var thePanel = null;
			if(this.executionInstance.document.typeCode == 'WORKSHEET'){
				//the worksheet has been constructed starting from a qbe document
				thePanel = this.miframe.getFrame().getWindow().qbe;
				if(thePanel==null){
					//the worksheet has been constructed starting from a smart filter document
					thePanel = this.miframe.getFrame().getWindow().Sbi.formviewer.formEnginePanel;
				}
				if(thePanel==null){
					//the worksheet is alone with out the qbe
					thePanel = this.miframe.getFrame().getWindow().workSheetPanel;
				}
			}else if(this.executionInstance.document.typeCode == 'DATAMART'){
				thePanel = this.miframe.getFrame().getWindow().qbe;
			}else if(this.executionInstance.document.typeCode == 'SMART_FILTER'){
				thePanel = this.miframe.getFrame().getWindow().Sbi.formviewer.formEnginePanel;
			}else{
				alert('Sorry, cannot perform operation. Invalid engine..');
				return null;
			}
			//var template = thePanel.getWorksheetTemplateAsString();
			var template = thePanel.validate();	
			return template;
		} catch (err) {
			throw err;
		}
   }
   
   , getWorksheetTemplateAsJSONObject: function() {
		var template = this.getWorksheetTemplateAsString();
		if(template==null){
			return null;
		}
		var templateJSON = Ext.util.JSON.decode(template);
		return templateJSON;
  }
   
   , saveWorksheetAs: function () {
		var templateJSON = this.getWorksheetTemplateAsJSONObject();
		if(templateJSON==null){
			// if it is null validation error has been already showed in QbePanel
			//Sbi.exception.ExceptionHandler.showWarningMessage(LN('sbi.worksheet.validation.error.text'),LN('sbi.worksheet.validation.error.title'));
		}else{

			var documentWindowsParams = this.getSaveDocumentWindowsParams(templateJSON);
			this.win_saveDoc = new Sbi.execution.SaveDocumentWindow(documentWindowsParams);
			this.win_saveDoc.show();
		}
   }
   
   , getSaveDocumentWindowsParams: function(templateJSON){
		var wkDefinition = templateJSON.OBJECT_WK_DEFINITION;
		var params = {
				'OBJECT_ID': this.executionInstance.OBJECT_ID,
				'OBJECT_TYPE': 'WORKSHEET',
				'OBJECT_WK_DEFINITION': wkDefinition,
				'OBJECT_DATA_SOURCE': this.executionInstance.document.datasource
			};
		if(this.executionInstance.document.typeCode == 'DATAMART'){
			params.OBJECT_QUERY = templateJSON.OBJECT_QUERY;
		}else if(this.executionInstance.document.typeCode == 'SMART_FILTER'){
			params.OBJECT_FORM_VALUES=templateJSON.OBJECT_FORM_VALUES;
			params = Ext.apply(this.executionInstance, params);
		}
		return params;
   }
   
   , stopWorksheetEditing: function() {
	   this.documentMode = 'VIEW';
	   this.synchronizeToolbar(this.executionInstance, this.miframe, this.southPanel, this.northPanel, this.parametersPanel, this.shortcutsPanel);
	   var newUrl = this.changeDocumentExecutionUrlParameter('ACTION_NAME', 'WORKSHEET_ENGINE_START_ACTION');
	   this.miframe.getFrame().setSrc(newUrl);
   }
   
   , changeDocumentExecutionUrlParameter: function(parameterName, parameterValue) {
		var frame = this.miframe.getFrame();
	    var docurl = frame.getDocumentURI();
	    var startIndex = docurl.indexOf('?')+1;
	    var endIndex = docurl.length;
	    var baseUrl = docurl.substring(0, startIndex);
	    var docurlPar = docurl.substring(startIndex, endIndex);
	    
	    docurlPar = docurlPar.replace(/\+/g, " ");
	    var parurl = Ext.urlDecode(docurlPar);
	    parurl[parameterName] = parameterValue;
	    parurl = Ext.urlEncode(parurl);
	    var endUrl = baseUrl +parurl;
	    return endUrl;
   }
	 
	 , saveQbe: function () {
		try {
			if (!Sbi.user.functionalities.contains('BuildQbeQueriesFunctionality')) {
				// If user is not a Qbe power user, he can only save worksheet
				this.saveWorksheetAs();
			} else {
				// If the user is a Qbe power user, he can save both current query and worksheet definition.
				// We must get the current active tab in order to understand what must be saved.
				var qbeWindow = this.miframe.getFrame().getWindow();
				var qbePanel = qbeWindow.qbe;
				var anActiveTab = qbePanel.tabs.getActiveTab();
				var activeTabId = anActiveTab.getId();
				var isBuildingWorksheet = (activeTabId === 'WorksheetDesignerPanel' || activeTabId === 'WorkSheetPreviewPage');
				if (isBuildingWorksheet) {
					// save worksheet as document
					this.saveWorksheetAs();
				} else {
					// save query as customized view
					qbePanel.queryEditorPanel.showSaveQueryWindow();
				}
			}
		} catch (err) {
			alert('Sorry, cannot perform operation.');
			throw err;
		}
   }
});