/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.SingletonConfig;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.tools.dataset.common.datareader.IDataReader;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class FileDataProxy extends AbstractDataProxy {
	
	String fileName;
	
	private static transient Logger logger = Logger.getLogger(FileDataProxy.class);
	
	
	public FileDataProxy() {
		
	}
			
	public FileDataProxy(String fileName) {
		
	}
	
	public IDataStore load(String statement, IDataReader dataReader) throws EMFUserError {
		throw new UnsupportedOperationException("metothd FileDataProxy not yet implemented");
	}
	
	public IDataStore load(IDataReader dataReader) {
		
		IDataStore dataStore = null;
		FileInputStream inputStream = null;
		
		try {
			// recover the file from resources!
			SingletonConfig configSingleton = SingletonConfig.getInstance();
			String pathh  = configSingleton.getConfigValue("SPAGOBI.RESOURCE_PATH_JNDI_NAME");
			String filePath= SpagoBIUtilities.readJndiResource(pathh);
			filePath += "/dataset/files";
			filePath+="/"+fileName;
			//File dir=new File(filePath);
			inputStream = new FileInputStream(filePath);
			dataStore = dataReader.read( inputStream );
		}
		catch (Throwable t) {
			throw new SpagoBIRuntimeException("Impossible to load dataset", t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error closing input stream", e);
				}
			}
		}
		return dataStore;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
