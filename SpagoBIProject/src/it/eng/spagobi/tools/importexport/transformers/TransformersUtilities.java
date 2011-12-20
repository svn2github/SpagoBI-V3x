/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport.transformers;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.tools.importexport.ImportExportConstants;
import it.eng.spagobi.tools.importexport.ImportUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransformersUtilities {

	/**
	 * Gets the connection to database.
	 * 
	 * @param pathImpTmpFolder the path imp tmp folder
	 * @param archiveName the archive name
	 * 
	 * @return the connection to database
	 */
	public static Connection getConnectionToDatabase(String pathImpTmpFolder, String archiveName) {
		Connection connection = null;
		try{
			String driverName = "org.hsqldb.jdbcDriver";
			Class.forName(driverName);
			String url = "jdbc:hsqldb:file:" + pathImpTmpFolder + "/" + archiveName + "/metadata/metadata;shutdown=true"; 
			String username = "sa";
			String password = "";
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(true);
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, TransformersUtilities.class.getName(), "getConnectionToDatabase",
					               "Error while getting connection to database " + e);	
		}
		return connection;
	}
	
	/**
	 * Compress contents of a folder into an output stream.
	 * 
	 * @param pathFolder The path of the folder to compress
	 * @param out The Compress output stream
	 * @param pathExportFolder the path export folder
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void compressFolder(String pathExportFolder, String pathFolder, ZipOutputStream out) throws EMFUserError {
		File folder = new File(pathFolder);
		String[] entries = folder.list();
	    byte[] buffer = new byte[4096];   
	    int bytes_read;
	    try{
		    for(int i = 0; i < entries.length; i++) {
		      File f = new File(folder, entries[i]);
		      if(f.isDirectory()) {  
		    	  compressFolder(pathExportFolder, pathFolder + "/" + f.getName(), out); 
		      } else {
		    	  FileInputStream in = new FileInputStream(f); 
		    	  String completeFileName = pathFolder + "/" + f.getName();
		    	  String relativeFileName = f.getName();
		    	  if(completeFileName.lastIndexOf(pathExportFolder)!=-1) {
		    		  int index = completeFileName.lastIndexOf(pathExportFolder);
		    		  int len = pathExportFolder.length();
		    		  relativeFileName = completeFileName.substring(index + len + 1);
		    	  }
		    	  ZipEntry entry = new ZipEntry(relativeFileName);  
		    	  out.putNextEntry(entry);                     
		    	  while((bytes_read = in.read(buffer)) != -1)  
		    		  out.write(buffer, 0, bytes_read);
		    	  in.close();
		      }
		    }
	    } catch (Exception e) {
	    	SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, TransformersUtilities.class.getName(), "compressSingleFolder",
	    						   "Error while creating archive file " + e);
	    	throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	    }
	}
	
	/**
	 * Decompress archive.
	 * 
	 * @param pathImpTmpFold the path imp tmp fold
	 * @param archiveName the archive name
	 * @param archiveCont the archive cont
	 * 
	 * @throws Exception the exception
	 */
	public static void decompressArchive(String pathImpTmpFold, String archiveName, byte[] archiveCont) throws Exception {
		// create directories of the tmp import folder
		File impTmpFold = new File(pathImpTmpFold);
		impTmpFold.mkdirs();
		// write content uploaded into a tmp archive
		String pathArchiveFile = pathImpTmpFold + "/" +archiveName;
		File archive = new File(pathArchiveFile);
		FileOutputStream fos = new FileOutputStream(archive); 
		fos.write(archiveCont);
		fos.flush();
		fos.close();
		// decompress archive
		ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
		// erase archive file 
		archive.delete();
	}
	
	/**
	 * Creates the compress export file.
	 * 
	 * @param pathExportFolder the path export folder
	 * @param nameExportFile the name export file
	 * 
	 * @return The path of the exported compress file
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static byte[] createExportArchive(String pathExportFolder, String nameExportFile) throws EMFUserError {
		byte[] content = null;
		String archivePath = pathExportFolder + "/" + nameExportFile + ".zip";
		File archiveFile = new File(archivePath);
		if(archiveFile.exists()){
			archiveFile.delete();
		}
		String pathBase = pathExportFolder + "/" + nameExportFile;
		try{
			FileOutputStream fos = new FileOutputStream(archivePath);
			ZipOutputStream out = new ZipOutputStream(fos);
			compressFolder(pathExportFolder, pathBase, out);
			out.flush();
			out.close();
			fos.close();
			FileInputStream fis = new FileInputStream(archivePath);
			content = GeneralUtilities.getByteArrayFromInputStream(fis);
			fis.close();
		} catch (Exception e){
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, TransformersUtilities.class.getName(), "createExportArchive",
					   			   "Error while creating archive file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
		return content;
	}
	
}
