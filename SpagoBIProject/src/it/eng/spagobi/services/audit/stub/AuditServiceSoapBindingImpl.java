/**
 * AuditServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.audit.stub;

import it.eng.spagobi.services.audit.service.AuditServiceImpl;

public class AuditServiceSoapBindingImpl implements it.eng.spagobi.services.audit.stub.AuditService{
    public java.lang.String log(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, java.lang.String in6, java.lang.String in7) throws java.rmi.RemoteException {
	AuditServiceImpl service=new AuditServiceImpl();
	return service.log(in0, in1, in2, in3, in4, in5, in6, in7);
    }

}
