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

import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.kpi.goal.metadata.bo.Goal;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnitGrant;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

public class GoalJSONSerializer  implements Serializer {
	
	public static final String ID = "id";
	public static final String LABEL = "label";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String START_DATE = "startdate";
	public static final String END_DATE = "enddate";
	public static final String GRANT_ID = "grantid";
	public static final String GRANT_NAME = "grantname";
	
	public Object serialize(Object o, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		if( !(o instanceof Goal) ) {
			throw new SerializationException("GoalJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {
			Goal goal = (Goal) o;
			result = new JSONObject();
			result.put(ID, goal.getId() );
			result.put(NAME, goal.getName() );
			result.put(DESCRIPTION, goal.getDescription() );
			result.put(LABEL, goal.getLabel() );
			String df = GeneralUtilities.getServerDateFormat();
			SimpleDateFormat dateFormat = new SimpleDateFormat();
			dateFormat.applyPattern(df);
			dateFormat.setLenient(false);
			result.put(START_DATE, dateFormat.format(goal.getStartDate()) );
			result.put(END_DATE, dateFormat.format(goal.getEndDate()) );
			
			if(goal.getGrant()!=null){
				
				OrganizationalUnitGrant grant = DAOFactory.getOrganizationalUnitDAO().getGrant(goal.getGrant());
				result.put(GRANT_ID, goal.getGrant());
				result.put(GRANT_NAME, grant.getName());
			}
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		}
		
		return result;
	}
	
	
}
