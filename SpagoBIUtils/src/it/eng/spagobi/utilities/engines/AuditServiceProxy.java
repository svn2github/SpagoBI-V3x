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
package it.eng.spagobi.utilities.engines;

import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import javax.servlet.http.HttpSession;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AuditServiceProxy {
	
	private String auditId;
	private String userId;
	private HttpSession session;
	private AuditAccessUtils proxy;
	
	public AuditServiceProxy(String auditId, String userId, HttpSession session) {
		setAuditId(auditId);
		setUserId(userId);
		setSession(session);
		proxy = new AuditAccessUtils(auditId);
	}

	private void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	private void setUserId(String userId) {
		this.userId = userId;
	}

	private void setSession(HttpSession session) {
		this.session = session;
	}

	public void notifyServiceStartEvent() {
		proxy.updateAudit(session, userId, auditId, 
				new Long(System.currentTimeMillis()), null, 
				"EXECUTION_STARTED", null, null);
	}

	public void notifyServiceErrorEvent(String msg) {
		proxy.updateAudit(session, userId, auditId,  
				null, new Long(System.currentTimeMillis()), 
				"EXECUTION_FAILED", msg, null);
	}

	public void notifyServiceEndEvent() {
		proxy.updateAudit(session, userId, auditId,  
				null, new Long(System.currentTimeMillis()), 
				"EXECUTION_PERFORMED", null, null);
		
	}

}
