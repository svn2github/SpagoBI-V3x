/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.qbe.query;

import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Query {
	String id;
	String name;
	String description;
	
	boolean distinctClauseEnabled;
	
	List selectFields;	
	List whereClause;
	List havingClause;
	
	ExpressionNode whereClauseStructure;
	boolean nestedExpression;

	Map whereFieldMap;
	Map havingFieldMap;
	
	Query parentQuery;
	Map subqueries;
	
	public Query() {
		selectFields = new ArrayList();		
		whereClause = new ArrayList();
		havingClause = new ArrayList();
		whereFieldMap = new HashMap();
		havingFieldMap = new HashMap();
		subqueries  = new HashMap();
	}
	
	/*
	public Query(Query query) {
		
	}
	*/
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public boolean isEmpty() {
		List fields;
		fields = getDataMartSelectFields(true);
		Assert.assertNotNull(fields, "getDataMartSelectFields method cannot return a null value");
		return (fields.size() == 0);
	}
	
	public void addSelectFiled(String fieldUniqueName, String function, String fieldAlias, boolean include, boolean visible,
			boolean groupByField, String orderType, String pattern) {
		selectFields.add( new DataMartSelectField(fieldUniqueName, function, fieldAlias, include, visible, groupByField, orderType, pattern) );
	}
	
	public void addCalculatedFiled(String fieldAlias, String expression, String type, boolean included, boolean visible) {
		selectFields.add( new CalculatedSelectField(fieldAlias, expression, type, included, visible) );
	}
	
	public void addInLineCalculatedFiled(String fieldAlias, String expression, String type, boolean included, boolean visible, boolean groupByField, String orderType, String funct) {
		selectFields.add( new InLineCalculatedSelectField(fieldAlias, expression, type, included, visible, groupByField, orderType, funct) );
	}

	
	public void addWhereField(String name, String description, boolean promptable,
			it.eng.qbe.query.WhereField.Operand leftOperand, String operator, it.eng.qbe.query.WhereField.Operand rightOperand,
			String booleanConnector) {
		
		WhereField whereField = new WhereField(name, description, promptable,  leftOperand, operator, rightOperand, booleanConnector);
		
		whereClause.add( whereField );
		whereFieldMap.put("$F{" + name + "}", whereField);
	}
	
	public void addHavingField(String name, String description, boolean promptable, 
			it.eng.qbe.query.HavingField.Operand leftOperand, String operator, it.eng.qbe.query.HavingField.Operand rightOperand,
			String booleanConnector) {
		
		HavingField havingField = new HavingField(name, description, promptable, leftOperand, operator, rightOperand, booleanConnector);
		
		havingClause.add( havingField );
		havingFieldMap.put("$F{" + name + "}", havingField);
	}
	
	public WhereField getWhereFieldByName(String fname) {
		return (WhereField)whereFieldMap.get(fname.trim());
	}
	
	public HavingField getHavingFieldByName(String fname) {
		return (HavingField)havingFieldMap.get(fname.trim());
	}
	

	
	public List getSelectFields(boolean onlyIncluded) {
		List fields;
		if(onlyIncluded == false) {
			fields = new ArrayList(selectFields);
		} else {
			fields = new ArrayList();
			Iterator it = selectFields.iterator();
			while(it.hasNext()) {
				ISelectField field = (ISelectField)it.next();
				if(field.isIncluded()) {
					fields.add(field);
				}
			}
		}
		return fields;
	}
	
	public List getSelectFieldsByName(String uniqueName) {
		List fields;
		Iterator it;
		DataMartSelectField field;
		
		fields = new ArrayList();
		it = getSelectFields(false).iterator();
		while(it.hasNext()) {
			ISelectField f = (ISelectField)it.next();
			if(f.isDataMartField()) {
				field = (DataMartSelectField)f;
				if(field.getUniqueName().equalsIgnoreCase(uniqueName)) {
					fields.add(field);
				}
			}
				
		}
		
		return fields;
	}
	
	public ISelectField getSelectFieldByIndex(int fieldIndex) {
		Assert.assertTrue(fieldIndex >= 0 && fieldIndex < selectFields.size(), "Index [" + fieldIndex + "] out of bound for select fields list (0 - " + selectFields.size() + ")");
		return (ISelectField)selectFields.get(fieldIndex);
	}
	
	public int getSelectFieldIndex(String uniqueName) {
		int index;
		
		index = -1;
				
		for(int i = 0; i < selectFields.size(); i++) {
			ISelectField f = (ISelectField)selectFields.get(i);
			if(f.isDataMartField()) {
				DataMartSelectField field = (DataMartSelectField)f;
				if(field.getUniqueName().equalsIgnoreCase(uniqueName)) {
					index = i;
					break;
				}
			}
		}
		
		return index;
	}
	

	public List getDataMartSelectFields(boolean onlyIncluded) {
		List dataMartSelectFields;
		Iterator it;
		ISelectField field;
		
		dataMartSelectFields = new ArrayList();
		it = selectFields.iterator();
		while(it.hasNext()) {
			field = (ISelectField)it.next();
			if(field.isDataMartField()) {
				if( onlyIncluded == false || (onlyIncluded == true && field.isIncluded()) ) {
					dataMartSelectFields.add(field);
				}				
			}
		}
		
		return dataMartSelectFields;
	}
	
	public List getCalculatedSelectFields(boolean onlyIncluded) {
		List calculatedSelectFields;
		Iterator it;
		ISelectField field;
		
		calculatedSelectFields = new ArrayList();
		it = getSelectFields(false).iterator();
		while(it.hasNext()) {
			field = (ISelectField)it.next();
			if(field.isCalculatedField()) {
				if( onlyIncluded == false || (onlyIncluded == true && field.isIncluded()) ) {
					calculatedSelectFields.add(field);
				}
			}
		}
		
		return calculatedSelectFields;
	}
	
	public List getInLineCalculatedSelectFields(boolean onlyIncluded) {
		List inLineCalculatedSelectFields;
		Iterator it;
		ISelectField field;
		
		inLineCalculatedSelectFields = new ArrayList();
		it = getSelectFields(false).iterator();
		while(it.hasNext()) {
			field = (ISelectField)it.next();
			if(field.isInLineCalculatedField()) {
				if( onlyIncluded == false || (onlyIncluded == true && field.isIncluded()) ) {
					inLineCalculatedSelectFields.add(field);
				}
			}
		}
		
		return inLineCalculatedSelectFields;
	}
	
	public List getWhereFields() {
		return whereClause;
	}
	
	public List getHavingFields() {
		return havingClause;
	}

	public boolean isDistinctClauseEnabled() {
		return distinctClauseEnabled;
	}
	
	public void setDistinctClauseEnabled(boolean distinctClauseEnabled) {
		this.distinctClauseEnabled = distinctClauseEnabled;
	}
	
	public List getOrderByFields() {
		List orderByFields = new ArrayList();
		Iterator it = this.getDataMartSelectFields(false).iterator();
		while( it.hasNext() ) {
			DataMartSelectField selectField = (DataMartSelectField)it.next();
			if(selectField.isOrderByField()) {
				orderByFields.add(selectField);
			}
		}
		return orderByFields;
	}
	
	
	public List getGroupByFields() {
		List groupByFields = new ArrayList();
		Iterator it = this.getDataMartSelectFields(false).iterator();
		while( it.hasNext() ) {
			DataMartSelectField selectField = (DataMartSelectField)it.next();
			if(selectField.isGroupByField()) {
				groupByFields.add(selectField);
			}
		}
		
		Iterator<InLineCalculatedSelectField> it2 = this.getInLineCalculatedSelectFields(false).iterator();
		while( it2.hasNext() ) {
			InLineCalculatedSelectField selectField = (InLineCalculatedSelectField)it2.next();
			if(selectField.isGroupByField()) {
				groupByFields.add(selectField);
			}
		}
		
		return groupByFields;
	}
	
	
	public ExpressionNode getWhereClauseStructure() {
		return whereClauseStructure;
	}

	public void setWhereClauseStructure(ExpressionNode whereClauseStructure) {
		this.whereClauseStructure = whereClauseStructure;
	}
	
	/*
	 * true iff it is an expression built using the client side expression wizard
	 */
	public boolean isNestedExpression() {
		return nestedExpression;
	}

	public void setNestedExpression(boolean nestedExpression) {
		this.nestedExpression = nestedExpression;
	}
	
	
	public Query getParentQuery() {
		return parentQuery;
	}

	public void setParentQuery(Query parentQuery) {
		this.parentQuery = parentQuery;
	}
	
	public boolean hasParentQuery() {
		return getParentQuery() != null;
	}
	
	public void addSubquery(Query subquery) {
		subqueries.put(subquery.getId(), subquery);
		subquery.setParentQuery(this);
	}
	
	public Query getSubquery(String id) {
		return (Query)subqueries.get(id);
	}
	
	public Set getSubqueryIds() {
		return new HashSet(subqueries.keySet());
	}
	
	public Query removeSubquery(String id) {
		Query subquery = (Query)subqueries.remove(id);
		if(subquery != null) subquery.setParentQuery(null);
		return subquery;
	}

	/*
	public Query copy() {
		return new Query(this);
	}
	*/
	

}
