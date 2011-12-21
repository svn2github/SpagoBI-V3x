/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.services.execute.service;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

public class ServiceKpiValueXml {
	
	private static transient Logger logger=Logger.getLogger(ServiceKpiValueXml.class);
	
	public String getKpiValueXML(String token, String user,Integer kpiValueID){
		logger.debug("IN");
		SsoServiceInterface proxyService = SsoServiceFactory.createProxyService();
		String xml = "";
		try {
			
			proxyService.validateTicket(token, user);
			logger.debug("Token validated");
			xml = DAOFactory.getKpiDAO().loadKPIValueXml(kpiValueID);
			logger.debug("Xml Retrieved");
			
		} catch (EMFUserError e) {
			e.printStackTrace();
			logger.error("Problem while retrieving xml of Kpivalue with id "+(kpiValueID!=null?kpiValueID:"null"),e);
		} catch (SecurityException e) {
			e.printStackTrace();
			logger.error("Security Exception while retrieving xml of Kpivalue with id "+(kpiValueID!=null?kpiValueID:"null"),e);
		}	
		logger.debug("OUT");
		return xml;
	}
}
