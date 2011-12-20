<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
--%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base311.jsp"%>
<%@ page import="it.eng.spagobi.commons.bo.Domain,
				 java.util.ArrayList,
				 java.util.List,
				 org.json.JSONArray" %>
<%

	List nodeTypesCd = (List) aSessionContainer.getAttribute("thrTypesList");
    List thrSeverityTypesCd = (List) aSessionContainer.getAttribute("thrSeverityTypes");

%>

<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/service/ServiceRegistry.js")%>'></script>

<script type="text/javascript">

	<%	
	JSONArray severityTypesArray = new JSONArray();
	if(thrSeverityTypesCd != null){
		for(int i=0; i< thrSeverityTypesCd.size(); i++){
			Domain domain = (Domain)thrSeverityTypesCd.get(i);
			JSONArray temp = new JSONArray();
			temp.put(domain.getValueCd());
			severityTypesArray.put(temp);
		}
	}	
	String severityTypes = severityTypesArray.toString();
	severityTypes = severityTypes.replaceAll("\"","'");	
	
	JSONArray nodeTypesArray = new JSONArray();
	if(nodeTypesCd != null){
		for(int i=0; i< nodeTypesCd.size(); i++){
			Domain domain = (Domain)nodeTypesCd.get(i);
			JSONArray temp = new JSONArray();
			temp.put(domain.getValueCd());
			nodeTypesArray.put(temp);
		}
	}	
	String nodeTypes = nodeTypesArray.toString();
	nodeTypes = nodeTypes.replaceAll("\"","'");
    %>

    var config = {};

	config.nodeTypesCd = <%= nodeTypes%>;
	config.thrSeverityTypesCd = <%= severityTypes%>;
	
	var url = {
    	host: '<%= request.getServerName()%>'
    	, port: '<%= request.getServerPort()%>'
    	, contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
    	   				  request.getContextPath().substring(1):
    	   				  request.getContextPath()%>'
    	    
    };

    Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({
    	baseUrl: url
    });

Ext.onReady(function(){
	Ext.QuickTips.init();
	var manageThresholds = new Sbi.kpi.ManageThresholds(config);
   var viewport = new Ext.Viewport({
		layout: 'border'
		, items: [
		    {
		       region: 'center',
		       layout: 'fit',
		       items: [manageThresholds]
		    }
		]

	});
   	
});


</script>


<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>