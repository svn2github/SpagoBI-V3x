/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.model.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.eng.spagobi.commons.utilities.StringUtilities;

/**
 * @author Andrea Gioia
 */
public class DataMartEntity extends AbstractDataMartItem {
	
	protected DataMartEntity root;	
	
	protected String path;		
	protected String role;	
	protected String type;	
	
	protected Map<String,DataMartField> fields;	
	protected Map<String, DataMartCalculatedField> calculatedFields;	
	protected Map<String,DataMartEntity> subEntities;
	

	// =========================================================================
	// COSTRUCTORS 
	// =========================================================================
	
	
	public DataMartEntity(String name, String path, String role, String type,
			DataMartModelStructure structure) {
		
		setStructure( structure );
		
		setId ( structure.getNextId() );
		setName( name );		
		setPath( path == null? "" : path );
		setRole( role );
		setType( type );
		
		setParent(null);
		this.fields = new HashMap<String,DataMartField>();
		this.calculatedFields = new HashMap<String, DataMartCalculatedField>();
		this.subEntities = new HashMap<String,DataMartEntity>();
	}
	
	// =========================================================================
	// ACCESORS 
	// =========================================================================
	
	
	public String getUniqueName() {
		String uniqueName = "";
			
		uniqueName += getRoot().getType() + ":";
		uniqueName += getPath() + ":";
		uniqueName += getName();
		if(getRole() != null) uniqueName +=  "(" + getRole() + ")";
		
		return uniqueName;
	}
	
	public boolean equals(Object o){
		if ( this == o ) return true;
		if ( !(o instanceof DataMartEntity) ) return false;
		DataMartEntity de = (DataMartEntity)o;
		return this.getUniqueName().equals( de.getUniqueName() );
	}

	
	public String getUniqueType() {
		String entityType = getType();
		if ( !StringUtilities.isEmpty( getRole() ) ) {
			entityType += "(" + getRole() + ")";
		}
		return entityType;
	}
	
	
	private void addField(DataMartField field) {
		fields.put(field.getUniqueName(), field);
		getStructure().addField(field);
	}
	
	
	private DataMartField addField(String fieldName, boolean isKey) {
		
		DataMartField field = new DataMartField(fieldName, this);
		field.setKey(isKey);
		addField(field);
		return field;
	}
	
	
	public DataMartField addNormalField(String fieldName) {
		return addField(fieldName, false);
	}
	
	
	public DataMartField addKeyField(String fieldName) {		
		return addField(fieldName, true);
	}
	
	
	
	public DataMartField getField(String fieldName) {
		return (DataMartField)fields.get(fieldName);
	}
	
	public void addCalculatedField(DataMartCalculatedField calculatedField) {
		// bound field to structure
		calculatedField.setId(getStructure().getNextId());
		calculatedField.setStructure(getStructure());
		calculatedField.setParent(this);
		
		// append field to entity
		calculatedFields.put(calculatedField.getUniqueName(), calculatedField);
		
		// append field to structure level facade
		getStructure().addCalculatedField(getUniqueName(), calculatedField);
	}	
	
	public void deleteCalculatedField(String fieldName) {
		DataMartCalculatedField calculatedField;
		
		calculatedField = (DataMartCalculatedField)calculatedFields.remove(fieldName);
		if(calculatedField != null) {
			getStructure().removeCalculatedFiield(calculatedField.getParent().getUniqueName(), calculatedField);
		}
		
	}
	
	public List<DataMartCalculatedField>  getCalculatedFields() {
		List<DataMartCalculatedField> list;
		
		list = new ArrayList<DataMartCalculatedField>();
		String key = null;
		for(Iterator<String> it = calculatedFields.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(calculatedFields.get(key));			
		}
		
		return list;
	}	
	
	public List<DataMartField> getAllFields() {
		List<DataMartField> list;
		
		list = new ArrayList<DataMartField>();
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(fields.get(key));			
		}
		
		return list;
	}	
	
	
	private List<DataMartField> getFieldsByType(boolean isKey) {
		List<DataMartField> list = new ArrayList<DataMartField>();
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartField field = (DataMartField)fields.get(key);
			if(field.isKey() == isKey) {
				list.add(field);		
			}
		}
		return list;
	}
	
	
	public List<DataMartField> getKeyFields() {
		return getFieldsByType(true);
	}
	
	
	public Iterator<DataMartField> getKeyFieldIterator() {
		return getKeyFields().iterator();
	}
	
	
	public List<DataMartField> getNormalFields() {
		return getFieldsByType(false);
	}
	
	
	public Iterator<DataMartField> getNormalFieldIterator() {
		return getNormalFields().iterator();
	}	
	
	public DataMartEntity addSubEntity(String subEntityName, String subEntityRole, String subEntityType) {
				
		String subEntityPath = "";
		if(getParent() != null) {
			subEntityPath = getName() +  "(" + getRole() + ")";
			if(!getPath().equalsIgnoreCase("")) {
				subEntityPath = getPath() + "." + subEntityPath;
			}
		}
		
		DataMartEntity subEntity = new DataMartEntity(subEntityName, subEntityPath, subEntityRole, subEntityType, getStructure());
		subEntity.setParent(this);
		
		addSubEntity(subEntity);
		return subEntity;
	}
	
	
	private void addSubEntity(DataMartEntity entity) {
		subEntities.put(entity.getUniqueName(), entity);
		getStructure().addEntity(entity);
	}
	
	
	public DataMartEntity getSubEntity(String entityUniqueName) {
		return (DataMartEntity)subEntities.get(entityUniqueName);
	}
	
	
	public List<DataMartEntity> getSubEntities() {
		List<DataMartEntity> list = new ArrayList<DataMartEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(subEntities.get(key));			
		}
		return list;
	}
	
	public List<DataMartEntity> getAllSubEntities() {
		List<DataMartEntity> list = new ArrayList<DataMartEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartEntity entity = (DataMartEntity)subEntities.get(key);
			list.add(entity);
			list.addAll(entity.getAllSubEntities());
		}
		return list;
	}
	
	
	public List<DataMartEntity> getAllSubEntities(String entityName) {
		List<DataMartEntity> list = new ArrayList<DataMartEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			DataMartEntity entity = (DataMartEntity)subEntities.get(key);
			if(entity.getName().equalsIgnoreCase(entityName)) {
				list.add(entity);
			}
			
			list.addAll(entity.getAllSubEntities(entityName));
		}
		return list;
	}
	
	public List<DataMartField> getAllFieldOccurencesOnSubEntity(String entityName, String fieldName) {
		List<DataMartField> list = new ArrayList<DataMartField>();
		List<DataMartEntity> entities = getAllSubEntities(entityName);
		for(int i = 0; i < entities.size(); i++) {
			DataMartEntity entity = entities.get(i);
			List<DataMartField> fields = entity.getAllFields();
			for(int j = 0; j < fields.size(); j++) {
				DataMartField field = fields.get(j);
				if(field.getName().endsWith("." + fieldName)) {
					list.add(field);
				}
			}
		}
		
		return list;
	}

	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		String line = getName().toUpperCase() + "(id="+getId()
			+";path="+path
			+";parent:" + (getParent()==null?"NULL": getParent().getName())
			+";role="+ role;
		
		
		buffer.append(line + "\n");
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			Object o = fields.get(key);
			buffer.append(" - " + (o==null? "NULL": o.toString()) + "\n");
		}
		
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext();) {
			key = it.next();
			Object o = subEntities.get(key);
			buffer.append(" + " + (o==null? "NULL": o.toString()));
		}
		return buffer.toString();
	}

	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRole() {
		return role!= null? role.toLowerCase(): null;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DataMartEntity getRoot() {
		if(root == null) {
			root = this;
			while(root.getParent() != null) {
				root = root.getParent();
			}
		}		
		
		return root;
	}

	public void setRoot(DataMartEntity root) {
		this.root = root;
	}

	
}
