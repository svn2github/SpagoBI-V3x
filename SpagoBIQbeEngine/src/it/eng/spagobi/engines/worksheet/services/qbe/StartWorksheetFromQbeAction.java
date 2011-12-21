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
package it.eng.spagobi.engines.worksheet.services.qbe;

import java.util.Map;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.catalogue.QueryCatalogue;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;
import it.eng.spagobi.engines.worksheet.WorksheetEngineInstance;
import it.eng.spagobi.engines.worksheet.services.initializers.WorksheetEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import org.apache.log4j.Logger;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *          Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class StartWorksheetFromQbeAction extends WorksheetEngineStartAction {

	private static final long serialVersionUID = 7152366345295485818L;

	public static transient Logger logger = Logger.getLogger(StartWorksheetFromQbeAction.class);
    
    public static final String ENGINE_NAME = "SpagoBIWorksheetEngine";
	
    @Override
	public SourceBean getTemplateAsSourceBean() {
//    	SourceBean toReturn = WorksheetXMLTemplateParser.getEmtpyWorsheetTemplate();
//    	return toReturn;
    	return null;
	}
    
    @Override
    protected QbeEngineInstance getQbeEngineInstance(WorksheetEngineInstance worksheetEngineInstance) {
    	QbeEngineInstance qbeEngineInstance = (QbeEngineInstance) getAttributeFromSession(EngineConstants.ENGINE_INSTANCE);
    	// the following instructions are needed because: if a user enter a saved query and then click on the worksheet tab,
    	// there is no active query and therefore worksheet initialization fails.
    	Query query = qbeEngineInstance.getActiveQuery();
    	if (query == null) {
    		QueryCatalogue catalogue = qbeEngineInstance.getQueryCatalogue();
    		if (catalogue == null || catalogue.getAllQueries().size() == 0) {
    			logger.error("Query catalogue is empty: no queries are defined");
    			throw new SpagoBIEngineRuntimeException("No queries are defined");
    		}
    		qbeEngineInstance.setActiveQuery(catalogue.getFirstQuery()); // this builds the statement object
    	}
		return qbeEngineInstance;
	}
    
    @Override
    protected boolean goToWorksheetPreentation() {
		return false;
	}

    // We must override the getEnv method, since the WorksheetEngine must inherit the same environment 
    // (that contains analytical drivers) of Qbe engine instance
	@Override
	public Map getEnv() {
		QbeEngineInstance qbeEngineInstance = (QbeEngineInstance) getAttributeFromSession(EngineConstants.ENGINE_INSTANCE);
		return qbeEngineInstance.getEnv();
	}
    
    
}
