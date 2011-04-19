/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.chiron.serializer;

import java.util.Locale;

import org.json.JSONObject;

import it.eng.spagobi.commons.bo.Config;

/**
 * @author Monia Spinelli (monia.spinelli@eng.it)
 */
public class ConfigJSONSerializer implements Serializer {
	
	public static final String CONFIG_CODE = "CONFIG_CD";
	public static final String CONFIG_NAME = "CONFIG_NM";
	
	public static final String VALUE_ID = "VALUE_ID";
	public static final String VALUE_CODE = "VALUE_CD";
	public static final String VALUE_NAME = "VALUE_NM";
	public static final String VALUE_DECRIPTION = "VALUE_DS";
	public static final String VALUE_CHECK = "VALUE_CHECK";
	public static final String VALUE_TYPE = "VALUE_TYPE";
	
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Config) ) {
			throw new SerializationException("ConfigJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Config config = (Config)o;
			result = new JSONObject();
			result.put(CONFIG_CODE, config.getLabel()); // BIOBJ_TYPE
			result.put(CONFIG_NAME, config.getName()); // BI Object types
			
			result.put(VALUE_ID, config.getId()); // ex. 1
			result.put(VALUE_CODE, config.getLabel()); // REPORT
			result.put(VALUE_NAME, config.getName()); // ex. Report
			result.put(VALUE_DECRIPTION, config.getDescription()); // Basic business intelligence objects type
			result.put(VALUE_CHECK, config.getValueCheck());
			result.put(VALUE_TYPE, config.getValueTypeId());
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}

}
