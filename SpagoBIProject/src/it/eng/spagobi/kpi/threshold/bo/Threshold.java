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
package it.eng.spagobi.kpi.threshold.bo;

import java.io.Serializable;
import java.util.List;


public class Threshold implements Serializable{
	
	Integer id = null;
	String name = null;//all thresholds referred to the same kpi will have this Name equal
	String description = null;//all thresholds referred to the same kpi will have this description equal
	String code = null;
	Integer thresholdTypeId = null;
	String thresholdTypeCode = null;
	List thresholdValues=null;
	
	public String getThresholdTypeCode() {
		return thresholdTypeCode;
	}

	public void setThresholdTypeCode(String thresholdTypeCode) {
		this.thresholdTypeCode = thresholdTypeCode;
	}

	public Threshold() {
		super();
	}


	public Integer getId() {
		return id;
	}


	@Override
	public boolean equals(Object obj) {
		Threshold t = (Threshold)obj;
		if (this.id!= null && this.id.equals(t.getId())){
		return true;
		}else{
			return false;
		}
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Integer getThresholdTypeId() {
		return thresholdTypeId;
	}


	public void setThresholdTypeId(Integer thresholdTypeId) {
		this.thresholdTypeId = thresholdTypeId;
	}


	public List getThresholdValues() {
		return thresholdValues;
	}


	public void setThresholdValues(List thresholdValues) {
		this.thresholdValues = thresholdValues;
	}
	



}
