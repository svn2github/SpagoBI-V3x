/*
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


var asUrls = new Object();
var asTitleDocs = new Object();
var asZoomDocs = new Object();
var asExportDSDocs = new Object();
var asLinkedDocs = new Object();
var asLinkedFields = new Object();
var asLinkedCross = new Object();
var asStylePanels = new Object();
var numDocs = 0;


function setDocs(pUrls, pTitle, pZoom, pExport){
	for (i in pUrls)
	{
	   numDocs++;
	}
	asUrls = pUrls;
	asTitleDocs = pTitle;
	asZoomDocs = pZoom;
	asExportDSDocs = pExport;
	
}

function setLinkedDocs(pLinkedDocs){
	asLinkedDocs = pLinkedDocs;
}

function setLinkedFields(pLinkedFields){
	asLinkedFields = pLinkedFields;
}

function setLinkedCross(pLinkedCross){
	asLinkedCross = pLinkedCross;
}

function setStylePanels(pStylePanels){
	asStylePanels = pStylePanels;
}


function execDrill(name, url) {
	alert("The 'execDrill' function is deprecated. For the refresh of the document call execCrossNavigation(windowName, labelDoc, parameters).");
}

/* Update the input url with value for refresh linked documents and execute themes */
function execCrossNavigation(windowName, label, parameters) {
	var baseName = "iframe_";
	var labelDocClicked = windowName.substring(baseName.length);
	var tmpUrl = "";
	var reload = false;
	
	for(var docMaster in asUrls){
		var sbiLabelMasterDoc = docMaster;
		var generalLabelDoc = "";
		if (sbiLabelMasterDoc == labelDocClicked){
			for (var docLabel in asLinkedDocs){ 
				if (docLabel.indexOf(sbiLabelMasterDoc) >= 0){
					generalLabelDoc = asLinkedDocs[docLabel];
					var sbiLabelDocLinked = generalLabelDoc[0];
					//gets iframe element
					var nameIframe = "iframe_" + sbiLabelDocLinked;
					var element = document.getElementById(nameIframe);
					
					//updating url with fields found in object
					var j=0; 
					var sbiParMaster = "";
					var tmpOldSbiSubDoc = "";
					var newUrl = "";
					tmpUrl = "";
					var finalUrl = "";
					//checks the cross type (internal or external)
					for (var fieldCross in asLinkedCross){
						var totalCrossPar =  asLinkedCross[fieldCross];
						var	typeCross = totalCrossPar[0];
						var crossTypeDoc = fieldCross.substring(0, fieldCross.indexOf("__"));
						if (crossTypeDoc == sbiLabelDocLinked){
							break;
						}
					}

					for (var fieldLabel in asLinkedFields){ 
						var totalLabelPar =  asLinkedFields[fieldLabel];
						var	sbiLabelPar = totalLabelPar[0];
						var sbiSubDoc 	= fieldLabel.substring(0, fieldLabel.indexOf("__"));
	
						if (sbiSubDoc == sbiLabelDocLinked){
							if (tmpOldSbiSubDoc != sbiSubDoc){
								newUrl = asUrls[sbiSubDoc]; //final url
								if (newUrl === undefined ) {
									//check if the url is an external type
									newUrl = asUrls["EXT__" + sbiSubDoc]; 
								}
							 	tmpUrl = newUrl[0].substring(newUrl[0].indexOf("?")+1);
							 	finalUrl = newUrl[0];
								tmpOldSbiSubDoc = sbiSubDoc;
							}
							var paramsNewValues = parameters.split("&");
							var tmpNewValue = "";
							var tmpNewLabel = "";
							var tmpOldValue = "";	
							var tmpOldLabel = "";								
							if (paramsNewValues != null && paramsNewValues.length > 0) {
								for (j = 0; j < paramsNewValues.length; j++) {
									tmpNewValue = paramsNewValues[j];
									tmpNewLabel = tmpNewValue.substring(0,tmpNewValue.indexOf("="));
									var paramsOldValues = null;
								 	paramsOldValues = tmpUrl.split("&");
								 	//EXTERNAL navigation
									if (typeCross === 'EXTERNAL'){
										if (paramsOldValues != null && paramsOldValues.length > 0) {
											for (k = 0; k < paramsOldValues.length; k++) {
												tmpOldLabel = paramsOldValues[k].substring(0, paramsOldValues[k].indexOf("="));
												//replace all old values of parameter:											
												if (tmpOldLabel == tmpNewLabel){
													reload = true; 
													tmpNewValue = tmpNewValue.substring(tmpNewValue.indexOf("=")+1);
													tmpOldValue = paramsOldValues[k] ;
													tmpOldValue = tmpOldValue.substring(tmpOldValue.indexOf("=")+1);
													if ( tmpNewValue != ""){
													    if (tmpNewValue == "%") tmpNewValue = "%25";
														finalUrl = finalUrl.replace(tmpOldLabel+"="+tmpOldValue, tmpNewLabel+"="+tmpNewValue);
														newUrl[0] = finalUrl;
														tmpOldValue = "";
														tmpNewValue = "";
														break;		
													}
												}
											}
										}
									}
									else{
										//old management (INTERNAL navigation)
										var idParSupp = "";
										var idPar = fieldLabel.substring(fieldLabel.indexOf("__")+2);
										idParSupp = idPar.substring(0,idPar.indexOf("__"))+"__";
										idParSupp = idParSupp+idPar.substring(idParSupp.length,idPar.indexOf("__",idParSupp.length));
										sbiParMaster = asLinkedFields["SBI_LABEL_PAR_MASTER__" + idParSupp];
	
										if ((tmpNewValue.substring(0, tmpNewValue.indexOf("=")) == sbiParMaster) ){
											reload = true; //reload only if document target has the parameter inline
											tmpNewValue = tmpNewValue.substring(tmpNewValue.indexOf("=")+1);
											
											if (paramsOldValues != null && paramsOldValues.length > 0) {
												for (k = 0; k < paramsOldValues.length; k++) {
													tmpOldLabel = paramsOldValues[k].substring(0, paramsOldValues[k].indexOf("="));
													//gets old value of parameter:											
													if (tmpOldLabel == sbiLabelPar){
														tmpOldValue = paramsOldValues[k] ;
														tmpOldValue = tmpOldValue.substring(tmpOldValue.indexOf("=")+1);
														if (tmpOldValue != "" && tmpNewValue != ""){
														    if (tmpNewValue == "%") tmpNewValue = "%25";
															finalUrl = finalUrl.replace(sbiLabelPar+"="+tmpOldValue, sbiLabelPar+"="+tmpNewValue);															
															newUrl[0] = finalUrl;
															tmpOldValue = "";
															tmpNewValue = "";
															break;
														}
													}
												}
											}
										}
									}
								}						
							}
		
						}
					} //for (var fieldLabel in asLinkedFields){ 	
					//updated general url  with new values
					if (reload){
						if (asUrls[generalLabelDoc] !== undefined){
							asUrls[generalLabelDoc][0]=newUrl[0];
						}else{
							asUrls["EXT__" + generalLabelDoc][0]=newUrl[0];
						}
						RE = new RegExp("&amp;", "ig");
						var lastUrl = newUrl[0];
						lastUrl = lastUrl.replace(RE, "&");					
						var msg = {
								label: sbiLabelDocLinked
							  , windowName: this.name//docLabel
							  , typeCross: typeCross
						  	  };						
						sendUrl(nameIframe,lastUrl,msg);
						reload = false; 
					}
				}//if (docLabel.indexOf(sbiLabelMasterDoc) >= 0){
			}//for (var docLabel in asLinkedDocs){ 
		}
	}   
  
	return;
}

function sendUrl(nameIframe, url, msg){
	if (msg !== null && msg !== undefined && msg.typeCross === 'EXTERNAL'){
		//EXTERNAL cross management
		var params =  url.substring(url.indexOf("?")+1);
		if (params.substring(0,1) === '&') params = params.substring(1);
		msg.parameters = params;
		msg.target = 'self';
        sendMessage(msg, 'crossnavigation');
	}else{
		//INTERNAL cross management
		Ext.get(nameIframe).setSrc(url);
	}
	return;	
}

function pause(interval)
{
    var now = new Date();
    var exitTime = now.getTime() + interval;

    while(true)
    {
        now = new Date();
        if(now.getTime() > exitTime) return;
    }
}
 
//create panels for each document
Ext.onReady(function() {  
	if (numDocs > 0){   
  			for (var docLabel in asUrls){ 	
  				if (docLabel.substring(0,5) !== 'EXT__'){
	  				var totalDocLabel=docLabel;	
	  				var strDocLabel = totalDocLabel.substring(totalDocLabel.indexOf('|')+1);
	  				//gets style (width and height)
	  				var style = asStylePanels[strDocLabel];	  				
	  				var zoomDoc = asZoomDocs[strDocLabel] || "false";
	  				var exportDSDoc = asExportDSDocs[strDocLabel] || "false";
	  				//the title drives the header's visualization
	  				var titleDoc = asTitleDocs[strDocLabel] ;
	  				if (titleDoc[0] === "" && (zoomDoc[0] === "false" || exportDSDoc[0] === "false")){
	  					titleDoc = null;
	  				}
					var widthPx = "";
					var heightPx = "";
					if (style != null){
						widthPx = style[0].substring(0, style[0].indexOf("|"));
						heightPx = style[0].substring(style[0].indexOf("|")+1);
						widthPx = widthPx.substring(widthPx.indexOf("WIDTH_")+6);
			       		heightPx = heightPx.substring(heightPx.indexOf("HEIGHT_")+7);
					}
					//defines the tools (header's buttons):
					var arTools = [];
					if (zoomDoc !== undefined && zoomDoc[0] === "true"){
						//add the maximize (zoom) button
						var toolZoom = 	{
									    id: 'maximize', //restore
									    qtip: 'Zoom da internazionalizzare',
									    handler: function(){
									    	alert("ZOOM: mi hai cliccato!");
							      		//	this.refresh();
									    }, scope: this
							      	};
						arTools.push(toolZoom);
						//add the minimize (zoom) button hidden
						toolZoom = 	{
								    id: 'restore',
								    qtip: 'Zoom da internazionalizzare',
								    hidden: true,
								    handler: function(){
								    	alert("ZOOM restore: mi hai cliccato!");
						      		//	this.refresh();
								    }, scope: this
						      	};
					arTools.push(toolZoom);
					}
					if (exportDSDoc !== undefined && exportDSDoc[0] === "true"){
						//add the export dataset button
						var toolExport = {
									    id: 'gear',
									    qtip: 'Export dataset da internazionalizzare',
									    handler: function(){
									    	alert("EXPORT: mi hai cliccato!");
							      		//	this.refresh();
									    }, scope: this
									  };
						arTools.push(toolExport);
					}
					//create panel with iframe
					var p = new  Ext.ux.ManagedIframePanel({
							frameConfig:{autoCreate:{id:'iframe_' + strDocLabel, name:'iframe_' + strDocLabel}}
							,renderTo   : 'divIframe_'+ strDocLabel
			                //,title      : (titleDoc==null || titleDoc== "")?null:titleDoc
			                ,title      : titleDoc
			                ,defaultSrc : asUrls[docLabel]+""
			                ,loadMask   : true//(Ext.isIE)?true:false
			                ,border		: false //the border style should be defined into document template within the "style" tag
							,height		: Number(heightPx)
							,scrolling  : 'auto'	 //possible values: yes, no, auto  
							//,collapsible: true
							,tools		: arTools
							,bodyStyle	:'padding:1px'
					});
  				}
  	}}
}); 

