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

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@ page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter,				 
				 it.eng.spagobi.commons.dao.DAOFactory,			
				 java.util.List,java.util.Map,java.util.HashMap,			 
				 it.eng.spagobi.commons.bo.Domain,
				 java.util.Iterator,
				 it.eng.spagobi.engines.config.bo.Engine,			
				 it.eng.spago.base.SourceBean,			
				 java.util.Date"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.eng.spagobi.tools.datasource.bo.DataSource"%>
<%@page import="it.eng.spagobi.monitoring.dao.AuditManager"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@page import="it.eng.spagobi.engines.kpi.bo.ChartImpl"%>
<%@page import="it.eng.spago.error.EMFErrorHandler"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.*"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="it.eng.spagobi.kpi.config.bo.Kpi"%>
<%@page import="it.eng.spagobi.kpi.config.bo.KpiInstance"%>

<%  
	EMFErrorHandler errorHandler=aResponseContainer.getErrorHandler();
	if(errorHandler.isOK()){    
	SessionContainer permSession = aSessionContainer.getPermanentContainer();

	if(userProfile==null){
		userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		userId=(String)((UserProfile)userProfile).getUserId();
	}
	Integer kpiInstId = (Integer)aServiceResponse.getAttribute("kpiInstId");
	String resName = (String)aServiceResponse.getAttribute("resName");
	String TimeRangeFrom = (String)aServiceResponse.getAttribute("TimeRangeFrom");
	String TimeRangeTo = (String)aServiceResponse.getAttribute("TimeRangeTo");
	String endDate = (String)aServiceResponse.getAttribute("endDate");
	Date timeFrom = (Date)aServiceResponse.getAttribute("timeFrom");
	Date timeTo = (Date)aServiceResponse.getAttribute("timeTo");
	Date d = (Date)aServiceResponse.getAttribute("d");
	Integer resID = (Integer)aServiceResponse.getAttribute("resID");
	
	KpiInstance ki = DAOFactory.getKpiInstanceDAO().loadKpiInstanceById(kpiInstId);
	Integer kpiID = ki.getKpi();
	Kpi k = DAOFactory.getKpiDAO().loadKpiById(kpiID);	
	String title = "";
	if (resName!= null){
		title = msgBuilder.getMessage("sbi.kpi.trendTitleWithResource", request);
		title = title.replaceAll("%0", k.getKpiName());
		title = title.replaceAll("%1", resName);
	}else{
		title = msgBuilder.getMessage("sbi.kpi.trendTitle", request);
		title = title.replaceAll("%0", k.getKpiName());
	}

	String chartType = "LineChart";		
	ChartImpl sbi = ChartImpl.createChart(chartType);
	
	String result = null ;
	if(timeFrom!=null && timeTo!=null && timeFrom.before(timeTo)){
		result = DAOFactory.getKpiDAO().getKpiTrendXmlResult(resID, kpiInstId, timeFrom,timeTo);
		String subTitle = msgBuilder.getMessage("sbi.kpi.trendPeriod", request);
		subTitle = subTitle.replaceAll("%0",TimeRangeFrom);
		subTitle = subTitle.replaceAll("%1",TimeRangeTo);
		sbi.setSubName(subTitle);
	}else{
	    result = DAOFactory.getKpiDAO().getKpiTrendXmlResult(resID, kpiInstId, d);
	    String subTitle = msgBuilder.getMessage("sbi.kpi.trendEndDate", request);
	    subTitle = subTitle.replaceAll("%0",endDate);
	    sbi.setSubName(subTitle);
	}    

	sbi.setProfile(userProfile);
	sbi.calculateValue(result);
	sbi.setName(title);	

	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	UUID uuidObj = uuidGen.generateTimeBasedUUID();
	String executionId = uuidObj.toString();
	
	JFreeChart chart = sbi.createChart();
    ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
    String path_param=executionId;
	String dir=System.getProperty("java.io.tmpdir");
	String path=dir+"/"+executionId+".png";
	java.io.File file1 = new java.io.File(path);
	
	ChartUtilities.saveChartAsPNG(file1, chart, 450, 310, info);

	String urlPng=GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + 
	"?ACTION_NAME=GET_PNG2&NEW_SESSION=TRUE&userid="+userId+"&path="+path_param+"&LIGHT_NAVIGATOR_DISABLED=TRUE";
	 %><br>
 		<div align="center">
			<img id="image" src="<%=urlPng%>" BORDER="1" USEMAP="#chart"/>
		</div>
		<br>	
	  <% 
	} // End no error case	
	%>

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>