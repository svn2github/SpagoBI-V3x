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
package it.eng.spagobi.commons.metadata;

import java.util.Set;


public class SbiUserFunctionality  extends SbiHibernateModel {


     private int id;
     private String name=null;
     private String description=null;
     private Set roleType;
     
    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
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
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the role type.
     * 
     * @return the role type
     */
    public Set getRoleType() {
        return roleType;
    }
    
    /**
     * Sets the role type.
     * 
     * @param roleType the new role type
     */
    public void setRoleType(Set roleType) {
        this.roleType = roleType;
    }
    




}