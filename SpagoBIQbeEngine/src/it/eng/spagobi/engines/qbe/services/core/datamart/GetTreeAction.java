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
package it.eng.spagobi.engines.qbe.services.core.datamart;
       
import it.eng.qbe.query.Query;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.engines.qbe.tree.ExtJsQbeTreeBuilder;
import it.eng.spagobi.engines.qbe.tree.filter.IQbeTreeEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.IQbeTreeFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeOrderEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeOrderFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeQueryEntityFilter;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetTreeAction extends AbstractQbeEngineAction {

	// INPUT PARAMETERS
	public static final String QUERY_ID = "parentQueryId";
	public static final String DATAMART_NAME = "datamartName";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GetTreeAction.class);
    
    
	public void service(SourceBean request, SourceBean response) {
		
		
		String queryId = null;
		String datamartName = null;
		Query query = null;
		
		IQbeTreeEntityFilter entityFilter = null;
		IQbeTreeFieldFilter fieldFilter = null;
		QbeTreeFilter treeFilter = null;
		
		ExtJsQbeTreeBuilder qbeBuilder = null;
		JSONArray nodes = null;
		
		logger.debug("IN");
		
		try {
			super.service(request, response);	
			
			
			queryId = getAttributeAsString(QUERY_ID); 		
			logger.debug("Parameter [" + QUERY_ID + "] is equals to [" + queryId + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
				
			logger.debug("Filtering entities list ...");			
			entityFilter = new QbeTreeAccessModalityEntityFilter();
			logger.debug("Apply entity filter [" + entityFilter.getClass().getName() + "]");
			if(queryId != null) {
				logger.debug("Filtering on query [" + queryId + "] selectd entities");
				query = getEngineInstance().getQueryCatalogue().getQuery(queryId);
				if(query != null){
					entityFilter = new QbeTreeQueryEntityFilter(entityFilter, query);
				}
			}
			entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
			logger.debug("Apply field filter [" + entityFilter.getClass().getName() + "]");
			
			logger.debug("Filtering fields list ...");	
			fieldFilter = new QbeTreeAccessModalityFieldFilter();
			logger.debug("Apply field filter [" + fieldFilter.getClass().getName() + "]");
			fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
			logger.debug("Apply field filter [" + fieldFilter.getClass().getName() + "]");
			
			treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
			
			
			qbeBuilder = new ExtJsQbeTreeBuilder(treeFilter);	 
			
			datamartName = getAttributeAsString(DATAMART_NAME);
			if (datamartName != null) {
				nodes = qbeBuilder.getQbeTree(getDataSource(), getLocale(), datamartName);			
			} else {
				nodes = new JSONArray();
				List datamartsNames = getEngineInstance().getDataSource().getDatamartNames();
				Iterator it = datamartsNames.iterator();
				while (it.hasNext()) {
					String aDatamartName = (String) it.next();
					JSONArray temp = qbeBuilder.getQbeTree(getDataSource(), getLocale(), aDatamartName);
					for (int i = 0; i < temp.length(); i++) {
						Object object = temp.get(i);
						nodes.put(object);
					}
				}
			}
			
			try {
				writeBackToClient( new JSONSuccess(nodes) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}
		

	}
}
