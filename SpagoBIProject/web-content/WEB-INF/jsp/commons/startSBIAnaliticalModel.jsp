<!--
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
-->

<%@ page language="java"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         import="it.eng.spagobi.commons.constants.SpagoBIConstants,
         		 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spago.base.SourceBean,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="it.eng.spago.base.SessionContainer"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>

<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<portlet:defineObjects/>




<div class="div_background">
    <br/>	
	<table>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.FUNCTIONALITIES_MANAGEMENT)) {%>
			<tr class="portlet-font">
				<td width="100" align="center">
					<img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/folderAdministrationIcon.png", currTheme)%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
				    <br/> 
					<a href='<portlet:actionURL><portlet:param name="PAGE" value="BIObjectsPage"/>
						<portlet:param name="OPERATION" value="<%= SpagoBIConstants.FUNCTIONALITIES_OPERATION %>"/></portlet:actionURL>' 
						class="link_main_menu" >
						<spagobi:message key = "SBISet.linkFunctMan" />
					</a>
				</td>
			</tr>
		<% } %>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) ||
			   userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)) {%>
			<tr class="portlet-font" vAlign="middle">
				<td width="100" align="center">
					<img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/objectAdministrationIcon.png", currTheme)%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
				    <br/> 
					<a href='<portlet:actionURL>
						<portlet:param name="PAGE" value="BIObjectsPage"/>
						</portlet:actionURL>' 
						class="link_main_menu" >
						<spagobi:message key = "SBISet.linkDocMan" />
					</a>
				</td>
			</tr>		
		<% } %>
	</table>
	<br/>
</div>
<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>
