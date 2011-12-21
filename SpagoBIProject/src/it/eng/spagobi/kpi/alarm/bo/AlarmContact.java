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
package it.eng.spagobi.kpi.alarm.bo;

/**
 * Title: SpagoBI
 * Description: SpagoBI
 * Copyright: Copyright (c) 2008
 * Company: Xaltia S.p.A.
 * 
 * @author Enrico Cesaretti
 * @version 1.0
 */

 

import it.eng.spagobi.kpi.alarm.metadata.SbiAlarm;

import java.util.HashSet;
import java.util.Set;

public class AlarmContact  implements java.io.Serializable {

 	/**
     * 
     */
    private static final long serialVersionUID = 5460823307866761965L;
	private Integer id; 
 	private String name; 
 	private String email; 
 	private String mobile;
 	private String resources; 
 	private Set<SbiAlarm> sbiAlarms = new HashSet<SbiAlarm>(0); 


    public AlarmContact() {}
	

    public AlarmContact(String name, String email, String mobile, String resources, Set<SbiAlarm> sbiAlarms) {
       this.name = name;
       this.email = email;
       this.mobile = mobile;
       this.sbiAlarms = sbiAlarms;
       this.resources = resources;
    }

   
    public Integer getId() {
        return this.id;
    }    
    
    public void setId(Integer id) {
        this.id = id;
    }    
    public String getName() {
        return this.name;
    }    
    
    public void setName(String name) {
        this.name = name;
    }    
    public String getEmail() {
        return this.email;
    }    
    
    public void setEmail(String email) {
        this.email = email;
    }    
    public String getMobile() {
        return this.mobile;
    }    
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }    
   
    public Set<SbiAlarm> getSbiAlarms() {
        return this.sbiAlarms;
    }    
    
    public void setSbiAlarms(Set<SbiAlarm> sbiAlarms) {
        this.sbiAlarms = sbiAlarms;
    }  
    
    public String getResources() {
        return resources;
    }


    public void setResources(String resources) {
        this.resources = resources;
    }


	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString(){
	    final String TAB = ", ";
	    StringBuffer retValue = new StringBuffer();
	    retValue.append("SbiAlarmContact ( ")
	        .append(super.toString()).append(TAB)
			.append("id = ").append(this.id).append(TAB)			
			.append("name = ").append(this.name).append(TAB)			
			.append("email = ").append(this.email).append(TAB)			
			.append("mobile = ").append(this.mobile).append(TAB)
			.append("resources = ").append(this.resources).append(TAB)	
	        .append(" )");
	    return retValue.toString();
	}


}


