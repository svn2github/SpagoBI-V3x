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
package it.eng.spagobi.engines.console.services.initializers;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.console.ConsoleEngine;
import it.eng.spagobi.engines.console.ConsoleEngineInstance;
import it.eng.spagobi.services.proxy.DataSetServiceProxy;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineStartupException;

import java.util.Locale;
import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;


/**
 * Entry point action.
 */
public class ConsoleEngineStartAction extends AbstractEngineStartAction {
	
	// INPUT PARAMETERS
	
	// OUTPUT PARAMETERS
	public static final String LANGUAGE = "LANGUAGE";
	public static final String COUNTRY = "COUNTRY";
	public static final String DOCUMENT_LABEL = "DOCUMENT_LABEL";
	public static final String PROXY_DATASET = "PROXY_DATASET";
	
	// SESSION PARAMETRES	
	public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ConsoleEngineStartAction.class);
    
    private static final String ENGINE_NAME = "SpagoBIConsoleEngine";
    private String documentLabel;
	

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)  {
		logger.debug("IN");	
		Monitor monitor =MonitorFactory.start("SpagoBI_Console.ConsoleEngineStartAction.service");
		Locale locale;
		DataSetServiceProxy proxyDS = null;
		ConsoleEngineInstance consoleEngineInstance = null;
		
		try {
			setEngineName(ENGINE_NAME);
			super.service(serviceRequest, serviceResponse);
			
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Document Label: " + getDocumentLabel());
			logger.debug("Template: " + getTemplateAsString());
						
			if(getAuditServiceProxy() != null) {
				logger.debug("Audit enabled: [TRUE]");
				getAuditServiceProxy().notifyServiceStartEvent();
			} else {
				logger.debug("Audit enabled: [FALSE]");
			}
			
			logger.debug("Creating engine instance ...");
			
			try {
				consoleEngineInstance = ConsoleEngine.createInstance( getTemplateAsJSONObject(), getEnv() );
			} catch(Throwable t) {
				SpagoBIEngineStartupException serviceException;
				String msg = "Impossible to create engine instance for document [" + getDocumentId() + "].";
				Throwable rootException = t;
				while(rootException.getCause() != null) {
					rootException = rootException.getCause();
				}
				String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
				msg += "\nThe root cause of the error is: " + str;
				serviceException = new SpagoBIEngineStartupException(ENGINE_NAME, msg, t);
				
				
				throw serviceException;
			}
			logger.debug("Engine instance succesfully created");
			
			locale = (Locale)consoleEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
			documentLabel = getDocumentLabel();
			logger.debug("Parameter [" + DOCUMENT_LABEL + "] is equal to [" + documentLabel + "]");
			setAttribute(DOCUMENT_LABEL, documentLabel);
			setAttributeInSession( ENGINE_INSTANCE, consoleEngineInstance);		
			setAttribute(ENGINE_INSTANCE, consoleEngineInstance);
			
			setAttribute(LANGUAGE, locale.getLanguage());
			setAttribute(COUNTRY, locale.getCountry());

			
		} catch (Exception e) {
			SpagoBIEngineStartupException serviceException = null;
						
			if(e instanceof SpagoBIEngineStartupException) {
				serviceException = (SpagoBIEngineStartupException)e;
			} else {
				Throwable rootException = e;
				while(rootException.getCause() != null) {
					rootException = rootException.getCause();
				}
				String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
				String message = "An unpredicted error occurred while executing " + getEngineName() + " service."
								 + "\nThe root cause of the error is: " + str;
				
				serviceException = new SpagoBIEngineStartupException(getEngineName(), message, e);
			}
			
			throw serviceException;
		} finally {
			monitor.stop();
			logger.debug("OUT");
		}
	}
	
	 /**
	  * Gets the document label.
	  * 
	  * @return the document label
	  */
	 private String getDocumentLabel() {
		 if(documentLabel == null) {

			 logger.debug("IN");

			 documentLabel = getAttributeAsString( DOCUMENT_LABEL );			 
			 logger.debug("Document Label parameter received: documentLabel = [" + documentLabel + "]");
			 
			 logger.debug("OUT");
		 }

		 return documentLabel;
	 }
}