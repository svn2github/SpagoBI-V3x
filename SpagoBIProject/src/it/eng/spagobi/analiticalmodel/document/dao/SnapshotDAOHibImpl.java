/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSnapshots;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SnapshotDAOHibImpl extends AbstractHibernateDAO implements ISnapshotDAO {

	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO#deleteSnapshot(java.lang.Integer)
	 */
	public void deleteSnapshot(Integer idSnap) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiSnapshots hibSnapshot = (SbiSnapshots)aSession.load(SbiSnapshots.class, idSnap);
			SbiBinContents hibBinCont = hibSnapshot.getSbiBinContents();
			aSession.delete(hibSnapshot);
			aSession.delete(hibBinCont);
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

	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO#getSnapshots(java.lang.Integer)
	 */
	public List getSnapshots(Integer idBIObj)  throws EMFUserError {
		List snaps = new ArrayList();
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "from SbiSnapshots ss where ss.sbiObject.biobjId = " + idBIObj;
			String hql = "from SbiSnapshots ss where ss.sbiObject.biobjId = ?" ;
			
			Query query = aSession.createQuery(hql);
			query.setInteger(0, idBIObj.intValue());
			
			List hibSnaps = query.list();
			Iterator iterHibSnaps = hibSnaps.iterator();
			while(iterHibSnaps.hasNext()) {
				SbiSnapshots hibSnap = (SbiSnapshots)iterHibSnaps.next();
				Snapshot snap = toSnapshot(hibSnap);
				snaps.add(snap);
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
		return snaps;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO#saveSnapshot(byte[], java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void saveSnapshot(byte[] content, Integer idBIObj, String name, String description) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, idBIObj);
			SbiBinContents hibBinContent = new SbiBinContents();
			hibBinContent.setContent(content);
			updateSbiCommonInfo4Insert(hibBinContent);
			Integer idBin = (Integer)aSession.save(hibBinContent);
			// recover the saved binary hibernate object
			hibBinContent = (SbiBinContents) aSession.load(SbiBinContents.class, idBin);
			// store the object note
			SbiSnapshots hibSnap = new SbiSnapshots();
			hibSnap.setCreationDate(new Date());
			hibSnap.setDescription(description);
			hibSnap.setName(name);
			hibSnap.setSbiBinContents(hibBinContent);
			hibSnap.setSbiObject(hibBIObject);
			updateSbiCommonInfo4Insert(hibSnap);
			aSession.save(hibSnap);
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


	
	private Snapshot toSnapshot(SbiSnapshots hibSnap) {
		Snapshot snap = new Snapshot();
		snap.setBiobjId(hibSnap.getSbiObject().getBiobjId());
		snap.setBinId(hibSnap.getSbiBinContents().getId());
		snap.setDateCreation(hibSnap.getCreationDate());
		snap.setDescription(hibSnap.getDescription());
		snap.setId(hibSnap.getSnapId());
		snap.setName(hibSnap.getName());
		return snap;
	}




	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO#loadSnapshot(java.lang.Integer)
	 */
	public Snapshot loadSnapshot(Integer idSnap) throws EMFUserError {
		Snapshot snap = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiSnapshots hibSnap = (SbiSnapshots)aSession.load(SbiSnapshots.class, idSnap);
			snap = toSnapshot(hibSnap);
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
		return snap;
	}
	
	
}
