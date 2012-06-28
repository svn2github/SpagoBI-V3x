<%-- SpagoBI, the Open Source Business Intelligence suite

Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. --%>
 
  
 
  
<html>
	
	<head>
		<%@include file="commons/includeExtJS_410_RC.jspf" %>
		<%@include file="commons/includeSbiChartJS.jspf"%>
	</head>
	
	<body>		
    	<script type="text/javascript">  
	    	var template =  <%= chartTemplate  %>;	    
	    	var chartPanel = {};
			
	    	Sbi.config = {};
			
			var url = {
				  host: '<%= engineServerHost %>'
				, port: '<%= enginePort %>'
				, contextPath: '<%= engineContext %>'
			};
		
			var params = {
				SBI_EXECUTION_ID: <%=executionId %>			
			  , LIGHT_NAVIGATOR_DISABLED: 'TRUE'
			};
			Sbi.config.serviceRegistry = Ext.create('Sbi.service.ServiceRegistry',{ baseUrl: url
    																			  , baseParams: params
    									  }); 
			Sbi.config.spagobiServiceRegistry = Ext.create('Sbi.service.ServiceRegistry',{
															baseUrl: {contextPath: '<%= spagobiContext %>'}
														  , baseParams: {LIGHT_NAVIGATOR_DISABLED: 'TRUE'}
												});
			
			function exportChart(exportType) {								
			  	var top = 0,
				  	width = 0,
				  	groupIsCreated = false,
				  	svgTitle = "",
				  	svgSubtitle = "";
				  
			  	var chart = chartPanel.chart;
	          	var svg = chart.save({type:'image/svg'});	          	
				svg = svg.substring(svg.indexOf("<svg"));

	          	var tmpSvg = svg.replace("<svg","<g transform='translate(10,50)'");
				tmpSvg = tmpSvg.replace("</svg>", "</g>");
				var tmpHeight = chartPanel.height;			
				
	          	
	          	//adds title and subtitle
	          	if (chartPanel.title){
	          		//var nameEl = "'"+<%=executionId%>+"'_title";
	          		//var tmpTitle = document.getElementById(nameEl);
	          		//var titleX = (tmpTitle.offsetWidth-chartPanel.title.text.length)/2;
	          		var titleStyle = chartPanel.title.style;
	          		titleStyle = titleStyle.replace("color","fill");
	          		svgTitle += "<text y='25' style='" + titleStyle +"'>"+chartPanel.title.text+"</text>";
	          		tmpHeight += 100;
	          	}
	          	if (chartPanel.subtitle){
	          		var subtitleStyle = chartPanel.subtitle.style;
	          		subtitleStyle = subtitleStyle.replace("color","fill");
	          		svgSubtitle += "<text y='45' style='" + subtitleStyle +"'>"+chartPanel.subtitle.text+"</text>";
	          		tmpHeight += 100;
	          	}
	          				
	          	svg = "<svg height='"+tmpHeight+"px' width='100%' version='1.1' xmlns='http://www.w3.org/2000/svg'>";
				svg += tmpSvg;
				svg += svgTitle;
				svg += svgSubtitle;
				svg += "</svg>";
	          		          	
	          	params.type = exportType;
		  	  	urlExporter = Sbi.config.serviceRegistry.getServiceUrl({serviceName: 'EXPORT_EXTCHART_ACTION'
																  	  , baseParams:params
																	   });
	          	Ext.DomHelper.useDom = true; // need to use dom because otherwise an html string is composed as a string concatenation,
	          // but, if a value contains a " character, then the html produced is not correct!!!
	          // See source of DomHelper.append and DomHelper.overwrite methods
	          // Must use DomHelper.append method, since DomHelper.overwrite use HTML fragments in any case.
	          var dh = Ext.DomHelper;

	          var form = document.getElementById('export-chart-form');
	          if (form === undefined || form === null) {
		          var form = dh.append(Ext.getBody(), { // creating the hidden form
		          id: 'export-chart-form'
		          , tag: 'form'
		          , method: 'post'
		          , cls: 'export-form'
		          });
		          
		          dh.append(form, {					// creating the hidden input in form
						tag: 'input'
						, type: 'hidden'
						, name: 'svg'
						, value: ''  // do not put value now since DomHelper.overwrite does not work properly!!
					});
	          }          
	          // putting the chart data into hidden input
	          //form.elements[0].value = Ext.encode(svg);
	          form.elements[0].value = svg;
	          form.action = urlExporter;
	          form.target = '_blank'; // result into a new browser tab
	          form.submit();		 
	          
			}

			
	        var config ={};
	        config.template = template
	        config.divId = "<%=executionId%>";
	        config.documentLabel = "<%=documentLabel%>";
	        config.dsLabel = "<%=dsLabel%>";
	        config.dsTypeCd = "<%=dsTypeCd%>";
	        
			Ext.onReady(function() { 
				Ext.QuickTips.init();
				chartPanel = Ext.widget('ExtJSChartPanel',config); //by alias
				var viewport = new Ext.Viewport(chartPanel);
			});


	    </script>
	    <div id="<%=executionId%>_title" align="center" style="width:90%;"></div>
	    <div id="<%=executionId%>_subtitle" align="center" style="width:90%;"></div>
	    <div id="<%=executionId%>" align="center" style="width:90%;"></div>    
	</body>

</html>
