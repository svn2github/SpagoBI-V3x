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
package it.eng.spagobi.tools.dataset.common.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class MetaData implements IMetaData {
	
	int idFieldIndex;
	List fieldsMeta;
	Map name2IndexMap;
	Map<String, Object> properties;

	public MetaData() {
		idFieldIndex = -1;
		name2IndexMap = new HashMap();
		fieldsMeta = new ArrayList();
		properties = new HashMap();
	}
	
	public int getIdFieldIndex() {
		return idFieldIndex;
	}
	
	public void setIdField(int fieldIndex) {
		this.idFieldIndex = fieldIndex;
	}

	public int getFieldCount() {
		return fieldsMeta.size();
	}
	

	public int getFieldIndex(String fieldName) {
		Integer columnIndex = null;
		
		columnIndex = (Integer)name2IndexMap.get(fieldName.toUpperCase());
		
		return columnIndex == null? -1: columnIndex.intValue();
	}

	public IFieldMetaData getFieldMeta(int fieldIndex) {
		IFieldMetaData fieldMeta = null;

		fieldMeta = (IFieldMetaData)fieldsMeta.get( fieldIndex );
		
		return fieldMeta;
	}
	
	public List findFieldMeta(String propertyName, Object propertyValue) {
		List results;
		Iterator it;
		
		results = new ArrayList();
		it = fieldsMeta.iterator();
		while(it.hasNext()) {
			IFieldMetaData fieldMeta = (IFieldMetaData)it.next();
			if(fieldMeta.getProperty(propertyName) != null 
					&& fieldMeta.getProperty(propertyName).equals(propertyValue)) {
				results.add(fieldMeta);
			}
		}
		
		return results;
	}
	
	public String getFieldName(int fieldIndex) {
		String fieldName = null;
		IFieldMetaData fieldMeta;
		
		fieldMeta = getFieldMeta(fieldIndex);		
		if(fieldMeta != null) {
			String alias = fieldMeta.getAlias();
			if(alias!=null && !alias.equals("")){
				fieldName = alias;
			}else{
				fieldName = fieldMeta.getName();
			}
		}
		
		return fieldName;
	}

	public Class getFieldType(int fieldIndex) {
		Class fieldType = null;
		IFieldMetaData fieldMeta;
		
		fieldMeta = getFieldMeta(fieldIndex);		
		if(fieldMeta != null) {
			fieldType = fieldMeta.getType();
		}
		
		return fieldType;
	}

	
	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public void setProperty(String propertyName, Object proprtyValue) {
		properties.put(propertyName, proprtyValue);
		
	}
	
	public void addFiedMeta(IFieldMetaData fieldMetaData) {
		Integer fieldIndex = new Integer(fieldsMeta.size());
		fieldsMeta.add(fieldMetaData);
		String fieldKey = fieldMetaData.getName();
		name2IndexMap.put(fieldKey.toUpperCase(), fieldIndex);
	}

	public String toString() {
		return fieldsMeta.toString();
	}

	public void deleteFieldMetaDataAt(int pivotFieldIndex) {
		name2IndexMap.remove( getFieldMeta(pivotFieldIndex) );
		fieldsMeta.remove( pivotFieldIndex );	
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public List getFieldsMeta() {
		return fieldsMeta;
	}

	public void changeFieldAlias(int fieldIndex, String newAlias) {
		IFieldMetaData m = this.getFieldMeta(fieldIndex);
		m.setAlias(newAlias);
	}

}
