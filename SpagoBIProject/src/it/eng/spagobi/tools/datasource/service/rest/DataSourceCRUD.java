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
package it.eng.spagobi.tools.datasource.service.rest;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.serializer.SerializationException;
import it.eng.spagobi.commons.serializer.SerializerFactory;
import it.eng.spagobi.commons.utilities.AuditLogUtilities;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.tools.datasource.dao.IDataSourceDAO;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONFailure;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 * 
 */
@Path("/datasources")
public class DataSourceCRUD {

	static private Logger logger = Logger.getLogger(DataSourceCRUD.class);
	static private String deleteNullIdDataSourceError = "error.mesage.description.data.source.cannot.be.null";
	static private String deleteInUseDSError = "error.mesage.description.data.source.deleting.inuse";
	static private String canNotFillResponseError = "error.mesage.description.generic.can.not.responce";
	static private String saveDuplicatedDSError = "error.mesage.description.data.source.saving.duplicated";

	@GET
	@Path("/listall")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDataSources(@Context HttpServletRequest req) {
		IDataSourceDAO dataSourceDao = null;
		IDomainDAO domaindao = null;
		List<DataSource> dataSources;
		List<Domain> dialects = null;
		IEngUserProfile profile = (IEngUserProfile) req.getSession()
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		JSONObject datasorcesJSON = new JSONObject();
		try {
			dataSourceDao = DAOFactory.getDataSourceDAO();
			dataSourceDao.setUserProfile(profile);
			dataSources = dataSourceDao.loadAllDataSources();

			domaindao = DAOFactory.getDomainDAO();
			dialects = domaindao.loadListDomainsByType("DIALECT_HIB");

			datasorcesJSON = serializeDatasources(dataSources, dialects);

		} catch (Throwable t) {
			throw new SpagoBIServiceException(
					"An unexpected error occured while instatiating the dao", t);
		}
		return datasorcesJSON.toString();

	}


	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteDataSource(@Context HttpServletRequest req) {
		IEngUserProfile profile = (IEngUserProfile) req.getSession()
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		HashMap<String, String> logParam = new HashMap();

		try {
			String id = (String) req.getParameter("DATASOURCE_ID");
			Assert.assertNotNull(id,deleteNullIdDataSourceError );
			// if the ds is associated with any BIEngine or BIObjects, creates
			// an error
			boolean bObjects = DAOFactory.getDataSourceDAO().hasBIObjAssociated(id);
			boolean bEngines = DAOFactory.getDataSourceDAO().hasBIEngineAssociated(id);
			if (bObjects || bEngines) {
				HashMap params = new HashMap();
				logger.debug(deleteInUseDSError);
				updateAudit(req, profile, "DATA_SOURCE.DELETE", null, "ERR");
				return (serializeException(deleteInUseDSError,null));
			}

			IDataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByID(new Integer(id));
			DAOFactory.getDataSourceDAO().eraseDataSource(ds);
			logParam.put("TYPE", ds.getJndi());
			logParam.put("NAME", ds.getLabel());
			updateAudit(req, profile, "DATA_SOURCE.DELETE", null, "OK");
			return (new JSONAcknowledge()).toString();
		} catch (Exception ex) {
			logger.error("Cannot fill response container", ex);
			updateAudit(req, profile, "DATA_SOURCE.DELETE", null, "ERR");
			logger.debug(canNotFillResponseError);
			try {
				return (serializeException(canNotFillResponseError,null));
			} catch (Exception e) {
				logger.debug("Cannot fill response container.");
				throw new SpagoBIRuntimeException(
						"Cannot fill response container", e);
			}
		}
	}
	
	@POST
	@Path("/save")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveDataSource(@Context HttpServletRequest req) {
		IEngUserProfile profile = (IEngUserProfile) req.getSession().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		try {
			IDataSourceDAO dao=DAOFactory.getDataSourceDAO();
			dao.setUserProfile(profile);
			DataSource dsNew = recoverDataSourceDetails(req);

			HashMap<String, String> logParam = new HashMap();
			logParam.put("JNDI",dsNew.getJndi());
			logParam.put("NAME",dsNew.getLabel());
			logParam.put("URL",dsNew.getUrlConnection());



			if (dsNew.getDsId()==-1) {
				//if a ds with the same label not exists on db ok else error
				if (DAOFactory.getDataSourceDAO().loadDataSourceByLabel(dsNew.getLabel()) != null){
					updateAudit(req, profile, "DATA_SOURCE.ADD", logParam, "KO");
					throw new SpagoBIRuntimeException(saveDuplicatedDSError);
				}	 		
				dao.insertDataSource(dsNew);
							
				IDataSource tmpDS = dao.loadDataSourceByLabel(dsNew.getLabel());
				dsNew.setDsId(tmpDS.getDsId());
				updateAudit(req, profile, "DATA_SOURCE.ADD", logParam, "OK");
			} else {				
				//update ds
				dao.modifyDataSource(dsNew);
				updateAudit(req, profile, "DATA_SOURCE.MODIFY", logParam, "OK");
			}  
					
			return (new JSONAcknowledge()).toString();
		} catch (SpagoBIRuntimeException ex) {
			logger.error("Cannot fill response container", ex);
			updateAudit(req, profile, "DATA_SOURCE.DELETE", null, "ERR");
			logger.debug(ex.getMessage());
			try {
				return (serializeException(ex.getMessage(),null));
			} catch (Exception e) {
				logger.debug("Cannot fill response container.");
				throw new SpagoBIRuntimeException(
						"Cannot fill response container", e);
			}
		} catch (Exception ex) {
			logger.error("Cannot fill response container", ex);
			updateAudit(req, profile, "DATA_SOURCE.DELETE", null, "ERR");
			logger.debug(canNotFillResponseError);
			try {
				return (serializeException(canNotFillResponseError,null));
			} catch (Exception e) {
				logger.debug("Cannot fill response container.");
				throw new SpagoBIRuntimeException(
						"Cannot fill response container", e);
			}
		}
	}


	private static void updateAudit(HttpServletRequest request,
			IEngUserProfile profile, String action_code,
			HashMap<String, String> parameters, String esito) {
		try {
			AuditLogUtilities.updateAudit(request, profile, action_code,
					parameters, esito);
		} catch (Exception e) {
			logger.debug("Error writnig audit", e);
		}
	}
	
	private JSONObject serializeDatasources(List<DataSource> dataSources,
			List<Domain> dialects) throws SerializationException, JSONException {
		JSONObject dataSourcesJSON = new JSONObject();
		// JSONObject aDataSourcesJSON = new JSONObject();
		JSONArray dataSourcesJSONArray = new JSONArray();
		JSONArray dialectsJSONArray = new JSONArray();
		if (dataSources != null) {
			dataSourcesJSONArray = (JSONArray) SerializerFactory.getSerializer(
					"application/json").serialize(dataSources, null);
			dataSourcesJSON.put("root", dataSourcesJSONArray);
			// aDataSourcesJSON = dataSourcesJSONArray.getJSONObject(0);
			// Iterator<String> iter = aDataSourcesJSON.keys();
		}
		if (dialects != null) {
			dialectsJSONArray = (JSONArray) SerializerFactory.getSerializer(
					"application/json").serialize(dialects, null);
			dataSourcesJSON.put("dialects", dialectsJSONArray);
		}
		return dataSourcesJSON;
	}
	
	private DataSource recoverDataSourceDetails (HttpServletRequest req) throws EMFUserError, SourceBeanException, IOException  {
		DataSource ds  = new DataSource();
		Integer id=-1;
		String idStr = (String)req.getParameter("DATASOURCE_ID");
		if(idStr!=null && !idStr.equals("")){
			id = new Integer(idStr);
		}
		Integer dialectId = Integer.valueOf((String)req.getParameter("DIALECT_ID"));	
		String description = (String)req.getParameter("DESCRIPTION");	
		String label = (String)req.getParameter("DATASOURCE_LABEL");
		String jndi = (String)req.getParameter("JNDI_URL");
		String url = (String)req.getParameter("CONNECTION_URL");
		String user = (String)req.getParameter("USER");
		String pwd = (String)req.getParameter("PASSWORD");
		String driver = (String)req.getParameter("DRIVER");
		String schemaAttr = (String)req.getParameter("CONNECTION_URL");
		String multiSchema = (String)req.getParameter("MULTISCHEMA");
		Boolean isMultiSchema = false;
		if(multiSchema!=null && multiSchema.equals("true")){
			isMultiSchema = true;
		}
		
		ds.setDsId(id.intValue());
		ds.setDialectId(dialectId);
		ds.setLabel(label);
		ds.setDescr(description);
		ds.setJndi(jndi);
		ds.setUrlConnection(url);
		ds.setUser(user);
		ds.setPwd(pwd);
		ds.setDriver(driver);
		ds.setSchemaAttribute(schemaAttr);
		ds.setMultiSchema(isMultiSchema);
				
		return ds;
	}
	
	private String serializeException(String message, String localizedMessage) throws JSONException{
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONObject je = new JSONObject();
		if(message != null){
			jo.put("message", message);
		}
		if(localizedMessage != null){
			jo.put("localizedMessage", localizedMessage);
		}
		ja.put(jo);
		je.put("errors", ja);
		return je.toString();
	}
	
	private String serializeException(Exception e) throws JSONException{
		return serializeException(e.getMessage(),null);
	}


}
