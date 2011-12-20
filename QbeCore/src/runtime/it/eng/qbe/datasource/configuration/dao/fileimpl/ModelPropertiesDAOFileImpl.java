/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
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
package it.eng.qbe.datasource.configuration.dao.fileimpl;

import it.eng.qbe.datasource.configuration.dao.IModelPropertiesDAO;
import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.properties.SimpleModelProperties;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;

/**
 * Implementation of IModelPropertiesDAO that read model properties from a property
 * file named qbe.properties stored withing the jar file passed in as argument to
 * the class costructore.
 * 
 * NOTE: this class does not support interface methods saveProperties. Calling it will
 * cause an exception
 * 
 * @author Andrea Gioia
 */
public class ModelPropertiesDAOFileImpl implements IModelPropertiesDAO {
	
	File modelJarFile;
	
	private static final String PROPERTIES_FILE_NAME = "qbe.properties";
	
	 public static transient Logger logger = Logger.getLogger(IModelPropertiesDAO.class);
	
	public ModelPropertiesDAOFileImpl(File file) {
		modelJarFile = file;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.DatamartPropertiesDAO#loadDatamartProperties(java.lang.String)
	 */
	public IModelProperties loadModelProperties() {
		SimpleModelProperties properties;
		
		JarFile jf = null;
		try {
			jf = new JarFile( modelJarFile );
			properties = loadQbePropertiesFormJarFile(jf);
		} catch (IOException e) {
			logger.error("Error loadin properties",e);
			return new SimpleModelProperties();
		}finally{
			try {
				if(jf!=null){
					jf.close();	
				}
			} catch (Exception e2) {
				logger.error("Error closing the jar file",e2);
			}
			
		}			
		
		return properties;	
	}

	protected SimpleModelProperties loadQbePropertiesFormJarFile(JarFile jf){
		Properties prop = null;
		
		try{
			ZipEntry ze = jf.getEntry(PROPERTIES_FILE_NAME);
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			} else {
				prop = new Properties();
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
			return new SimpleModelProperties();
		}
		return new SimpleModelProperties(prop);
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.dao.DatamartPropertiesDAO#saveDatamartProperties(java.lang.String, it.eng.qbe.bo.DatamartProperties)
	 */
	public void saveModelProperties(IModelProperties modelProperties) {
		throw new SpagoBIRuntimeException("saveDatamartProperties method not supported");
	}
}
