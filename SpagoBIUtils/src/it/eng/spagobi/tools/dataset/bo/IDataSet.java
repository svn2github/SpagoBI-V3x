/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.IDataSetBehaviour;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreFilter;
import it.eng.spagobi.tools.dataset.common.metadata.IMetaData;
import it.eng.spagobi.tools.dataset.common.transformer.IDataStoreTransformer;
import it.eng.spagobi.tools.dataset.persist.IDataSetTableDescriptor;

import java.sql.Connection;
import java.util.Map;

public interface IDataSet {
	
	String getDsMetadata();
	void setDsMetadata(String dsMetadata);
	IMetaData getMetadata();
	void setMetadata(IMetaData metadata);
	
	// general properties ....
	int getId();
	void setId(int id);
	
	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);
	
	String getLabel();
	void setLabel(String label);
	
	Integer getCategoryId();
	void setCategoryId(Integer categoryId);
	
	String getCategoryCd();
	void setCategoryCd(String categoryCd);
	
	String getDsType();
	void setDsType(String dsType);

	Map getProperties();
	void setProperties(Map map);
	
	
	// parametrization ....
	// --------------------------------------------------------------------------------------------------
	// INVESTIGATE: why this 2 similar methods ??? FIND OUT & REFACTOR !
	String getParameters();
	void setParameters(String parameters);

	Map getParamsMap();
	void setParamsMap(Map params);
	// --------------------------------------------------------------------------------------------------
	
	// profilation ...
	public Map getUserProfileAttributes();
	public void setUserProfileAttributes(Map<String, Object> attributes);
	
	// execution ...
	// --------------------------------------------------------------------------------------------------
	void loadData();
	void loadData(int offset, int fetchSize, int maxResults);
	// --------------------------------------------------------------------------------------------------
 	
	IDataStore getDataStore();	
	
	
	// extension points ...
	boolean hasBehaviour(String behaviourId);
	Object getBehaviour(String behaviourId);
	void addBehaviour(IDataSetBehaviour behaviour);	
	
	// =================================================================================================
	// TO BE DEPRECATED ( do not cross this line ;-) )
	// =================================================================================================
	
	// --------------------------------------------------------------------------------------------------
	// TODO these methods do NOT belong to the dataset interface. remove them and refactor the code.
	
	String getResourcePath();
	void setResourcePath(String resPath);
	Object getQuery();
	void setQuery(Object query);
	String getQueryScript();
	void setQueryScript(String script);
	
	
	Integer getTransformerId();
	void setTransformerId(Integer transformerId);
	
	String getTransformerCd() ;
    void setTransformerCd(String transfomerCd);

	String getPivotColumnName();
	void setPivotColumnName(String pivotColumnName);

	String getPivotRowName();
	void setPivotRowName(String pivotRowName);
	
	boolean isNumRows();
	void setNumRows(boolean numRows);

	String getPivotColumnValue();
	void setPivotColumnValue(String pivotColumnValue);
	
	boolean hasDataStoreTransformer() ;
	void removeDataStoreTransformer() ;
	
	void setAbortOnOverflow(boolean abortOnOverflow);
	void addBinding(String bindingName, Object bindingValue);
	
	void setDataStoreTransformer(IDataStoreTransformer transformer);
	IDataStoreTransformer getDataStoreTransformer();
	
	
	// TODO these methods do NOT belong to the dataset interface. remove them and refactor the code.
	// --------------------------------------------------------------------------------------------------
	
	// --------------------------------------------------------------------------------------------------
	// TODO these methods must be moved into a proper factory that convert SpagoBI BO into data bean passed
	// to the DAO. For the conversion from data bean and SpagoBI BO such a factory alredy exist
	// NOTE: SpagoBiDataSet: change when possible the name SpagoBiDataSet following a convention common to 
	// all data bean
	SpagoBiDataSet toSpagoBiDataSet();
	// --------------------------------------------------------------------------------------------------
	
	
	
	IDataStore test();
	IDataStore test(int offset, int fetchSize, int maxResults);
	
	String getSignature();
	
	IDataSetTableDescriptor persist(String tableName, Connection connection);
	
	public IDataStore getDomainValues(String fieldName, 
            Integer start, Integer limit, IDataStoreFilter filter);
	
	public IDataStore decode(IDataStore datastore);
	
	boolean isCalculateResultNumberOnLoadEnabled();
	
	void setCalculateResultNumberOnLoad(boolean enabled); 
	
}