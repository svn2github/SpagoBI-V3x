/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe;

import it.eng.qbe.datasource.naming.IDataSourceNamingStrategy;
import it.eng.qbe.datasource.naming.SimpleDataSourceNamingStrategy;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.file.FileUtils;

import java.io.File;
import java.util.Locale;

import org.apache.log4j.Logger;



/**
 * The Class QbeEngineConf.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineConfig {
	
	
	private EnginConf engineConfig;
	
	private Locale locale = null;
	
	
	public static String QBE_MODE = "QBE_MODE";
	public static String QBE_DATAMART_DIR = "QBE_DATAMART_DIR";
	public static String WORKSHEET_DIR = "WORKSHEET_DIR";
	public static String WORKSHEET_IMAGES_MAX_SIZE = "WORKSHEET_IMAGES_MAX_SIZE";
	public static String WORKSHEET_IMAGES_MAX_NUMBER = "WORKSHEET_IMAGES_MAX_NUMBER";
	public static String QBE_DATAMART_RETRIVER = "QBE_DATAMART_RETRIVER";	
	public static String SPAGOBI_SERVER_URL = "SPAGOBI_SERVER_URL";
	public static String DEFAULT_SPAGOBI_SERVER_URL = "http://localhost:8080/SpagoBI";
	
	
	private static transient Logger logger = Logger.getLogger(QbeEngineConfig.class);
	
	// -- singleton pattern --------------------------------------------
	private static QbeEngineConfig instance;
	
	public static QbeEngineConfig getInstance(){
		if(instance==null) {
			instance = new QbeEngineConfig();
		}
		return instance;
	}
	
	private QbeEngineConfig() {
		setEngineConfig( EnginConf.getInstance() );
	}
	// -- singleton pattern --------------------------------------------
		
	// -- CORE SETTINGS ACCESSOR Methods--------------------------------
	
	public File getQbeDataMartDir() {
		File qbeDataMartDir;
		
		qbeDataMartDir = null;
		
		String property = getProperty( QBE_DATAMART_DIR );
		if( property != null ) {
			String baseDirStr = getEngineResourcePath();
			File baseDir = new File(baseDirStr);																			
			if( !FileUtils.isAbsolutePath( property ) )  {
				property = baseDir + System.getProperty("file.separator") + property;
				qbeDataMartDir = new File(property);
			}
		} 
				
		return qbeDataMartDir;		
	}
	
	// engine settings
	
	public String getEngineResourcePath() {
		String path = null;
		if(getEngineConfig().getResourcePath() != null) {
			path = getEngineConfig().getResourcePath() + System.getProperty("file.separator") + "qbe";
		} else {
			path = ConfigSingleton.getRootPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "qbe";
		}
		
		return path;
	}
	
	public boolean isWebModalityActive() {
		boolean isWebModalityActive;
		
		isWebModalityActive = true;
		
		String property = getProperty( QBE_MODE );
		if( property != null ) {
			isWebModalityActive = property.equalsIgnoreCase("WEB");
		} 
		
		
		return isWebModalityActive;		
	}
	
	
	
	// utils 
	
	public String getProperty(String propertName) {
		String propertyValue = null;		
		SourceBean sourceBeanConf;
		
		Assert.assertNotNull( getConfigSourceBean(), "Impossible to parse engine-config.xml file");
		
		sourceBeanConf = (SourceBean) getConfigSourceBean().getAttribute( propertName);
		if(sourceBeanConf != null) {
			propertyValue  = (String) sourceBeanConf.getCharacters();
			logger.debug("Configuration attribute [" + propertName + "] is equals to: [" + propertyValue + "]");
		}
		
		return propertyValue;		
	}
	
	public File getWorksheetDir() {
		File worksheetDir;
		
		worksheetDir = null;
		
		String property = getProperty( WORKSHEET_DIR );
		if( property != null ) {
			String baseDirStr = getEngineResourcePath();
			File baseDir = new File(baseDirStr);																			
			if( !FileUtils.isAbsolutePath( property ) )  {
				String fs = File.separator;
				property = baseDir + fs + property;
				worksheetDir = new File(property);
			}
		} 
				
		return worksheetDir;		
	}
	
	
	public File getWorksheetImagesDir() {
		File worksheetDir = getWorksheetDir();
		File worksheetImagesDir = new File(worksheetDir, "images");
		if (worksheetImagesDir.exists() && !worksheetImagesDir.isDirectory()) {
			throw new SpagoBIEngineRuntimeException("Cannot create worksheet images dir! A file with the same name exists!");
		}
		if (!worksheetImagesDir.exists()) {
			boolean success = worksheetImagesDir.mkdirs();
			if (!success) {
				throw new SpagoBIEngineRuntimeException("Cannot create worksheet images dir!");
			}
		}				
		return worksheetImagesDir;		
	}
	
	public int getWorksheetImagesMaxSize() {
		String property = getProperty( WORKSHEET_IMAGES_MAX_SIZE );
		int toReturn = Integer.parseInt(property);
		return toReturn;
	}
	
	public int getWorksheetImagesMaxNumber() {
		String property = getProperty( WORKSHEET_IMAGES_MAX_NUMBER );
		int toReturn = Integer.parseInt(property);
		return toReturn;
	}

	public Integer getResultLimit() {
		Integer resultLimit = null;
		String resultLimitStr = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.value");
		if(resultLimitStr == null || resultLimitStr.equalsIgnoreCase("none")) {
			resultLimit = null;
		} else {
			try {
				resultLimit = new Integer(resultLimitStr);
			} catch(Throwable t) {
				logger.error(t);
			}
		}
		return resultLimit;
	}
	
	/**
	 * Returns true if the query must be validated before saving, false otherwise
	 * @return true if the query must be validated before saving, false otherwise
	 */
	public boolean isQueryValidationEnabled() {
		boolean isEnabled = false;
		String isEnabledStr = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-VALIDATION.enabled");
		isEnabled = Boolean.parseBoolean(isEnabledStr);
		return isEnabled;
	}
	
	/**
	 * Returns true if query validation before saving is blocking (i.e. incorrect queries cannot be saved), false otherwise
	 * @return true if query validation before saving is blocking (i.e. incorrect queries cannot be saved), false otherwise
	 */
	public boolean isQueryValidationBlocking() {
		boolean isBlocking = false;
		String isBlockingStr = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-VALIDATION.isBlocking");
		isBlocking = Boolean.parseBoolean(isBlockingStr);
		return isBlocking;
	}
	
	public boolean isMaxResultLimitBlocking() {
		boolean isBlocking = false;
		String isBlockingStr = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-SQL-RESULT-LIMIT.isBlocking");
		isBlocking = Boolean.parseBoolean(isBlockingStr);
		return isBlocking;
	}
	
	/**
	 * Gets the naming strategy.
	 * 
	 * @return the naming strategy
	 */
	public IDataSourceNamingStrategy getNamingStrategy() {
		return new SimpleDataSourceNamingStrategy();
	}
	
	
	// -- ACCESS Methods  --------------------------------------------
	
	public EnginConf getEngineConfig() {
		return engineConfig;
	}

	public void setEngineConfig(EnginConf engineConfig) {
		this.engineConfig = engineConfig;
	}
	
	public SourceBean getConfigSourceBean() {
		return getEngineConfig().getConfig();
	}
	
	public int getQueryExecutionTimeout() {
		int timeout = 300000;
		String timeoutStr = (String) ConfigSingleton.getInstance()
				.getAttribute("QBE.QBE-TIMEOUT-FOR-QUERY-EXECUTION.value");
		if (timeoutStr != null) {
			try {
				timeout = Integer.parseInt(timeoutStr);
			} catch (Throwable t) {
				logger.error(
						"Wrong value for 'value' attribute in tag QBE-TIMEOUT-FOR-QUERY-EXECUTION in qbe.xml: it must be an integer and instead it is "
								+ timeoutStr + ". Using default that is 300000",
						t);
			}
		} else {
			logger.warn("No value for 'value' attribute in tag QBE-TIMEOUT-FOR-QUERY-EXECUTION in qbe.xml. Using default that is 300000");
		}
		logger.debug("Returning " + timeout);
		return timeout;
	}
	
	public int getCrosstabCellLimit() {
		int cellLimit = 0;
		try {
			cellLimit = new Integer((String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CROSSTAB-CELLS-LIMIT.value")) ;
		} catch (Exception e) {
			logger.debug("No cell limit has been defined in the qbe.xml");
		}
		return cellLimit;
	}
	
	public int getCrosstabCFDecimalPrecision() {
		int precision = 2;
		try {
			precision = new Integer((String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CROSSTAB-CALCULATEDFIELDS-DECIMAL.value")) ;
		} catch (Exception e) {
			logger.debug("No decimal precision for the crosstab has been defined in the qbe.xml");
		}
		return precision;
	}
}
