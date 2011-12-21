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
package it.eng.spagobi.sdk.proxy;

import java.rmi.Remote;

import org.apache.axis.client.Stub;
import org.apache.ws.security.handler.WSHandlerConstants;

import it.eng.spagobi.sdk.callbacks.ClientCredentialsHolder;

public class EnginesServiceProxy extends AbstractSDKServiceProxy implements it.eng.spagobi.sdk.engines.stub.EnginesService {
  private String _endpoint = null;
  private it.eng.spagobi.sdk.engines.stub.EnginesService enginesService = null;
  private ClientCredentialsHolder cch = null;
  
	public EnginesServiceProxy(String user, String pwd) {
		cch = new ClientCredentialsHolder(user, pwd);
		_initEnginesServiceProxy();
	}
  
  private void _initEnginesServiceProxy() {
	    try {
			it.eng.spagobi.sdk.engines.stub.EnginesServiceServiceLocator locator = new it.eng.spagobi.sdk.engines.stub.EnginesServiceServiceLocator();
			Remote remote = locator.getPort(it.eng.spagobi.sdk.engines.stub.EnginesService.class);
	        Stub axisPort = (Stub) remote;
	        axisPort._setProperty(WSHandlerConstants.USER, cch.getUsername());
	        axisPort._setProperty(WSHandlerConstants.PW_CALLBACK_REF, cch);
	        //axisPort.setTimeout(30000); //used in SpagoBIStudio
	        enginesService = (it.eng.spagobi.sdk.engines.stub.EnginesService) axisPort;
			
	      if (enginesService != null) {
	        if (_endpoint != null)
	          ((javax.xml.rpc.Stub)enginesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	        else
	          _endpoint = (String)((javax.xml.rpc.Stub)enginesService)._getProperty("javax.xml.rpc.service.endpoint.address");
	      }
	      
	    }
	    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (enginesService != null)
      ((javax.xml.rpc.Stub)enginesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.eng.spagobi.sdk.engines.stub.EnginesService getEnginesService() {
    if (enginesService == null)
      _initEnginesServiceProxy();
    return enginesService;
  }
  
  public it.eng.spagobi.sdk.engines.bo.SDKEngine[] getEngines() throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException{
    if (enginesService == null)
      _initEnginesServiceProxy();
    return enginesService.getEngines();
  }
  
  public it.eng.spagobi.sdk.engines.bo.SDKEngine getEngine(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException{
    if (enginesService == null)
      _initEnginesServiceProxy();
    return enginesService.getEngine(in0);
  }
  
  
}