/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

package it.eng.spagobi.commons;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import it.eng.spagobi.commons.dao.ConfigDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IConfigDAO;
import it.eng.spagobi.commons.metadata.SbiConfig;
import it.eng.spagobi.services.common.EnginConf;

/**
 * Defines the Singleton SpagoBI implementations.
 * 
 * @author Monia Spinelli
 */

public class SingletonConfig {

	private static SingletonConfig instance = null;
	private static transient Logger logger = Logger.getLogger(SingletonConfig.class);
	
	private HashMap<String, String> cache=null;
	
	public synchronized static SingletonConfig getInstance() {
		if (instance == null)
			instance = new SingletonConfig();
		return instance;
	}

	private SingletonConfig() {
		logger.debug("Resource: Table SbiConfig");
		
		IConfigDAO dao= new ConfigDAO();  // sostituirlo con la DAOFactory.getConfigDAO()  che c'�
		try {
			List<SbiConfig> allConfig= dao.loadAllConfigParameters();
			
			for (SbiConfig config: allConfig ) {
				cache.put(config.getLabel(), config.getValueCheck());
				logger.info("Add: "+config.getLabel() +" / "+config.getValueCheck());
			}
			
		} catch (Exception e) {
			logger.error("Impossible to load configuration for report engine",e);
		}
	}

	/**
	 * Gets the config.
	 * 
	 * @return SourceBean contain the configuration
	 * 
	 * QUESTO METODO LO UTILIZZI PER LEGGERE LA CONFIGURAZIONE DEI SINGOLI ELEMENTI:
	 * ES:    String configurazione= SingletonConfig.getInstance().getConfigValue("home.banner");
	 */
	public synchronized String getConfigValue(String key) {
		return cache.get(key);

	}
	/**
	 * QUESTO METODO LO UTILIZZI ALL'INTERNO DEL SERVIZIO DI SALVATAGGIO CONFIGURAZIONE
	 * OGNI VOLTA CHE SALVIAMO UNA RIGA SVUOTIAMO LA CACHE
	 */
	public synchronized void clearCache() {
		instance=null;
	}	
}