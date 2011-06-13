/**
 * DataSetsSDKServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.spagobi.sdk.datasets.stub;

import it.eng.spagobi.sdk.datasets.impl.DataSetsSDKServiceImpl;

public class DataSetsSDKServiceSoapBindingImpl implements it.eng.spagobi.sdk.datasets.stub.DataSetsSDKService{
    public it.eng.spagobi.sdk.datasets.bo.SDKDataSet[] getDataSets() throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSetsSDKServiceImpl impl = new DataSetsSDKServiceImpl();
    	return impl.getDataSets();
    }

    public it.eng.spagobi.sdk.datasets.bo.SDKDataSet getDataSet(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSetsSDKServiceImpl impl = new DataSetsSDKServiceImpl();
    	return impl.getDataSet(in0);
    }

    public it.eng.spagobi.sdk.datasets.bo.SDKDataStoreMetadata getDataStoreMetadata(it.eng.spagobi.sdk.datasets.bo.SDKDataSet in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.InvalidParameterValue, it.eng.spagobi.sdk.exceptions.MissingParameterValue, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSetsSDKServiceImpl impl = new DataSetsSDKServiceImpl();
    	return impl.getDataStoreMetadata(in0);
    }

    public java.lang.Integer saveDataset(it.eng.spagobi.sdk.datasets.bo.SDKDataSet in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSetsSDKServiceImpl impl = new DataSetsSDKServiceImpl();
    	return impl.saveDataset(in0);
    }

    public java.lang.String executeDataSet(java.lang.String in0, it.eng.spagobi.sdk.datasets.bo.SDKDataSetParameter[] in1) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSetsSDKServiceImpl impl = new DataSetsSDKServiceImpl();
    	return impl.executeDataSet(in0,in1);
    }

}
