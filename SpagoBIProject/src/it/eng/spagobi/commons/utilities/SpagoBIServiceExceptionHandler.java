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
package it.eng.spagobi.commons.utilities;

import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class SpagoBIServiceExceptionHandler {
	private static  SpagoBIServiceExceptionHandler instance;
		
	private static transient Logger logger = Logger.getLogger(SpagoBIServiceExceptionHandler.class);

	public static SpagoBIServiceExceptionHandler getInstance() {
		if(instance == null) {
			instance = new SpagoBIServiceExceptionHandler();
		}
		
		return instance;
	}
	
	private SpagoBIServiceExceptionHandler() {
		
	}
	
	
	/**
	 * 
	 * @param serviceName
	 * <code>
	 * public void service(request, response) {
	 * 		
	 * 		logger.debug("IN");
	 * 
	 * 		try {
	 * 			...
	 * 		} catch (Throwable t) {
	 * 			throw SpagoBIServiceExceptionHandler.getInstance().getWrappedException(serviceName, t);
	 * 		} finally {
	 * 			// relese resurces if needed
	 * 		}
	 * 
	 * 		logger.debug("OUT");
	 * }
	 * </code>
	 * 
	 * 
	 * @param e
	 * @return
	 */
	public SpagoBIEngineServiceException getWrappedException(String serviceName,  Throwable e) {
		SpagoBIServiceException serviceException = null;
		MessageBuilder msgBuild = new MessageBuilder();
		Locale locale = null;	
		RequestContainer requestContainer=RequestContainer.getRequestContainer();
		if(requestContainer!=null){
			SessionContainer permSess=requestContainer.getSessionContainer().getPermanentContainer();
			String lang=(String)permSess.getAttribute(SpagoBIConstants.AF_LANGUAGE);
			String country=(String)permSess.getAttribute(SpagoBIConstants.AF_COUNTRY);
			if(lang!=null && country!=null){
				locale=new Locale(lang,country,"");
			}
		}else{
			locale = GeneralUtilities.getDefaultLocale();	
		}
		
		if(e instanceof SpagoBIServiceException) {
			// this mean that the service have catched the exception nicely
			serviceException = (SpagoBIServiceException)e;
			String sms = serviceException.getMessage();
			sms = msgBuild.getMessage(sms, locale);	
			serviceException = new SpagoBIServiceException(serviceName, sms, e);
		} else {
			// otherwise an unpredicted exception has been raised. 	
			
			// This is the last line of defense against exceptions. Bytheway all exceptions that are catched 
			// only here for the first time can be considered as bugs in the exception handling mechanism. When
			// such an exception is raised the code in the service should be fixed in order to catch it before and add some meaningfull
			// informations on what have caused it.
			Throwable rootException = e;
			while(rootException.getCause() != null) {
				rootException = rootException.getCause();
			}
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			str = msgBuild.getMessage(str, locale);	
			String message = "An unexpecetd error occurred while executing service."
							 + "\nThe root cause of the error is: " + str;
			
			serviceException = new SpagoBIServiceException(serviceName, message, e);
			
		}

		logError(serviceException);
		
		throw serviceException;
	}
	
	public static void logError(SpagoBIServiceException serviceError) {
		logger.error(serviceError.getMessage());
		logger.error("The error root cause is: " + serviceError.getRootCause());	
		if(serviceError.getHints().size() > 0) {
			Iterator hints = serviceError.getHints().iterator();
			while(hints.hasNext()) {
				String hint = (String)hints.next();
				logger.info("hint: " + hint);
			}
			
		}
		logger.error("The error root cause stack trace is:",  serviceError.getCause());	
		logger.error("The error full stack trace is:", serviceError);			
	}
}
