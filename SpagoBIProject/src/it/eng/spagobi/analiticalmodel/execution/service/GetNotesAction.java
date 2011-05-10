/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.execution.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjNote;
import it.eng.spagobi.analiticalmodel.document.handlers.BIObjectNotesManager;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.serializer.SerializationException;
import it.eng.spagobi.commons.serializer.SerializerFactory;
import it.eng.spagobi.commons.services.AbstractSpagoBIAction;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Zerbetto Davide

 *
 */
public class GetNotesAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_NOTES_ACTION";
	// REQUEST PARAMETERS
	public static final String MESSAGE = "MESSAGE";
	public static final String OWNER = "OWNER";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetNotesAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			// retrieving execution instance from session, no need to check if user is able to execute the current document
			ExecutionInstance executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			BIObject obj = executionInstance.getBIObject();
			BIObjectNotesManager objectNManager = new BIObjectNotesManager();
			String execIdentifier = objectNManager.getExecutionIdentifier(obj);
			String message = this.getAttributeAsString(MESSAGE);
			logger.debug("Parameter [" + MESSAGE + "] is equal to [" + message + "]");
			
			String owner = (this.getAttributeAsString(OWNER)==null)?"":this.getAttributeAsString(OWNER);
			logger.debug("Parameter [" + OWNER + "] is equal to [" + owner + "]");
			if ("".equals(owner)){
				SessionContainer sessCont = getSessionContainer();
				SessionContainer permCont = sessCont.getPermanentContainer();
				IEngUserProfile	profile = (IEngUserProfile)permCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				
				owner = (String)((UserProfile)profile).getUserId();
				logger.debug("Getting owner from profile. Is equal to [" + owner + "]");
			}

			if ("GET_LIST_NOTES".equalsIgnoreCase(message))
				getListNotes(obj, execIdentifier,owner);
			else if ("GET_DETAIL_NOTE".equalsIgnoreCase(message))
				getDetailNote(obj, execIdentifier, owner);
			else if ("INSERT_NOTE".equalsIgnoreCase(message)){
				getEmptyNote();
			}
			else{
				logger.error("The input MESSAGE doesn't exist! Cannot load Note.");
				getEmptyNote();
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void getEmptyNote(){
		try {
			JSONArray notesListJSON = new JSONArray();
			JSONObject results = new JSONObject();
			results.put("results", notesListJSON);
			results.put("totalCount", notesListJSON.length());
			writeBackToClient( new JSONSuccess( results ) ); 
		} catch (IOException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
		} catch (JSONException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
		}
	}
	
	private void getListNotes(BIObject obj, String execIdentifier, String owner){
		List notesList = null;
		List updatedNotesList = null; //final list with notes public or of user owner
		try {
			notesList = DAOFactory.getObjNoteDAO().getListExecutionNotes(obj.getId(), execIdentifier);
		} catch (Exception e) {
			logger.error("Cannot load notes for document [id: " + obj.getId() + ", label: " + obj.getLabel() + ", name: " + obj.getName() + "]", e);
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot load notes", e);
		}
		try {
			JSONArray notesListJSON = new JSONArray();
			if (notesList != null){
				updatedNotesList = new ArrayList();
				//define isDeletable flag if the user is equal owner
				for (int i=0; i<notesList.size(); i++){
					ObjNote objNote = (ObjNote)notesList.get(i);
					if (objNote.getOwner().equals(owner)){
						objNote.setIsDeletable(true);
						updatedNotesList.add(objNote);
					}
					else if (objNote.getIsPublic()){						
						objNote.setIsDeletable(false);
						updatedNotesList.add(objNote);
					}									
				}
					
				notesListJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize( updatedNotesList,null );
			}
			JSONObject results = new JSONObject();
			results.put("results", notesListJSON);
			results.put("totalCount", notesListJSON.length());
			writeBackToClient( new JSONSuccess( results ) );  
		} catch (IOException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
		} catch (SerializationException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects", e);
		} catch (JSONException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
		}
	}
	
	private void getDetailNote(BIObject obj, String execIdentifier, String owner){
		ObjNote objnote = null;
		
		try {
			objnote = DAOFactory.getObjNoteDAO().getExecutionNotesByOwner(obj.getId(), execIdentifier, owner);
		} catch (Exception e) {
			logger.error("Cannot load notes for document [id: " + obj.getId() + ", label: " + obj.getLabel() + ", name: " + obj.getName() + "]", e);
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot load notes", e);
		}
		
		try {
			JSONObject noteJSON = new JSONObject();
			JSONObject results = new JSONObject();
			if (objnote != null)
				noteJSON = (JSONObject) SerializerFactory.getSerializer("application/json").serialize( objnote,null );
			//results.put("results", noteJSON);
			writeBackToClient( new JSONSuccess( noteJSON ) );
		} catch (IOException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
		} catch (SerializationException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects", e);
		} /*catch (JSONException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
		}*/
	}

}
