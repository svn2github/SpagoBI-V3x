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
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.tools.dataset.common.behaviour.FilteringBehaviour;
import it.eng.spagobi.tools.dataset.common.behaviour.SelectableFieldsBehaviour;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreFilter;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData.FieldType;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;
import it.eng.spagobi.tools.dataset.persist.IDataSetTableDescriptor;
import it.eng.spagobi.tools.dataset.persist.temporarytable.DatasetTemporaryTableUtils;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

public abstract class AbstractCustomDataSet extends AbstractDataSet implements IDataSet {

	private IMetaData metadata;	
	private Map userAttributes;

	private static transient Logger logger = Logger.getLogger(AbstractCustomDataSet.class);

	public AbstractCustomDataSet() {
		super();
		addBehaviour( new FilteringBehaviour(this) );
		addBehaviour( new SelectableFieldsBehaviour(this) );
		userAttributes = new HashMap();
	}
	
	@Override
	public void setParamsMap(Map paramsMap) {
		if (paramsMap == null) {
			super.setParamsMap(paramsMap);
			return;
		}
		Map toSet = new HashMap();
		Set keys = paramsMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = paramsMap.get(key);
			if (value != null && value instanceof String) {
				String valueStr = (String) value;
				String[] values = valueStr.split(",");
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < values.length; i++) {
					String aValue = values[i];
					if (aValue.startsWith("'") && aValue.endsWith("'")) {
						buffer.append(aValue.substring(1, aValue.length() - 1));
					} else {
						buffer.append(aValue);
					}
					if (i < values.length - 1) {
						buffer.append(",");
					}
				}
				valueStr = buffer.toString();
				toSet.put(key, valueStr);
			} else {
				toSet.put(key, value);
			}
		}
		super.setParamsMap(toSet);
	}

	public IMetaData getMetadata() {
		return this.metadata;
	}

	public void setMetadata(IMetaData metadata){
		this.metadata = metadata;
	}

	public IDataSetTableDescriptor createTemporaryTable(String tableName
			, Connection connection){
		logger.debug("IN: creating the tempoary table with name "+tableName);
		IDataSetTableDescriptor descriptor = null;
		SelectableFieldsBehaviour behaviour = (SelectableFieldsBehaviour) this.getBehaviour(SelectableFieldsBehaviour.ID);
		try {
			descriptor = DatasetTemporaryTableUtils.createTemporaryTable(connection, this.getMetadata(), tableName, behaviour.getSelectedFields());
		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("Error creating temporary table", t);
		}
		logger.debug("Temporary table created successfully");
		logger.debug("OUT");
		return descriptor;
	}


	// *********** Abstract methods **************


	// no implement
	public abstract IDataStore test();
	public abstract String getSignature();
	public abstract IDataStore getDomainValues(String attributeName, Integer start, Integer limit, IDataStoreFilter filter);
	public abstract Map<String, List<String>> getDomainDescriptions(Map<String, List<String>> codes); 
	public abstract IDataSetTableDescriptor persist(String tableName, Connection connection);

	public Map getUserProfileAttributes() {
		return userAttributes;
	}

	public void setUserProfileAttributes(Map attributes) {
		this.userAttributes = attributes;

	}

	public IDataStore getDataStore() {
		throw new RuntimeException("This method is not implemented. It should not be invoked");
	}

	public Object getQuery() {
		throw new RuntimeException("This method is not implemented. It should not be invoked");
	}

	public void setQuery(Object query) {
		throw new RuntimeException("This method is not implemented. It should not be invoked");
	}

	public void setAbortOnOverflow(boolean abortOnOverflow) {
		throw new RuntimeException("This method is not implemented. It should not be invoked");
	}

	public void addBinding(String bindingName, Object bindingValue) {
		throw new RuntimeException("This method is not implemented. It should not be invoked");
	}

	public IDataStore decode(IDataStore datastore) {
		Map<String, List<String>> codes = this.getCodes(datastore);
		LogMF.debug(logger, "Codes : {0}", codes);
		Map<String, List<String>> descriptions = this.getDomainDescriptions(codes);
		LogMF.debug(logger, "Descriptions : {0}", descriptions);
		this.substituteCodeWithDescriptions(datastore, codes, descriptions);
		LogMF.debug(logger, "Datastore decoded : {0}", datastore);
		return datastore;
	}

	private void substituteCodeWithDescriptions(IDataStore datastore,
			Map<String, List<String>> codes,
			Map<String, List<String>> descriptions) {
		IMetaData metadata = datastore.getMetaData();
		int count = metadata.getFieldCount();
		for (int i = 0 ; i < count ; i++) {
			IFieldMetaData fieldMetadata = metadata.getFieldMeta(i);
			if (fieldMetadata.getFieldType().ordinal() == FieldType.MEASURE.ordinal()) {
				continue;
			}
			String key = fieldMetadata.getName();
			if (descriptions.containsKey(key)) {
				// se esiste la descrizione, allora la sostituisco ai codici
				substituteCodeWithDescriptionsOnColumn(i, datastore, codes.get(key), descriptions.get(key));
			}
		}
	}

	private void substituteCodeWithDescriptionsOnColumn(int columnIndex,
			IDataStore datastore, List<String> codes, List<String> descriptions) {
		Iterator it = datastore.iterator();
		while (it.hasNext()) {
			IRecord record = (IRecord) it.next();
			IField field = record.getFieldAt(columnIndex);
			Object value = field.getValue();
			String code = value == null ? "null" : value.toString();
			// recupero la posizione del codice dalla lista dei codici
			int index = codes.indexOf(code);
			// recupero la relativa descrizione prendendolo dalla stessa posizione nella lista delle descrizioni
			String newValue = descriptions.get(index);
			field.setValue(newValue);
		}
	}

	private Map<String, List<String>> getCodes(IDataStore datastore) {
		IMetaData metadata = datastore.getMetaData();
		int count = metadata.getFieldCount();
		
		Map<String, List<String>> codes = new HashMap<String, List<String>>();
		
		for (int i = 0 ; i < count ; i++) {
			IFieldMetaData fieldMetadata = metadata.getFieldMeta(i);
			if (fieldMetadata.getFieldType().ordinal() == FieldType.MEASURE.ordinal()) {
				continue;
			}
			String key = fieldMetadata.getName();
			Set value = datastore.getFieldDistinctValues(i);
			List<String> strings = new ArrayList<String>();
			Iterator it = value.iterator();
			while (it.hasNext()) {
				Object aValue = it.next();
				strings.add(aValue == null ? "null" : aValue.toString());
			}
			codes.put(key, strings);
		}
		
		return codes;
	}
	
}
