package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.tools.dataset.common.behaviour.FilteringBehaviour;
import it.eng.spagobi.tools.dataset.common.behaviour.SelectableFieldsBehaviour;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreFilter;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;
import it.eng.spagobi.tools.dataset.common.metadata.MetaData;
import it.eng.spagobi.tools.dataset.functionalities.temporarytable.DatasetTempTable;
import it.eng.spagobi.tools.dataset.persist.IDataSetTableDescriptor;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class AbstractCustomDataSet extends AbstractDataSet implements IDataSet {

	private IMetaData metadata;	

	private static transient Logger logger = Logger.getLogger(AbstractCustomDataSet.class);

	public AbstractCustomDataSet() {
		super();
		addBehaviour( new FilteringBehaviour(this) );
		addBehaviour( new SelectableFieldsBehaviour(this) );
	}

	public IMetaData getMetadata(){
		return this.metadata;
	}

	public void setMetadata(IMetaData metadata){
		this.metadata = metadata;
	}

	public IDataSetTableDescriptor createTemporaryTable(String tableName
			, MetaData metadata
			, Connection connection){
		logger.debug("IN");
		IDataSetTableDescriptor descriptor = DatasetTempTable.createTemporaryTable(connection, metadata, tableName);
		logger.debug("Temporary table created successfully");
		logger.debug("OUT");
		return descriptor;
	}


	// *********** Abstract methods **************

	
	 // no implement
	public abstract IDataStore test();
	public abstract String getSignature();
	public abstract IDataStore getDomainValues(String attributeName, Integer start, Integer limit, IDataStoreFilter filter);
	public abstract Map<String, List<String>> getDomainDescriptions(Map<String, List<String>> codes); 
	public abstract IDataSetTableDescriptor persist(String tableName, Connection connection);

}
