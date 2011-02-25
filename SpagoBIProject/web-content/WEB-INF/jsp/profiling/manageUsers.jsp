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
<%@ page import="it.eng.spagobi.commons.bo.Role,
				 it.eng.spagobi.profiling.bean.SbiAttribute,
				 java.util.ArrayList,
				 java.util.List" %>
<%
	List<SbiAttribute> attributes = (List<SbiAttribute>) aSessionContainer.getAttribute("attributesList");
	List<Role> roles = (List<Role>) aSessionContainer.getAttribute("rolesList");
%>
<script type="text/javascript">
	<%
	String attributesList ="{}";
	if(attributes != null){
		attributesList="[";
		for(int i=0; i< attributes.size(); i++){
			SbiAttribute attr = attributes.get(i);
			attributesList+="{";
			attributesList+="'id':"+attr.getAttributeId()+",";
			attributesList+="'name':'"+attr.getAttributeName()+"',";
			attributesList+="'value':''";
			attributesList+="}";
			if(i != (attributes.size()-1)){
				attributesList+=",";
			}
		}
		attributesList+="]";
	}
	
	String rolesList ="{}";
	if(roles != null){
		rolesList="[";
		for(int i=0; i< roles.size(); i++){
			Role role = roles.get(i);
			rolesList+="{";
			rolesList+="'id':"+role.getId()+",";
			rolesList+="'name':'"+role.getName()+"',";
			rolesList+="'description':'"+role.getDescription()+"',";
			rolesList+="'checked':false";
			rolesList+="}";
			if(i != (roles.size()-1)){
				rolesList+=",";
			}
		}
		rolesList+="]";
	}
	%>
	Sbi.config.passwordAbilitated = false;
	
	var config = {
				  attributesEmpyList:<%=attributesList%>,
				  rolesEmptyList:<%=rolesList%>
				  };
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
		var manageUsers = new Sbi.profiling.ManageUsers(config);
		
		var viewport = new Ext.Viewport({
			layout: 'border'
			, items: [
			    {
			       region: 'center',
			       layout: 'fit',
			       items: [manageUsers]
			    }
			]
	
		});
	   	
	});


</script>


<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>