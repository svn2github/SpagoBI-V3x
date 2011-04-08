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
package it.eng.spagobi.commons.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.bo.Config;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.metadata.SbiConfig;
import it.eng.spagobi.commons.metadata.SbiDomains;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

public class ConfigDAO extends AbstractHibernateDAO implements IConfigDAO {

    static private Logger logger = Logger.getLogger(ConfigDAO.class);
    
    /* (non-Javadoc)
     * @see it.eng.spagobi.commons.dao.IUserFunctionalityDAO#loadAllConfigParameters()
     */
    public List loadAllConfigParameters() throws Exception{
    	logger.debug("IN");
		
		ArrayList toReturn = new ArrayList();
		Session aSession = null;
		Transaction tx = null;
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			List roleTypes = new ArrayList();
			
			Query hibQuery = aSession.createQuery(" from SbiConfig");
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();			
			while (it.hasNext()) {			
				SbiConfig hibMap = (SbiConfig) it.next();	
				if (hibMap != null) {
					Config biMap = hibMap.toConfig();	
					toReturn.add(biMap);
				}
			}
			tx.commit();
		}catch(HibernateException he){
			logger.error("HibernateException during query",he);
			
			if (tx != null) tx.rollback();	
	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
		return toReturn;
	
    }

    /**
	 * Load configuration by id.
	 * 
	 * @param id the configuration id
	 * 
	 * @return the config object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.common.bo.dao.ISbiConfigDAO#loadConfigParametersById(integer)
	 */
    public Config loadConfigParametersById(String id) throws Exception {
    	logger.debug("IN");
    	Config toReturn = null;
		Session tmpSession = null;
		Transaction tx = null;

		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			SbiConfig hibMap = (SbiConfig)tmpSession.load(SbiConfig.class,  id);
			toReturn = hibMap.toConfig();
			tx.commit();

		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();

			}
			logger.debug("OUT");
		}		
		return toReturn;
    }
    
    /**
	 * Load configuration by complete label.
	 * 
	 * @param label the configuration label
	 * 
	 * @return the config object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.common.bo.dao.ISbiConfigDAO#loadConfigParametersById(string)
	 */
    public Config loadConfigParametersByLabel(String label) throws Exception{
    	logger.debug("IN");
    	Config toReturn = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("label",label);
			Criteria criteria = tmpSession.createCriteria(SbiConfig.class);
			criteria.add(labelCriterrion);	
	
			SbiConfig hibConfig = (SbiConfig) criteria.uniqueResult();
			if (hibConfig == null) return null;
			toReturn = hibConfig.toConfig();				

			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			logger.debug("OUT");
		}
		return toReturn;		
    }
    
    /**
	 * Load configuration by a property node.
	 * 
	 * @param prop the configuration label
	 * 
	 * @return a list with all children of the property node
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.common.bo.dao.ISbiConfigDAO#loadConfigParametersByProperties(string)
	 */
    public List loadConfigParametersByProperties(String prop) throws Exception{
    	logger.debug("IN");
    	
		ArrayList toReturn = new ArrayList(); 
		List allConfig = loadAllConfigParameters();
		//filter with the 'prop' parameter
		Iterator it = allConfig.iterator();			
		while (it.hasNext()) {			
			Config tmpConf = (Config) it.next();	
			if (tmpConf.isActive() && tmpConf.getLabel().startsWith(prop))
				toReturn.add(tmpConf);
		}
		
		return toReturn;
    }
    
    public SbiConfig fromConfig(Config Config){
		SbiConfig hibConfig = new SbiConfig();
		hibConfig.setValueCheck(Config.getValueCheck());
		hibConfig.setId(Config.getId());
		hibConfig.setName(Config.getName());
		hibConfig.setLabel(Config.getLabel());
		hibConfig.setDescription(Config.getDescription());
		return hibConfig;
	}
	
	/**
	 * Save domain by id.
	 * 
	 * @param id the id
	 * 
	 * @return void
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
    public void saveConfig(Config config) throws EMFUserError {
    	// TODO Auto-generated method stub
    	Config toSave = null;
    	Session aSession = null;
    	Transaction tx = null;

    	try {
    		aSession = getSession();
    		tx = aSession.beginTransaction();
    				
    		aSession.save(this.fromConfig(config));
    	
    		tx.commit();
    	
    	} catch (HibernateException he) {
    		logException(he);

    		if (tx != null)
    			tx.rollback();

    		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

    	} finally {
    		if (aSession!=null){
    			if (aSession.isOpen()) aSession.close();
    		}
    	}
    	
    }

    /**
     * Update domain by id.
     * 
     * @param id the id
     * 
     * @return void
     * 
     * @throws EMFUserError the EMF user error
     * 
     */
    public void updateCongig(Config config) throws EMFUserError {
    	// TODO Auto-generated method stub
    	Session aSession = null;
    	Transaction tx = null;

    	try {
    		aSession = getSession();
    		tx = aSession.beginTransaction();
    		
    		aSession.update(this.fromConfig(config));
    	
    		tx.commit();
    	
    	} catch (HibernateException he) {
    		logException(he);

    		if (tx != null)
    			tx.rollback();

    		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

    	} finally {
    		if (aSession!=null){
    			if (aSession.isOpen()) aSession.close();
    		}
    	}
    	
    }

    /**
     * Delete domain by id.
     * 
     * @param id the id
     * 
     * @return void
     * 
     * @throws EMFUserError the EMF user error
     * 
     */
    public void delete(String codeConfig, String codeValue)  throws EMFUserError {
    	Session sess = null;
    	Transaction tx = null;

    	try {
    		sess = getSession();
    		tx = sess.beginTransaction();
    		
    		Criterion aCriterion = Expression.and(Expression.eq("domainCd",
    				codeConfig), Expression.eq("valueCd", codeValue));
    		Criteria criteria = sess.createCriteria(SbiDomains.class);
    		criteria.add(aCriterion);
    		SbiDomains aSbiDomains = (SbiDomains) criteria.uniqueResult();
    		if (aSbiDomains!=null) sess.delete(aSbiDomains);
    		tx.commit();
    		
    	} catch (HibernateException he) {
    		logException(he);

    		if (tx != null)
    			tx.rollback();

    		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

    	} finally {
    		if (sess!=null){
    			if (sess.isOpen()) sess.close();
    		}
    	}
    }

}
