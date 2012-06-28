<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>
 
 <%@page import="java.io.InputStream"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Random"%>
<%@page import="it.eng.spagobi.sdk.proxy.DocumentsServiceProxy"%>
<%@page import="it.eng.spagobi.sdk.documents.bo.SDKTemplate"%>
<%@page import="javax.activation.DataHandler"%>
<%@page import="it.eng.spagobi.commons.utilities.SpagoBIUtilities"%>
<%
/**
This page invokes a SpagoBI web services in order to execute the document's methods.
It's a JSP for ONLY case tests.
To call it the url must be: http://localhost:8080/SpagoBISDK/document.jsp?doUpload=true&folderUpload=<nome_folder>
By default a foodmart/datamart.jar is ever downloaded.
The parameter 'doUpload' force an upload operation after the previous download operation.
The parameter 'folderUpload' gives the name of the folder to upload the file. If 'doUpload' is true and 'folderUpload' is null
a random folder name is created.
*/
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
String user = "biadmin";
String password = "biadmin";
String message = "Il documento � stato ";
String doUpload = "false";
String folderUpload = null;
String action = null;
String bodyCode = "";

if (user != null && password != null) {
	try { 
		DocumentsServiceProxy proxy = new DocumentsServiceProxy(user, password);
		proxy.setEndpoint("http://localhost:8080/SpagoBI/sdk/DocumentsService");		
		//gets request variables
		doUpload = (String)request.getParameter("doUpload");
		folderUpload = (String)request.getParameter("folderUpload");
		action = (String)request.getParameter("action");
		
		SDKTemplate template = new SDKTemplate();
		
		if (action.equalsIgnoreCase("getAllDatamartModels")){
			HashMap <String,String> mapModels = proxy.getAllDatamartModels();
			if (mapModels != null){
				message = "<H2>Sul server sono presenti i seguenti modelli: </H2><br>";
				
				for (Iterator iterator = mapModels.keySet().iterator(); iterator.hasNext();) {
					String folderName = (String) iterator.next();
					String fileName = mapModels.get(folderName);
					message += " <b>Folder:</b> " + folderName + "  - <b>Model:</b> " + fileName + "<br>";
				}				
				bodyCode += "<body>  " + message + " </body></html>";
			}
		}
		else{
			//test download datamart.jar
			template = proxy.downloadDatamartModelFiles("foodmart", "datamart.jar", "foodmart.sbimodel");
			//template = proxy.downloadDatamartFile("foodmart", "datamart.jar");
			if (template != null){ 
				if (doUpload == null || doUpload.equalsIgnoreCase("false")) {
			
					DataHandler dh = template.getContent();
					InputStream is = dh.getInputStream();
					String fileName = template.getFileName();
					
					java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
					java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(baos);
		
					int c = 0;
					byte[] b = new byte[1024];
					while ((c = is.read(b)) != -1) {
						if (c == 1024)
							bos.write(b);
						else
							bos.write(b, 0, c);
					}
					bos.flush();
					byte[] content = baos.toByteArray();
					bos.close();
					baos.close();
					
					response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\";");
					response.setContentLength(content.length);
					response.setContentType("application/x-zip-compressed");
					//response.setContentType("application/xml");
					response.getOutputStream().write(content);
	
					message += "scaricato con successo!";
				}
			}else{
				message += "scaricato con errori! Verifica i logs.";
				bodyCode += "<body> <h2> " + message + " </h2> " + new java.util.Date() +" </body></html>";
			}
			//test modifica datamart esistente			
			if(doUpload != null && doUpload.equalsIgnoreCase("true")){
				//test upload datamart.jar
				if (folderUpload == null){
					Random randomGenerator = new Random();
					folderUpload = "foodmartSDK_"+ randomGenerator.nextInt(100);
				}
				template.setFileName("datamart.jar");
				template.setFolderName(folderUpload);
				template.setContent(template.getContent());
				//proxy.uploadDatamartTemplate(template);
				proxy.uploadDatamartModel(template);
				message += "aggiornato con successo!";
				bodyCode += "<body> <h2> " + message + " </h2> " + new java.util.Date() +" </body></html>";
			}
		}
	}  catch (Exception e) {
		e.printStackTrace();
			
	}
} else {
	response.sendRedirect("login.jsp");
}

%>
<%=bodyCode%>
