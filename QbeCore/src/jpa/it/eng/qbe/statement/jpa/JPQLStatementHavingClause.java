/**
 * 
 */
package it.eng.qbe.statement.jpa;

import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.jpa.JPQLStatementConditionalOperators.IConditionalOperator;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPQLStatementHavingClause extends AbstractJPQLStatementFilteringClause {
	
	public static final String HAVING = "HAVING";
	
	public static transient Logger logger = Logger.getLogger(JPQLStatementHavingClause.class);
	
	public static String build(JPQLStatement parentStatement, Query query, Map<String, Map<String, String>> entityAliasesMaps){
		JPQLStatementHavingClause clause = new JPQLStatementHavingClause(parentStatement);
		return clause.buildClause(query, entityAliasesMaps);
	}
	
	protected JPQLStatementHavingClause(JPQLStatement statement) {
		parentStatement = statement;
	}
	
	public String buildClause(Query query, Map<String, Map<String, String>> entityAliasesMaps) {
		
		StringBuffer buffer = new StringBuffer();
		
		if( query.getHavingFields().size() > 0) {
			buffer.append(" " + HAVING + " ");
			
			List<HavingField> havingFields = query.getHavingFields();
			String booleanConnetor = "";
			for (HavingField havingField : havingFields) {
				
				buffer.append(" " + booleanConnetor + " ");
				
				String leftOperandType = havingField.getLeftOperand().type;
				if(havingField.getLeftOperand().values[0].contains("expression")){
					String havingClauseElement;
					
					IConditionalOperator conditionalOperator = null;
					conditionalOperator = (IConditionalOperator)JPQLStatementConditionalOperators.getOperator( havingField.getOperator() );
					Assert.assertNotNull(conditionalOperator, "Unsopported operator " + havingField.getOperator() + " used in query definition");

					havingClauseElement =  buildInLineCalculatedFieldClause(havingField.getOperator(), havingField.getLeftOperand(), havingField.isPromptable(), havingField.getRightOperand(), query, entityAliasesMaps, conditionalOperator);
					
					
					
					buffer.append(havingClauseElement);
				}else{
						buffer.append( buildHavingClauseElement(havingField, query, entityAliasesMaps) );
				}
				
				
				booleanConnetor =  havingField.getBooleanConnector();
				
			}
		}
		
		return buffer.toString().trim();
	}
	
	private String buildHavingClauseElement(HavingField havingField, Query query, Map entityAliasesMaps) {
		
		String havingClauseElement;
		String[] leftOperandElements;
		String[] rightOperandElements;
				
		logger.debug("IN");
		
		try {
			IConditionalOperator conditionalOperator = null;
			conditionalOperator = (IConditionalOperator)JPQLStatementConditionalOperators.getOperator( havingField.getOperator() );
			Assert.assertNotNull(conditionalOperator, "Unsopported operator " + havingField.getOperator() + " used in query definition");
			
			leftOperandElements = buildOperand(havingField.getLeftOperand(), query, entityAliasesMaps);
			
			if (parentStatement.OPERAND_TYPE_STATIC.equalsIgnoreCase(havingField.getRightOperand().type) 
					&& havingField.isPromptable()) {
				// get last value first (the last value edited by the user)
				rightOperandElements = havingField.getRightOperand().lastValues;
			} else {
				rightOperandElements = buildOperand(havingField.getRightOperand(), query, entityAliasesMaps);
			}
			
			if (parentStatement.OPERAND_TYPE_STATIC.equalsIgnoreCase(havingField.getLeftOperand().type) )  {
				leftOperandElements = getTypeBoundedStaticOperand(havingField.getRightOperand(), havingField.getOperator(), leftOperandElements);
			}
			
			if (parentStatement.OPERAND_TYPE_STATIC.equalsIgnoreCase(havingField.getRightOperand().type) )  {
				rightOperandElements = getTypeBoundedStaticOperand(havingField.getLeftOperand(), havingField.getOperator(), rightOperandElements);
			}
			
			havingClauseElement = conditionalOperator.apply(leftOperandElements[0], rightOperandElements);
			logger.debug("Having clause element value [" + havingClauseElement + "]");
		} finally {
			logger.debug("OUT");
		}
		
		
		return  havingClauseElement;
	}
}
