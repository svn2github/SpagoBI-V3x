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

import org.apache.log4j.Logger;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.query.Query;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.EngineConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractQbeEngineAction.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class AbstractQbeEngineAction extends AbstractEngineAction {
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(AbstractQbeEngineAction.class);
    
	
    public QbeEngineInstance getEngineInstance() {
    	return (QbeEngineInstance)getAttributeFromSession( EngineConstants.ENGINE_INSTANCE );
    }
    
	public DataMartModel getDatamartModel() {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return null;
    	}
    	return qbeEngineInstance.getDatamartModel();
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return;
    	}
    	qbeEngineInstance.setDatamartModel(datamartModel);
	}
	
	
	public Query getQuery() {
		QbeEngineInstance qbeEngineInstance  = null;
    	qbeEngineInstance = getEngineInstance();
    	if(qbeEngineInstance == null) {
    		return null;
    	}
    	return qbeEngineInstance.getActiveQuery();
	}	
}
