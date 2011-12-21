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
package it.eng.spagobi.analiticalmodel.document.metadata;

import it.eng.spagobi.commons.metadata.SbiCommonInfo;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.kpi.config.metadata.SbiKpiDocument;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;




/**
 * SbiObjects generated by hbm2java
 */
public class SbiObjects  extends SbiHibernateModel {


	private Integer biobjId;
	private SbiEngines sbiEngines;
	private SbiDomains stateConsideration;
	private SbiDomains state;
	private SbiDomains execMode;
	private SbiDomains objectType;
	private SbiDataSource dataSource;
	private String objectTypeCode;
	private Short encrypt;
	private Short visible;
	private String profiledVisibility;
	private String stateCode;
	private Short schedFl;
	private String execModeCode;
	private String stateConsiderationCode;
	private String label;
	private String name;
	private String descr;
	private String path;
	private String relName;
	private Set sbiObjPars;
	private Set sbiObjFuncs;
	private Set sbiObjStates;
	private String uuid;
	private Date creationDate=null;
	private String creationUser=null;
	private Integer refreshSeconds=null;     
	private SbiDataSetConfig dataSet=null;
	private Set sbiKpiDocumentses = new HashSet(0);

	// Constructors

	/**
	 * Gets the creation date.
	 * 
	 * @return the creation date
	 */
	 public Date getCreationDate() {
		 return creationDate;
	 }

		
	 /**
	  * Sets the creation date.
	  * 
	  * @param creationDate the new creation date
	  */
	 public void setCreationDate(Date creationDate) {
		 this.creationDate = creationDate;
	 }

	 /**
	  * Gets the creation user.
	  * 
	  * @return the creation user
	  */
	 public String getCreationUser() {
		 return creationUser;
	 }

	 /**
	  * Sets the creation user.
	  * 
	  * @param creationUser the new creation user
	  */
	 public void setCreationUser(String creationUser) {
		 this.creationUser = creationUser;
	 }

	 /**
	  * default constructor.
	  */
	 public SbiObjects() {
		 this.biobjId = -1;
	 }

	 /**
	  * constructor with id.
	  * 
	  * @param biobjId the biobj id
	  */
	 public SbiObjects(Integer biobjId) {
		 this.biobjId = biobjId;
	 }


	


	 // Property accessors

	 public Set getSbiKpiDocumentses() {
		return sbiKpiDocumentses;
	}

	public void setSbiKpiDocumentses(Set sbiKpiDocumentses) {
		this.sbiKpiDocumentses = sbiKpiDocumentses;
	}

	/**
	  * Gets the uuid.
	  * 
	  * @return the uuid
	  */
	 public String getUuid() {
		 return uuid;
	 }

	 /**
	  * Sets the uuid.
	  * 
	  * @param uuid the new uuid
	  */
	 public void setUuid(String uuid) {
		 this.uuid = uuid;
	 }

	 /**
	  * Gets the biobj id.
	  * 
	  * @return the biobj id
	  */
	 public Integer getBiobjId() {
		 return this.biobjId;
	 }

	 /**
	  * Sets the biobj id.
	  * 
	  * @param biobjId the new biobj id
	  */
	 public void setBiobjId(Integer biobjId) {
		 this.biobjId = biobjId;
	 }

	 /**
	  * Gets the sbi engines.
	  * 
	  * @return the sbi engines
	  */
	 public SbiEngines getSbiEngines() {
		 return this.sbiEngines;
	 }

	 /**
	  * Sets the sbi engines.
	  * 
	  * @param sbiEngines the new sbi engines
	  */
	 public void setSbiEngines(SbiEngines sbiEngines) {
		 this.sbiEngines = sbiEngines;
	 }

	 /**
	  * Gets the state consideration.
	  * 
	  * @return the state consideration
	  */
	 public SbiDomains getStateConsideration() {
		 return this.stateConsideration;
	 }

	 /**
	  * Sets the state consideration.
	  * 
	  * @param sbiDomains the new state consideration
	  */
	 public void setStateConsideration(SbiDomains sbiDomains) {
		 this.stateConsideration = sbiDomains;
	 }

	 /**
	  * Gets the state.
	  * 
	  * @return the state
	  */
	 public SbiDomains getState() {
		 return this.state;
	 }

	 /**
	  * Sets the state.
	  * 
	  * @param sbiDomains_1 the new state
	  */
	 public void setState(SbiDomains sbiDomains_1) {
		 this.state = sbiDomains_1;
	 }

	 /**
	  * Gets the exec mode.
	  * 
	  * @return the exec mode
	  */
	 public SbiDomains getExecMode() {
		 return this.execMode;
	 }

	 /**
	  * Sets the exec mode.
	  * 
	  * @param sbiDomains_2 the new exec mode
	  */
	 public void setExecMode(SbiDomains sbiDomains_2) {
		 this.execMode = sbiDomains_2;
	 }

	 /**
	  * Gets the object type.
	  * 
	  * @return the object type
	  */
	 public SbiDomains getObjectType() {
		 return this.objectType;
	 }

	 /**
	  * Sets the object type.
	  * 
	  * @param sbiDomains_3 the new object type
	  */
	 public void setObjectType(SbiDomains sbiDomains_3) {
		 this.objectType = sbiDomains_3;
	 }

	 /**
	  * Gets the object type code.
	  * 
	  * @return the object type code
	  */
	 public String getObjectTypeCode() {
		 return this.objectTypeCode;
	 }

	 /**
	  * Sets the object type code.
	  * 
	  * @param biobjTypeCd the new object type code
	  */
	 public void setObjectTypeCode(String biobjTypeCd) {
		 this.objectTypeCode = biobjTypeCd;
	 }

	 /**
	  * Gets the encrypt.
	  * 
	  * @return the encrypt
	  */
	 public Short getEncrypt() {
		 return this.encrypt;
	 }

	 /**
	  * Sets the encrypt.
	  * 
	  * @param encrypt the new encrypt
	  */
	 public void setEncrypt(Short encrypt) {
		 this.encrypt = encrypt;
	 }

	 /**
	  * Gets the visible.
	  * 
	  * @return the visible
	  */
	 public Short getVisible() {
		 return this.visible;
	 }

	 /**
	  * Sets the visible.
	  * 
	  * @param visible the new visible
	  */
	 public void setVisible(Short visible) {
		 this.visible = visible;
	 }


	 /**
	  * Gets the state code.
	  * 
	  * @return the state code
	  */
	 public String getStateCode() {
		 return this.stateCode;
	 }

	 /**
	  * Sets the state code.
	  * 
	  * @param stateCd the new state code
	  */
	 public void setStateCode(String stateCd) {
		 this.stateCode = stateCd;
	 }

	 /**
	  * Gets the sched fl.
	  * 
	  * @return the sched fl
	  */
	 public Short getSchedFl() {
		 return this.schedFl;
	 }

	 /**
	  * Sets the sched fl.
	  * 
	  * @param schedFl the new sched fl
	  */
	 public void setSchedFl(Short schedFl) {
		 this.schedFl = schedFl;
	 }

	 /**
	  * Gets the exec mode code.
	  * 
	  * @return the exec mode code
	  */
	 public String getExecModeCode() {
		 return this.execModeCode;
	 }

	 /**
	  * Sets the exec mode code.
	  * 
	  * @param execModeCd the new exec mode code
	  */
	 public void setExecModeCode(String execModeCd) {
		 this.execModeCode = execModeCd;
	 }

	 /**
	  * Gets the state consideration code.
	  * 
	  * @return the state consideration code
	  */
	 public String getStateConsiderationCode() {
		 return this.stateConsiderationCode;
	 }

	 /**
	  * Sets the state consideration code.
	  * 
	  * @param stateConsCd the new state consideration code
	  */
	 public void setStateConsiderationCode(String stateConsCd) {
		 this.stateConsiderationCode = stateConsCd;
	 }

	 /**
	  * Gets the label.
	  * 
	  * @return the label
	  */
	 public String getLabel() {
		 return this.label;
	 }

	 /**
	  * Sets the label.
	  * 
	  * @param label the new label
	  */
	 public void setLabel(String label) {
		 this.label = label;
	 }

	 /**
	  * Gets the descr.
	  * 
	  * @return the descr
	  */
	 public String getDescr() {
		 return this.descr;
	 }

	 /**
	  * Sets the descr.
	  * 
	  * @param descr the new descr
	  */
	 public void setDescr(String descr) {
		 this.descr = descr;
	 }

	 /**
	  * Gets the path.
	  * 
	  * @return the path
	  */
	 public String getPath() {
		 return this.path;
	 }

	 /**
	  * Sets the path.
	  * 
	  * @param path the new path
	  */
	 public void setPath(String path) {
		 this.path = path;
	 }

	 /**
	  * Gets the rel name.
	  * 
	  * @return the rel name
	  */
	 public String getRelName() {
		 return this.relName;
	 }

	 /**
	  * Sets the rel name.
	  * 
	  * @param relName the new rel name
	  */
	 public void setRelName(String relName) {
		 this.relName = relName;
	 }

	 /**
	  * Gets the sbi obj pars.
	  * 
	  * @return the sbi obj pars
	  */
	 public Set getSbiObjPars() {
		 return this.sbiObjPars;
	 }

	 /**
	  * Sets the sbi obj pars.
	  * 
	  * @param sbiObjPars the new sbi obj pars
	  */
	 public void setSbiObjPars(Set sbiObjPars) {
		 this.sbiObjPars = sbiObjPars;
	 }

	 /**
	  * Gets the sbi obj funcs.
	  * 
	  * @return the sbi obj funcs
	  */
	 public Set getSbiObjFuncs() {
		 return this.sbiObjFuncs;
	 }

	 /**
	  * Sets the sbi obj funcs.
	  * 
	  * @param sbiObjFuncs the new sbi obj funcs
	  */
	 public void setSbiObjFuncs(Set sbiObjFuncs) {
		 this.sbiObjFuncs = sbiObjFuncs;
	 }

	 /**
	  * Gets the sbi obj states.
	  * 
	  * @return the sbi obj states
	  */
	 public Set getSbiObjStates() {
		 return this.sbiObjStates;
	 }

	 /**
	  * Sets the sbi obj states.
	  * 
	  * @param sbiObjStates the new sbi obj states
	  */
	 public void setSbiObjStates(Set sbiObjStates) {
		 this.sbiObjStates = sbiObjStates;
	 }

	 /**
	  * Gets the data source.
	  * 
	  * @return the data source
	  */
	 public SbiDataSource getDataSource() {
		 return this.dataSource;
	 }

	 /**
	  * Sets the data source.
	  * 
	  * @param sbiDataSource the new data source
	  */
	 public void setDataSource(SbiDataSource sbiDataSource) {
		 this.dataSource = sbiDataSource;
	 }

	 /**
	  * Gets the name.
	  * 
	  * @return the name
	  */
	 public String getName() {
		 return name;
	 }

	 /**
	  * Sets the name.
	  * 
	  * @param name the new name
	  */
	 public void setName(String name) {
		 this.name = name;
	 }

	 /**
	  * Gets the data set.
	  * 
	  * @return the data set
	  */
	 public SbiDataSetConfig getDataSet() {
		 return dataSet;
	 }

	 /**
	  * Sets the data set.
	  * 
	  * @param dataSet the new data set
	  */
	 public void setDataSet(SbiDataSetConfig dataSet) {
		 this.dataSet = dataSet;
	 }

	 /**
	  * Gets the refresh Seconds.
	  * 
	  * @return the refresh Seconds
	  */

	 public Integer getRefreshSeconds() {
		 return refreshSeconds;
	 }

	 /**
	  * Sets the refresh Seconds.
	  * 
	  * @param Integer the refreshSeconds
	  */

	 public void setRefreshSeconds(Integer refreshSeconds) {
		 this.refreshSeconds = refreshSeconds;
	 }

	 public String getProfiledVisibility() {
		 return profiledVisibility;
	 }

	 public void setProfiledVisibility(String profiledVisibility) {
		 this.profiledVisibility = profiledVisibility;
	 }


}