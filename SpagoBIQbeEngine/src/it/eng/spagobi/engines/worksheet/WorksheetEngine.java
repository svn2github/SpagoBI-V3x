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
package it.eng.spagobi.engines.worksheet;

import it.eng.spagobi.engines.qbe.QbeEngine;
import it.eng.spagobi.engines.qbe.crosstable.serializer.CrosstabDeserializerFactory;
import it.eng.spagobi.engines.qbe.crosstable.serializer.CrosstabSerializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.AttributeDeserializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.AttributeSerializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.MeasureDeserializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.MeasureSerializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.WorkSheetDeserializerFactory;
import it.eng.spagobi.engines.worksheet.serializer.WorkSheetSerializerFactory;
import it.eng.spagobi.tools.dataset.bo.IDataSet;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class WorksheetEngine {
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeEngine.class);

	public static WorksheetEngineInstance createInstance(Object object, Map env) throws WorksheetEngineException {
		WorksheetEngineInstance worksheetEngineInstance = null;
		initDeserializers();
		initSerializers();
		logger.debug("IN");
		if(object instanceof IDataSet){
			worksheetEngineInstance = new WorksheetEngineInstance((IDataSet)object, env, true);
		}else{
			worksheetEngineInstance = new WorksheetEngineInstance(object, env);
		}
		
		logger.debug("OUT");

		return worksheetEngineInstance;
	}
	
	private static void initDeserializers() {
    	WorkSheetDeserializerFactory.getInstance();
    	CrosstabDeserializerFactory.getInstance();
    	AttributeDeserializerFactory.getInstance();
    	MeasureDeserializerFactory.getInstance();
	}


	private static void initSerializers() {
    	WorkSheetSerializerFactory.getInstance();
    	CrosstabSerializerFactory.getInstance();
    	AttributeSerializerFactory.getInstance();
    	MeasureSerializerFactory.getInstance();
	}

}
