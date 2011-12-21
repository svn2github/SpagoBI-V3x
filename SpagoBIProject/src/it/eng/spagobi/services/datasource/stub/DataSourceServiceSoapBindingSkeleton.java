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

package it.eng.spagobi.services.datasource.stub;

public class DataSourceServiceSoapBindingSkeleton implements it.eng.spagobi.services.datasource.stub.DataSourceService, org.apache.axis.wsdl.Skeleton {
    private it.eng.spagobi.services.datasource.stub.DataSourceService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDataSource", _params, new javax.xml.namespace.QName("", "getDataSourceReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://bo.datasource.services.spagobi.eng.it", "SpagoBiDataSource"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobidatasource", "getDataSource"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDataSource") == null) {
            _myOperations.put("getDataSource", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDataSource")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDataSourceByLabel", _params, new javax.xml.namespace.QName("", "getDataSourceByLabelReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://bo.datasource.services.spagobi.eng.it", "SpagoBiDataSource"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobidatasource", "getDataSourceByLabel"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDataSourceByLabel") == null) {
            _myOperations.put("getDataSourceByLabel", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDataSourceByLabel")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAllDataSource", _params, new javax.xml.namespace.QName("", "getAllDataSourceReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:spagobidatasource", "ArrayOf_tns2_SpagoBiDataSource"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:spagobidatasource", "getAllDataSource"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAllDataSource") == null) {
            _myOperations.put("getAllDataSource", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAllDataSource")).add(_oper);
    }

    public DataSourceServiceSoapBindingSkeleton() {
        this.impl = new it.eng.spagobi.services.datasource.stub.DataSourceServiceSoapBindingImpl();
    }

    public DataSourceServiceSoapBindingSkeleton(it.eng.spagobi.services.datasource.stub.DataSourceService impl) {
        this.impl = impl;
    }
    public it.eng.spagobi.services.datasource.bo.SpagoBiDataSource getDataSource(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        it.eng.spagobi.services.datasource.bo.SpagoBiDataSource ret = impl.getDataSource(in0, in1, in2);
        return ret;
    }

    public it.eng.spagobi.services.datasource.bo.SpagoBiDataSource getDataSourceByLabel(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        it.eng.spagobi.services.datasource.bo.SpagoBiDataSource ret = impl.getDataSourceByLabel(in0, in1, in2);
        return ret;
    }

    public it.eng.spagobi.services.datasource.bo.SpagoBiDataSource[] getAllDataSource(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        it.eng.spagobi.services.datasource.bo.SpagoBiDataSource[] ret = impl.getAllDataSource(in0, in1);
        return ret;
    }

}
