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
/*
 * Created on 21-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.behaviouralmodel.analyticaldriver.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a BI Object Parameter.  
 * 
 * @author Zoppello
 */
public class BIObjectParameterDAOHibImpl extends AbstractHibernateDAO implements IBIObjectParameterDAO{
	static private Logger logger = Logger.getLogger(BIObjectParameterDAOHibImpl.class);
	
	/**
	 * Load by id.
	 * 
	 * @param id the id
	 * 
	 * @return the sbi obj par
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#loadById(java.lang.Integer)
	 */
	public SbiObjPar loadById(Integer id) throws EMFUserError {
		SbiObjPar hibObjPar = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			hibObjPar = (SbiObjPar) aSession.load(SbiObjPar.class,  id);
			tx.commit();
		} catch(HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return hibObjPar;
	}
	
	/**
	 * Load for detail by obj par id.
	 * 
	 * @param objParId the obj par id
	 * 
	 * @return the BI object parameter
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#loadForDetailByObjParId(java.lang.Integer)
	 */
	public BIObjectParameter loadForDetailByObjParId(Integer objParId) throws EMFUserError {
		
		BIObjectParameter toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiObjPar hibObjPar = (SbiObjPar) aSession.load(SbiObjPar.class,  objParId);
			
			toReturn = toBIObjectParameter(hibObjPar);
			tx.commit();
		} catch(HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return toReturn;
	}
	

	/**
	 * Modify bi object parameter.
	 * 
	 * @param aBIObjectParameter the a bi object parameter
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#modifyBIObjectParameter(it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter)
	 */
	public void modifyBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiObjPar hibObjPar = (SbiObjPar) aSession.load(SbiObjPar.class,  aBIObjectParameter.getId());

			if (hibObjPar == null) {
				logger.error("the BIObjectParameter with id="+aBIObjectParameter.getId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1033);
			}
			
			SbiObjects aSbiObject = (SbiObjects) aSession.load(SbiObjects.class, aBIObjectParameter.getBiObjectID());
			SbiParameters aSbiParameter = (SbiParameters)aSession.load(SbiParameters.class, aBIObjectParameter.getParameter().getId());
			
			hibObjPar.setSbiObject(aSbiObject);
			hibObjPar.setSbiParameter(aSbiParameter);
			hibObjPar.setLabel(aBIObjectParameter.getLabel());
			hibObjPar.setReqFl(new Short(aBIObjectParameter.getRequired().shortValue()));
			hibObjPar.setModFl(new Short(aBIObjectParameter.getModifiable().shortValue()));
			hibObjPar.setViewFl(new Short(aBIObjectParameter.getVisible().shortValue()));
			hibObjPar.setMultFl(new Short(aBIObjectParameter.getMultivalue().shortValue()));
			hibObjPar.setParurlNm(aBIObjectParameter.getParameterUrlName());
			Integer oldPriority = hibObjPar.getPriority();
			Integer newPriority = aBIObjectParameter.getPriority();
			if (!oldPriority.equals(newPriority)) {
				Query query = null;
				if (oldPriority.intValue() > newPriority.intValue()) {
					String hqlUpdateShiftRight = "update SbiObjPar s set s.priority = (s.priority + 1) where s.priority >= " 
						+ newPriority + " and s.priority < " + oldPriority + "and s.sbiObject.biobjId = " + aSbiObject.getBiobjId();
					query = aSession.createQuery(hqlUpdateShiftRight);
				} else {
					String hqlUpdateShiftLeft = "update SbiObjPar s set s.priority = (s.priority - 1) where s.priority > " 
						+ oldPriority + " and s.priority <= " + newPriority + "and s.sbiObject.biobjId = " + aSbiObject.getBiobjId();
					query = aSession.createQuery(hqlUpdateShiftLeft);
				}
				query.executeUpdate();
			}
			hibObjPar.setPriority(newPriority);
			hibObjPar.setProg(new Integer(1));
			
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
	 * Insert bi object parameter.
	 * 
	 * @param aBIObjectParameter the a bi object parameter
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#insertBIObjectParameter(it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter)
	 */
	public void insertBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiObjects aSbiObject = (SbiObjects) aSession.load(SbiObjects.class, aBIObjectParameter.getBiObjectID());
			SbiParameters aSbiParameter = (SbiParameters) aSession.load(SbiParameters.class, aBIObjectParameter.getParameter().getId());
			
			SbiObjPar hibObjectParameterNew = new SbiObjPar();
			hibObjectParameterNew.setSbiObject(aSbiObject);
			hibObjectParameterNew.setSbiParameter(aSbiParameter);
			hibObjectParameterNew.setProg(new Integer(1));
			hibObjectParameterNew.setLabel(aBIObjectParameter.getLabel());
			hibObjectParameterNew.setReqFl(new Short(aBIObjectParameter.getRequired().shortValue()));
			hibObjectParameterNew.setModFl(new Short(aBIObjectParameter.getModifiable().shortValue()));
			hibObjectParameterNew.setViewFl(new Short(aBIObjectParameter.getVisible().shortValue()));
			hibObjectParameterNew.setMultFl(new Short(aBIObjectParameter.getMultivalue().shortValue()));
			hibObjectParameterNew.setParurlNm(aBIObjectParameter.getParameterUrlName());
			
			String hqlUpdateShiftRight = "update SbiObjPar s set s.priority = (s.priority + 1) where s.priority >= " 
				+ aBIObjectParameter.getPriority() + " and s.sbiObject.biobjId = " + aSbiObject.getBiobjId();
			Query query = aSession.createQuery(hqlUpdateShiftRight);
			query.executeUpdate();
			
			hibObjectParameterNew.setPriority(aBIObjectParameter.getPriority());
			
			aSession.save(hibObjectParameterNew);
			
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
	 * Erase bi object parameter.
	 * 
	 * @param aBIObjectParameter the a bi object parameter
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#eraseBIObjectParameter(it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter)
	 */
	public void eraseBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiObjPar hibObjPar = (SbiObjPar) aSession.load(SbiObjPar.class,  aBIObjectParameter.getId());
			
			if (hibObjPar == null) {		
				logger.error("the BIObjectParameter with id="+aBIObjectParameter.getId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1034);
			}
			
			aSession.delete(hibObjPar);
			
			Integer biobjId = hibObjPar.getSbiObject().getBiobjId();
			
			String hqlUpdateShiftRight = "update SbiObjPar s set s.priority = (s.priority - 1) where s.priority >= " 
				+ aBIObjectParameter.getPriority() + "and s.sbiObject.biobjId = " + biobjId;
			Query query = aSession.createQuery(hqlUpdateShiftRight);
			query.executeUpdate();
			
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
	 * Gets the document labels list using parameter.
	 * 
	 * @param parId the par id
	 * 
	 * @return the document labels list using parameter
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#getDocumentLabelsListUsingParameter(java.lang.Integer)
	 */
	public List getDocumentLabelsListUsingParameter(Integer parId) throws EMFUserError {
		
		List toReturn = new ArrayList();
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = 
					"select " +
					"	distinct(obj.label) " +
					"from " +
					"	SbiObjects obj, SbiObjPar objPar " +
					"where " +
					"	obj.biobjId = objPar.sbiObject.biobjId and " +
					"	objPar.sbiParameter.parId = " + parId;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			
			toReturn = result;
			
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
		return toReturn;
	}
	
	/**
	 * Load bi object parameters by id.
	 * 
	 * @param biObjectID the bi object id
	 * 
	 * @return the list
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO#loadBIObjectParametersById(java.lang.Integer)
	 */
	public List loadBIObjectParametersById(Integer biObjectID) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		List resultList = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			String hql = "from SbiObjPar s where s.sbiObject.biobjId = " + biObjectID + " order by s.priority asc";

			Query hqlQuery = aSession.createQuery(hql);
			List hibObjectPars = hqlQuery.list();

			Iterator it = hibObjectPars.iterator();
			int count = 1;
			while (it.hasNext()) {
				BIObjectParameter aBIObjectParameter = toBIObjectParameter((SbiObjPar) it.next());
				//*****************************************************************
				//**************** START PRIORITY CONTROL *************************
				//*****************************************************************
				Integer priority = aBIObjectParameter.getPriority();
				// if the priority is different from the value expected, 
				// recalculates it for all the parameter of the document
				if (priority == null || priority.intValue() != count) {
					logger.error("The priorities of the biparameters for the document with id = " + biObjectID + " are not sorted. Priority recalculation starts.");
					recalculateBiParametersPriority(biObjectID, aSession);
					// restarts this method in order to load updated priorities
					aBIObjectParameter.setPriority(new Integer(count));
				}
				count++;
				//*****************************************************************
				//**************** END PRIORITY CONTROL ***************************
				//*****************************************************************
				resultList.add(aBIObjectParameter);
			}
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
		return resultList;
	 }
	
	/**
	 * Recalculates the priority of all the BiParameters of the document, identified by its biObjectID,
	 * in the Hibernate session passed at input.
	 * 
	 * @param biObjectID The id of the document
	 * @param aSession The Hibernate session
	 */
	public void recalculateBiParametersPriority(Integer biObjectID, Session aSession) {
			String hql = "from SbiObjPar s where s.sbiObject.biobjId = " + biObjectID + " order by s.priority asc";
			Query hqlQuery = aSession.createQuery(hql);
			List hibObjectPars = hqlQuery.list();
			Iterator it = hibObjectPars.iterator();
			int count = 1;
			while (it.hasNext()) {
				SbiObjPar aSbiObjPar = (SbiObjPar) it.next();
				aSbiObjPar.setPriority(new Integer(count));
				count++;
				aSession.save(aSbiObjPar);
			}
	}


	/**
	 * From the hibernate BI object parameter at input, gives
	 * the corrispondent <code>BIObjectParameter</code> object.
	 * 
	 * @param hiObjPar The hybernate BI object parameter
	 * 
	 * @return The corrispondent <code>BIObjectParameter</code>
	 */
	public BIObjectParameter toBIObjectParameter(SbiObjPar hiObjPar){
		BIObjectParameter aBIObjectParameter = new BIObjectParameter();
		aBIObjectParameter.setId(hiObjPar.getObjParId());
		aBIObjectParameter.setLabel(hiObjPar.getLabel());
		aBIObjectParameter.setModifiable(new Integer(hiObjPar.getModFl().intValue()));
		aBIObjectParameter.setMultivalue(new Integer(hiObjPar.getMultFl().intValue()));
		aBIObjectParameter.setBiObjectID(hiObjPar.getSbiObject().getBiobjId());
		aBIObjectParameter.setParameterUrlName(hiObjPar.getParurlNm());
		aBIObjectParameter.setParID(hiObjPar.getSbiParameter().getParId());
		aBIObjectParameter.setRequired(new Integer(hiObjPar.getReqFl().intValue()));
		aBIObjectParameter.setVisible(new Integer(hiObjPar.getViewFl().intValue()));
		aBIObjectParameter.setPriority(hiObjPar.getPriority());
		aBIObjectParameter.setProg(hiObjPar.getProg());
		Parameter parameter = new Parameter();
		parameter.setId(hiObjPar.getSbiParameter().getParId());
		aBIObjectParameter.setParameter(parameter);
		return aBIObjectParameter;
	}


	
	
	
	
}
