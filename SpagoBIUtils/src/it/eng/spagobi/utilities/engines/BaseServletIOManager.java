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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import it.eng.spagobi.container.ContextManager;
import it.eng.spagobi.container.IBeanContainer;
import it.eng.spagobi.container.IContainer;
import it.eng.spagobi.container.SpagoBIContainerFactory;
import it.eng.spagobi.container.strategy.ExecutionContextRetrieverStrategy;
import it.eng.spagobi.container.strategy.IContextRetrieverStrategy;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BaseServletIOManager {
	HttpServletRequest request;	
	IContainer requestContainer;
	
	HttpServletResponse response;
	
	HttpSession session;
	ContextManager contextManager;
	
	private static final String EXECUTION_ID = "SBI_EXECUTION_ID";
	
	private Logger logger = Logger.getLogger(BaseServletIOManager.class);
	
	//----------------------------------------------------------------------------------------------------
    // Constructor
    //----------------------------------------------------------------------------------------------------
	
	public BaseServletIOManager(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.requestContainer = SpagoBIContainerFactory.getContainer( request );
		this.response = response;
		this.session = request.getSession();
		IContextRetrieverStrategy contextRetriveStrategy;
		IBeanContainer sessionContainer = (IBeanContainer)SpagoBIContainerFactory.getContainer( session );
		Object str;
		str = request.getParameter(EXECUTION_ID);
		str = request.getAttribute(EXECUTION_ID);
		contextRetriveStrategy = new ExecutionContextRetrieverStrategy( this.requestContainer );
		this.contextManager = new ContextManager(sessionContainer, contextRetriveStrategy);
	}
	
	//----------------------------------------------------------------------------------------------------
    // Request
    //----------------------------------------------------------------------------------------------------
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public IContainer getRequestContainer() {
		return requestContainer;
	}
	
	public void setRequestContainer(IContainer request) {
		this.requestContainer = request;
	}
	
	public Object getParameter(String parName) {
		return getRequestContainer().get( parName );
	}
	
	public String getParameterAsString(String parName) {
		return getRequestContainer().getString( parName );
	}
	
	public boolean requestContainsParameter(String parName) {
		return !getRequestContainer().isNull(parName);
	}
	
	//----------------------------------------------------------------------------------------------------
    // Response
    //----------------------------------------------------------------------------------------------------
	
	
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public boolean tryToWriteBackToClient(String message) {
		try {
			writeBackToClient(message);
		} catch (IOException e) {
			logger.error("Impossible to write back to the client the message: [" + message + "]", e);
			return false;
		}
		
		return true;
	}
	
	public void writeBackToClient(String message) throws IOException {
		writeBackToClient(200, message,
				true,
				"service-response",
				"text/plain");
	}
	
	public void writeBackToClient(int statusCode, String content, boolean inline, String fileName, String contentType) throws IOException {
		
		// setup response header
		if(getResponse() instanceof HttpServletResponse) {
			((HttpServletResponse)getResponse()).setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + fileName + "\";");
		}
		
		getResponse().setContentType( contentType );
		getResponse().setContentLength( content.length() );
		
		if(getResponse() instanceof HttpServletResponse) {
			((HttpServletResponse)getResponse()).setStatus(statusCode);
		}
		
		getResponse().getWriter().print(content);
		getResponse().getWriter().flush();
	}
	
	public void writeBackToClient(int statusCode, File file, boolean inline, String fileName, String contentType) throws IOException {
		
		// setup response header
		if(getResponse() instanceof HttpServletResponse) {
			getResponse().setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + fileName + "\";");
		}
		
		getResponse().setContentType( contentType );
		getResponse().setStatus(statusCode);
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		
		int b = -1;
		int contentLength = 0;
		byte[] buf = new byte[1024];
		while((b = in.read(buf)) != -1) {
			getResponse().getOutputStream().write(buf, 0, b);
			contentLength += b;
		}	
		getResponse().setContentLength( contentLength );
		getResponse().getOutputStream().flush();
		
		in.close();
	}
	
	
	//----------------------------------------------------------------------------------------------------
    // Session
    //----------------------------------------------------------------------------------------------------
	
	
	public Object getParameterFromSession(String paramName) {
		return contextManager.get( paramName );
	}
	
	public String getParameterFromSessionAsString(String paramName) {
		return contextManager.getString( paramName );
	}
	
	public HttpSession getHttpSession() {
		return session;
	}

	
	
	
}
