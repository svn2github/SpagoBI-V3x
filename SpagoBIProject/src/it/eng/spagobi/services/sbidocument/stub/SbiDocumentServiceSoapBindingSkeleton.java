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

package it.eng.spagobi.services.sbidocument.stub;

public class SbiDocumentServiceSoapBindingSkeleton implements it.eng.spagobi.services.sbidocument.stub.SbiDocumentService, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.services.sbidocument.stub.SbiDocumentService impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentAnalyticalDrivers", _params, new javax.xml.namespace.QName("", "getDocumentAnalyticalDriversReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobisbidocument", "ArrayOf_tns2_SpagobiAnalyticalDriver"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisbidocument", "getDocumentAnalyticalDrivers"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentAnalyticalDrivers") == null) {
            _myOperations.put("getDocumentAnalyticalDrivers", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentAnalyticalDrivers")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"), java.lang.Integer.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDocumentAnalyticalDriversJSON", _params, new javax.xml.namespace.QName("", "getDocumentAnalyticalDriversJSONReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobisbidocument", "getDocumentAnalyticalDriversJSON"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDocumentAnalyticalDriversJSON") == null) {
            _myOperations.put("getDocumentAnalyticalDriversJSON", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDocumentAnalyticalDriversJSON")).add(_oper);
    }

    public SbiDocumentServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.services.sbidocument.stub.SbiDocumentServiceSoapBindingImpl();
    }

    public SbiDocumentServiceSoapBindingSkeleton(it.eng.spagobi.services.sbidocument.stub.SbiDocumentService impl) {
        this.impl = impl;
    }
    public it.eng.spagobi.services.sbidocument.bo.SpagobiAnalyticalDriver[] getDocumentAnalyticalDrivers(java.lang.String in0, java.lang.String in1, java.lang.Integer in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException
    {
        it.eng.spagobi.services.sbidocument.bo.SpagobiAnalyticalDriver[] ret = impl.getDocumentAnalyticalDrivers(in0, in1, in2, in3, in4);
        return ret;
    }

    public java.lang.String getDocumentAnalyticalDriversJSON(java.lang.String in0, java.lang.String in1, java.lang.Integer in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getDocumentAnalyticalDriversJSON(in0, in1, in2, in3, in4);
        return ret;
    }

}
