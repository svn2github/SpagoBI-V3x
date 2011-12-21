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

import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;

import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @author Chiarelli Chiara
 */
public class MetadataJSONSerializer implements Serializer {
	
	private static Logger logger = Logger.getLogger(MetadataJSONSerializer.class);
	
	public static final String METADATA_ID = "meta_id";
	public static final String BIOBJECT_ID = "biobject_id";
	public static final String SUBOBJECT_ID = "subobject_id";
	public static final String NAME = "meta_name";
	public static final String TYPE = "meta_type";
	public static final String TEXT = "meta_content";
	public static final String CREATION_DATE = "meta_creation_date";
	public static final String CHANGE_DATE = "meta_change_date";
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		logger.debug("IN");
		JSONObject result = new JSONObject();

		if ( !(o instanceof ObjMetaDataAndContent) ) {
			throw new SerializationException("MetadataJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			ObjMetaDataAndContent both = (ObjMetaDataAndContent)o;
			ObjMetadata meta = both.getMeta();
			ObjMetacontent content = both.getMetacontent();

			result.put(METADATA_ID, meta.getObjMetaId());
			result.put(NAME, meta.getName());
			result.put(TYPE, meta.getDataTypeCode());
			
			if (content != null) {
				String contentText = new String(content.getContent(),"ISO-8859-1");
				result.put(BIOBJECT_ID, content.getBiobjId());
				result.put(SUBOBJECT_ID, content.getSubobjId() != null ? content.getSubobjId() : -1);
				result.put(TEXT,contentText );
				result.put(CREATION_DATE, content.getCreationDate());
				result.put(CHANGE_DATE, content.getLastChangeDate());
			} else {
				result.put(BIOBJECT_ID, -1);
				result.put(SUBOBJECT_ID, -1);
				result.put(TEXT, "");
				result.put(CREATION_DATE, "");
				result.put(CHANGE_DATE, "");
			}
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			logger.debug("OUT");
		}
		return result;
	}

}
