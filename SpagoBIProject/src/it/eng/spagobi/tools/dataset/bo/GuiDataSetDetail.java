/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.dataset.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chiara Chiarelli
 */
public class GuiDataSetDetail implements Serializable{

	private int dsHId;	
	private boolean active = true;
	private int dsId;
	private int versionNum;

	private String dsType;
	
	private Integer categoryId;
    private String categoryValueNm;
	private String parameters=null;
	private String dsMetadata=null;
	
	private Integer transformerId;
    private String transformerCd;
	private String pivotColumnName=null;
	private String pivotRowName=null;
	private String pivotColumnValue=null;
	private boolean numRows = false;
	
	private String userIn=null;
	private String sbiVersionIn=null;
	private String metaVersion=null;
	private Date timeIn = null;
	
	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getDsHId() {
		return dsHId;
	}

	public void setDsHId(int dsHId) {
		this.dsHId = dsHId;
	}

	public String getUserIn() {
		return userIn;
	}

	public void setUserIn(String userIn) {
		this.userIn = userIn;
	}

	public String getSbiVersionIn() {
		return sbiVersionIn;
	}

	public void setSbiVersionIn(String sbiVersionIn) {
		this.sbiVersionIn = sbiVersionIn;
	}

	public String getMetaVersion() {
		return metaVersion;
	}

	public void setMetaVersion(String metaVersion) {
		this.metaVersion = metaVersion;
	}

	public Date getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Date timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * @return the numRows
	 */
	public boolean isNumRows() {
		return numRows;
	}

	/**
	 * @param numRows the numRows to set
	 */
	public void setNumRows(boolean numRows) {
		this.numRows = numRows;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public String getParameters() {
	    return parameters;
	}
	
	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(String parameters) {
	    this.parameters = parameters;
	}
	
	/**
	 * Gets the pivot column name.
	 * 
	 * @return the pivot column name
	 */
	public String getPivotColumnName() {
		return pivotColumnName;
	}
	
	/**
	 * Sets the pivot column name
	 * 
	 * @param pivotColumnName the new pivot column name
	 */
	public void setPivotColumnName(String pivotColumnName) {
		this.pivotColumnName = pivotColumnName;
	}

	/**
	 * Gets the pivot column value.
	 * 
	 * @return the pivot column value
	 */
	public String getPivotColumnValue() {
		return pivotColumnValue;
	}

	/**
	 * Sets the pivot column value
	 * 
	 * @param pivotColumnValue the new pivot column value
	 */
	public void setPivotColumnValue(String pivotColumnValue) {
		this.pivotColumnValue = pivotColumnValue;
	}

	public String getPivotRowName() {
		return pivotRowName;
	}

	public void setPivotRowName(String pivotRowName) {
		this.pivotRowName = pivotRowName;
	}

    public int getDsId() {
		return dsId;
	}

	public void setDsId(int dsId) {
		this.dsId = dsId;
	}

	public String getDsType() {
		return dsType;
	}

	public void setDsType(String dsType) {
		this.dsType = dsType;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryValueName() {
		return categoryValueNm;
	}

	public void setCategoryValueName(String categoryValName) {
		this.categoryValueNm = categoryValName;
	}

	public Integer getTransformerId() {
		return transformerId;
	}

	public void setTransformerId(Integer transformerId) {
		this.transformerId = transformerId;
	}

	public String getTransformerCd() {
		return transformerCd;
	}

	public void setTransformerCd(String transformerCd) {
		this.transformerCd = transformerCd;
	}

	/**
     * Gets the metadata.
     * 
     * @return metadata
     */
	public String getDsMetadata() {
		return dsMetadata;
	}

    /**
     *  the metadata.
     * 
     * @param transformer the new metadata
     */
	public void setDsMetadata(String dsMetadata) {
		this.dsMetadata = dsMetadata;
	}

    	
}
