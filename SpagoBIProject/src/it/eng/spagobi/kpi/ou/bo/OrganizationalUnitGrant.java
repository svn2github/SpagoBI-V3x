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
package it.eng.spagobi.kpi.ou.bo;

import it.eng.spagobi.kpi.model.bo.ModelInstance;

import java.util.Date;


/**
 * This class represents the grant to an Organizational Unit hierarchy for a KPI model instance
 * 
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class OrganizationalUnitGrant {


    // Fields    

     private Integer id;
     private ModelInstance modelInstance; // the root node of the KPI model instance
     private OrganizationalUnitHierarchy hierarchy; 
     private Date startDate;
     private Date endDate;
     private String label;
     private String name;
     private String description;
     private Boolean isAvailable;
    // Constructors

    /** default constructor */
    public OrganizationalUnitGrant() {
    }
    
    /** full constructor */
    public OrganizationalUnitGrant(Integer id,Boolean isAvailable, ModelInstance modelInstance, OrganizationalUnitHierarchy hierarchy, Date startDate, Date endDate, String label, String name, String description) {
        this.id = id;
        this.modelInstance = modelInstance;
        this.hierarchy = hierarchy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.label = label;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	public void setModelInstance(ModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	}

	public OrganizationalUnitHierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(OrganizationalUnitHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

    @Override
	public String toString() {
		return "OrganizationalUnitGrant [id=" + id + ", modelInstance="
				+ modelInstance + ", hierarchy=" + hierarchy + ", startDate="
				+ startDate + ", endDate=" + endDate + ", label=" + label
				+ ", name=" + name + ", description=" + description + "]";
	}
	
}
