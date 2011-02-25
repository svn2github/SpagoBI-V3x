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

package it.eng.spagobi.tools.udp.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.model.metadata.SbiResources;
import it.eng.spagobi.tools.udp.bo.Udp;
import it.eng.spagobi.tools.udp.metadata.SbiUdp;

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

/**
 * 
 * @see it.eng.spagobi.tools.udp.bo.SbiUdp
 * @author Antonella Giachino
 */
public class UdpDAOHibImpl extends AbstractHibernateDAO implements IUdpDAO {

	private static final Logger logger = Logger.getLogger(UdpDAOHibImpl.class);


	public Integer insert(SbiUdp prop) {
		logger.debug("IN");
		Session session = getSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
			id = (Integer)session.save(prop);
			tx.commit();
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			if(session != null){
				session.close();
			}
			logger.debug("OUT");
			return id;
		}
	}


	public void insert(Session session, SbiUdp prop) {
		logger.debug("IN");
		session.save(prop);
		logger.debug("OUT");
	}

	public void update(SbiUdp prop) {
		logger.debug("IN");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(prop);
			tx.commit();

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");		
	}	

	public void update(Session session, SbiUdp prop) {
		logger.debug("IN");
		session.update(prop);
		logger.debug("OUT");
	}	

	public void delete(SbiUdp prop) {
		logger.debug("IN");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(prop);
			tx.commit();

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");
	}

	public void delete(Session session, SbiUdp item) {
		logger.debug("IN");
		session.delete(item);
		logger.debug("OUT");
	}

	public void delete(Integer id) {
		logger.debug("IN");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(session.load(SbiUdp.class, id));
			tx.commit();

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");
	}


	public void delete(Session session, Integer id) {
		logger.debug("IN");
		session.delete(session.load(SbiUdp.class, id));
		logger.debug("OUT");
	}

	@SuppressWarnings("unchecked")
	public SbiUdp findById(Integer id) {
		logger.debug("IN");
		SbiUdp prop = null;
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			prop = (SbiUdp)session.get(SbiUdp.class, id);
			tx.commit();

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");
		return prop;
	}



	public Udp loadById(Integer id) {
		logger.debug("IN");
		Session session = getSession();
		Udp udp = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			SbiUdp prop = (SbiUdp)session.get(SbiUdp.class, id);
			tx.commit();
			udp=toUdp(prop);
		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");
		return udp;
	}



	/**
	 *  Load a Udp by Label
	 * @throws EMFUserError 
	 */

	public Udp loadByLabel(String label) throws EMFUserError {
		logger.debug("IN");
		Udp udp = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("label", label);
			Criteria criteria = tmpSession.createCriteria(SbiUdp.class);
			criteria.add(labelCriterrion);	
			SbiUdp hibUDP = (SbiUdp) criteria.uniqueResult();
			if (hibUDP == null) return null;
			udp = toUdp(hibUDP);				

			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the udp with label " + label, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return udp;		

	}


	/**
	 *  Load a Udp by Label and Family code
	 * @throws EMFUserError 
	 */

	public Udp loadByLabelAndFamily(String label, String family) throws EMFUserError {
		logger.debug("IN");
		Udp udp = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			// get familyId
			Criterion labelCriterrionFam = Expression.eq("valueCd", family);	
			Criteria criteria = tmpSession.createCriteria(SbiDomains.class);
			criteria.add(labelCriterrionFam);	
			Criterion labelCriterrionFamDom = Expression.eq("domainCd", "UDP_FAMILY");	
			criteria.add(labelCriterrionFamDom);	
			SbiDomains famiDom = (SbiDomains) criteria.uniqueResult();
			if (famiDom == null) return null;

			Criterion labelCriterrion = Expression.eq("label", label);
			Criteria criteria2 = tmpSession.createCriteria(SbiUdp.class);
			criteria2.add(labelCriterrion);	
			Criterion famCriterrion = Expression.eq("familyId", famiDom.getValueId());
			criteria2.add(famCriterrion);	

			SbiUdp hibUDP = (SbiUdp) criteria2.uniqueResult();
			if (hibUDP == null) return null;
			udp = toUdp(hibUDP);				

			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the udp with label " + label, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return udp;		

	}



	@SuppressWarnings("unchecked")
	public List<SbiUdp> findAll() {
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			List<SbiUdp> list = (List<SbiUdp>)session.createQuery("from SbiUdp").list();
			tx.commit();
			return list;

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
	}	


	@SuppressWarnings("unchecked")
	public List<Udp> loadAllByFamily(String familyCd) throws EMFUserError {
		logger.debug("IN");
		Session session = getSession();
		List<Udp> toReturn = null;
		// get Domain id form KPI family
		Transaction tx = null;
		try {

			Integer domainId;
			SbiDomains domain = DAOFactory.getDomainDAO().loadSbiDomainByCodeAndValue("UDP_FAMILY", familyCd);

			if(domain== null){
				logger.error("could not find domain of type UDP_FAMILY with value code "+familyCd);
				return null;
			}
			else{
				domainId = domain.getValueId();
			}


			tx = session.beginTransaction();
			Query query = session.createQuery("from SbiUdp s where s.familyId = :idFamily");
			query.setInteger("idFamily", domainId);

			List<SbiUdp> list = (List<SbiUdp>)query.list();
			if(list != null){
				toReturn = new ArrayList<Udp>();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					SbiUdp sbiUdp = (SbiUdp) iterator.next();
					Udp udp = toUdp(sbiUdp);
					toReturn.add(udp);
				}
			}
			tx.commit();

		} catch (HibernateException e) {
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}catch (EMFUserError e) {
			logger.error("error probably in getting asked UDP_FAMILY domain", e);
			if( tx != null && tx.isActive() ){
				tx.rollback();
			}
			throw e;

		}finally{
			session.close();
		}
		logger.debug("OUT");
		return toReturn;
	}	


	public Udp toUdp(SbiUdp sbiUdp){
		logger.debug("IN");
		Udp toReturn=new Udp();

		toReturn.setUdpId(sbiUdp.getUdpId());
		toReturn.setLabel(sbiUdp.getLabel());
		toReturn.setName(sbiUdp.getName());
		toReturn.setDescription(sbiUdp.getDescription());
		toReturn.setDataTypeId(sbiUdp.getTypeId());
		toReturn.setFamilyId(sbiUdp.getFamilyId());
		toReturn.setMultivalue(sbiUdp.isIsMultivalue());

		// get the type ValueCd
		if (sbiUdp.getTypeId() != null){
			Domain domain;
			try {
				domain = DAOFactory.getDomainDAO().loadDomainById(sbiUdp.getTypeId());
				toReturn.setDataTypeValeCd(domain.getValueCd());
			} catch (EMFUserError e) {
				logger.error("error in loading domain with Id "+sbiUdp.getTypeId(), e);
			}
		}
		logger.debug("OUT");
		return toReturn;
	}


	public Integer countUdp() throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer resultNumber;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			String hql = "select count(*) from SbiUdp ";
			Query hqlQuery = aSession.createQuery(hql);
			resultNumber = (Integer)hqlQuery.uniqueResult();

		} catch (HibernateException he) {
			logger.error("Error while loading the list of SbiUdp", he);	
			if (tx != null)
				tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);
		
		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		return resultNumber;
	}


	public List<SbiUdp> loadPagedUdpList(Integer offset, Integer fetchSize)
			throws EMFUserError {
		logger.debug("IN");
		List<SbiUdp> toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		Integer resultNumber;
		Query hibernateQuery;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;
		
			String hql = "select count(*) from SbiUdp ";
			Query hqlQuery = aSession.createQuery(hql);
			resultNumber = (Integer)hqlQuery.uniqueResult();
			
			offset = offset < 0 ? 0 : offset;
			if(resultNumber > 0) {
				fetchSize = (fetchSize > 0)? Math.min(fetchSize, resultNumber): resultNumber;
			}
			
			hibernateQuery = aSession.createQuery("from SbiUdp order by name");
			hibernateQuery.setFirstResult(offset);
			if(fetchSize > 0) hibernateQuery.setMaxResults(fetchSize);			

			toReturn = (List<SbiUdp>)hibernateQuery.list();	

		} catch (HibernateException he) {
			logger.error("Error while loading the list of Resources", he);	
			if (tx != null)
				tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);
		
		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		return toReturn;
	}

}

