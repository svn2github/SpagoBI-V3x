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
package it.eng.spagobi.engines.worksheet.serializer.json;

import it.eng.qbe.serializer.ISerializer;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.engines.worksheet.bo.Serie;
import it.eng.spagobi.utilities.assertion.Assert;

import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 */
public class SerieJSONSerializer extends MeasureJSONSerializer implements ISerializer {

    public static transient Logger logger = Logger.getLogger(SerieJSONSerializer.class);

	@Override
	public Object serialize(Object o) throws SerializationException {
		JSONObject toReturn = null;
		Serie serie;
				
		Assert.assertNotNull(o, "Input parameter cannot be null");
		Assert.assertTrue(o instanceof Serie, "Unable to serialize objects of type [" + o.getClass().getName() + "]");
		
		try {
			serie = (Serie) o;
			
			toReturn = (JSONObject) super.serialize(o);
			
			toReturn.put(FieldsSerializationConstants.SERIENAME, serie.getSerieName());
			toReturn.put(FieldsSerializationConstants.COLOR, serie.getColor());
			toReturn.put(FieldsSerializationConstants.SHOWCOMMA, serie.getShowComma());
			toReturn.put(FieldsSerializationConstants.PRECISION, serie.getPrecision());
			toReturn.put(FieldsSerializationConstants.SUFFIX, serie.getSuffix());

		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return toReturn;
	}
    
		
}
