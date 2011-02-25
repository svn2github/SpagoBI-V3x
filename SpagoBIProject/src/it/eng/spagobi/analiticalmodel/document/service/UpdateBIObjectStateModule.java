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
package it.eng.spagobi.analiticalmodel.document.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

public class UpdateBIObjectStateModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(UpdateBIObjectStateModule.class);
	public final static String MODULE_PAGE = "UpdateBIObjectStateModule";
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		String message = (String) request.getAttribute("MESSAGEDET");
		
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				logger.debug("The message parameter is null");
				throw userError;
			}
			logger.debug("The message parameter is: " + message.trim());
			if (message.trim().equalsIgnoreCase("MOVE_STATE_UP")) {
				moveStateUp(request,  response);
			} 
			else if (message.trim().equalsIgnoreCase("MOVE_STATE_DOWN")) {
				moveStateDown(request,  response);
				} 
			
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	
	logger.debug("OUT");
    }
	
	private void moveStateUp(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException  {
		
		String objId = "";
		List params = request.getContainedAttributes();
	    ListIterator it = params.listIterator();

	    while (it.hasNext()) {

		Object par = it.next();
		SourceBeanAttribute p = (SourceBeanAttribute) par;
		String parName = (String) p.getKey();
		logger.debug("got parName=" + parName);
		if (parName.equals("OBJECT_ID")) {
		    objId = (String) request.getAttribute("OBJECT_ID");
		    logger.debug("got OBJECT_ID from Request=" + objId);
			} 
	    }
	    
	    if (objId != null && !objId.equals("")){
	    		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(new Integer(objId));
	    		if (obj!= null){
	    			String state = obj.getStateCode();
	    			if (state!= null && state.equals("DEV")){
	    				Domain dTemp = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("STATE", "TEST");
	    				obj.setStateCode("TEST");
	    				obj.setStateID(dTemp.getValueId());
	    			}else if (state!= null && state.equals("TEST")){
	    				Domain dTemp = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("STATE", "REL");
	    				obj.setStateCode("REL");
	    				obj.setStateID(dTemp.getValueId());
	    			}
	    			DAOFactory.getBIObjectDAO().modifyBIObject(obj);
	    		}
	     }
		
	 }

	
	private void moveStateDown(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException  {
		
		String objId = "";
		List params = request.getContainedAttributes();
	    ListIterator it = params.listIterator();

	    while (it.hasNext()) {

		Object par = it.next();
		SourceBeanAttribute p = (SourceBeanAttribute) par;
		String parName = (String) p.getKey();
		logger.debug("got parName=" + parName);
		if (parName.equals("OBJECT_ID")) {
		    objId = (String) request.getAttribute("OBJECT_ID");
		    logger.debug("got OBJECT_ID from Request=" + objId);
			} 
	    }
	    
	    if (objId != null && !objId.equals("")){
	    		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(new Integer(objId));
	    		if (obj!= null){
	    			String state = obj.getStateCode();
	    			if (state!= null && state.equals("REL")){
	    				Domain dTemp = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("STATE", "TEST");
	    				obj.setStateCode("TEST");
	    				obj.setStateID(dTemp.getValueId());
	    			}else if (state!= null && state.equals("TEST")){
	    				Domain dTemp = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("STATE", "DEV");
	    				obj.setStateCode("DEV");
	    				obj.setStateID(dTemp.getValueId());
	    			}
	    			DAOFactory.getBIObjectDAO().modifyBIObject(obj);
	    		}
	     }
	 }
 

}
