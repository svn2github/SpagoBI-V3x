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
package it.eng.spagobi.tools.importexport.dao;

import it.eng.spagobi.tools.importexport.bo.AssociationFile;

import java.util.List;

public interface IAssociationFileDAO {

	/**
	 * Save association file.
	 * 
	 * @param assfile the assfile
	 * @param content the content
	 */
	public void saveAssociationFile(AssociationFile assfile, byte[] content);
	
	/**
	 * Gets the association files.
	 * 
	 * @return the association files
	 */
	public List getAssociationFiles();
	
	/**
	 * Delete association file.
	 * 
	 * @param assfile the assfile
	 */
	public void deleteAssociationFile(AssociationFile assfile);
	
	/**
	 * Gets the content.
	 * 
	 * @param assfile the assfile
	 * 
	 * @return the content
	 */
	public byte[] getContent(AssociationFile assfile);
	
	/**
	 * Load from id.
	 * 
	 * @param id the id
	 * 
	 * @return the association file
	 */
	public AssociationFile loadFromID(String id);
	
	/**
	 * Exists.
	 * 
	 * @param id the id
	 * 
	 * @return true, if successful
	 */
	public boolean exists(String id);
	
}
