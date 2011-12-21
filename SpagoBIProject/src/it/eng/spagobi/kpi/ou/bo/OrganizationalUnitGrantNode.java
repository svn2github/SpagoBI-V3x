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

import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;

/**
 * This class represents a grant for a particular Organizational Unit of a hierarchy for a node of a KPI model instance
 * 
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class OrganizationalUnitGrantNode {


    // Fields    

     private OrganizationalUnitNode ouNode;
     private ModelInstanceNode modelInstanceNode;
     private OrganizationalUnitGrant grant;


    // Constructors

    /** default constructor */
    public OrganizationalUnitGrantNode() {
    }

    
    /** full constructor */
    public OrganizationalUnitGrantNode(OrganizationalUnitNode ouNode, ModelInstanceNode modelInstanceNode, OrganizationalUnitGrant grant) {
        this.ouNode = ouNode;
        this.modelInstanceNode = modelInstanceNode;
        this.grant = grant;
    }


	public OrganizationalUnitNode getOuNode() {
		return ouNode;
	}


	public void setOuNode(OrganizationalUnitNode ouNode) {
		this.ouNode = ouNode;
	}


	public ModelInstanceNode getModelInstanceNode() {
		return modelInstanceNode;
	}


	public void setModelInstanceNode(ModelInstanceNode modelInstanceNode) {
		this.modelInstanceNode = modelInstanceNode;
	}


	public OrganizationalUnitGrant getGrant() {
		return grant;
	}


	public void setGrant(OrganizationalUnitGrant grant) {
		this.grant = grant;
	}


	@Override
	public String toString() {
		return "OrganizationalUnitGrantNode [ouNode=" + ouNode
				+ ", modelInstanceNode=" + modelInstanceNode + ", grant="
				+ grant + "]";
	}

}
