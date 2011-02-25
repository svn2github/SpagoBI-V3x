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
/*
 * Created on 13-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.behaviouralmodel.analyticaldriver.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuse;

import java.util.List;

import org.hibernate.Session;


/**
 * Defines the interfaces for all methods needed to insert, 
 * modify and deleting a parameter use mode.
 * 
 * @author zoppello
 */
public interface IParameterUseDAO {


	/**
	 * Loads all detail information for a parameter use mode identified by
	 * its <code>id</code>. All these information, achived by a query
	 * to the DB, are stored into a<code>SbiParuse</code> object, which is
	 * returned.
	 * 
	 * @param id The id for the parameter use mode to load
	 * 
	 * @return A <code>SbiParuse</code> object containing all loaded information
	 * 
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public SbiParuse loadById(Integer id) throws EMFUserError;

	/**
	 * Loads all detail information for a parameter use mode identified by
	 * its <code>useID</code>. All these information, achived by a query
	 * to the DB, are stored into a<code>ParameterUse</code> object, which is
	 * returned.
	 * 
	 * @param useID The id for the parameter use mode to load
	 * 
	 * @return A <code>ParameterUse</code> object containing all loaded information
	 * 
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public ParameterUse loadByUseID(Integer useID) throws EMFUserError;


	/**
	 * Loads all detail information for a parameter use mode identified by
	 * a parameter id and a role name. All these information, achived by a query
	 * to the DB, are stored into a<code>ParameterUse</code> object, which is
	 * returned.
	 * 
	 * @param parameterId The id for the parameter associated
	 * @param roleName The role name associated
	 * 
	 * @return A <code>ParameterUse</code> object containing all loaded information
	 * 
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public ParameterUse loadByParameterIdandRole(Integer parameterId, String roleName) throws EMFUserError;


	/**
	 * Given at input a <code>ParameterUse</code> objects, asks for all possible Checks
	 * associated whith it and fills the <code>AssociatedChecks</code> object's list.
	 * 
	 * @param aParameterUse the a parameter use
	 * 
	 * @throws EMFUserError if an Exception occurred.
	 */
	public void fillAssociatedChecksForParUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Given at input a <code>ParameterUse</code> objects, asks for all possible Roles
	 * associated whith it and fills the <code>ListRoles</code> object's list.
	 * 
	 * @param aParameterUse the a parameter use
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void fillRolesForParUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Implements the query to modify a parameter use mode. All information needed
	 * is stored into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse The object containing all modify information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void modifyParameterUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Implements the query to insert a parameter use mode. All information needed
	 * is stored into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse The object containing all insert information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertParameterUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Implements the query to erase a ParameterUse mode. All information needed is stored
	 * into the input <code>ParameterUse</code> object.
	 * 
	 * @param aParameterUse The object containing all erase information
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseParameterUse(ParameterUse aParameterUse) throws EMFUserError;

	/**
	 * Controls if a parameter has some use modes associated or not. It is useful
	 * because a parameter can be deleted only if it hasn't any use mode associated.
	 * 
	 * @param parId The parameter id
	 * 
	 * @return True if the parameter has one or more modes associated;
	 * false if it hasn't any
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */
	public boolean hasParUseModes (String parId) throws EMFUserError;

	/**
	 * Loads the list of parameter use modes associated to the input
	 * parameter id.
	 * 
	 * @param parId the par id
	 * 
	 * @return A list of <code>ParameterUse</code> objects
	 * 
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public List loadParametersUseByParId(Integer parId) throws EMFUserError;


	/**
	 * Delete all the parameter use modes associated to the input
	 * parameter id.
	 * 
	 * @param parId the par id
	 * 
	 * @throws EMFUserError EMFUserError If an Exception occurred
	 */
	public void eraseParameterUseByParId(Integer parId) throws EMFUserError;

	/**
	 * Gets the list of parameter uses associated to the lov identified by the lovId at input.
	 * 
	 * @param lovId The integer id of the lov
	 * 
	 * @return The list of parameter uses associated to the lov identified by the lovId at input
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public List getParameterUsesAssociatedToLov(Integer lovId) throws EMFUserError;


	/**
	 * Delete from hibernate session a parameter use
	 * 
	 * @param Session hibernate Session
	 * 
	 * @return The list of parameter uses associated to the lov identified by the lovId at input
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void eraseParameterUseByParIdSameSession(Integer parId, Session sessionCurrDB) throws EMFUserError;

}