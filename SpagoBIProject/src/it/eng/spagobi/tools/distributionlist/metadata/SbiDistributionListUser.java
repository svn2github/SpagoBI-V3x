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
package it.eng.spagobi.tools.distributionlist.metadata;

import it.eng.spagobi.commons.metadata.SbiHibernateModel;
// Generated 19-feb-2008 18.15.47 by Hibernate Tools 3.1.0 beta3



/**
 * SbiDistributionListUser generated by hbm2java
 */

public class SbiDistributionListUser  extends SbiHibernateModel {


    // Fields    

     private Integer dluId;
     private SbiDistributionList sbiDistributionList;
     private String userId;
     private String EMail;


     /**
      * Instantiates a new sbi distribution list user.
      */
     public SbiDistributionListUser() {
     }

     
     /**
      * full constructor.
      * 
      * @param dluId the dlu id
      * @param sbiDistributionList the sbi distribution list
      * @param userId the user id
      * @param EMail the e mail
      */
     public SbiDistributionListUser(Integer dluId, SbiDistributionList sbiDistributionList, String userId, String EMail) {
         this.dluId = dluId;
         this.sbiDistributionList = sbiDistributionList;
         this.userId = userId;
         this.EMail = EMail;
     }
     

    
     // Property accessors

     /**
      * Gets the dlu id.
      * 
      * @return the dlu id
      */
     public Integer getDluId() {
         return this.dluId;
     }
     
     /**
      * Sets the dlu id.
      * 
      * @param dluId the new dlu id
      */
     public void setDluId(Integer dluId) {
         this.dluId = dluId;
     }

     /**
      * Gets the sbi distribution list.
      * 
      * @return the sbi distribution list
      */
     public SbiDistributionList getSbiDistributionList() {
         return this.sbiDistributionList;
     }
     
     /**
      * Sets the sbi distribution list.
      * 
      * @param sbiDistributionList the new sbi distribution list
      */
     public void setSbiDistributionList(SbiDistributionList sbiDistributionList) {
         this.sbiDistributionList = sbiDistributionList;
     }

     /**
      * Gets the user id.
      * 
      * @return the user id
      */
     public String getUserId() {
         return this.userId;
     }
     
     /**
      * Sets the user id.
      * 
      * @param userId the new user id
      */
     public void setUserId(String userId) {
         this.userId = userId;
     }

     /**
      * Gets the e mail.
      * 
      * @return the e mail
      */
     public String getEMail() {
         return this.EMail;
     }
     
     /**
      * Sets the e mail.
      * 
      * @param EMail the new e mail
      */
     public void setEMail(String EMail) {
         this.EMail = EMail;
     }
    
}
