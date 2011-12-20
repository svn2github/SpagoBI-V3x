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
<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiDefaultHeaderForSpagoBI.jsp"%>

<%	//START ADDING TITLE AND SUBTITLE
	String title = (String)sbModuleResponse.getAttribute("title");
	String subTitle = (String)sbModuleResponse.getAttribute("subName");
	if (title!=null){
		%>
		<div class="kpi_title_section"><%=title%></div>
	<%}
	if (subTitle!=null){%>
		<div class="kpi_subtitle_section"><%=subTitle%></div>
	<%}
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
	//END creating resources list
	%>
	
	<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiScriptsForTree.jsp"%>	
	<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiAddItemForTree.jsp"%>		
		
    <%//START CONSTRUCTING TREE FOR EACH RESOURCE
	if(!kpiRBlocks.isEmpty()){
		Iterator blocksIt = kpiRBlocks.iterator();%>
		<p> 
		<% if(options.getWeighted_values().booleanValue()){%>
			<div class="kpi_note_section">Weighted Values</div>
	    <%}%>
		</p>
		    
		<%while(blocksIt.hasNext()){%>
			<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiAddBlockForTree.jsp"%>		
		<%}			
	}//END CONSTRUCTING TREE FOR EACH RESOURCE	
	
	// put in session the KpiResourceBlocks, title and subtitle for the PDF creation
	if(kpiRBlocks!=null){
		session.setAttribute("KPI_BLOCK",kpiRBlocks);
		session.setAttribute("TITLE",title);
		session.setAttribute("SUBTITLE",subTitle);
	}
%>		
<%@ include file="/WEB-INF/jsp/engines/kpi/default/kpiinclusions/kpiDefaultFooterForSpagoBI.jsp"%>
<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>		

		