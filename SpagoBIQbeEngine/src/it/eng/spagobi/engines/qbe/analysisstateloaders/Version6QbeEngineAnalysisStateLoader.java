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
package it.eng.spagobi.engines.qbe.analysisstateloaders;

import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 */
public class Version6QbeEngineAnalysisStateLoader extends AbstractQbeEngineAnalysisStateLoader{

	public final static String FROM_VERSION = "6";
    public final static String TO_VERSION = "7";
    
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(Version6QbeEngineAnalysisStateLoader.class);
    	
    public Version6QbeEngineAnalysisStateLoader() {
    	super();
    }
    
    public Version6QbeEngineAnalysisStateLoader(IQbeEngineAnalysisStateLoader loader) {
    	super(loader);
    }
    
	public JSONObject convert(JSONObject data) {
		logger.debug( "IN" );
		try {
			Assert.assertNotNull(data, "Data to convert cannot be null");
			
			logger.debug( "Converting from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] ..." );
			logger.debug( "Data to convert [" + data.toString() + "]");
			
			data.put("crosstabdefinition", new JSONObject());
			
			logger.debug( "Converted data [" + data.toString() + "]");
			logger.debug( "Conversion from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] terminated succesfully" );
		}catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible to load from rowData [" + data + "]", t);
		} finally {
			logger.debug( "OUT" );
		}
		return data;
	}
	
	private void convertQuery(JSONObject queryJSON) {
		JSONArray whereConditionsJSON;
		JSONArray havingConditionsJSON;
		String queryId = null;
		JSONArray subqueriesJSON;
		
		
		logger.debug("IN");
		
		try {
			Assert.assertNotNull(queryJSON, "Query to be converted cannot be null");
			queryId = queryJSON.getString("id");
			logger.debug( "Converting query [" + queryId + "] ...");
			logger.debug( "Query content to be converted [" + queryJSON.toString() + "]");
			
			// convert WHERE filters: values in IN, NOT IN, BETWEEN and NOT BETWEEN are separated by ","; they are converted into an array
			whereConditionsJSON = queryJSON.optJSONArray( "filters" );	
			if (whereConditionsJSON != null && whereConditionsJSON.length() > 0) {
				logger.debug( "Query [" + queryId + "] has [" + whereConditionsJSON.length() + "] WHERE filters to convert");
				for (int i = 0; i < whereConditionsJSON.length(); i++) {
					JSONObject aWhereConditionJSON = (JSONObject) whereConditionsJSON.get(i);
					convertFilter(aWhereConditionJSON);
				}
			}
			
			// convert HAVING filters: values in IN, NOT IN, BETWEEN and NOT BETWEEN are separated by ","; they are converted into an array
			havingConditionsJSON = queryJSON.optJSONArray( "havings" );	
			if (havingConditionsJSON != null && havingConditionsJSON.length() > 0) {
				logger.debug( "Query [" + queryId + "] has [" + havingConditionsJSON.length() + "] HAVING filters to convert");
				for (int i = 0; i < havingConditionsJSON.length(); i++) {
					JSONObject aHavingConditionsJSON = (JSONObject) havingConditionsJSON.get(i);
					convertFilter(aHavingConditionsJSON);
				}
			}
			
			// convert subqueries
			subqueriesJSON = queryJSON.getJSONArray( "subqueries" );
			logger.debug( "Query [" + queryId + "] have [" + subqueriesJSON.length() + "] subqueries to convert");
			for(int j = 0; j < subqueriesJSON.length(); j++) {
				logger.debug( "Converting subquery [" + (j+1)+ "] of query [" + queryId + "] ...");
				convertQuery( subqueriesJSON.getJSONObject(j) );
			}
			
			logger.debug( "Query [" + queryId + "] converted succesfully");
		}catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible convert query [" + queryId + "]", t);
		} finally {
			logger.debug( "OUT" );
		}
	}

	/**
	 * In previous version, when using IN, NOT IN, BETWEEN or NOT BETWEEN operators, the right operand values contained all values 
	 * joined by ","; now those values are converted into an array
	 * @param filterJSON
	 */
	private void convertFilter(JSONObject filterJSON) {
		logger.debug( "IN" );
		try {
			Assert.assertNotNull(filterJSON, "Filter to be converted cannot be null");
			JSONArray rightValuesJSON = new JSONArray();
			JSONArray rightLastValuesJSON = new JSONArray();
			JSONArray rightDefaultValuesJSON = new JSONArray();
			logger.debug("Converting filter : " + filterJSON.toString());
			String operator = filterJSON.getString("operator");
			String rightOperandType = filterJSON.getString(			"rightOperandType");
			String rightOperandValue = filterJSON.optString(		"rightOperandValue");
			String rightOperandLastValue = filterJSON.optString(	"rightOperandLastValue");
			String rightOperandDefaultValue = filterJSON.optString(	"rightOperandDefaultValue");
			logger.debug("Previous rigth operand : " + rightOperandValue);
			if ( "Static Value".equalsIgnoreCase(rightOperandType) && 
					( "IN".equalsIgnoreCase( operator ) 
					|| "NOT IN".equalsIgnoreCase( operator )
					|| "BETWEEN".equalsIgnoreCase( operator )
					|| "NOT BETWEEN".equalsIgnoreCase( operator ) )) {
				rightValuesJSON = 			splitValuesUsingComma(rightOperandValue, 		operator);
				rightLastValuesJSON = 		splitValuesUsingComma(rightOperandLastValue, 	operator);
				rightDefaultValuesJSON = 	splitValuesUsingComma(rightOperandDefaultValue, operator);
			} else {
				if (rightOperandValue != null) 			rightValuesJSON.put(		rightOperandValue);
				if (rightOperandLastValue != null) 		rightLastValuesJSON.put(	rightOperandLastValue);
				if (rightOperandDefaultValue != null) 	rightDefaultValuesJSON.put(	rightOperandDefaultValue);
			}
			logger.debug("New rigth operand : " + 					rightValuesJSON.toString());
			logger.debug("New rigth operand last values : " + 		rightLastValuesJSON.toString());
			logger.debug("New rigth operand default values : " + 	rightDefaultValuesJSON.toString());
			filterJSON.put("rightOperandValue", 		rightValuesJSON);
			filterJSON.put("rightOperandLastValue", 	rightLastValuesJSON);
			filterJSON.put("rightOperandDefaultValue", 	rightDefaultValuesJSON);
			logger.debug("Filter converted properly");
		} catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible convert filter " + (filterJSON == null ? "NULL" : filterJSON.toString()), t);
		} finally {
			logger.debug( "OUT" );
		}
	}
	
	private static JSONArray splitValuesUsingComma(String value, String operator) {
		logger.debug("IN: value = " + value + "; operator = " + operator);
		JSONArray toReturn = new JSONArray();
		if (value != null) {
			String[] values = null;
			// case BETWEEN or NOT BETWEEN using dates
			if ("BETWEEN".equalsIgnoreCase( operator ) || "NOT BETWEEN".equalsIgnoreCase( operator )) {
				
				if (value.contains("STR_TO_DATE") || value.contains("TO_TIMESTAMP")) {
					int firstComma = value.indexOf(',');
					int correctComma = value.indexOf(',', firstComma+1);
					String minValue = value.substring(0, correctComma-1);
					String maxValue = value.substring(correctComma+1, value.length());
					toReturn.put(minValue);
					toReturn.put(maxValue);
				} else {
					values = value.split(",");
					for (int i = 0; i < values.length ; i++) {
						toReturn.put(values[i]);
					}
				}
				
			} else if ((values = value.split(",")).length > 0) {
				for (int i = 0; i < values.length ; i++) {
					toReturn.put(values[i]);
				}
			}
		}
		logger.debug("OUT: new value = " + toReturn.toString());
		return toReturn;
	}
	

}
