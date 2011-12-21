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

package it.eng.spagobi.services.scheduler.stub;

import it.eng.spagobi.services.scheduler.service.SchedulerServiceImpl;

public class SchedulerServiceSoapBindingImpl implements it.eng.spagobi.services.scheduler.stub.SchedulerService{
    public java.lang.String getJobList(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
            SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String jobListXml = service.getJobList(in0,in1);
	    return jobListXml;
    }

    public java.lang.String getJobSchedulationList(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String schedListXml = service.getJobSchedulationList(in0, in1,in2,in3);
	    return schedListXml;
    }

    public java.lang.String deleteSchedulation(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.deleteSchedulation(in0,in1,in2,in3);
	    return res;
    }

    public java.lang.String deleteJob(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.deleteJob(in0,in1,in2,in3);
	    return res;
    }

    public java.lang.String defineJob(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.defineJob(in0,in1,in2);
	    return res;
    }

    public java.lang.String getJobDefinition(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.getJobDefinition(in0,in1,in2,in3);
	    return res;
    }

    public java.lang.String scheduleJob(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.scheduleJob(in0,in1,in2);
	    return res;
    }

    public java.lang.String getJobSchedulationDefinition(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.getJobSchedulationDefinition(in0,in1,in2,in3);
	    return res;
    }

    public java.lang.String existJobDefinition(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.existJobDefinition(in0,in1,in2,in3);
	    return res;
    }

}
