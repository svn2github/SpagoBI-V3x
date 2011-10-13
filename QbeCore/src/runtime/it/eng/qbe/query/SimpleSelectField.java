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

import it.eng.qbe.query.serializer.json.QuerySerializationConstants;
import it.eng.spagobi.tools.dataset.common.query.AggregationFunctions;
import it.eng.spagobi.tools.dataset.common.query.IAggregationFunction;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SimpleSelectField extends AbstractSelectField {
	
	private String uniqueName;
	private IAggregationFunction function;
	
	private String pattern;
	


	public SimpleSelectField(String uniqueName, String function, String alias, boolean include, boolean visible,
		boolean groupByField, String orderType, String pattern ) {
		
		super(alias, ISelectField.SIMPLE_FIELD, include, visible);
				
		setUniqueName(uniqueName);
		setFunction( AggregationFunctions.get(function) );		
		setGroupByField(groupByField);
		setOrderType(orderType);
		setPattern(pattern);
	}
	
	public SimpleSelectField(SimpleSelectField field) {
			
			this(field.getUniqueName(), 
				field.getFunction().getName(), 
				field.getAlias(), 
				field.isIncluded(), 
				field.isVisible(),
				field.isGroupByField(), 
				field.getOrderType(), 
				field.getPattern());					
	}


	
	public IAggregationFunction getFunction() {
		return function;
	}

	public void setFunction(IAggregationFunction function) {
		this.function = function;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	
	public ISelectField copy() {
		return new SimpleSelectField( this );
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getName() {
		return getUniqueName();
	}

	public void setName(String name) {
		setUniqueName(name);
	}
	
	public String updateNature(String iconCls){
		//if an aggregation function is defined or if the field is declared as "measure" into property file,
		//  then it is a measure, elsewhere it is an attribute
		if ((getFunction() != null 
			&& !getFunction().equals(AggregationFunctions.NONE_FUNCTION))
			|| iconCls.equals("measure") || iconCls.equals("mandatory_measure")) {
			
			if(iconCls.equals("mandatory_measure")){
				nature = QuerySerializationConstants.FIELD_NATURE_MANDATORY_MEASURE;
			}
			else{
				nature = QuerySerializationConstants.FIELD_NATURE_MEASURE;
			}
		} else {

			if(iconCls.equals("segment_attribute")){
				nature = QuerySerializationConstants.FIELD_NATURE_SEGMENT_ATTRIBUTE;
			}
			else{
				nature = QuerySerializationConstants.FIELD_NATURE_ATTRIBUTE;
			}
		}
		return nature;
	}


}
