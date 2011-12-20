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
package it.eng.spagobi.engines.worksheet.serializer;

import it.eng.qbe.serializer.IDeserializer;
import it.eng.qbe.serializer.IDeserializerFactory;
import it.eng.qbe.serializer.SerializationManager;
import it.eng.spagobi.engines.worksheet.bo.WorkSheetDefinition;
import it.eng.spagobi.engines.worksheet.serializer.json.WorkSheetJSONDeserializer;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class WorkSheetDeserializerFactory implements IDeserializerFactory {
	
	static WorkSheetDeserializerFactory instance;
	
	static WorkSheetDeserializerFactory getIntsnce() {
		return instance;
	}
	
	static {
		instance = new WorkSheetDeserializerFactory();
		SerializationManager.registerDeserializerFactory(WorkSheetDefinition.class, instance);
		
	}
	
	public static WorkSheetDeserializerFactory getInstance() {
		if (instance == null) {
			instance = new WorkSheetDeserializerFactory();
		}
		return instance;
	}
	
	private WorkSheetDeserializerFactory() {}

	public IDeserializer getDeserializer(String mimeType) {
		if (mimeType != null && !mimeType.equalsIgnoreCase("application/json")) {
			throw new SpagoBIRuntimeException("Deserializer for mimeType " + mimeType + " not implemented");
		}
		return new WorkSheetJSONDeserializer();
	}

}
