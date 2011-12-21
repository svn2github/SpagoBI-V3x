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
/*
 * Created on 13-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.commons.dao.ISpagoBIDao;

import java.util.List;


/**
 * Defines  the interfaces for all methods needed to insert, 
 * modify and deleting a viewpoint.
 * 
 * @author Giachino
 */
public interface IViewpointDAO extends ISpagoBIDao{
	
	/**
	 * Loads all detail information for all viewpoints. For each of them, detail
	 * information is stored into a <code>ViewPoint</code> object. After that,
	 * all value constraints are stored into a <code>List</code>, which is returned.
	 * 
	 * @return A list containing all viewpoints objects
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllViewpoints() throws EMFUserError;
	
	/**
	 * Loads all detail information for all viewpoints of a object specified.
	 * For each of them, detail information is stored into a <code>ViewPoint</code>
	 * object. After that,
	 * all value constraints are stored into a <code>List</code>, which is returned.
	 * 
	 * @param objId the obj id
	 * 
	 * @return A list containing all viewpoints objects
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllViewpointsByObjID(Integer objId) throws EMFUserError;
	
	
	/**
	 * Loads all detail information for a viewpoint identified by its
	 * <code>id</code>. All these information are stored into a
	 * <code>Viewpoint</code> object, which is
	 * returned.
	 * 
	 * @param id The id for the viewpoint to load
	 * 
	 * @return A <code>Viewpoint</code> object containing all loaded information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public Viewpoint loadViewpointByID(Integer id) throws EMFUserError;
	
	/**
	 * Loads all detail information for a viewpoint identified by its
	 * <code>name</code> and relevant document identifier. All these information are stored into a
	 * <code>Viewpoint</code> object, which is
	 * returned.
	 * 
	 * @param name The name for the viewpoint to load
	 * @param name The id of the document
	 * 
	 * @return A <code>Viewpoint</code> object containing all loaded information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public Viewpoint loadViewpointByNameAndBIObjectId(String name, Integer biobjectId) throws EMFUserError;

	
	/**
	 * Implements the query to erase a viewpoint. All information needed is stored
	 * into the input <code>Viewpoint</code> object.
	 * 
	 * @param ID The identifier of viewpoint
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseViewpoint(Integer ID) throws EMFUserError;
	
	/**
	 * Implements the query to insert a viewpoint. All information needed is stored
	 * into the input <code>Viewpoint</code> object.
	 * 
	 * @param viewpoint the viewpoint
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertViewpoint(Viewpoint viewpoint) throws EMFUserError;
	
	/**
	 * Implements the query to modify a viewpoint. All information needed is stored
	 * into the input <code>Viewpoint</code> object.
	 * 
	 * @param objId the obj id
	 * @param userProfile the user profile
	 * 
	 * @return the list
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	//public void modifyViewpoint(Viewpoint viewpoint) throws EMFUserError;
	
	/**
	 * Loads all detail information for all viewpoints of a object specified accessibile to the user profile at input. 
	 * For each of them, detail information is stored into a <code>ViewPoint</code> 
	 * object. After that, all value constraints are stored into a <code>List</code>, which is returned.
	 * 
	 * @return A list containing all viewpoints objects
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAccessibleViewpointsByObjId(Integer objId, IEngUserProfile userProfile) throws EMFUserError;

}