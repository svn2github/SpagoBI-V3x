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
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * 
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class GetAnalysisMetaAction extends AbstractQbeEngineAction {

	public static final String SERVICE_NAME = "GET_ANALYSIS_META_ACTION";
	public String getActionName(){return SERVICE_NAME;}
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GetAnalysisMetaAction.class);
    
    
	public void service(SourceBean request, SourceBean response) {
		EngineAnalysisMetadata analysisMetadata = null;
		JSONObject meta;
		String rowMeta = "";
		
		logger.debug("IN");
		
		try {
			super.service(request, response);	
					
			analysisMetadata = getEngineInstance().getAnalysisMetadata();
			
			meta = new JSONObject();
			meta.put("id", analysisMetadata.getId());
			meta.put("name", analysisMetadata.getName());
			meta.put("description", analysisMetadata.getDescription());				
			meta.put("scope", analysisMetadata.getScope());
						
			
			try {
				writeBackToClient( new JSONSuccess(meta) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			} 
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}
	}
}
