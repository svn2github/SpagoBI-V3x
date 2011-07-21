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
package it.eng.qbe.statement.jpa;

import it.eng.spagobi.utilities.objects.Couple;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.ModelViewEntity;
import it.eng.qbe.model.structure.ModelViewEntity.ViewRelationship;
import it.eng.qbe.query.DataMartSelectField;
import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.Operand;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.WhereField;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class JPQLBusinessViewUtility {

	private JPQLStatement statement;
	
	public JPQLBusinessViewUtility(JPQLStatement statement){
		this.statement = statement;
	}
	
	/**
	 * Build the string with the join conditions between the views
	 * and the entity in relation with the view
	 * @param entityAliasesMaps 
	 * @param query the query
	 * @param queryWhereClause the where clause of the query (used for add the strin WHERE or AND at
	 * the beginning of the clause)
	 * @return
	 */
	public String buildViewsRelations(Map entityAliasesMaps, Query query, String queryWhereClause) {
		IModelField datamartField;
		Set<ViewRalationClause> viewRelations = new HashSet<ViewRalationClause>();
		StringBuffer whereClause = new StringBuffer("");
		Set<IModelEntity> concernedEntities = new HashSet<IModelEntity>();
		String clauseToReturn;
		
		//get all the fields
		
		//Get the select fields
		List<DataMartSelectField> selectFields = query.getSelectFields(false);
		if(selectFields!=null){
			for(int i=0; i<selectFields.size(); i++){
				datamartField = statement.getDataSource().getModelStructure().getField(selectFields.get(i).getUniqueName());
				concernedEntities.add(datamartField.getPathParent());
			}
		}

		
		//Get the where fields
		List<WhereField> whereFields = query.getWhereFields();
		if(whereFields!=null){
			for(int i=0; i<whereFields.size(); i++){
				WhereField whereField = whereFields.get(i);
				Operand leftOperand = whereField.getLeftOperand();
				if(statement.OPERAND_TYPE_FIELD.equalsIgnoreCase(leftOperand.type)){
					datamartField = statement.getDataSource().getModelStructure().getField( leftOperand.values[0] );
					concernedEntities.add(datamartField.getPathParent());
				}
				Operand rightOperand = whereField.getLeftOperand();
				if(statement.OPERAND_TYPE_FIELD.equalsIgnoreCase(rightOperand.type)){
					datamartField = statement.getDataSource().getModelStructure().getField( rightOperand.values[0] );
					concernedEntities.add(datamartField.getPathParent());
				}
			}
		}

		
		//Get the having fields
		List<HavingField> havingFields = query.getHavingFields();
		if(havingFields!=null){
			for(int i=0; i<havingFields.size(); i++){
				HavingField havingField = havingFields.get(i);
				Operand leftOperand = havingField.getLeftOperand();
				if(statement.OPERAND_TYPE_FIELD.equalsIgnoreCase(leftOperand.type)){
					datamartField = statement.getDataSource().getModelStructure().getField( leftOperand.values[0] );
					concernedEntities.add(datamartField.getPathParent());
				}
				Operand rightOperand = havingField.getLeftOperand();
				if(statement.OPERAND_TYPE_FIELD.equalsIgnoreCase(rightOperand.type)){
					datamartField = statement.getDataSource().getModelStructure().getField( rightOperand.values[0] );
					concernedEntities.add(datamartField.getPathParent());
				}
			}
		}

		
		//Get the order by fields
		List<DataMartSelectField> orderFields = query.getOrderByFields();
		if(orderFields!=null){
			for(int i=0; i<orderFields.size(); i++){
				datamartField = statement.getDataSource().getModelStructure().getField(orderFields.get(i).getUniqueName());
				concernedEntities.add(datamartField.getPathParent());
			}
		}

		
		//Get the group by fields
		List<DataMartSelectField> groupFields = query.getGroupByFields();
		if(groupFields!=null){
			for(int i=0; i<groupFields.size(); i++){
				datamartField = statement.getDataSource().getModelStructure().getField(groupFields.get(i).getUniqueName());
				concernedEntities.add(datamartField.getPathParent());
			}
		}

		
		Iterator<IModelEntity> concernedEntitiesIter = concernedEntities.iterator();
		while (concernedEntitiesIter.hasNext()) {
			buildViewsRelationsBackVistitPath((IModelEntity) concernedEntitiesIter.next(), null, viewRelations, entityAliasesMaps, query);			
		}
				
		Iterator<ViewRalationClause> iter = viewRelations.iterator();
		while (iter.hasNext()) {
			ViewRalationClause viewRalationClause = (ViewRalationClause) iter.next();
			whereClause.append(viewRalationClause.toString());
			if(iter.hasNext()){
				whereClause.append(" and ");
			}
		}
		
		clauseToReturn =whereClause.toString();
		if(clauseToReturn!=null && clauseToReturn.length()>1){
			if(queryWhereClause!=null && queryWhereClause.length()<4){
				clauseToReturn = " WHERE "+clauseToReturn;
			}else{
				clauseToReturn = " and "+clauseToReturn;
			}
		}
			
		
		return clauseToReturn;
	}
	
	/**
	 * Visit backward the branch from the field to the root..
	 * When it finds a view it add the join condition between the entity in the path
	 *  in relation with the view  
	 * @param entity
	 * @param child
	 * @param viewRelations the set of the join condition 
	 * @param entityAliases
	 * @param query
	 */
	private void buildViewsRelationsBackVistitPath(IModelEntity entity, IModelEntity child,  Set<ViewRalationClause> viewRelations,  Map entityAliases, Query query){
		
		if(entity==null){
			return;
		} else if(entity instanceof ModelViewEntity){
			addRelationForTheView(entity.getPathParent(), (ModelViewEntity)entity, child,viewRelations, entityAliases, query);
		}
		buildViewsRelationsBackVistitPath(entity.getPathParent(), entity, viewRelations, entityAliases, query);
		
	}
	
	/**
	 * Takes the relation parent-->view and view-->child and builds
	 * the join condition
	 * @param parent the parent of the view
	 * @param view the view
	 * @param child the child entity of the view
	 * @param viewRelations the set of the join condition 
	 * @param entityAliases
	 * @param query the query
	 */
	private void addRelationForTheView(IModelEntity parent, ModelViewEntity view, IModelEntity child, Set<ViewRalationClause> viewRelations, Map entityAliases, Query query){
		List<ViewRelationship> relations = view.getRelationships();
		IModelEntity inEntity,outEntity;
		ViewRelationship relation;
		for(int i=0; i<relations.size(); i++){
			relation = relations.get(i);
			outEntity = relation.getSourceEntity();
			inEntity = relation.getDestinationEntity();
			if( (view.getInnerEntities().contains(inEntity) && parent!=null && outEntity.getType().equals(parent.getType())) || //income relation
				(view.getInnerEntities().contains(outEntity) && child!=null && inEntity.getType().equals(child.getType()))){    //outcome relation
				//build the relation constraints
				viewRelations.addAll(buildRelationConditionString(relation.getSourceFileds(), relation.getDestinationFileds(), entityAliases, query));
			}
		}
	}
	
	/**
	 * Takes the fields of the relation source and destination entity 
	 * and builds the join condition
	 * @param sourceFields the list of the source entity in the relation
	 * @param destFields the list of the destination entity in the relation
	 * @param entityAliases
	 * @param query
	 * @return
	 */
	private Set<ViewRalationClause> buildRelationConditionString(List<IModelField> sourceFields, List<IModelField> destFields, Map entityAliases, Query query){
		Set<ViewRalationClause> clauses = new HashSet<ViewRalationClause>();
		IModelField sourceField, destField;
		for(int i=0; i<sourceFields.size(); i++){
			sourceField = sourceFields.get(i);
			destField = destFields.get(i);
			clauses.add(new ViewRalationClause(getFieldString(sourceField, entityAliases,query),getFieldString(destField, entityAliases,query)));
		}
		return clauses;
		
	}
	
	/**
	 * Builds the JPQL string of a field for the join condition..
	 * @param datamartField the field
	 * @param entityAliasesMaps
	 * @param query
	 * @return
	 */
	private String getFieldString(IModelField datamartField, Map entityAliasesMaps, Query query){
		String queryName;
		
		IModelEntity rootEntity;
		
		Couple queryNameAndRoot = datamartField.getQueryName();
		
		queryName = (String) queryNameAndRoot.getFirst();
		
		if(queryNameAndRoot.getSecond()!=null){
			rootEntity = (IModelEntity)queryNameAndRoot.getSecond(); 	
		}else{
			rootEntity = datamartField.getParent().getRoot(); 	
		}
		
		
//		if(datamartField.getPathParent() instanceof ModelViewEntity){
//			rootEntity = (datamartField.getParent()).getRoot(); 
//		}else{
//			rootEntity = datamartField.getParent().getRoot(); 
//		}
	
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
		String rootEntityAlias = (String)entityAliases.get(rootEntity.getUniqueName());
				
		if(rootEntityAlias == null) {
			rootEntityAlias = statement.getNextAlias(entityAliasesMaps);
			entityAliases.put(rootEntity.getUniqueName(), rootEntityAlias);
		}
				
		return rootEntityAlias + "." + queryName.substring(0,1).toLowerCase()+queryName.substring(1);
	}
	
	/**
	 * 
	 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
	 *
	 * A simple class that stay for a couple of
	 * non ordered strings..
	 * Note: (ViewRalationClause(a,b)).equals(ViewRalationClause(b,a)) = true.. 
	 *
	 */
	private class ViewRalationClause{
		private String field1;
		private String field2;
		
		public ViewRalationClause(String field1, String field2){
			this.field1 = field1;
			this.field2 = field2;
		}
		
		public String toString(){
			return "(" +this.field1 +" = "+ this.field2+ ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((field1 == null) ? 0 : field1.hashCode());
			result = prime * result
					+ ((field2 == null) ? 0 : field2.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ViewRalationClause other = (ViewRalationClause) obj;
			if (field1 == null) {
				if (other.field1 != null)
					return false;
			}
			if (field2 == null) {
				if (other.field2 != null)
					return false;
			}
			if ((field1.equals(other.field2)) || (field2.equals(other.field1)) ){
				if(!(field1.equals(other.field2)) || !(field2.equals(other.field1))){
					return false;
				}
			}
			if ((field1.equals(other.field1)) && !(field2.equals(other.field2)) ){
				return false;
			}	
			if ((field2.equals(other.field2)) && !(field1.equals(other.field1)) ){
				return false;
			}
			return true;
		}
	}
	
}
