/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe.services.dataset;

import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.engines.qbe.services.core.ExecuteQueryAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * The Class GetSQLQueryAction.
 * 
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 */
public class GetSQLQueryAction extends AbstractQbeEngineAction {	
	
	// INPUT PARAMETERS
	
	// OUTPUT PARAMETERS
	
	// SESSION PARAMETRES	
	
	// AVAILABLE PUBLISHERS

	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(GetSQLQueryAction.class);
    
    public static final String ENGINE_NAME = "SpagoBIQbeEngine";
		
    public void service(SourceBean request, SourceBean response) {
		
		Query query = null;
		IStatement statement = null;
    	
    	logger.debug("IN");
       
    	try {
			super.service(request, response);	
			
			// retrieving query specified by id on request
			query = getEngineInstance().getActiveQuery();
			if (query == null) {
				query = getEngineInstance().getQueryCatalogue().getFirstQuery();
				getEngineInstance().setActiveQuery(query);
			}
			Assert.assertNotNull(query, "Query not found!!");

			// promptable filters values may come with request (read-only user modality)
			ExecuteQueryAction.updatePromptableFiltersValue(query, this);
			
			statement = getEngineInstance().getStatment();	
			statement.setParameters( getEnv() );
			
			String jpaQueryStr = statement.getQueryString();
			String sqlQuery = statement.getSqlQueryString();
			logger.debug("Executable query (HQL/JPQL): [" +  jpaQueryStr+ "]");
			logger.debug("Executable query (SQL): [" + sqlQuery + "]");
			
			JSONObject toReturn = new JSONObject();
			toReturn.put("sql", sqlQuery);
			
			try {
				writeBackToClient( new JSONSuccess(toReturn) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}		

	}

}
