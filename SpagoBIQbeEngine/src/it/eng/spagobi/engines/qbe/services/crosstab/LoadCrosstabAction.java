/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.qbe.services.crosstab;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.query.Query;
import it.eng.qbe.serializer.SerializationManager;
import it.eng.qbe.statement.IStatement;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.QbeEngineStaticVariables;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.engines.qbe.QbeEngineConfig;
import it.eng.spagobi.engines.qbe.crosstable.CrossTab;
import it.eng.spagobi.engines.qbe.crosstable.CrosstabDefinition;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.engines.qbe.services.formviewer.ExecuteMasterQueryAction;
import it.eng.spagobi.engines.qbe.utils.crosstab.CrosstabQueryCreator;
import it.eng.spagobi.engines.qbe.utils.temporarytable.TemporaryTableManager;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class LoadCrosstabAction extends AbstractQbeEngineAction {	
	
	// INPUT PARAMETERS


	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(LoadCrosstabAction.class);
    public static transient Logger auditlogger = Logger.getLogger("audit.query");
    
	
	public void service(SourceBean request, SourceBean response)  {				
				
		IDataStore dataStore = null;
		
		Query query = null;
		IStatement statement = null;
				
		JSONObject jsonFormState=null;
		
		Integer maxSize = null;
		Integer resultNumber = null;
		CrosstabDefinition crosstabDefinition = null;
		
		Monitor totalTimeMonitor = null;
		Monitor errorHitsMonitor = null;
					
		logger.debug("IN");
		
		try {
		
			super.service(request, response);	
			
			totalTimeMonitor = MonitorFactory.start("QbeEngine.executeCrosstabQueryAction.totalTime");
			
			JSONObject crosstabDefinitionJSON = getAttributeAsJSONObject( QbeEngineStaticVariables.CROSSTAB_DEFINITION );
			jsonFormState = loadSmartFilterFormValues();
			logger.debug("Form state retrieved as a string: " + jsonFormState);
			
			Assert.assertNotNull(crosstabDefinitionJSON, "Parameter [" + QbeEngineStaticVariables.CROSSTAB_DEFINITION + "] cannot be null in oder to execute " + this.getActionName() + " service");
			logger.debug("Parameter [" + crosstabDefinitionJSON + "] is equals to [" + crosstabDefinitionJSON.toString() + "]");
			//crosstabDefinition = SerializerFactory.getDeserializer("application/json").deserializeCrosstabDefinition(crosstabDefinitionJSON);;
			crosstabDefinition = (CrosstabDefinition)SerializationManager.deserialize(crosstabDefinitionJSON, "application/json", CrosstabDefinition.class);
			crosstabDefinition.setCellLimit( new Integer((String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-CROSSTAB-CELLS-LIMIT.value")) );
			maxSize = QbeEngineConfig.getInstance().getResultLimit();			
			logger.debug("Configuration setting  [" + "QBE.QBE-SQL-RESULT-LIMIT.value" + "] is equals to [" + (maxSize != null? maxSize: "none") + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			
			// retrieving first QbE query and setting it as active query
			query = getEngineInstance().getQueryCatalogue().getFirstQuery();
			
			//build the query filtered for the smart filter
			if (jsonFormState != null) {
				query = getFilteredQuery(query, jsonFormState);
			}
			
			getEngineInstance().setActiveQuery(query);
			
			statement = getEngineInstance().getStatment();	
			statement.setParameters( getEnv() );
			

			String sqlQuery = statement.getSqlQueryString();
			UserProfile userProfile = (UserProfile)getEnv().get(EngineConstants.ENV_USER_PROFILE);
			
			ConnectionDescriptor connection = (ConnectionDescriptor)getDataSource().getConfiguration().loadDataSourceProperties().get("connection");
			DataSource dataSource = getDataSource(connection);
			
			String sqlStatement = buildSqlStatement(crosstabDefinition, query, sqlQuery, statement);
			logger.debug("Querying temporary table: user [" + userProfile.getUserId() + "] (SQL): [" + sqlStatement + "]");
			
			if (!TemporaryTableManager.isEnabled()) {
				logger.warn("TEMPORARY TABLE STRATEGY IS DISABLED!!! " +
						"Using inline view construct, therefore performance will be very low");
				int beginIndex = sqlStatement.toUpperCase().indexOf(" FROM ") + " FROM ".length(); 
				int endIndex = sqlStatement.indexOf(" ", beginIndex);
				String inlineSQLQuery = sqlStatement.substring(0, beginIndex) + " ( " + sqlQuery + " ) TEMP " + sqlStatement.substring(endIndex);
				logger.debug("Executable query for user [" + userProfile.getUserId() + "] (SQL): [" + inlineSQLQuery + "]");
				auditlogger.info("[" + userProfile.getUserId() + "]:: SQL: " + inlineSQLQuery);
				JDBCDataSet dataSet = new JDBCDataSet();
				dataSet.setDataSource(dataSource);
				dataSet.setQuery(inlineSQLQuery);
				dataSet.loadData();
				dataStore = (DataStore) dataSet.getDataStore();
			} else {
				logger.debug("Using temporary table strategy....");
		
				logger.debug("Temporary table definition for user [" + userProfile.getUserId() + "] (SQL): [" + sqlQuery + "]");
		
				auditlogger.info("Temporary table definition for user [" + userProfile.getUserId() + "]:: SQL: " + sqlQuery);
				auditlogger.info("Querying temporary table: user [" + userProfile.getUserId() + "] (SQL): [" + sqlStatement + "]");

				try {
					dataStore = TemporaryTableManager.queryTemporaryTable(userProfile, sqlStatement, sqlQuery, dataSource, null, null);
				} catch (Exception e) {
					logger.debug("Query execution aborted because of an internal exception");
					String message = "An error occurred in " + getActionName() + " service while querying temporary table";				
					SpagoBIEngineServiceException exception = new SpagoBIEngineServiceException(getActionName(), message, e);
					exception.addHint("Check if the base query is properly formed: [" + statement.getQueryString() + "]");
					exception.addHint("Check if the crosstab's query is properly formed: [" + sqlStatement + "]");
					exception.addHint("Check connection configuration: connection's user must have DROP and CREATE privileges");
					
					throw exception;
				}
			}

			Assert.assertNotNull(dataStore, "The dataStore cannot be null");
			logger.debug("Query executed succesfully");
			
			resultNumber = (Integer)dataStore.getMetaData().getProperty("resultNumber");
			Assert.assertNotNull(resultNumber, "property [resultNumber] of the dataStore returned by queryTemporaryTable method of the class [" + TemporaryTableManager.class.getName()+ "] cannot be null");
			logger.debug("Total records: " + resultNumber);			
			
			
			boolean overflow = maxSize != null && resultNumber >= maxSize;
			if (overflow) {
				logger.warn("Query results number [" + resultNumber + "] exceeds max result limit that is [" + maxSize + "]");
				auditlogger.info("[" + userProfile.getUserId() + "]:: max result limit [" + maxSize + "] exceeded with SQL: " + sqlQuery);
			}
			
			CrossTab crossTab = new CrossTab(dataStore, crosstabDefinition);
			JSONObject crossTabDefinition = crossTab.getJSONCrossTab();
			
			try {
				writeBackToClient( new JSONSuccess(crossTabDefinition) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			errorHitsMonitor = MonitorFactory.start("QbeEngine.errorHits");
			errorHitsMonitor.stop();
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			if (totalTimeMonitor != null) totalTimeMonitor.stop();
			logger.debug("OUT");
		}	
	}

	/**
	 * Loads the values of the form if the calling engine is smart filter
	 * @return
	 * @throws JSONException
	 */
	protected JSONObject loadSmartFilterFormValues() throws JSONException{
		String jsonEncodedFormState = getAttributeAsString( ExecuteMasterQueryAction.FORM_STATE );
		if(jsonEncodedFormState!=null){
			return new JSONObject(jsonEncodedFormState);
		}
		return null;
	}
	
	/**
	 * Build the sql statement for the temporary table 
	 * @param crosstabDefinition definition of the crosstab
	 * @param baseQuery base query
	 * @param sqlQuery the sql rappresentation of the base query
	 * @param stmt the qbe statement
	 * @return
	 * @throws JSONException
	 */
	protected String buildSqlStatement(CrosstabDefinition crosstabDefinition, Query baseQuery, String sqlQuery, IStatement stmt) throws JSONException{
		return CrosstabQueryCreator.getCrosstabQuery(crosstabDefinition, baseQuery, null, sqlQuery, stmt);
	}
	
}
