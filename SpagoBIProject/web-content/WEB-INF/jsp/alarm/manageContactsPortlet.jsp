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

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>

<%
String messageBunle = "component_kpi_messages";
Map backUrlPars = new HashMap();
backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "sbi.kpi.contactsDefinition.label" bundle="<%=messageBunle%>" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      			title='<spagobi:message key = "sbi.generic.back" />' 
      			src='<%=urlBuilder.getResourceLinkByTheme(request, "/img/back.png", currTheme)%>' 
      			alt='<spagobi:message key = "sbi.generic.back" />' />
			</a>
		</td>
	</tr>
</table>

<%

String url = GeneralUtilities.getSpagoBIProfileBaseUrl(userUniqueIdentifier)+  "&ACTION_NAME=MANAGE_CONTACTS_ACTION";
url += "&LANGUAGE=" + locale.getLanguage();
url += "&COUNTRY=" + locale.getCountry();
%>
	<iframe 
		id='contactsIframe'
		name='contactsIframe'
		src='<%= url %>'
		frameBorder = 0
		width=100%
		height=<%= ChannelUtilities.getPreferenceValue(aRequestContainer, "HEIGHT", "500") %>
	/>


<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>