/* SpagoBI, the Open Source Business Intelligence suite

* � 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this file,
* You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.services.audit.stub;

public interface AuditServiceService extends javax.xml.rpc.Service {
    public java.lang.String getAuditServiceAddress();

    public it.eng.spagobi.services.audit.stub.AuditService getAuditService() throws javax.xml.rpc.ServiceException;

    public it.eng.spagobi.services.audit.stub.AuditService getAuditService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
