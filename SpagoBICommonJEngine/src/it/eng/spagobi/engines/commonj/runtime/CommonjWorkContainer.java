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

package it.eng.spagobi.engines.commonj.runtime;

import it.eng.spagobi.utilities.threadmanager.WorkManager;

import javax.servlet.http.HttpSession;

import commonj.work.Work;
import commonj.work.WorkItem;

import de.myfoo.commonj.work.FooRemoteWorkItem;

/**
 * stato del work, contenuto nella mappa
 * @author bernabei
 *
 */
public class CommonjWorkContainer {

	String pid=null;
	
	Work work=null;

	CommonjWorkListener listener;

	String name=null;

	WorkManager wm=null;

	FooRemoteWorkItem fooRemoteWorkItem=null;
	WorkItem workItem = null;	

	public FooRemoteWorkItem getFooRemoteWorkItem() {
		return fooRemoteWorkItem;
	}

	public void setFooRemoteWorkItem(FooRemoteWorkItem fooRemoteWorkItem) {
		this.fooRemoteWorkItem = fooRemoteWorkItem;
	}

	public WorkManager getWm() {
		return wm;
	}

	public void setWm(WorkManager wm) {
		this.wm = wm;
	}


	public WorkItem getWorkItem() {
		return workItem;
	}

	public void setWorkItem(WorkItem workItem) {
		this.workItem = workItem;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public CommonjWorkListener getListener() {
		return listener;
	}

	public void setListener(CommonjWorkListener listener) {
		this.listener = listener;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 *  No more used
	 * @param documentId
	 * @param session
	 */
	
	public void setInSession(String documentId,HttpSession session){
		session.setAttribute("SBI_PROCESS_"+documentId, this);
	}

	
	
	public boolean isInSession(String documentId,HttpSession session){
		Object o=session.getAttribute("SBI_PROCESS_"+documentId);
		if(o!=null){
			return true;
		}
		else return false;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	
	

}
