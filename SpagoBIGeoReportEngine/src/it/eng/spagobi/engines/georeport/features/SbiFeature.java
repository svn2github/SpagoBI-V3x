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
package it.eng.spagobi.engines.georeport.features;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.mapfish.geo.MfFeature;
import org.mapfish.geo.MfGeometry;

/**
 * @authors Andrea Gioia (andrea.gioia@eng.it), Fabio D'Ovidio (f.dovidio@inovaos.it)
 */
public class SbiFeature extends MfFeature {
	
	private String id;
	private MfGeometry geometry;
	private JSONObject properties;
	
	SbiFeature(final String id, final MfGeometry geometry, final JSONObject properties) {
		this.id = id;
		this.geometry = geometry;
		this.properties = properties;
	}
	
	public String getFeatureId() {
		return id;
    }
    
	public MfGeometry getMfGeometry() {
		return geometry;
    }
    
	public void toJSON(JSONWriter builder) throws JSONException {
		Iterator iter = properties.keys();
		while (iter.hasNext()){
			String key = (String) iter.next();
            Object value = properties.get(key);
            builder.key(key).value(value);                    	  
        }
    }
	
	public JSONObject getProperties() {
		return properties;
	}
}
