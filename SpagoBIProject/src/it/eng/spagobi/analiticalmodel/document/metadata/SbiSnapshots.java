/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.analiticalmodel.document.metadata;

import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiCommonInfo;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;

import java.util.Date;


public class SbiSnapshots  extends SbiHibernateModel {

    private Integer snapId;
    private SbiObjects sbiObject;
    private SbiBinContents sbiBinContents;
    private String name;
    private String description;
    private Date creationDate;
    
	/**
	 * Gets the snap id.
	 * 
	 * @return the snap id
	 */
	public Integer getSnapId() {
		return snapId;
	}
	
	/**
	 * Sets the snap id.
	 * 
	 * @param snapId the new snap id
	 */
	public void setSnapId(Integer snapId) {
		this.snapId = snapId;
	}
	
	/**
	 * Gets the sbi object.
	 * 
	 * @return the sbi object
	 */
	public SbiObjects getSbiObject() {
		return sbiObject;
	}
	
	/**
	 * Sets the sbi object.
	 * 
	 * @param sbiObject the new sbi object
	 */
	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}
	
	/**
	 * Gets the sbi bin contents.
	 * 
	 * @return the sbi bin contents
	 */
	public SbiBinContents getSbiBinContents() {
		return sbiBinContents;
	}
	
	/**
	 * Sets the sbi bin contents.
	 * 
	 * @param sbiBinContents the new sbi bin contents
	 */
	public void setSbiBinContents(SbiBinContents sbiBinContents) {
		this.sbiBinContents = sbiBinContents;
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
    
    
	
}