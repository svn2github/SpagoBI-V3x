<!--
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
-->

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>

<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("CheckLinksModule"); 
		String objId = (String)moduleResponse.getAttribute("SUBJECT_ID");
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(new Integer(objId));
		String objLabel = obj.getLabel();
		String objName = obj.getName();
		String descr = obj.getDescription();	
	%>
	<table width="100%" cellspacing="0" border="0" class='header-table-portlet-section'>		
		<tr class='header-sub-row-portlet-section'>
			<td class='header-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "sbi.detailbiobj.DocLinked"  />  
			</td>
			
		</tr>
	</table>	
	
	<table style='width:100%;margin-top:1px' id = "docTable" >
	<tr>
	<td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDataSet.columnLabel"  />   
			</td>
	<td class='portlet-section-header' style='text-align:left'>
		<spagobi:message key = "SBISet.ListDataSet.columnName"  />  
			</td>
	<td class='portlet-section-header' style='text-align:left'>		
			<spagobi:message key = "SBISet.ListDataSet.columnDescr"  />  
			</td>
	</tr>	
	<tr class='portlet-font'>
		 			<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>
				    	<%= objLabel%>
				    	</td>
				   	<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%= objName%>		   				
				   	</td>	
				    <td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%= descr%>			   				
				   	</td>				
	</tr>		
	</table> 

<spagobi:checkbox moduleName="CheckLinksModule" filter="enabled"/>
