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
package it.eng.spagobi.engines.drivers.accessibility;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.SingletonConfig;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.engines.drivers.AbstractDriver;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class AccessibilityDriver extends AbstractDriver implements
		IEngineDriver {

	static Logger logger = Logger.getLogger(AccessibilityDriver.class);

	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject,
			IEngUserProfile profile) throws InvalidOperationRequest {
		logger.warn("Function not implemented");
		throw new InvalidOperationRequest();
	}

	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject,
			IEngUserProfile profile) throws InvalidOperationRequest {
		logger.warn("Function not implemented");
		throw new InvalidOperationRequest();
	}

	public Map getParameterMap(Object biobject, 
			IEngUserProfile profile,
			String roleName) {
		logger.debug("IN");
		Map map = new Hashtable();
		try {
			BIObject biobj = (BIObject) biobject;
			map = getMap(biobj);
		} catch (ClassCastException cce) {
			logger.error("The parameter is not a BIObject type", cce);
		}
		map = applySecurity(map, profile);
		logger.debug("OUT");
		return map;
	}

	public Map getParameterMap(Object object, 
			Object subObject,
			IEngUserProfile profile, 
			String roleName) {
		return getParameterMap(object, profile, roleName);
	}

	private Map getMap(BIObject biobj) {
		logger.debug("IN");
		Map pars = new Hashtable();

		String documentId = biobj.getId().toString();
		pars.put("document", documentId);
		logger.debug("Add document parameter:" + documentId);
		pars.put("documentLabel", biobj.getLabel());
		logger.debug("Add document parameter:" + biobj.getLabel());

		// adding date format parameter
		SingletonConfig config = SingletonConfig.getInstance();
		String formatSB = config.getConfigValue("SPAGOBI.DATE-FORMAT.format");
		String format = (formatSB == null) ? "DD-MM-YYYY" : formatSB;
		pars.put("dateformat", format);

		pars = addBIParameters(biobj, pars);
		pars = addBIParameterDescriptions(biobj, pars);

		logger.debug("OUT");
		return pars;
	}

	private Map addBIParameters(BIObject biobj, Map pars) {
		logger.debug("IN");
		if (biobj == null) {
			logger.warn("BIObject is null");
			return pars;
		}

		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
		if (biobj.getBiObjectParameters() != null) {
			BIObjectParameter biobjPar = null;
			for (Iterator it = biobj.getBiObjectParameters().iterator(); it
					.hasNext();) {
				try {
					biobjPar = (BIObjectParameter) it.next();
					String value = parValuesEncoder.encode(biobjPar);
					if (value != null)
						pars.put(biobjPar.getParameterUrlName(), value);
					else
						logger.warn("value encoded IS null");
					logger.debug("Add parameter:"
							+ biobjPar.getParameterUrlName() + "/" + value);
				} catch (Exception e) {
					logger.error("Error while processing a BIParameter", e);
				}
			}
		}
		logger.debug("OUT");
		return pars;
	}
}
