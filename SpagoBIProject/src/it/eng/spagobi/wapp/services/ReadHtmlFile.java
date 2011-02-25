/**

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

**/
package it.eng.spagobi.wapp.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.AuditLogUtilities;
import it.eng.spagobi.commons.utilities.HibernateUtil;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.wapp.bo.Menu;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ReadHtmlFile extends AbstractHttpAction{

	static private Logger logger = Logger.getLogger(ReadHtmlFile.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
	    logger.debug("IN");
		freezeHttpResponse();
		
		//Start writing log in the DB
		Session aSession =null;
		try {
			aSession = HibernateUtil.currentSession();
			Connection jdbcConnection = aSession.connection();
			IEngUserProfile profile = UserUtilities.getUserProfile();
			AuditLogUtilities.updateAudit(jdbcConnection,  profile, "activity.HTMLMenu", null);
		} catch (HibernateException he) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		//End writing log in the DB
		
		HttpServletResponse httpResp = getHttpResponse();
		httpResp.setContentType("text/html");
		ServletOutputStream out = httpResp.getOutputStream();

		String menuId=(String)serviceRequest.getAttribute("MENU_ID");
		logger.debug("menuId="+menuId);
		if(menuId!=null){
			Menu menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(menuId));
			String fileName=menu.getStaticPage();
			
			if (fileName==null) {
				logger.error("Menu with id = " + menu.getMenuId() + " has no file name specified");
				throw new Exception("Menu has no file name specified");
			}

			// check the validity of the fileName (it must not be a path)
			// TODO remove this control and write better this action, or remove the action at all
			if (fileName.contains("\\") || fileName.contains("/") || fileName.contains("..")) {
				logger.error("Menu with id = " + menu.getMenuId() + " has file name [" + fileName + "] containing file separator character!!!");
				throw new Exception("Menu file name cannot contain file separator character");
			}
			
			logger.debug("fileName="+fileName);
			
			ConfigSingleton configSingleton = ConfigSingleton.getInstance();
			SourceBean sb = (SourceBean)configSingleton.getAttribute("SPAGOBI.RESOURCE_PATH_JNDI_NAME");
			String path = (String) sb.getCharacters();
			String filePath= SpagoBIUtilities.readJndiResource(path);
			filePath+="/static_menu/"+fileName;
			
			logger.debug("filePath="+filePath);
			
			FileInputStream fis=null;
			try{
			fis=new FileInputStream(filePath);
			}
			catch (Exception e) {
				logger.error("Could not open file "+filePath);
				throw new Exception("Could not open file");
			}
			
			int avalaible = fis.available();   // Mi informo sul num. bytes.

			for(int i=0; i<avalaible; i++) {
				out.write(fis.read()); 
			}

			fis.close();
			out.flush();	
			out.close();	 	
			logger.debug("OUT");
		}
		else{
		    logger.error("missing id");
			throw new Exception("missing id");
		}
	}

}
