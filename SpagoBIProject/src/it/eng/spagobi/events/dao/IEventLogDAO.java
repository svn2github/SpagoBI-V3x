/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.events.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.events.bo.EventLog;

import java.util.List;

/**
 * Defines the interfaces for all methods needed to insert, modify and deleting an
 * Event object.
 * 
 * @author Gioia
 *
 */
public interface IEventLogDAO {
		
	/**
	 * Loads an event log given its id.
	 * 
	 * @param id The Integer representing the event id
	 * 
	 * @return A <code>EventLog</code> with the id passed at input
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public EventLog loadEventLogById(Integer id) throws EMFUserError;
		
	
	/**
	 * Loads the list of all events logs associated to the user profile at input.
	 * 
	 * @param profile The user profile
	 * 
	 * @return A <code>List</code> of <code>EventLog</code> containing all loaded information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadEventsLogByUser(IEngUserProfile profile) throws EMFUserError;
	
	/**
	 * Register a new EventLog.
	 * 
	 * @param eventLog the event log
	 * 
	 * @return the newly created event unique identifier
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public Integer insertEventLog(EventLog eventLog) throws EMFUserError;
	
	/**
	 * Erase an event log.
	 * 
	 * @param eventLog the event log
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseEventLog(EventLog eventLog) throws EMFUserError;
	
	/**
	 * Erase all event logs registered by the specificated user.
	 * 
	 * @param user The user who registered the events
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseEventsLogByUser(String user) throws EMFUserError;
}
