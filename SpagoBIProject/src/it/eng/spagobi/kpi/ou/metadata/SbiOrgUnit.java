package it.eng.spagobi.kpi.ou.metadata;
// Generated 21-set-2010 12.29.29 by Hibernate Tools 3.1.0 beta3

import java.util.HashSet;
import java.util.Set;


/**
 * SbiOrgUnit generated by hbm2java
 */

public class SbiOrgUnit  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String label;
     private String name;
     private String description;
     private Set sbiOrgUnitNodeses = new HashSet(0);


    // Constructors

    /** default constructor */
    public SbiOrgUnit() {
    }

	/** minimal constructor */
    public SbiOrgUnit(Integer id, String label, String name) {
        this.id = id;
        this.label = label;
        this.name = name;
    }
    
    /** full constructor */
    public SbiOrgUnit(Integer id, String label, String name, String description, Set sbiOrgUnitNodeses) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.description = description;
        this.sbiOrgUnitNodeses = sbiOrgUnitNodeses;
    }
    

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Set getSbiOrgUnitNodeses() {
        return this.sbiOrgUnitNodeses;
    }
    
    public void setSbiOrgUnitNodeses(Set sbiOrgUnitNodeses) {
        this.sbiOrgUnitNodeses = sbiOrgUnitNodeses;
    }
   








}
