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
package it.eng.spagobi.analiticalmodel.document.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class GetSnapshotContentAction extends AbstractHttpAction {
	public static int SUCCESS = 200;
	static Logger logger = Logger.getLogger(GetSnapshotContentAction.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		String objectIdStr = (String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer objectId = new Integer(objectIdStr);
		String idSnapStr = (String)request.getAttribute(SpagoBIConstants.SNAPSHOT_ID);
		Integer idSnap = new Integer(idSnapStr);
		logger.debug("Required snapshot with id = " + idSnap + " of document with id = " + objectId);
		IEngUserProfile profile = (IEngUserProfile) this.getRequestContainer().getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(objectId);
		// check if the user is able to see the document
		// TODO check if the user is able to execute the document (even if it does no make sense to be able to see the document but not to execute it...)
		byte[] content = null;
		if (ObjectsAccessVerifier.canSee(obj, profile)) {
			logger.debug("Current user [" + ((UserProfile) profile).getUserId().toString() + "] can see snapshot with id = " + idSnap + " of document with id = " + objectId);
			ISnapshotDAO snapdao = DAOFactory.getSnapshotDAO();
			Snapshot snap = snapdao.loadSnapshot(idSnap);
			content = snap.getContent();
		} else {
			logger.error("Current user [" + ((UserProfile) profile).getUserId().toString() + "] CANNOT see snapshot with id = " + idSnap + " of document with id = " + objectId);
			//content = "You cannot see required snapshot.".getBytes();
			content = "You cannot see required snapshot.".getBytes("UTF-8");
		}
		//httpResp.setContentType("text/html");
		httpResp.setContentLength(content.length);
		httpResp.getOutputStream().write(content);
		httpResp.setStatus(SUCCESS);
		httpResp.getOutputStream().flush();
		logger.debug("OUT");
	}

}
