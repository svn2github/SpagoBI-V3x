/**

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

**/
package it.eng.spagobi.services.proxy;

import it.eng.spagobi.container.SpagoBIHttpSessionContainer;
import it.eng.spagobi.services.audit.stub.AuditServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Proxy of Autit Service
 *
 */
public final class AuditServiceProxy extends AbstractServiceProxy{

	static private final String SERVICE_NAME = "Audit Service";
	
    static private Logger logger = Logger.getLogger(AuditServiceProxy.class);

    /**
     * The Constructor.
     * 
     * @param user userId
     * @param session Http Session
     */
    public AuditServiceProxy(String user,HttpSession session) {
    	super(user, session);
    }
    
    private AuditServiceProxy() {
	super();
    }    
    
    private it.eng.spagobi.services.audit.stub.AuditService lookUp() throws SecurityException {
	try {
	    AuditServiceServiceLocator locator = new AuditServiceServiceLocator();
	    it.eng.spagobi.services.audit.stub.AuditService service=null;
	    if (serviceUrl!=null ){
		    service = locator.getAuditService(serviceUrl);		
	    }else {
		    service = locator.getAuditService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Impossible to locate [" + SERVICE_NAME + "] at [" + serviceUrl + "]");
	    throw new SecurityException("Impossible to locate [" + SERVICE_NAME + "] at [" + serviceUrl + "]", e);
	}
    }
    
    /**
     * Log.
     * 
     * @param id id
     * @param start start time
     * @param end end time
     * @param state state
     * @param message message
     * @param errorCode error code
     * 
     * @return String
     */
    public String log(String id,String start,String end,String state,String message,String errorCode){
	logger.debug("IN");
	try {
	    return lookUp().log( readTicket(), userId, id, start, end, state, message, errorCode);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("OUT");
	}
	return null;
    }
}
