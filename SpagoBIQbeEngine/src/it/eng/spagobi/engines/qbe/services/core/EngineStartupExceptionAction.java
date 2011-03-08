/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.qbe.services.core;


import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineStartupException;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class EngineStartupExceptionAction extends AbstractQbeEngineAction {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(EngineStartupExceptionAction.class);
	
   
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)  {
		
		EMFErrorHandler errorHandler;
		Collection errors ;
		Iterator it;
		
		logger.debug("IN");
		try {
			
			errorHandler = getErrorHandler();
			Assert.assertNotNull(errorHandler, "error handler cannot be null");
			
			errors = errorHandler.getErrors();
			logger.debug("error handler contains [" + errors.size() + "] error/s");
			
			it = errors.iterator();
			while(it.hasNext()) {
				Object o = it.next();
				logger.debug("Error type [" + o.getClass().getName()+ "]");
				if(o instanceof EMFInternalError) {
					EMFInternalError error = (EMFInternalError)o;
					Exception e = error.getNativeException();
					if(e instanceof SpagoBIEngineStartupException) {
						SpagoBIEngineStartupException serviceError = (SpagoBIEngineStartupException)e;
						logError(serviceError);
					} else {
						logger.error("Unespected exception",e);		
					}		
				} else {
					logger.error(o.toString());
				}
			}
		} catch(Throwable t) {
			logger.error("An error occurred while handling a previously thrown Exception");
		} finally {
			logger.debug("OUT");
		}
	}


	private void logError(SpagoBIEngineStartupException serviceError) {
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
