<%--
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

Authors - Monica Franceschini
--%>
<%@page import="it.eng.spagobi.engines.kpi.KpiEngineUtil"%>
<%@page import="org.json.JSONObject, 
				 java.util.ArrayList,
				 java.util.List,
				 org.json.JSONArray"%>
				 
<%@ include file="/WEB-INF/jsp/commons/portlet_base311.jsp"%>

<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiDefaultHeaderForSpagoBI.jsp"%>

<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/service/ServiceRegistry.js")%>'></script>
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLinkByTheme(request, "css/kpi/kpi.css",currTheme)%>' 
      type='text/css' />

<%	//START ADDING TITLE AND SUBTITLE
	String title = (String)sbModuleResponse.getAttribute("title");
	String subTitle = (String)sbModuleResponse.getAttribute("subName");

	//END ADDING TITLE AND SUBTITLE

	String metadata_publisher_Name =(String)sbModuleResponse.getAttribute("metadata_publisher_Name");
	String trend_publisher_Name =(String)sbModuleResponse.getAttribute("trend_publisher_Name");
	List kpiRBlocks =(List)sbModuleResponse.getAttribute("kpiRBlocks");
	KpiLineVisibilityOptions options = new KpiLineVisibilityOptions();
	
	//START creating resources list
	if(!kpiRBlocks.isEmpty()){
		Iterator blocksIt = kpiRBlocks.iterator();
		while(blocksIt.hasNext()){
			KpiResourceBlock block = (KpiResourceBlock) blocksIt.next();
			if(block.getR()!=null){
				resources.add( block.getR());
			}
		}
	}
	
	JSONArray kpiRowsArray = new JSONArray();
	
	
	if(!kpiRBlocks.isEmpty()){
		Iterator blocksIt = kpiRBlocks.iterator();

		while(blocksIt.hasNext()){			
			KpiResourceBlock block = (KpiResourceBlock) blocksIt.next();
			KpiLine root = block.getRoot();
			JSONObject modelInstJson =  KpiEngineUtil.recursiveGetJsonObject(root);
			kpiRowsArray.put(modelInstJson);
		}			
	}

%>		
<script type="text/javascript">
		var url = {
			host: 'localhost'
			, port: '8080'
			, contextPath: 'SpagoBI'

		};

		Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({
			baseUrl: url
		});

		var config = {
			subtitle: '<%= subTitle%>',
			json: <%=kpiRowsArray%>
		};


		Ext.onReady(function(){

			var item = new Sbi.kpi.KpiGUILayout(config);
			//var item = new Sbi.kpi.KpiGridPanel(config);

		    var viewport = new Ext.Viewport({
		        layout:'fit',
		        items:[item]
		    });

		});

</script>


<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>

		