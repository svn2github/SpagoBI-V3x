/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.commons.dao.ISpagoBIDao;

import java.util.List;

public interface ISnapshotDAO extends ISpagoBIDao{

	/**
	 * Save a snapshot of the object.
	 * 
	 * @param content byte array containing the content of the snapshot
	 * @param idBIObj the id of the biobject parent
	 * @param name the name of the new subobject
	 * @param description the description of the new subobject
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void saveSnapshot(byte[] content, Integer idBIObj, String name, String description) throws EMFUserError;
	
	/**
	 * Gets the list of the snapshot details that are children of a biobject.
	 * 
	 * @param idBIObj the id of the biobject parent
	 * 
	 * @return List of BIObject.BIObjectSnapshot objects
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public List getSnapshots(Integer idBIObj)  throws EMFUserError;
	
	/**
	 * Delete a snapshot.
	 * 
	 * @param idSnap the id of the snapshot
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void deleteSnapshot(Integer idSnap) throws EMFUserError;
	
	/**
	 * Load a snapshot.
	 * 
	 * @param idSnap the id of the snapshot
	 * 
	 * @return Snapshot the snapshot loaded
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public Snapshot loadSnapshot(Integer idSnap) throws EMFUserError;
	
	
}
