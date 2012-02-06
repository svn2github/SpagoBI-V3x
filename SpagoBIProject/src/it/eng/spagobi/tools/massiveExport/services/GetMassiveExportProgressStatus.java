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

package it.eng.spagobi.tools.massiveExport.services;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.AbstractSpagoBIAction;
import it.eng.spagobi.tools.massiveExport.bo.ProgressThread;
import it.eng.spagobi.tools.massiveExport.dao.IProgressThreadDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetMassiveExportProgressStatus extends AbstractSpagoBIAction {

	private final String SERVICE_NAME = "GET_MASSIVE_EXPORT_PROGRESS_STATUS";

	// logger component
	private static Logger logger = Logger.getLogger(GetMassiveExportProgressStatus.class);


	public static final String  MESSAGE_STARTED = "STARTED";
	public static final String  MESSAGE_DOWNLOAD = "DOWNLOAD";	
	public static final String  MESSAGE_CLOSED = "CLOSED";
	
	// JSON ATTRIBUTE PASSED
	public static final String  NO_WORK_PRESENT = "noWorkPresent";
	public static final String  FUNCT_CD = "functCd";
	public static final String  TOTAL = "total";
	public static final String  PARTIAL = "partial";
	public static final String  RANDOM_KEY = "randomKey";
	public static final String  PROGRESS_THREAD_ID = "progressThreadId";
	public static final String  MESSAGE = "message";
	public static final String  TYPE = "type";
	
	
	
	
	static Integer progress = 0;


	@Override
	public void doService() {
		//logger.debug("IN");

		IEngUserProfile profile = getUserProfile();
		String userId = profile.getUserUniqueIdentifier().toString();

		Integer total = null;
		Integer partial = null;

		IProgressThreadDAO progressThreadDAO = null;
			
		try{
			progressThreadDAO = DAOFactory.getProgressThreadDAO();

			List<ProgressThread> progressThreads = progressThreadDAO.loadNotClosedProgressThreadsByUserId(userId);
			JSONArray response = new JSONArray();
		
			if(progressThreads != null){
				
				for (ProgressThread progressThread : progressThreads) {
					
					JSONObject obj = new JSONObject();
					partial = progressThread.getPartial();
					total = progressThread.getTotal();
					obj.put(FUNCT_CD, progressThread.getFunctionCd());
					obj.put(TOTAL, total);
					obj.put(PARTIAL, partial);
					obj.put(RANDOM_KEY, progressThread.getRandomKey());
					obj.put(PROGRESS_THREAD_ID, progressThread.getProgressThreadId());
					obj.put(TYPE, progressThread.getType());
					
					if(partial >= total){
						obj.put(MESSAGE, MESSAGE_DOWNLOAD);					
					} else{
						obj.put(MESSAGE, MESSAGE_STARTED);											
					}
					response.put(obj);
				}				

			}
		
			writeBackToClient(new JSONSuccess(response));

		} catch (EMFUserError err) {
			throw new SpagoBIServiceException("Impossible to write back the responce to the client", err);
		}
		catch (JSONException err) {
			throw new SpagoBIServiceException("Impossible to write back the responce to the client", err);
		}
		catch (IOException err) {
			throw new SpagoBIServiceException("Impossible to write back the responce to the client", err);
		}
	}


}
