/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.model.structure;

import it.eng.spagobi.commons.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia
 */
public class ModelEntity extends AbstractModelNode implements IModelEntity{
	
	protected IModelEntity root;	
	
	protected String path;		
	protected String role;	
	protected String type;	
	
	protected Map<String,IModelField> fields;	
	protected Map<String, ModelCalculatedField> calculatedFields;	
	protected Map<String,IModelEntity> subEntities;
	

	// =========================================================================
	// COSTRUCTORS 
	// =========================================================================
	
	
	public ModelEntity(String name, String path, String role, String type,	IModelStructure structure) {
		
		setStructure( structure );
		
		setId ( structure.getNextId() );
		setName( name );		
		setPath( path == null? "" : path );
		setRole( role );
		setType( type );
		
		setParent(null);
		this.fields = new HashMap<String,IModelField>();
		this.calculatedFields = new HashMap<String, ModelCalculatedField>();
		this.subEntities = new HashMap<String,IModelEntity>();
		
		initProperties();
	}
	
	public ModelEntity(String name, String role, String type , IModelEntity parent,	IModelStructure structure) {
		
		setStructure( structure );
		
		setId ( structure.getNextId() );
		setName( name );		
		setRole( role );
		setType( type );
		
		String thisPath = "";
		if(parent != null) {
			thisPath = getName();
			if(!parent.getPath().equalsIgnoreCase("")) {
				thisPath = parent.getPath() + "." + thisPath;
			}
		}
		this.path = thisPath;
		this.parent = parent;
		this.fields = new HashMap<String,IModelField>();
		this.calculatedFields = new HashMap<String, ModelCalculatedField>();
		this.subEntities = new HashMap<String,IModelEntity>();
		
		initProperties();
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
		if ( !(o instanceof ModelEntity) ) return false;
		ModelEntity de = (ModelEntity)o;
		return this.getUniqueName().equals( de.getUniqueName() );
	}

	
	public String getUniqueType() {
		String entityType = getType();
		if ( !StringUtilities.isEmpty( getRole() ) ) {
			entityType += "(" + getRole() + ")";
		}
		return entityType;
	}
	
	
	public void addField(IModelField field) {
		fields.put(field.getUniqueName(), field);
		getStructure().addField(field);
	}
	
	
	private IModelField addField(String fieldName, boolean isKey) {
		
		IModelField field = new ModelField(fieldName, this);
		field.setKey(isKey);
		addField(field);
		return field;
	}
	
	
	public IModelField addNormalField(String fieldName) {
		return addField(fieldName, false);
	}
	
	
	public IModelField addKeyField(String fieldName) {		
		return addField(fieldName, true);
	}
	
	
	
	public IModelField getField(String fieldUniqueName) {
		return (IModelField)fields.get(fieldUniqueName);
	}
	
	public IModelField getFieldByName(String fieldName) {
		IModelField field = null;
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			IModelField f = (IModelField)fields.get(key);
			if(f.getName().equals(fieldName)) {
				field = f;
				break;
			}
		}
		return field;
	}
	
	public void addCalculatedField(ModelCalculatedField calculatedField) {
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
		ModelCalculatedField calculatedField;
		
		calculatedField = (ModelCalculatedField)calculatedFields.remove(fieldName);
		if(calculatedField != null) {
			getStructure().removeCalculatedField(calculatedField.getParent().getUniqueName(), calculatedField);
		}
		
	}
	
	public List<ModelCalculatedField>  getCalculatedFields() {
		List<ModelCalculatedField> list;
		
		list = new ArrayList<ModelCalculatedField>();
		String key = null;
		for(Iterator<String> it = calculatedFields.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(calculatedFields.get(key));			
		}
		
		return list;
	}	
	
	public List<IModelField> getAllFields() {
		List<IModelField> list;
		
		list = new ArrayList<IModelField>();
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(fields.get(key));			
		}
		
		return list;
	}	
	
	
	public List<IModelField> getFieldsByType(boolean isKey) {
		List<IModelField> list = new ArrayList<IModelField>();
		String key = null;
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			IModelField field = (IModelField)fields.get(key);
			if(field.isKey() == isKey) {
				list.add(field);		
			}
		}
		return list;
	}
	
	
	public List<IModelField> getKeyFields() {
		return getFieldsByType(true);
	}
	
	
	public Iterator<IModelField> getKeyFieldIterator() {
		return getKeyFields().iterator();
	}
	
	
	public List<IModelField> getNormalFields() {
		return getFieldsByType(false);
	}
	
	
	public Iterator<IModelField> getNormalFieldIterator() {
		return getNormalFields().iterator();
	}	
	
	public IModelEntity addSubEntity(String subEntityName, String subEntityRole, String subEntityType) {
				
		String subEntityPath = "";
		if(getParent() != null) {
			subEntityPath = getName() +  "(" + getRole() + ")";
			if(!getPath().equalsIgnoreCase("")) {
				subEntityPath = getPath() + "." + subEntityPath;
			}
		}
		
		IModelEntity subEntity = new ModelEntity(subEntityName, subEntityPath, subEntityRole, subEntityType, getStructure());
		subEntity.setParent(this);
		
		addSubEntity(subEntity);
		return subEntity;
	}
	
	
	public void addSubEntity(IModelEntity entity) {
		subEntities.put(entity.getUniqueName(), entity);
		getStructure().addEntity(entity);
	}
	
	
	public IModelEntity getSubEntity(String entityUniqueName) {
		return (IModelEntity)subEntities.get(entityUniqueName);
	}
	
	
	public List<IModelEntity> getSubEntities() {
		List<IModelEntity> list = new ArrayList<IModelEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			list.add(subEntities.get(key));			
		}
		return list;
	}
	
	public List<IModelEntity> getAllSubEntities() {
		List<IModelEntity> list = new ArrayList<IModelEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			IModelEntity entity = (IModelEntity)subEntities.get(key);
			list.add(entity);
			list.addAll(entity.getAllSubEntities());
		}
		return list;
	}
	
	
	public List<IModelEntity> getAllSubEntities(String entityName) {
		List<IModelEntity> list = new ArrayList<IModelEntity>();
		String key = null;
		for(Iterator<String> it = subEntities.keySet().iterator(); it.hasNext(); ) {
			key = (String)it.next();
			IModelEntity entity = (IModelEntity)subEntities.get(key);
			if(entity.getName().equalsIgnoreCase(entityName)) {
				list.add(entity);
			}
			
			list.addAll(entity.getAllSubEntities(entityName));
		}
		return list;
	}
	
	public List<IModelField> getAllFieldOccurencesOnSubEntity(String entityName, String fieldName) {
		List<IModelField> list = new ArrayList<IModelField>();
		List<IModelEntity> entities = getAllSubEntities(entityName);
		for(int i = 0; i < entities.size(); i++) {
			IModelEntity entity = entities.get(i);
			List<IModelField> fields = entity.getAllFields();
			for(int j = 0; j < fields.size(); j++) {
				IModelField field = fields.get(j);
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

	public IModelEntity getRoot() {
		if(root == null) {
			root = this;
			while(root.getParent() != null) {
				root = root.getParent();
			}
		}		
		
		return root;
	}

	public void setRoot(IModelEntity root) {
		this.root = root;
	}
	
	public IModelEntity clone(IModelEntity newParent, String parentView){
		
		IModelEntity newModelEntity = new ModelEntity(name, role, type, newParent, structure);
		if(newParent==null || newParent.getRoot()==null){
			newModelEntity.setRoot(newParent);
		}else{
			newModelEntity.setRoot(newParent.getRoot());
		}
		
		
		//newModelEntity.setProperties(properties);
		
		Map<String,Object> properties2 = new HashMap<String, Object>();
		for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext();) {
			String key= (String)iterator.next();
			String o = (String)properties.get(key);
			properties2.put(key.substring(0), o.substring(0));
		}
		properties2.put("parentView", parentView);
				
		newModelEntity.setProperties(properties2);
		
		List<IModelField> fields = this.getNormalFields();
		for(int i=0; i<fields.size(); i++){
			newModelEntity.addField(fields.get(i).clone(newModelEntity));
		}
		List<ModelCalculatedField> calculatedFields = this.getCalculatedFields();
		for(int i=0; i<calculatedFields.size(); i++){
			newModelEntity.addCalculatedField((ModelCalculatedField)calculatedFields.get(i).clone(newModelEntity));
		}
		List<IModelEntity> subEntities = getSubEntities();
		for(int i=0; i<subEntities.size(); i++){
			newModelEntity.addSubEntity(subEntities.get(i).clone(newModelEntity, null));
		}
		return newModelEntity;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.model.structure.IModelNode#getParentViews()
	 */
	public List<ModelViewEntity> getParentViews() {
		return super.getParentViews(this);
	}

	
}
