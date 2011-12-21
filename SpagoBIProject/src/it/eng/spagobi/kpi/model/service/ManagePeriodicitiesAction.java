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
package it.eng.spagobi.kpi.model.service;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.serializer.SerializerFactory;
import it.eng.spagobi.commons.services.AbstractSpagoBIAction;
import it.eng.spagobi.kpi.config.bo.Periodicity;
import it.eng.spagobi.kpi.config.dao.IPeriodicityDAO;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.model.dao.IResourceDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManagePeriodicitiesAction extends AbstractSpagoBIAction {

	// logger component
	private static Logger logger = Logger.getLogger(ManagePeriodicitiesAction.class);
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String PERIODICTIES_LIST = "PERIODICTIES_LIST";
	private final String PERIODICITY_INSERT = "PERIODICITY_INSERT";
	private final String PERIODICITY_DELETE = "PERIODICITY_DELETE";

	// RES detail
	private final String ID = "idPr";
	private final String NAME = "name";
	private final String MONTHS = "months";
	private final String DAYS = "days";
	private final String HOURS = "hours";
	private final String MINUTES = "mins";

	@Override
	public void doService() {
		logger.debug("IN");
		IPeriodicityDAO perDao;
		try {
			perDao = DAOFactory.getPeriodicityDAO();
			perDao.setUserProfile(getUserProfile());
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME,	"Error occurred");
		}
		Locale locale = getLocale();

		String serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Service type "+serviceType);
		if (serviceType != null && serviceType.equalsIgnoreCase(PERIODICTIES_LIST)) {
			
			try {	
				List periodicities = perDao.loadPeriodicityList();
				logger.debug("Loaded periodicities list");
				JSONArray periodicitiesJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(periodicities, locale);
				JSONObject periodicitiesResponseJSON = createJSONResponsePeriodicities(periodicitiesJSON);

				writeBackToClient(new JSONSuccess(periodicitiesResponseJSON));

			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving users", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving users", e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(PERIODICITY_INSERT)) {
			
			String id = getAttributeAsString(ID);
			String name = getAttributeAsString(NAME);
			String months = getAttributeAsString(MONTHS);
			String days = getAttributeAsString(DAYS);
			String hours = getAttributeAsString(HOURS);
			String minutes = getAttributeAsString(MINUTES);		

			if (name != null) {
				Periodicity per = new Periodicity();
				per.setName(name);
				
				if(months!=null && !months.equals("")){
					per.setMonths(new Integer(months));
				}else{
					per.setMonths(new Integer("0"));
				}
				if(days!=null && !days.equals("")){
					per.setDays(new Integer(days));
				}else{
					per.setDays(new Integer("0"));
				}
				if(hours!=null && !hours.equals("")){
					per.setHours(new Integer(hours));
				}else{
					per.setHours(new Integer("0"));
				}
				if(minutes!=null && !minutes.equals("")){
					per.setMinutes(new Integer(minutes));
				}else{
					per.setMinutes(new Integer("0"));
				}			
				
				try {
					if(id != null && !id.equals("") && !id.equals("0")){							
						per.setIdKpiPeriodicity(Integer.valueOf(id));
						perDao.modifyPeriodicity(per);
						logger.debug("Resource "+id+" updated");
						JSONObject attributesResponseSuccessJSON = new JSONObject();
						attributesResponseSuccessJSON.put("success", true);
						attributesResponseSuccessJSON.put("responseText", "Operation succeded");
						attributesResponseSuccessJSON.put("id", id);
						writeBackToClient( new JSONSuccess(attributesResponseSuccessJSON) );
					}else{
						Integer perID = perDao.insertPeriodicity(per);
						logger.debug("New Resource inserted");
						JSONObject attributesResponseSuccessJSON = new JSONObject();
						attributesResponseSuccessJSON.put("success", true);
						attributesResponseSuccessJSON.put("responseText", "Operation succeded");
						attributesResponseSuccessJSON.put("id", perID);
						writeBackToClient( new JSONSuccess(attributesResponseSuccessJSON) );
					}

				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception occurred while saving new resource", e);
				}
								
			}else{
				logger.error("Resource name, code or type are missing");
				throw new SpagoBIServiceException(SERVICE_NAME,	"Please fill resource name, code and type");
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(PERIODICITY_DELETE)) {
			Integer id = getAttributeAsInteger(ID);
			try {
				perDao.deletePeriodicity(id);
				logger.debug("Resource deleted");
				writeBackToClient( new JSONAcknowledge("Operation succeded") );
			} catch (Throwable e) {
				logger.error("Exception occurred while retrieving resource to delete", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving resource to delete", e);
			}
		}
		logger.debug("OUT");
	}

	/**
	 * Creates a json array with children users informations
	 * 
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponsePeriodicities(JSONArray rows)
			throws JSONException {
		JSONObject results;

		results = new JSONObject();
		results.put("title", "Periodicities");
		results.put("rows", rows);
		return results;
	}

}
