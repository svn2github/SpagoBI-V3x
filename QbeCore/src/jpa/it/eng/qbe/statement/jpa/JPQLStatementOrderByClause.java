/**
 * 
 */
package it.eng.qbe.statement.jpa;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.AbstractSelectField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.SimpleSelectField;
import it.eng.qbe.query.InLineCalculatedSelectField;
import it.eng.qbe.query.Query;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.objects.Couple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPQLStatementOrderByClause  extends JPQLStatementClause {
	
	public static transient Logger logger = Logger.getLogger(JPQLStatementOrderByClause.class);
	
	protected JPQLStatementOrderByClause(JPQLStatement statement) {
		parentStatement = statement;
	}
	
	protected String buildOrderByClause(Query query, Map entityAliasesMaps) {
		StringBuffer buffer;
		
		
		buffer = new StringBuffer();
		
		List<ISelectField> orderByFields = query.getOrderByFields();
		if(orderByFields.size() == 0) return buffer.toString();
		
		buffer.append(JPQLStatementConstants.STMT_KEYWORD_ORDER_BY);
		
		Map entityAliases = (Map)entityAliasesMaps.get(query.getId());
		
		String fieldSeparator = "";
		
		for( ISelectField orderByField : orderByFields ) {
			Assert.assertTrue(orderByField.isOrderByField(), "Field [" + orderByField.getAlias() +"] is not an orderBy filed");
			
			buffer.append(fieldSeparator);
			
			if(orderByField.isSimpleField()) {				
				SimpleSelectField simpleField = (SimpleSelectField)orderByField;
				
				IModelField modelField = parentStatement.getDataSource().getModelStructure().getField(simpleField.getUniqueName());
				Couple queryNameAndRoot = modelField.getQueryName();
				
				String queryName = (String) queryNameAndRoot.getFirst();
				logger.debug("select field query name [" + queryName + "]");
				
				IModelEntity root;
				if(queryNameAndRoot.getSecond()!=null){
					root = (IModelEntity)queryNameAndRoot.getSecond(); 	
				} else {
					root = modelField.getParent().getRoot(); 	
				}
				
				if(!entityAliases.containsKey(root.getUniqueName())) {
					entityAliases.put(root.getUniqueName(), parentStatement.getNextAlias(entityAliasesMaps));
				}
				
				String entityAlias = (String)entityAliases.get( root.getUniqueName() );
				String fieldName = entityAlias + "." + queryName;
				
				buffer.append(" " + simpleField.getFunction().apply(fieldName));
			
			} else if(orderByField.isInLineCalculatedField()) {
				InLineCalculatedSelectField inlineCalculatedField = (InLineCalculatedSelectField)orderByField;
				String fieldName = parseInLinecalculatedField(inlineCalculatedField.getExpression(), inlineCalculatedField.getSlots(), query, entityAliasesMaps);
				
				buffer.append(" " + inlineCalculatedField.getFunction().apply(fieldName));
			} else {
				// TODO throw an exception here
			}
			
			buffer.append(" " + (orderByField.isAscendingOrder()? JPQLStatementConstants.STMT_KEYWORD_ASCENDING: JPQLStatementConstants.STMT_KEYWORD_DESCENDING) );
			
			fieldSeparator = ", ";			
		}
		
		return buffer.toString().trim();
	}
	
	
	
}
