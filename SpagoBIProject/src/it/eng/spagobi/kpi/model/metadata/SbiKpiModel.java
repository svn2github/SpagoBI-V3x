/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.kpi.model.metadata;

// Generated 5-nov-2008 17.17.20 by Hibernate Tools 3.1.0 beta3

import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;

import java.util.HashSet;
import java.util.Set;

/**
 * SbiKpiModel generated by hbm2java
 */

public class SbiKpiModel extends SbiHibernateModel {

	// Fields

  	private Integer kpiModelId;
	private SbiDomains modelType;
	private SbiKpiModel sbiKpiModel;
	private SbiKpi sbiKpi;
	private String kpiModelCd;
	private String kpiModelNm;
	private String kpiModelLabel;
	private String kpiModelDesc;
	private Set sbiKpiModels = new HashSet(0);
    private Set sbiKpiModelInsts = new HashSet(0);
	// Constructors

	/** default constructor */
	public SbiKpiModel() {
	this.kpiModelId = -1;
	}

	/** minimal constructor */
	public SbiKpiModel(Integer kpiModelId, SbiDomains sbiDomains) {
		this.kpiModelId = kpiModelId;
		this.modelType = sbiDomains;
	}
	
	/** minimal constructor */
	public SbiKpiModel(Integer kpiModelId) {
		this.kpiModelId = kpiModelId;
	}
	

	/** full constructor */
	public SbiKpiModel(Integer kpiModelId, SbiDomains sbiDomains,
			SbiKpiModel sbiKpiModel, SbiKpi sbiKpi, String kpiModelCd,
			String kpiModelNm, String kpiModelDesc,
			Set sbiKpiModels, Set sbiKpiModelInsts) {
		this.kpiModelId = kpiModelId;
		this.modelType = sbiDomains;
		this.sbiKpiModel = sbiKpiModel;
		this.sbiKpi = sbiKpi;
		this.kpiModelCd = kpiModelCd;
		this.kpiModelNm = kpiModelNm;
		this.kpiModelDesc = kpiModelDesc;
		this.sbiKpiModels = sbiKpiModels;
		this.sbiKpiModelInsts = sbiKpiModelInsts;
	}

	// Property accessors

	public Integer getKpiModelId() {
		return this.kpiModelId;
	}

	public void setKpiModelId(Integer kpiModelId) {
		this.kpiModelId = kpiModelId;
	}

	public SbiDomains getModelType() {
		return this.modelType;
	}

	public void setModelType(SbiDomains sbiDomains) {
		this.modelType = sbiDomains;
	}

	public SbiKpiModel getSbiKpiModel() {
		return this.sbiKpiModel;
	}

	public void setSbiKpiModel(SbiKpiModel sbiKpiModel) {
		this.sbiKpiModel = sbiKpiModel;
	}

	public SbiKpi getSbiKpi() {
		return this.sbiKpi;
	}

	public void setSbiKpi(SbiKpi sbiKpi) {
		this.sbiKpi = sbiKpi;
	}

	public String getKpiModelCd() {
		return this.kpiModelCd;
	}

	public void setKpiModelCd(String kpiModelCd) {
		this.kpiModelCd = kpiModelCd;
	}

	public String getKpiModelNm() {
		return this.kpiModelNm;
	}

	public void setKpiModelNm(String kpiModelNm) {
		this.kpiModelNm = kpiModelNm;
	}

	public String getKpiModelDesc() {
		return this.kpiModelDesc;
	}

	public void setKpiModelDesc(String kpiModelDesc) {
		this.kpiModelDesc = kpiModelDesc;
	}

	public Set getSbiKpiModels() {
		return this.sbiKpiModels;
	}

	public void setSbiKpiModels(Set sbiKpiModels) {
		this.sbiKpiModels = sbiKpiModels;
	}
	
	public Set getSbiKpiModelInsts() {
		return sbiKpiModelInsts;
	}

	public void setSbiKpiModelInsts(Set sbiKpiModelInsts) {
		this.sbiKpiModelInsts = sbiKpiModelInsts;
	}

	public String getKpiModelLabel() {
		return kpiModelLabel;
	}

	public void setKpiModelLabel(String kpiModelLabel) {
		this.kpiModelLabel = kpiModelLabel;
	}

}
