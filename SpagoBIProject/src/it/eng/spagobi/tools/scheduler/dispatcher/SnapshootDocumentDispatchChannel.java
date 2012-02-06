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
package it.eng.spagobi.tools.scheduler.dispatcher;

import java.util.List;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.massiveExport.services.StartMassiveScheduleAction;
import it.eng.spagobi.tools.scheduler.to.DispatchContext;
import it.eng.spagobi.tools.scheduler.utils.SchedulerUtilities;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SnapshootDocumentDispatchChannel implements IDocumentDispatchChannel {
	
	private DispatchContext dispatchContext;
	
	// logger component
	private static Logger logger = Logger.getLogger(SnapshootDocumentDispatchChannel.class); 
	
	public SnapshootDocumentDispatchChannel(DispatchContext dispatchContext) {
		this.dispatchContext = dispatchContext;
	}
	
	public void setDispatchContext(DispatchContext dispatchContext) {
		this.dispatchContext = dispatchContext;
	}

	public void close() {
		
	}
	
	public boolean canDispatch(BIObject document)  {
		return true;
	}
	
	public boolean dispatch(BIObject document, byte[] executionOutput) {
		IEngUserProfile profile;
		String descriptionSuffix; 
		
		logger.debug("IN");
		try {
			profile = dispatchContext.getUserProfile();
			descriptionSuffix = dispatchContext.getDescriptionSuffix();
			
			String snapName = dispatchContext.getSnapshotName();
			if( (snapName==null) || snapName.trim().equals("")) {
				throw new Exception("Document name not specified");
			}
			
			if (snapName.length() > 100) {
				logger.warn("Snapshot name [" + snapName + "] exceeds maximum length that is 100, it will be truncated");
				snapName = snapName.substring(0, 100);
			}

			String snapDesc = dispatchContext.getSnapshotDescription() != null ? dispatchContext.getSnapshotDescription() : "";
			snapDesc += descriptionSuffix;
			if (snapDesc.length() > 1000) {
				logger.warn("Snapshot description [" + snapDesc + "] exceeds maximum length that is 1000, it will be truncated");
				snapDesc = snapDesc.substring(0, 1000);
			}

			String historylengthStr = dispatchContext.getSnapshotHistoryLength();
			// store document as snapshot
			ISnapshotDAO snapDao = DAOFactory.getSnapshotDAO();
			snapDao.setUserProfile(profile);
			// get the list of snapshots
			List allsnapshots = snapDao.getSnapshots(document.getId());
			// get the list of the snapshot with the store name
			List snapshots = SchedulerUtilities.getSnapshotsByName(allsnapshots, snapName);
			// get the number of previous snapshot saved
			int numSnap = snapshots.size();
			// if the number of snapshot is greater or equal to the history length then
			// delete the unecessary snapshots
			if((historylengthStr!=null) && !historylengthStr.trim().equals("")){
				try{
					Integer histLenInt = new Integer(historylengthStr);
					int histLen = histLenInt.intValue();
					if(numSnap>=histLen){
						int delta = numSnap - histLen;
						for(int i=0; i<=delta; i++) {
							Snapshot snap = SchedulerUtilities.getNamedHistorySnapshot(allsnapshots, snapName, histLen-1);
							Integer snapId = snap.getId();
							snapDao.deleteSnapshot(snapId);
						}
					}
				} catch(Exception e) {
					logger.error("Error while deleting object snapshots", e);
				}
			}
			snapDao.saveSnapshot(executionOutput, document.getId(), snapName, snapDesc);	
			
		} catch (Exception e) {
			logger.error("Error while saving schedule result as new snapshot", e);
			return false;
		} finally{
			logger.debug("OUT");
		}
		
		return true;
	}
}
