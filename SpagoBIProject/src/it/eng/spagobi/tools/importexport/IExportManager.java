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
package it.eng.spagobi.tools.importexport;

import it.eng.spago.error.EMFUserError;

import java.util.List;

public interface IExportManager {

	/**
	 * Prepare the environment for export.
	 * 
	 * @param pathExpFold Path of the export folder
	 * @param nameExpFile the name to give to the exported file
	 * @param expSubObj Flag which tells if it's necessary to export subobjects
	 * @param expSnaps the exp snaps
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void prepareExport(String pathExpFold, String nameExpFile, 
			boolean expSubObj, boolean expSnaps) throws EMFUserError;
	
	/**
	 * Exports objects
	 * 
	 * @param objPaths List of path of the objects to export
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void exportObjects(List objPaths) throws EMFUserError;
	
	/**
	 * Exports resources (OLAP schemas, ETL jobs, datamarts)
	 * 
	 * @throws EMFUserError the EMF user error
	 */
//	public void exportResources() throws EMFUserError;
	
	/**
	 * Creates the archive export file
	 * 
	 * @throws EMFUserError
	 */
	public void createExportArchive() throws EMFUserError;
	
	/**
	 * Clean the export environment (close sessions and delete temporary files).
	 */
	public void cleanExportEnvironment();
}
