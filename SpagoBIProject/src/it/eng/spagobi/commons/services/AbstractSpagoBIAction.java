/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.commons.services;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFAbstractError;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBIServiceExceptionHandler;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.container.CoreContextManager;
import it.eng.spagobi.container.IBeanContainer;
import it.eng.spagobi.container.strategy.ExecutionContextRetrieverStrategy;
import it.eng.spagobi.container.strategy.IContextRetrieverStrategy;
import it.eng.spagobi.utilities.exceptions.CannotWriteErrorsToClientException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;
import it.eng.spagobi.utilities.service.JSONFailure;
import it.eng.spagobi.utilities.themes.ThemesManager;

import java.util.Collection;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class AbstractSpagoBIAction extends AbstractBaseHttpAction {
	
	public static final String SERVICE_NAME = "SPAGOBI_SERVICE";
	
	private static transient Logger logger = Logger.getLogger(AbstractSpagoBIAction.class);
	
	private CoreContextManager contextManager;
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) throws SpagoBIServiceException {
		EMFErrorHandler errorHandler = this.getErrorHandler();
		if (!errorHandler.isOK()) {
			writeErrorsBackToClient();
			return;
		}
		setSpagoBIRequestContainer( request );
		setSpagoBIResponseContainer( response );
		// setting language and country info for Spago framework
		String language = this.getAttributeAsString("SBI_LANGUAGE");
		String country = this.getAttributeAsString("SBI_COUNTRY");
		if (language != null && !language.trim().equals("")) {
			this.getSessionContainer().getPermanentContainer().setAttribute(Constants.USER_LANGUAGE, language);
			if (country != null && !country.trim().equals("")) {
				this.getSessionContainer().getPermanentContainer().setAttribute(Constants.USER_COUNTRY, country);
			}
		}
		try {
			this.doService();
		} catch (Throwable t) {
			handleException(t);
		};
	}	
	
	protected void writeErrorsBackToClient() {
		logger.debug("IN");
		Collection<EMFAbstractError> errors = getErrorHandler().getErrors();
		try {
			writeBackToClient( new JSONFailure(errors) );
		} catch (Throwable t) {
			logger.error(t);
			throw new CannotWriteErrorsToClientException(SERVICE_NAME, "Cannot write errors to client", t);
		}
		logger.debug("OUT");
	}
	
	public abstract void doService();
    
	
	public IEngUserProfile getUserProfile() {
		return (IEngUserProfile)getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	}
	
	
	public CoreContextManager getContext() {
		if(contextManager == null) {
			IBeanContainer contextsContainer = getSpagoBISessionContainer();
			IContextRetrieverStrategy contextRetriverStartegy = new ExecutionContextRetrieverStrategy( getSpagoBIRequestContainer() );
			contextManager = new CoreContextManager(contextsContainer, contextRetriverStartegy);
		}
			
		return contextManager;		 
	}
	
	public CoreContextManager createContext(String contextId) {
		IBeanContainer contextsContainer;
		IContextRetrieverStrategy contextRetriverStartegy;
		
		contextsContainer = getSpagoBISessionContainer();
		contextRetriverStartegy = new ExecutionContextRetrieverStrategy( contextId );
		contextManager = new CoreContextManager( contextsContainer, contextRetriverStartegy );
					
		return contextManager;		 
	}
	
	public Locale getLocale() {
		Locale locale=null;
		
		RequestContainer requestContainer;
		
		locale = MessageBuilder.getDefaultLocale();
		
		requestContainer = RequestContainer.getRequestContainer();
		if(requestContainer != null){
			
			SessionContainer permSess = getSessionContainer().getPermanentContainer();
			String lang = (String)permSess.getAttribute(SpagoBIConstants.AF_LANGUAGE);
			String country = (String)permSess.getAttribute(SpagoBIConstants.AF_COUNTRY);
			
			if(lang != null){
				if(country != null) {
					locale = new Locale(lang, country);
				} else {
					locale = new Locale(lang);
				}				
			}
		}
		
		return locale;
	}
	
	public String localize(String str) {
		String lnStr;
		IMessageBuilder msgBuilder;
		
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		lnStr = msgBuilder.getUserMessage(str , SpagoBIConstants.DEFAULT_USER_BUNDLE, getLocale());	
		
		return lnStr;
	}
	
	public String getTheme() {
		return ThemesManager.getCurrentTheme( getRequestContainer() );
	}
		
	
    public void handleException(Throwable t)  {
    	// wrap excption here 
    	// dump some context
    	// release resources
    	// rethrows the wrapped exception (it will be trapped)
    	
    	SpagoBIServiceExceptionHandler.getInstance().getWrappedException(SERVICE_NAME, t);
    }
    
    protected void checkError() {
    	
    }
}
