/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
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
package it.eng.spagobi.engines.worksheet.serializer.json;

import it.eng.qbe.serializer.IDeserializer;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.engines.worksheet.bo.Attribute;
import it.eng.spagobi.engines.worksheet.bo.Filter;
import it.eng.spagobi.utilities.assertion.Assert;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class FilterJSONDeserializer extends AttributeJSONDeserializer implements IDeserializer {

    public static transient Logger logger = Logger.getLogger(FilterJSONDeserializer.class);

	@Override
	public Filter deserialize(Object o) throws SerializationException {
		Filter toReturn = null;
		JSONObject filterJSON = null;
		
		logger.debug("IN");
		
		try {
			Assert.assertNotNull(o, "Object to be deserialized cannot be null");
			
			if(o instanceof String) {
				logger.debug("Deserializing string [" + (String)o + "]");
				try {
					filterJSON = new JSONObject( (String)o );
				} catch(Throwable t) {
					logger.debug("Object to be deserialized must be string encoding a JSON object");
					throw new SerializationException("An error occurred while deserializing query: " + (String)o, t);
				}
			} else if(o instanceof JSONObject) {
				filterJSON = (JSONObject)o;
			} else {
				Assert.assertUnreachable("Object to be deserialized must be of type string or of type JSONObject, not of type [" + o.getClass().getName() + "]");
			}
			
			try {
				toReturn = deserializeFilter(filterJSON);
			} catch (Exception e) {
				throw new SerializationException("An error occurred while deserializing serie: " + filterJSON.toString(), e);
			}

		} finally {
			logger.debug("OUT");
		}
		logger.debug("Measure deserialized");
		return toReturn;
	}
	
	private Filter deserializeFilter(JSONObject obj) throws JSONException, SerializationException {
		Attribute a = super.deserialize(obj);
		String mandatoryString = obj.optString(FieldsSerializationConstants.MANDATORY);
		String selectionString = obj.optString(FieldsSerializationConstants.SELECTION);
		String splittingFilter = obj.optString(FieldsSerializationConstants.SPLITTING_FILTER);
				
		boolean mandatory = (mandatoryString.equals("")) && (mandatoryString!=null) && (mandatoryString.equals("yes"));
		boolean multivalue = (mandatoryString.equals("")) || (selectionString==null) || (selectionString.equals("multivalue"));
		
		Filter f = new Filter(a.getEntityId(), a.getAlias(), a.getIconCls(), a.getNature(), a.getValues(), mandatory, multivalue);
		
		if(splittingFilter!=null && splittingFilter.equals("on")){
			f.setSplittingFilter(true);
		}

		return f;
	}

	
		
}
