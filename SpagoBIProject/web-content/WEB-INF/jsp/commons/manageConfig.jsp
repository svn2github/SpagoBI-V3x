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
--%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base311.jsp"%>

<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="org.json.JSONArray"%>
<%@page import="it.eng.spagobi.chiron.serializer.SerializerFactory"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>

<%
	List <Domain> domains = DAOFactory.getDomainDAO().loadListDomainsByType("PAR_TYPE");
	Iterator <Domain> it = domains.iterator();
	while(it.hasNext()){
		Domain domain = it.next();
		String i18nName = domain.getTranslatedValueName(locale);
		String i18nDS = domain.getTranslatedValueDescription(locale);
		domain.setValueName(i18nName);
		domain.setValueDescription(i18nDS);
	}
    JSONArray domainsJson = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(domains, locale); 
%>

<script type="text/javascript">

	var domains = <%=domainsJson.toString()%>;
	
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
		var manageConfig = new Sbi.config.ManageConfig({});
		var viewport = new Ext.Viewport({
			layout: 'border'
			, items: [
			    {
			       region: 'center',
			       layout: 'fit',
			       items: [manageConfig]
			    }
			]
	
		});
	   	
	});


</script>


<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>