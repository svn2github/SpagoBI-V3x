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
package it.eng.spagobi.engines.qbe.services.dataset;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.QbeEngineConfig;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * The Class GetDatamartsNamesAction.
 * 
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 */
public class GetDatamartsNamesAction extends AbstractQbeEngineAction {	
	
	// INPUT PARAMETERS
	public static String CALLBACK = "callback";
	
	// OUTPUT PARAMETERS
	
	// SESSION PARAMETRES	
	
	// AVAILABLE PUBLISHERS

	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(GetDatamartsNamesAction.class);
    
    public static final String ENGINE_NAME = "SpagoBIQbeEngine";
		
    public void service(SourceBean request, SourceBean response) {
    	
    	logger.debug("IN");
       
    	try {
			super.service(request, response);	
			
			List<String> datamartsName = getDatamartsName();
			
			JSONArray array = new JSONArray();
			Iterator<String> it = datamartsName.iterator();
			while (it.hasNext()) {
				String aDatamartName = it.next();
				JSONObject temp = new JSONObject();
				temp.put("datamart", aDatamartName);
				array.put(temp);
			}
			
			String callback = getAttributeAsString( CALLBACK );
			
			try {
				if(callback == null) {
					writeBackToClient( new JSONSuccess( array ));
				} else {
					writeBackToClient( new JSONSuccess( array, callback ));
				}
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
    
	private List<String> getDatamartsName() {
		logger.debug("IN");
		List<String> toReturn = new ArrayList<String>();
		File datamartsDir = QbeEngineConfig.getInstance().getQbeDataMartDir();
		File[] dirs = datamartsDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		if (dirs == null || dirs.length == 0) {
			throw new SpagoBIRuntimeException("No datamarts found!! Check configuration for datamarts repository");
		}
		for (int i = 0; i < dirs.length; i++) {
			toReturn.add(dirs[i].getName());
		}
		logger.debug("OUT");
		return toReturn;
	}

}
