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

import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class SnapshotJSONSerializer implements Serializer {
	
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String OWNER = "owner";
	public static final String CREATION_DATE = "creationDate";
	
	// dates are sent to the client using a fixed format, the one returned by GeneralUtilities.getServerDateFormat()
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat( GeneralUtilities.getServerTimeStampFormat() );

	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Snapshot) ) {
			throw new SerializationException("SnapshotJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Snapshot snapshot = (Snapshot)o;
			result = new JSONObject();
			result.put(ID, snapshot.getId() );
			result.put(NAME, snapshot.getName() );
			result.put(DESCRIPTION, snapshot.getDescription() );			
			result.put(CREATION_DATE, DATE_FORMATTER.format(  snapshot.getDateCreation() ) );
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;
	}
	
}
