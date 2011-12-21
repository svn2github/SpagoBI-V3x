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
package it.eng.spagobi.utilities.cache;


import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheSingleton implements CacheInterface{
	
    private static transient Logger logger = Logger.getLogger(CacheSingleton.class);
    
    
	private static CacheInterface instance=null;
	
	private CacheManager manager=null;
	private Cache cache=null;
	

	private CacheSingleton(){
		super();
		try {
			manager = CacheManager.create();
			cache = manager.getCache("sbi_parameters");	
		} catch (CacheException e) {
			logger.error("CacheException in inizialization",e);
		}
	
	}


	public synchronized static CacheInterface getInstance() {
		if (instance == null) {
			instance = new CacheSingleton();
		}
		return instance;
	}


	public boolean contains(String code){
		Element el=null;
		if (cache!=null){
			try {
				el = cache.get(code);
			} catch (IllegalStateException e) {
				logger.error("IllegalStateException",e);
			} catch (CacheException e) {
			logger.error("CacheException",e);
			}
			if (el==null) return false;
			if (el.getValue()==null) return false;
			return true;
		}else return false;
	}

	public void put(String code,String obj) {
		if (cache!=null){
		    Element element = new Element(code, obj);
			if (code!=null && obj != null){
				cache.put(element);
			}
		}
	}


	public String get(String code) {
		if (cache!=null && code!=null)	{
			try {
				Element el=cache.get(code);
				return (String) el.getValue();
			} catch (IllegalStateException e) {
					logger.error("IllegalStateException",e);
			} catch (CacheException e) {
				logger.error("CacheException",e);
			}
		}
		return null;
	}
}
