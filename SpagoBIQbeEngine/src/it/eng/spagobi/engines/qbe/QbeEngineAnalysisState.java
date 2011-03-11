/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.qbe;

import it.eng.qbe.commons.serializer.SerializerFactory;
import it.eng.qbe.crosstab.exporter.CrosstabDefinition;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.catalogue.QueryCatalogue;
import it.eng.spagobi.engines.qbe.analysisstateloaders.IQbeEngineAnalysisStateLoader;
import it.eng.spagobi.engines.qbe.analysisstateloaders.QbeEngineAnalysisStateLoaderFactory;
import it.eng.spagobi.utilities.engines.EngineAnalysisState;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeEngineAnalysisState extends EngineAnalysisState {
	
	// property name
	public static final String CATALOGUE = "CATALOGUE";
	public static final String CROSSTAB_DEFINITION = "CROSSTAB_DEFINITION";
	public static final String DATASOURCE = "DATAMART_MODEL";
	
	public static final String CURRENT_VERSION = "7";
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineAnalysisState.class);
	
	
	
	public QbeEngineAnalysisState( IDataSource dataSource ) {
		super( );
		setDataSource( dataSource );
	}

	public void load(byte[] rowData) throws SpagoBIEngineException {
		String str = null;
		JSONObject abalysisStateJSON = null;
		JSONObject rowDataJSON = null;
		String encodingFormatVersion;
		
		logger.debug("IN");

		try {
			str = new String( rowData );
			logger.debug("loading analysis state from row data [" + str + "] ...");
			
			rowDataJSON = new JSONObject(str);
			try {
				encodingFormatVersion = rowDataJSON.getString("version");
			} catch (JSONException e) {
				encodingFormatVersion = "0";
			}
			
			logger.debug("Row data encoding version  [" + encodingFormatVersion + "]");
			
			
			
			if(encodingFormatVersion.equalsIgnoreCase(CURRENT_VERSION)) {				
				abalysisStateJSON = rowDataJSON;
			} else {
				logger.warn("Row data encoding version [" + encodingFormatVersion + "] does not match with the current version used by the engine [" + CURRENT_VERSION + "] ");
				logger.debug("Converting from encoding version [" + encodingFormatVersion + "] to encoding version [" + CURRENT_VERSION + "]....");
				IQbeEngineAnalysisStateLoader analysisStateLoader;
				analysisStateLoader = QbeEngineAnalysisStateLoaderFactory.getInstance().getLoader(encodingFormatVersion);
				if(analysisStateLoader == null) {
					throw new SpagoBIEngineException("Unable to load data stored in format [" + encodingFormatVersion + "] ");
				}
				abalysisStateJSON = (JSONObject)analysisStateLoader.load(str);
				logger.debug("Encoding conversion has been executed succesfully");
			}
			
			JSONObject catalogueJSON = abalysisStateJSON.getJSONObject("catalogue");
			JSONObject crosstabDefinitionJSON = abalysisStateJSON.getJSONObject("crosstabdefinition");
			setProperty( CATALOGUE,  catalogueJSON);
			setProperty( CROSSTAB_DEFINITION,  crosstabDefinitionJSON);
			logger.debug("analysis state loaded succsfully from row data");
		} catch (JSONException e) {
			throw new SpagoBIEngineException("Impossible to load analysis state from raw data", e);
		} finally {
			logger.debug("OUT");
		}
	}

	public byte[] store() throws SpagoBIEngineException {
		JSONObject catalogueJSON = null;
		JSONObject crosstabDefinitionJSON = null;
		JSONObject rowDataJSON = null;
		String rowData = null;	
		
		catalogueJSON = (JSONObject)getProperty( CATALOGUE );
		crosstabDefinitionJSON = (JSONObject)getProperty( CROSSTAB_DEFINITION );
		
		try {
			rowDataJSON = new JSONObject();
			rowDataJSON.put("version", CURRENT_VERSION);
			rowDataJSON.put("catalogue", catalogueJSON);
			rowDataJSON.put("crosstabdefinition", crosstabDefinitionJSON);
			
			rowData = rowDataJSON.toString();
		} catch (Throwable e) {
			throw new SpagoBIEngineException("Impossible to store analysis state from catalogue object", e);
		}
		
		return rowData.getBytes();
	}

	public QueryCatalogue getCatalogue() {
		QueryCatalogue catalogue;
		JSONObject catalogueJSON;
		JSONArray queriesJSON;
		JSONObject queryJSON;
		Query query;
		
		catalogue = new QueryCatalogue();
		catalogueJSON = (JSONObject)getProperty( CATALOGUE );
		try {
			queriesJSON = catalogueJSON.getJSONArray("queries");
		
			for(int i = 0; i < queriesJSON.length(); i++) {
				queryJSON = queriesJSON.getJSONObject(i);
				query = SerializerFactory.getDeserializer("application/json").deserializeQuery(queryJSON, getDataSource());
								
				catalogue.addQuery(query);
			}
		} catch (Throwable e) {
			throw new SpagoBIEngineRuntimeException("Impossible to deserialize catalogue", e);
		}
		
		return catalogue;
	}

	public void setCatalogue(QueryCatalogue catalogue) {
		Set queries;
		Query query;
		JSONObject queryJSON;
		JSONArray queriesJSON;
		JSONObject catalogueJSON;
		
		catalogueJSON = new JSONObject();
		queriesJSON = new JSONArray();
		
		try {
			queries = catalogue.getAllQueries(false);
			Iterator it = queries.iterator();
			while(it.hasNext()) {
				query = (Query)it.next();
				queryJSON =  (JSONObject)SerializerFactory.getSerializer("application/json").serialize(query, getDataSource(), null);
				queriesJSON.put( queryJSON );
			}
			
			catalogueJSON.put("queries", queriesJSON);
		} catch (Throwable e) {
			throw new SpagoBIEngineRuntimeException("Impossible to serialize catalogue", e);
		}
		
		setProperty( CATALOGUE, catalogueJSON );
	}

	public IDataSource getDataSource() {
		return (IDataSource)getProperty( DATASOURCE );
	}

	public void setDataSource(IDataSource dataSource) {
		setProperty( DATASOURCE, dataSource );
	}

	public void setCrosstabDefinition(CrosstabDefinition crosstabDefinition) {
		JSONObject crosstabDefinitionJSON = null;
		try {
			crosstabDefinitionJSON = (JSONObject) SerializerFactory.getSerializer("application/json").serialize(crosstabDefinition);
		} catch (Throwable e) {
			throw new SpagoBIEngineRuntimeException("Impossible to serialize crosstab definition", e);
		}
		setProperty( CROSSTAB_DEFINITION, crosstabDefinitionJSON );
	}
	

	public CrosstabDefinition getCrosstabDefinition() {
		CrosstabDefinition crosstabDefinition;
		JSONObject crosstabDefinitionJSON;
		
		crosstabDefinitionJSON = (JSONObject)getProperty( CROSSTAB_DEFINITION );
		try {
			crosstabDefinition = SerializerFactory.getDeserializer("application/json").deserializeCrosstabDefinition(crosstabDefinitionJSON);
		} catch (Throwable e) {
			throw new SpagoBIEngineRuntimeException("Impossible to deserialize crosstab definition", e);
		}
		
		return crosstabDefinition;
		
	}
	
}
