package it.eng.qbe.statement;

import it.eng.qbe.datasource.AbstractDataSource;
import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.CalculatedSelectField;
import it.eng.qbe.query.DataMartSelectField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.InLineCalculatedSelectField;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.json.QueryJSONSerializer;
import it.eng.qbe.query.serializer.json.QuerySerializationConstants;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.tools.dataset.bo.AbstractDataSet;
import it.eng.spagobi.tools.dataset.bo.DataSetVariable;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreFilter;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.common.metadata.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData.FieldType;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.MetaData;
import it.eng.spagobi.tools.dataset.persist.DataSetTableDescriptor;
import it.eng.spagobi.tools.dataset.persist.IDataSetTableDescriptor;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.sql.SqlUtils;
import it.eng.spagobi.utilities.temporarytable.TemporaryTableManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

public abstract class AbstractQbeDataSet extends AbstractDataSet {


	protected IStatement statement;
	protected IDataStore dataStore;
	protected boolean abortOnOverflow;	
	protected Map bindings;
	protected Map userProfileAttributes;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(AbstractQbeDataSet.class);
    
	
	public AbstractQbeDataSet(IStatement statement) {
		setStatement(statement);
		bindings = new HashMap();
	}

	public IDataStore getDataStore() {
		return dataStore;
	}

	private MetaData getDataStoreMeta(Query query) {
		MetaData dataStoreMeta;
		ISelectField queryFiled;
		FieldMetadata dataStoreFieldMeta;
		
		dataStoreMeta = new MetaData();
		
		Iterator fieldsIterator = query.getSelectFields(true).iterator();
		while(fieldsIterator.hasNext()) {
			queryFiled = (ISelectField)fieldsIterator.next();
			
			dataStoreFieldMeta = new FieldMetadata();
			dataStoreFieldMeta.setAlias( queryFiled.getAlias() );
			if(queryFiled.isDataMartField()) {
				DataMartSelectField dataMartSelectField = (DataMartSelectField) queryFiled;
				dataStoreFieldMeta.setName( ((DataMartSelectField)queryFiled).getUniqueName() );
				dataStoreFieldMeta.setProperty("calculated", new Boolean(false));
				dataStoreFieldMeta.setProperty("uniqueName", dataMartSelectField.getUniqueName());
				dataStoreFieldMeta.setType(Object.class);
				String format = dataMartSelectField.getPattern();
				if (format != null && !format.trim().equals("")) {
					dataStoreFieldMeta.setProperty("format", format);
				}

				IModelField datamartField = ((AbstractDataSource)statement.getDataSource()).getModelStructure().getField( dataMartSelectField.getUniqueName() );
				String iconCls = datamartField.getPropertyAsString("type");	
				String nature = QueryJSONSerializer.getSelectFieldNature(dataMartSelectField, iconCls);
				dataStoreFieldMeta.setProperty("aggregationFunction", dataMartSelectField.getFunction().getName());
				if( nature.equals(QuerySerializationConstants.FIELD_NATURE_MANDATORY_MEASURE)||
					nature.equals(QuerySerializationConstants.FIELD_NATURE_MEASURE)){
					
					dataStoreFieldMeta.setFieldType(FieldType.MEASURE);
				}else{
					dataStoreFieldMeta.setFieldType(FieldType.ATTRIBUTE);
				}


			} else if(queryFiled.isCalculatedField()){
				CalculatedSelectField claculatedQueryField = (CalculatedSelectField)queryFiled;
				dataStoreFieldMeta.setName(claculatedQueryField.getAlias());
				dataStoreFieldMeta.setProperty("calculated", new Boolean(true));	
				// FIXME also calculated field must have uniquename for uniformity
				dataStoreFieldMeta.setProperty("uniqueName", claculatedQueryField.getAlias());
				DataSetVariable variable = new DataSetVariable(claculatedQueryField.getAlias(), claculatedQueryField.getType(), claculatedQueryField.getExpression());
				dataStoreFieldMeta.setProperty("variable", variable);	
				dataStoreFieldMeta.setType( variable.getTypeClass() );	
				
			} else if(queryFiled.isInLineCalculatedField()){
				InLineCalculatedSelectField claculatedQueryField = (InLineCalculatedSelectField)queryFiled;
				dataStoreFieldMeta.setName(claculatedQueryField.getAlias());
				dataStoreFieldMeta.setProperty("calculated", new Boolean(false));	
				// FIXME also calculated field must have uniquename for uniformity
				dataStoreFieldMeta.setProperty("uniqueName", claculatedQueryField.getAlias());
				DataSetVariable variable = new DataSetVariable(claculatedQueryField.getAlias(), claculatedQueryField.getType(), claculatedQueryField.getExpression());
				dataStoreFieldMeta.setProperty("variable", variable);	
				dataStoreFieldMeta.setType( variable.getTypeClass() );	
				String nature = QueryJSONSerializer.getSelectFieldNature(queryFiled, null);
				dataStoreFieldMeta.setProperty("nature", nature);
			}
			dataStoreFieldMeta.setProperty("visible", new Boolean(queryFiled.isVisible()));	
			
			dataStoreMeta.addFiedMeta(dataStoreFieldMeta);
		}
		
		return dataStoreMeta;
	}
	
	
	
	protected DataStore toDataStore(List result) {
		DataStore dataStore;
		MetaData dataStoreMeta;
		Object[] row;
	
		dataStore = new DataStore();
		dataStoreMeta = getDataStoreMeta( statement.getQuery() );
		dataStore.setMetaData(dataStoreMeta);
		
		Iterator it = result.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			
		    if (!(o instanceof Object[])){
		    	row = new Object[1];
		    	row[0] = o == null? "": o;
		    }else{
		    	row = (Object[])o;
		    }
		    
		    
		    IRecord record = new Record(dataStore);
		    for(int i = 0,  j = 0; i < dataStoreMeta.getFieldCount(); i++) {
				IFieldMetaData fieldMeta = dataStoreMeta.getFieldMeta(i);
				Boolean calculated = (Boolean)fieldMeta.getProperty("calculated");
				if(calculated.booleanValue() == false) {
					Assert.assertTrue(j < row.length, "Impossible to read field [" + fieldMeta.getName() + "] from resultset");
					record.appendField( new Field( row[j] ) );
					if(row[j] != null) fieldMeta.setType(row[j].getClass());
					j++;					
				} else {
					DataSetVariable variable = (DataSetVariable)fieldMeta.getProperty("variable");
					if(variable.getResetType() == DataSetVariable.RESET_TYPE_RECORD) {
						variable.reset();
					}
					
					record.appendField( new Field( variable.getValue()) );
					if(variable.getValue() != null)  fieldMeta.setType(variable.getValue().getClass());
				}
			}
		    
		    processCalculatedFields(record, dataStore);
		    dataStore.appendRecord(record);
		}
		
		return dataStore;
	}
	
	private void processCalculatedFields(IRecord record, IDataStore dataStore) {
		IMetaData dataStoreMeta;
		List calculatedFieldsMeta;
		
		dataStoreMeta = dataStore.getMetaData();
		calculatedFieldsMeta = dataStoreMeta.findFieldMeta("calculated", Boolean.TRUE);
		for(int i = 0; i < calculatedFieldsMeta.size(); i++) {
			IFieldMetaData fieldMeta = (IFieldMetaData)calculatedFieldsMeta.get(i);
			DataSetVariable variable = (DataSetVariable)fieldMeta.getProperty("variable");
			
			ScriptEngineManager scriptManager = new ScriptEngineManager();
			ScriptEngine groovyScriptEngine = scriptManager.getEngineByName("groovy");
			
			
			// handle bindings 
			// ... static bindings first
			Iterator it = bindings.keySet().iterator();
			while(it.hasNext()) {
				String bindingName = (String)it.next();
				Object bindingValue = bindings.get(bindingName);
				groovyScriptEngine.put(bindingName, bindingValue);
			}
			
			// ... then runtime bindings
			Map qFields = new HashMap();
			Map dmFields = new HashMap();
			Object[] columns = new Object[dataStoreMeta.getFieldCount()];
			for(int j = 0; j < dataStoreMeta.getFieldCount(); j++) {
				qFields.put(dataStoreMeta.getFieldMeta(j).getAlias(), record.getFieldAt(j).getValue());
				dmFields.put(dataStoreMeta.getFieldMeta(j).getProperty("uniqueName"), record.getFieldAt(j).getValue());
				columns[j] = record.getFieldAt(j).getValue();
			}
			
			groovyScriptEngine.put("qFields", qFields); // key = alias
			groovyScriptEngine.put("dmFields", dmFields); // key = id
			groovyScriptEngine.put("fields", qFields); // default key = alias
			groovyScriptEngine.put("columns", columns); // key = col-index
			
			// show time
			Object calculatedValue = null;
			try {
				calculatedValue = groovyScriptEngine.eval(variable.getExpression());
				
			} catch (ScriptException ex) {
				calculatedValue = "NA";
			    ex.printStackTrace();
			}	
			
			//logger.debug("Field [" + fieldMeta.getName()+ "] is equals to [" + calculatedValue + "]");
			variable.setValue(calculatedValue);
			
			record.getFieldAt(dataStoreMeta.getFieldIndex(fieldMeta.getName())).setValue(variable.getValue());
		}
	}
	
	
	public void printInfo() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		List<ScriptEngineFactory> factories = mgr.getEngineFactories();
		
		for (ScriptEngineFactory factory: factories) {
		    System.out.println("ScriptEngineFactory Info");
		    String engName = factory.getEngineName();
		    String engVersion = factory.getEngineVersion();
		    String langName = factory.getLanguageName();
		    String langVersion = factory.getLanguageVersion();
		    System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);
		    List<String> engNames = factory.getNames();
		    for(String name: engNames) {
		      System.out.printf("\tEngine Alias: %s\n", name);
		    }
		    System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
		  }   
	}
	
	public IStatement getStatement() {
		return statement;
	}


	public void setStatement(IStatement statement) {
		this.statement = statement;
	}
	
	public boolean isAbortOnOverflow() {
		return abortOnOverflow;
	}


	public void setAbortOnOverflow(boolean abortOnOverflow) {
		this.abortOnOverflow = abortOnOverflow;
	}
	
	public void addBinding(String bindingName, Object bindingValue) {
		bindings.put(bindingName, bindingValue);
	}

	public Object getQuery() {
		return this.statement.getQuery();
	}

	public void setQuery(Object query) {
		this.statement.setQuery((it.eng.qbe.query.Query) query);
		
	}
	
	public String getSQLQuery(){
		return statement.getSqlQueryString();
	}
	
	
	public IDataSetTableDescriptor persist(String tableName, Connection connection) {
		IDataSource dataSource = getDataSource();
		try {
			String sql = getSQLQuery();
			TemporaryTableManager.createTable(sql, tableName, dataSource);
			return getDataSetTableDescriptor(sql, statement.getQuery());
		} catch (Exception e) {
			logger.error("Error loading Persisting the temporary table with name"+tableName, e);
			throw new SpagoBIEngineRuntimeException("Error loading Persisting the temporary table with name"+tableName, e);
		}	
	}
	
	public IDataStore getDomainValues(String fieldName, Integer start, Integer limit, IDataStoreFilter filter) {
		IDataSource ds = getDataSource();	
		IDataStore toReturn = null;
		String sql = getSQLQuery();
		IDataSetTableDescriptor tableDescriptor = getDataSetTableDescriptor(sql, statement.getQuery());
		String filterColumnName = tableDescriptor.getColumnName(fieldName);
		String sqlStatement = "Select DISTINCT("+ filterColumnName+") FROM "+ TemporaryTableManager.getTableName((String)(getUserProfileAttributes().get(SsoServiceInterface.USER_ID)));
		try {
			toReturn = TemporaryTableManager.queryTemporaryTable(sqlStatement, ds, start, limit);
		} catch (Exception e) {
			logger.error("Error loading the domain values for the field "+fieldName, e);
			throw new SpagoBIEngineRuntimeException("Error loading the domain values for the field "+fieldName, e);
			
		}
		return toReturn;
	}
	
	/**
	 * Get the relation between the fields in the select clause
	 * of the Qbe Query and its sql representation
	 * @param sqlQuery Qbe Query translated in sql
	 * @param qbeQuery Qbe Query 
	 * @return
	 */
	private IDataSetTableDescriptor getDataSetTableDescriptor(String sqlQuery, Query qbeQuery){
		DataSetTableDescriptor dataSetTableDescriptor = new DataSetTableDescriptor();
		
		List<String[]> selectFieldsColumn = SqlUtils.getSelectFields(sqlQuery);
		List<ISelectField> selectFieldsNames = qbeQuery.getSelectFields(true);
		for(int i=0; i<selectFieldsColumn.size(); i++){
			dataSetTableDescriptor.addField(selectFieldsNames.get(i).getAlias(), selectFieldsColumn.get(i)[1], Object.class);
		}
		return dataSetTableDescriptor;
	}
	
	/**
	 * Build a datasource.. We need this object
	 * to build a JDBCDataSet
	 * @return
	 */
	private IDataSource getDataSource(){
		IDataSource ds = new DataSource();
		ConnectionDescriptor connectionDescriptor = ((AbstractDataSource)statement.getDataSource()).getConnection();
		ds.setHibDialectClass(connectionDescriptor.getDialect());
		ds.setDriver(connectionDescriptor.getDriverClass());
		ds.setJndi(connectionDescriptor.getJndiName());
		ds.setLabel(connectionDescriptor.getName());
		ds.setPwd(connectionDescriptor.getPassword());
		ds.setUrlConnection(connectionDescriptor.getUrl());
		ds.setUser(connectionDescriptor.getUsername());
		return ds;
	}
	
	public IMetaData getMetadata() {
		return getDataStoreMeta(statement.getQuery());
	}
	
	public String getSignature() {
		return getSQLQuery();
	}
	

	public Map getUserProfileAttributes() {
		return userProfileAttributes;
	}

	public void setUserProfileAttributes(Map attributes) {
		this.userProfileAttributes = attributes;
		
	}
	
}
