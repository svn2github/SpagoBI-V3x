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
         		 it.eng.spago.security.IEngUserProfile"
%>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<portlet:defineObjects/>



	
<div class="div_background">
	<br/> 
	<table>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.LOVS_MANAGEMENT)||userProfile.isAbleToExecuteAction(SpagoBIConstants.LOVS_VIEW)) {%>
			<tr class="portlet-font">
				<td width="100" align="center">
					<img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/valueModalityAdministrationIcon.png", currTheme)%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
				    <br/> 
					<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListLovsPage"/></portlet:actionURL>' 
						class="link_main_menu" >
						<spagobi:message key = "SBIDev.linkPredLov" /></a>
				</td>
			</tr>
		<%} %>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.CONTSTRAINT_MANAGEMENT)||userProfile.isAbleToExecuteAction(SpagoBIConstants.CONTSTRAINT_VIEW)) {%>
			<tr class="portlet-font">
				<td width="100" align="center">
					<img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/modalityCheckAdministrationIcon.png", currTheme)%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
				    <br/> 
					<a href='<portlet:actionURL><portlet:param name="PAGE" value="LISTMODALITIESCHECKSPAGE"/></portlet:actionURL>' 
						class="link_main_menu" >
						<spagobi:message key = "SBIDev.linkValConst" /></a>
				</td>
			</tr>
		<%} %>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT)||userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_VIEW)) {%>
			<tr class="portlet-font" vAlign="middle">
				<td width="100" align="center">
					<img src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/domainAdministrationIcon.png", currTheme)%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
				    <br/> 
					<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListParametersPage"/></portlet:actionURL>' 
						class="link_main_menu" >
						<spagobi:message key = "SBIDev.linkParam" /></a>
				</td>
			</tr>		
		<%} %>
	</table>
	<br/>
</div>
<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>

