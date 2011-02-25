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
package it.eng.spagobi.engines.qbe.services.formbuilder;
		
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import it.eng.qbe.cache.QbeCacheManager;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.services.core.AbstractQbeEngineAction;
import it.eng.spagobi.engines.qbe.tree.ExtJsQbeTreeBuilder;
import it.eng.spagobi.engines.qbe.tree.filter.IQbeTreeEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.IQbeTreeFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeOrderEntityFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeOrderFieldFilter;
import it.eng.spagobi.engines.qbe.tree.filter.QbeTreeQueryEntityFilter;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 */
public class GetEntityFieldsAction  extends AbstractQbeEngineAction {	

	// INPUT PARAMETERS
	public static final String FIELD_ID = "fieldId";
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(GetEntityFieldsAction.class);
   
	public void service(SourceBean request, SourceBean response) {				
	
		String fieldId;
		DataMartModel model;
		DataMartModelStructure structure;
		DataMartField field;
		DataMartEntity parentEntity;
		DataMartEntity dimensionalEntity;
		List fields;
		JSONArray toReturn;
		
		logger.debug("IN");
		
		try {		
			super.service(request, response);	
			
			fieldId = getAttributeAsString( FIELD_ID );
			logger.debug("Parameter [" + FIELD_ID + "] is equals to [" + fieldId + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			
			model = this.getDatamartModel();
			structure = model.getDataMartModelStructure();
			field = structure.getField(fieldId);
			parentEntity = field.getParent();
			fields = parentEntity.getAllFields();
			
			toReturn = new JSONArray();
			Iterator it = fields.iterator();
			while (it.hasNext()) {
				DataMartField aField = (DataMartField) it.next();
				JSONObject aJSONField = new JSONObject();
				aJSONField.put("id", aField.getUniqueName());
				String name = QbeCacheManager.getInstance().getLabels( getDatamartModel() , getLocale() ).getLabel(aField);
				if (name == null || name.trim().equals("")) 
					name = aField.getName();
				aJSONField.put("name", name);
				toReturn.put(aJSONField);
			}
			
			
			/*
			field = structure.getField(fieldId);
			
			parentEntity = field.getParent();
			
			String type = parentEntity.getType();
			
			// e se il nome della classe � nel package di default?
			// TODO: mettere un metodo DataMartModelStructure che, dato una entit� annidata, restituisce quella di primo livello
			String entityName = type + "::" + type.substring(type.lastIndexOf(".") + 1);
			
			dimensionalEntity = this.getDatamartModel().getDataMartModelStructure().getEntity(entityName);
			fields = dimensionalEntity.getAllFields();
			
			JSONArray toReturn = new JSONArray();
			
			Iterator it = fields.iterator();
			while (it.hasNext()) {
				DataMartField aField = (DataMartField) it.next();
				JSONObject aJSONField = new JSONObject();
				aJSONField.put("id", aField.getId());
				String name = QbeCacheManager.getInstance().getLabels( getDatamartModel() , getLocale() ).getLabel(aField);
				aJSONField.put("name", name);
				toReturn.put(aJSONField);
			}
			*/
			
			
			try {
				writeBackToClient( new JSONSuccess(toReturn) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {			
			logger.debug("OUT");
		}	
	}
}
