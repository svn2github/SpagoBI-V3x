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
package it.eng.spagobi.commons.serializer;

import it.eng.spagobi.kpi.model.bo.Resource;

import java.util.Locale;
import org.json.JSONObject;

public class ResourceJSONSerializer implements Serializer {

	public static final String RESOURCE_ID = "id";
	private static final String RESOURCE_NAME = "name";
	private static final String RESOURCE_DESCRIPTION = "description";
	private static final String RESOURCE_CODE = "code";
	private static final String RESOURCE_COL_NAME = "columnname";
	private static final String RESOURCE_TAB_NAME = "tablename";
	private static final String RESOURCE_TYPE_ID = "typeId";
	private static final String RESOURCE_TYPE_CD = "typeCd";
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Resource) ) {
			throw new SerializationException("ResourceJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Resource res = (Resource)o;
			result = new JSONObject();
			
			result.put(RESOURCE_ID, res.getId() );
			result.put(RESOURCE_NAME, res.getName() );
			result.put(RESOURCE_DESCRIPTION, res.getDescr() );
			result.put(RESOURCE_CODE, res.getCode() );
			result.put(RESOURCE_COL_NAME, res.getColumn_name() );
			result.put(RESOURCE_TAB_NAME, res.getTable_name() );
			result.put(RESOURCE_TYPE_ID, res.getTypeId() );
			result.put(RESOURCE_TYPE_CD, res.getType());			
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}

}
