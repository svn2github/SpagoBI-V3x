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
package it.eng.spagobi.behaviouralmodel.analyticaldriver.service;

/**
 * Implements a module which handles ObjParuse objects relevant to a BIObjectParameter, 
 * where a ObjParuse is a correlation between the parameter with another parameter 
 * of the same document: has methods for ObjParuse load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author zerbetto
 */

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IObjParuseDAO;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.config.service.ListEnginesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ListObjParuseModule extends AbstractModule {

	private EMFErrorHandler errorHandler;
	
	protected SessionContainer session = null;
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.module.AbstractModule#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		RequestContainer requestContainer = this.getRequestContainer();	
		session = requestContainer.getSessionContainer();	
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
				            "service", "begin of ListObjParuseModule modify/visualization " +
				            "service with message =" +message);
		errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "service", "The message parameter is null");
				throw userError;
			} 
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				getDetailObjParuses(request, response);
			} 	else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				modObjParuses(request, response);
			}	else if (message.trim().equalsIgnoreCase("EXIT_FROM_MODULE")) {
				exit(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	
	
	
	private void getDetailObjParuses(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			// get form session the biobject parameter
			BIObjectParameter objParameter = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
			// get the id of the bi parameter
			Integer biParamId = objParameter.getId();
			// get the id of the biobject father
			Integer biObjectId = objParameter.getBiObjectID();
			// get the id of the general parameter associated 
			Integer genParamId = objParameter.getParID();
			// load all the other bi parameters of the biobject
			List allBiObjBIParams = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(biObjectId);
			List otherBiObjBiParams = new ArrayList();
			Iterator allObjParametersIt = allBiObjBIParams.iterator();
			while (allObjParametersIt.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) allObjParametersIt.next();
				if (!aBIObjectParameter.getId().equals(biParamId)) 
					otherBiObjBiParams.add(aBIObjectParameter);
			}
			// get the correlation associated to the bi parameter 
			List biParamCorrelations = DAOFactory.getObjParuseDAO().loadObjParuses(biParamId);
			// load all the paruses associated to the general parameter
			List genParParuses = DAOFactory.getParameterUseDAO().loadParametersUseByParId(genParamId);
			// exclude form all the paruses the manual input ones	
			List paruses = new ArrayList();
			Iterator allParusesIt = genParParuses.iterator();
			while (allParusesIt.hasNext()) {
				ParameterUse paruse = (ParameterUse) allParusesIt.next();
				if(paruse.getManualInput().intValue() == 0) {
					paruses.add(paruse);
				}
			}
			// fill spago response
			response.setAttribute("biParameter", objParameter);
			response.setAttribute("allParuses", paruses);
			response.setAttribute("biParamCorrelation", biParamCorrelations);
			response.setAttribute("otherBiParameters", otherBiObjBiParams);
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ListObjParuseModule","getDetailObjParuses","Cannot fill response container", ex  );
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1047, new Vector(), params);
		}
	}
	
	
	
	
	private void modObjParuses(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		try {
			// get the id of the biparameter
			String biParamIdStr = (String) request.getAttribute("obj_par_id");
			Integer biParamId = new Integer (biParamIdStr);
			// get the xml description of new correlations from request and transform to sourceBean
			String xmlCorrsStr = (String) request.getAttribute("correlations_xml");
			SourceBean xmlCorrsSB = SourceBean.fromXMLString(xmlCorrsStr);
			// get the list of correlations SB
			List correlations = xmlCorrsSB.getAttributeAsList("correlation");
			// for each correlation create a new correlation object (SbiObjParuse)
			List newCorrelations = new ArrayList();
			Iterator corrIter = correlations.iterator();
			int prog = 1;
			while(corrIter.hasNext()) {
				SourceBean correlationSB = (SourceBean)corrIter.next();
				String preCondition = (String)correlationSB.getAttribute("precond");
				String postCondition = (String)correlationSB.getAttribute("postcond");
				String logicOperator = (String)correlationSB.getAttribute("logicoperator");
				String idParFatherStr = (String)correlationSB.getAttribute("idparameterfather");
				Integer idParFather = new Integer(idParFatherStr);
				String condition = (String)correlationSB.getAttribute("condition");
				List paruseSettings = correlationSB.getAttributeAsList("parusesettings.parusesetting");
				Iterator iterPUS = paruseSettings.iterator();
				while(iterPUS.hasNext()) {
					SourceBean paruseSetSB = (SourceBean)iterPUS.next();
					String active = (String)paruseSetSB.getAttribute("active");
					String paruseidStr = (String)paruseSetSB.getAttribute("paruseid");
					String oncolumn = (String)paruseSetSB.getAttribute("oncolumn");
					Integer paruseid = new Integer(paruseidStr);
					ObjParuse correlation = new ObjParuse();
					correlation.setFilterColumn(oncolumn);
					correlation.setFilterOperation(condition);
					correlation.setLogicOperator(logicOperator);
					correlation.setObjParFatherId(idParFather);
					correlation.setObjParId(biParamId);
					correlation.setParuseId(paruseid);
					correlation.setPostCondition(postCondition);
					correlation.setPreCondition(preCondition);
					correlation.setProg(new Integer(prog));
					newCorrelations.add(correlation);
				}
				prog ++;
			}		
			// load the previous saved correlations
			List oldcorrelations = DAOFactory.getObjParuseDAO().loadObjParuses(biParamId);
			if(oldcorrelations == null) 
				oldcorrelations = new ArrayList();
			// update database
			updateDatabase(oldcorrelations, newCorrelations);
			// fill the response 
			response.setAttribute("loopback", "true");
			session.setAttribute("RETURN_FROM_MODULE", "ListObjParuseModule");
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ListObjParuseModule","modObjParuses","Cannot fill response container", ex  );
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1048, new Vector(), params);
		}
		
	}
	
	
	private void updateDatabase(List oldCorrelations, List newCorrelations ) throws EMFUserError {
		//TODO all this operations must be performed inside a transaction
		IObjParuseDAO corrDao = DAOFactory.getObjParuseDAO();
		Iterator iterOldCorr = oldCorrelations.iterator();
		while(iterOldCorr.hasNext()) {
			ObjParuse oldCorr = (ObjParuse)iterOldCorr.next();
			corrDao.eraseObjParuse(oldCorr);
		}
		Iterator iterNewCorr = newCorrelations.iterator();
		while(iterNewCorr.hasNext()) {
			ObjParuse newCorr = (ObjParuse)iterNewCorr.next();
			corrDao.insertObjParuse(newCorr);
		}
	}
	
	
	
	private void exit(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		session.setAttribute("RETURN_FROM_MODULE", "ListObjParuseModule");
		response.setAttribute("loopback", "true");
	}

	
	
}
