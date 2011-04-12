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
package it.eng.qbe.model.structure.builder.jpa;

import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.model.properties.initializer.ModelStructurePropertiesInitializerFactory;
import it.eng.qbe.model.properties.initializer.IModelStructurePropertiesInitializer;
import it.eng.qbe.model.structure.IModelViewEntityDescriptor;
import it.eng.qbe.model.structure.ModelCalculatedField;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelStructure;
import it.eng.qbe.model.structure.ModelViewEntity;
import it.eng.qbe.model.structure.builder.IModelStructureBuilder;
import it.eng.qbe.model.structure.filter.IQbeTreeEntityFilter;
import it.eng.qbe.model.structure.filter.IQbeTreeFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderFieldFilter;
import it.eng.spagobi.utilities.assertion.Assert;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JPAModelStructureBuilder implements IModelStructureBuilder {
	
	private static transient Logger logger = Logger.getLogger(JPAModelStructureBuilder.class);

	private JPADataSource dataSource;	
	private EntityManager entityManager;
	IModelStructurePropertiesInitializer propertiesInitializer;


	/**
	 * Constructor
	 * @param dataSource the JPA DataSource
	 */
	public JPAModelStructureBuilder(JPADataSource dataSource) {
		if(dataSource == null) {
			throw new IllegalArgumentException("DataSource parameter cannot be null");
		}
		setDataSource( dataSource );
		propertiesInitializer = ModelStructurePropertiesInitializerFactory.getDataMartStructurePropertiesInitializer(dataSource);
	}
	
	/**
	 * This method builds a JPA datamart structure.
	 * @return DataMartModelStructure
	 */
	public IModelStructure build() {
		ModelStructure modelStructure;
		String modelName;
		Metamodel jpaMetamodel;
		Set<EntityType<?>> jpaEntities;
		
		logger.debug("IN");
		
		try {
			modelStructure = new ModelStructure();
	
			modelName = getDataSource().getConfiguration().getModelName();
			Assert.assertNotNull(getDataSource(), "datasource cannot be null");	
			setEntityManager(getDataSource().getEntityManager());
			Assert.assertNotNull(getEntityManager(), "Impossible to find the jar file associated to datamart named: [" + modelName + "]");
			
			
			propertiesInitializer.addProperties(modelStructure);	
			Map calculatedFields = getDataSource().getConfiguration().loadCalculatedFields();
			modelStructure.setCalculatedFields(calculatedFields);
				
			jpaMetamodel = getEntityManager().getMetamodel();		
			jpaEntities = jpaMetamodel.getEntities();
			logger.debug("Jpa metamodel contains ["+ jpaEntities.size() + "] entity types");
			
			for(EntityType<?> entityType: jpaEntities) {
				logger.debug("Adding entity type [" + entityType + "] to model structure");
				String entityTypeName =  entityType.getJavaType().getName();
				addEntity(modelStructure, modelName, entityTypeName);	
				logger.info("Entity type [" + entityType + "] succesfully added to model structure");
			}		
			
			List<IModelViewEntityDescriptor> list = getDataSource().getConfiguration().loadViews();
			if(list.size() > 0) {
				IModelViewEntityDescriptor viewDescriptor = list.get(0);
				ModelViewEntity viewEntity = new ModelViewEntity(viewDescriptor, modelName, modelStructure);
				propertiesInitializer.addProperties(viewEntity);
				modelStructure.addRootEntity(modelName, viewEntity);
				
				String viewEntityUniqueName = viewEntity.getUniqueName();
				for(IModelEntity e : viewEntity.getInnerEntities()) {
					e.getProperties().put("parentView", viewEntityUniqueName);
				}
			}

			logger.info("Model structure for model [" + modelName + "] succesfully built");
			
			return modelStructure;
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to build model structure", t);
		
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void addEntity (IModelStructure modelStructure, String modelName, String entityType){

		String entityName = getEntityNameFromEntityType(entityType);		
		IModelEntity dataMartEntity = modelStructure.addRootEntity(modelName, entityName, null, null, entityType);
		propertiesInitializer.addProperties(dataMartEntity);
		
		//addKeyFields(dataMartEntity);		
		List subEntities = addNormalFields(dataMartEntity);
		addCalculatedFields(dataMartEntity);
		addSubEntities(dataMartEntity, subEntities, 0);
		
	}
	
	private String getEntityNameFromEntityType(String entityType) {
		String entityName = entityType;
		entityName = (entityName.lastIndexOf('.') > 0 ?
				  entityName.substring(entityName.lastIndexOf('.') + 1 , entityName.length()) :
				  entityName);
				  
		return entityName;
	}

	/**
	 * This method adds the normal fields to the datamart entry structure
	 * @param dataMartEntity:  the datamart structure to complete
	 */
	public List addNormalFields(IModelEntity dataMartEntity) {		
		logger.debug("Adding the field "+dataMartEntity.getName());
		String[] propertyNames;
		List subEntities = new ArrayList();			
		EntityType thisEntityType = null;
		
		Metamodel classMetadata = getEntityManager().getMetamodel();
		
		for(Iterator it = classMetadata.getEntities().iterator(); it.hasNext(); ) {
			EntityType et = (EntityType)it.next();
			if(et.getJavaType().getName().equals(dataMartEntity.getType())){
				thisEntityType = et;
				break;
			}
		}	
		
		if(thisEntityType==null){
			return new ArrayList();
		}
		
		Set<Attribute> attributes = thisEntityType.getAttributes();
		Iterator<Attribute> attributesIt = attributes.iterator();
		
		
		while(attributesIt.hasNext()){
			Attribute a = attributesIt.next();
			//normal attribute
			if(a.getPersistentAttributeType().equals(PersistentAttributeType.BASIC)){		
				addField(a, dataMartEntity,"");
			}else// relation 
				if(a.getPersistentAttributeType().equals(PersistentAttributeType.MANY_TO_ONE)){
					Class c = a.getJavaType();
					String entityType = c.getName();
					String columnName = a.getName();
					String entityName =  a.getName();
			 		IModelEntity subentity = new ModelEntity(entityName, null, columnName, entityType, dataMartEntity.getStructure());		
			 		subEntities.add(subentity);		
			}else//composite key 
				if(a.getPersistentAttributeType().equals(PersistentAttributeType.EMBEDDED)){
				Set<Attribute> keyAttre = ((EmbeddableType)((SingularAttribute)a).getType()).getAttributes();
				Iterator<Attribute> keyIter = keyAttre.iterator();
				while(keyIter.hasNext()){
					addField(keyIter.next(), dataMartEntity, a.getName()+".");	
				}
			}
		}
		
		logger.debug("Field "+dataMartEntity.getName()+" added");
		return subEntities;
	}
	
	/**
	 * Add an attribute to the model
	 * @param attr the attribute
	 * @param dataMartEntity the parent entity
	 */
	private void addField(Attribute attr, IModelEntity dataMartEntity, String keyPrefix){
		String n = attr.getName();
		Member m = attr.getJavaMember();
		Class c = attr.getJavaType();
		String type = c.getName();
		
		// TODO: SCALE E PREC
		int scale = 0;
		int precision = 0;

		IModelField datamartField = dataMartEntity.addNormalField(keyPrefix+ attr.getName());
		datamartField.setType(type);
		datamartField.setPrecision(precision);
		datamartField.setLength(scale);
		propertiesInitializer.addProperties(datamartField);
	}
	
	// TODO: controllare correttezza per jpa...se va bene generalizzare metodo sia per jpa che hibernate!
	private void addCalculatedFields(IModelEntity dataMartEntity) {
		logger.debug("Adding the calculated field "+dataMartEntity.getName());
		List calculatedFileds;
		ModelCalculatedField calculatedField;
		
		calculatedFileds = dataMartEntity.getStructure().getCalculatedFieldsByEntity(dataMartEntity.getUniqueName()); 
		if(calculatedFileds != null) {
			for(int i = 0; i < calculatedFileds.size(); i++) {
				calculatedField = (ModelCalculatedField)calculatedFileds.get(i);
				dataMartEntity.addCalculatedField(calculatedField);
				propertiesInitializer.addProperties(calculatedField);
			}
		}
		logger.debug("Added the calculated field "+dataMartEntity.getName());
	}
	
	// TODO: controllare correttezza per jpa...se va bene generalizzare metodo sia per jpa che hibernate! 
	private void addSubEntities(IModelEntity dataMartEntity, List subEntities, int recursionLevel) {
		
		Iterator it = subEntities.iterator();
		while (it.hasNext()) {
			IModelEntity subentity = (IModelEntity)it.next();
			if (subentity.getType().equalsIgnoreCase(dataMartEntity.getType())){
				// ciclo di periodo 0!
			} else if(recursionLevel > 10) {
				// prune recursion tree 
			} else {
				addSubEntity(dataMartEntity, subentity, recursionLevel+1);
			}
		}
	}

	// TODO: controllare correttezza per jpa...se va bene generalizzare metodo sia per jpa che hibernate!	
	private void addSubEntity (IModelEntity parentEntity, IModelEntity subEntity, int recursionLevel){
		logger.debug("Adding the sub entity field "+subEntity.getName()+" child of "+parentEntity.getName());
		IModelEntity dataMartEntity;				
		
		//String entityName = getEntityNameFromEntityType(entityType);		
		dataMartEntity = parentEntity.addSubEntity(subEntity.getName(), subEntity.getRole(), subEntity.getType());
		propertiesInitializer.addProperties(dataMartEntity);
		
		//addKeyFields(dataMartEntity);			
		List subEntities = addNormalFields(dataMartEntity);		
		addCalculatedFields(dataMartEntity);
		addSubEntities(dataMartEntity, subEntities, recursionLevel);
		logger.debug("Added the sub entity field "+subEntity.getName()+" child of "+parentEntity.getName());
	}
	
	/**
	 * @return the JPADataSource
	 */
	public JPADataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param JPADataSource the datasource to set
	 */
	public void setDataSource(JPADataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
