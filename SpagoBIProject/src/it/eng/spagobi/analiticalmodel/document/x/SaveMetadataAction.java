/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.x;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.chiron.serializer.MetadataJSONSerializer;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.indexing.LuceneIndexer;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class SaveMetadataAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "SAVE_METADATA_ACTION";
	
	// REQUEST PARAMETERS
	public static final String OBJECT_ID = "OBJECT_ID";
	public static final String SUBOBJECT_ID = "SUBOBJECT_ID";
	public static final String METADATA = "METADATA";
	
	// logger component
	private static Logger logger = Logger.getLogger(SaveMetadataAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			Integer biobjectId = this.getAttributeAsInteger(OBJECT_ID);
			logger.debug("Object id = " + biobjectId);
			Integer subobjectId = null;
			try {
				subobjectId = this.getAttributeAsInteger(SUBOBJECT_ID);
			} catch (NumberFormatException e) {}
			logger.debug("Subobject id = " + subobjectId);
			String jsonEncodedMetadata = getAttributeAsString( METADATA );
			try {
				CharsetDecoder decoder=Charset.forName("UTF-8").newDecoder();
				jsonEncodedMetadata=decoder.decode(ByteBuffer.wrap(jsonEncodedMetadata.getBytes())).toString();
			} catch(Throwable t) {
				// firefox
			}

			
			logger.debug(METADATA + " = [" + jsonEncodedMetadata + "]");
			JSONArray metadata = new JSONArray(jsonEncodedMetadata);
			for (int i = 0; i < metadata.length(); i++) {					
				JSONObject aMetadata = metadata.getJSONObject(i);
				Integer metadataId = aMetadata.getInt(MetadataJSONSerializer.METADATA_ID);
				String text = aMetadata.getString(MetadataJSONSerializer.TEXT);
				ObjMetacontent aObjMetacontent = DAOFactory.getObjMetacontentDAO().loadObjMetacontent(metadataId, biobjectId, subobjectId);
				if (aObjMetacontent == null) {
					logger.debug("ObjMetacontent for metadata id = " + metadataId + ", biobject id = " + biobjectId + 
							", subobject id = " + subobjectId + " was not found, creating a new one...");
					aObjMetacontent = new ObjMetacontent();
					aObjMetacontent.setObjmetaId(metadataId);
					aObjMetacontent.setBiobjId(biobjectId);
					aObjMetacontent.setSubobjId(subobjectId);
					aObjMetacontent.setContent(text.getBytes("UTF-8"));
					aObjMetacontent.setCreationDate(new Date());
					aObjMetacontent.setLastChangeDate(new Date());
					DAOFactory.getObjMetacontentDAO().insertObjMetacontent(aObjMetacontent);
				} else {
					logger.debug("ObjMetacontent for metadata id = " + metadataId + ", biobject id = " + biobjectId + 
							", subobject id = " + subobjectId + " was found, it will be modified...");
					aObjMetacontent.setContent(text.getBytes("UTF-8"));
					aObjMetacontent.setLastChangeDate(new Date());
					DAOFactory.getObjMetacontentDAO().modifyObjMetacontent(aObjMetacontent);
				}

			}		
			/*
			*indexes biobject by modifying document in index
			**/
			BIObject biObjToIndex = DAOFactory.getBIObjectDAO().loadBIObjectById(biobjectId);
			LuceneIndexer.updateBiobjInIndex(biObjToIndex, false);
			
			
			try {
				writeBackToClient( new JSONAcknowledge() );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch (Exception e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Exception occurred while saving metadata", e);
		} finally {
			logger.debug("OUT");
		}
	}

}
