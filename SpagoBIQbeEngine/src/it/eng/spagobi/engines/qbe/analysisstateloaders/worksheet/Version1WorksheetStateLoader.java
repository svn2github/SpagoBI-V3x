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
package it.eng.spagobi.engines.qbe.analysisstateloaders.worksheet;

import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Version1WorksheetStateLoader extends AbstractWorksheetStateLoader {

	public final static String FROM_VERSION = "1";
    public final static String TO_VERSION = "2";
    
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(Version1WorksheetStateLoader.class);
	
    public Version1WorksheetStateLoader() {
    	super();
    }
    
    public Version1WorksheetStateLoader(IWorksheetStateLoader loader) {
    	super(loader);
    }
    
	@Override
	public JSONObject convert(JSONObject data) {
		logger.debug("IN");
		
		try {
			logger.debug( "Converting from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] ..." );
			
			putOptions(data);

			logger.debug( "Conversion from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] terminated succesfully" );
			
		} catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible to load from rowData [" + data + "]", t);
		} finally {
			logger.debug("OUT");
		}
		
		return data;
	}

	private void putOptions(JSONObject data) throws Exception {
		JSONArray options = new JSONArray();
		data.put("fieldsOptions", options);
	}

}
