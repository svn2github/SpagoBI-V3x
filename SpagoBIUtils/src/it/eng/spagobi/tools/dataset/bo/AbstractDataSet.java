/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.bo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.IDataSetBehaviour;
import it.eng.spagobi.tools.dataset.common.transformer.IDataStoreTransformer;
import it.eng.spagobi.tools.dataset.common.transformer.PivotDataSetTransformer;


import sun.net.www.MeteredStream;
/**
 * @author Angelo Bernabei angelo.bernabei@eng.it
 */
public abstract class AbstractDataSet implements IDataSet {

    private int id;
    private String name;
    private String description;
    private String label;
    private String parameters;
    private Map paramsMap;
    private Map behaviours;
    
    private Integer transformerId;
    private String pivotColumnName;
    private String pivotRowName;
    private String pivotColumnValue;
    private boolean numRows;
    private String dsMetadata;
    	
    IDataStoreTransformer dataSetTransformer;
    
    private static transient Logger logger = Logger.getLogger(AbstractDataSet.class);

    public AbstractDataSet() {
    	super();
    	behaviours = new HashMap();
    }
    
    public AbstractDataSet(SpagoBiDataSet dataSetConfig) {
    	super();
    	setId(dataSetConfig.getDsId());
    	setName(dataSetConfig.getName());
    	setLabel(dataSetConfig.getLabel());
    	setDescription(dataSetConfig.getDescription());
		setLabel(dataSetConfig.getLabel());
		setParameters(dataSetConfig.getParameters());
		
		setTransformerId(dataSetConfig.getTransformerId());
		setPivotColumnName(dataSetConfig.getPivotColumnName());
		setPivotRowName(dataSetConfig.getPivotRowName());
		setPivotColumnValue(dataSetConfig.getPivotColumnValue());
		setNumRows(dataSetConfig.isNumRows());
		setDsMetadata(dataSetConfig.getDsMetadata());
		
		if(this.getPivotColumnName() != null 
				&& this.getPivotColumnValue() != null
				&& this.getPivotRowName() != null){
			setDataStoreTransformer(
					new PivotDataSetTransformer(getPivotColumnName(), getPivotColumnValue(), getPivotRowName(), isNumRows()));
		}
		
		behaviours = new HashMap();
    }
    
    public SpagoBiDataSet toSpagoBiDataSet() {
		SpagoBiDataSet sbd = new SpagoBiDataSet();
		
		sbd.setDsId(getId());
		sbd.setLabel(getLabel());
		sbd.setName(getName());
		sbd.setParameters(getParameters());
		sbd.setDescription(getDescription());
		
		sbd.setTransformerId(getTransformerId());
		sbd.setPivotColumnName(getPivotColumnName());
		sbd.setPivotRowName(getPivotRowName());
		sbd.setPivotColumnValue(getPivotColumnValue());
		sbd.setNumRows(isNumRows());
		return sbd;
	}


    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }
    
    public String getLabel() {
    	return label;
    }

    public void setLabel(String label) {
    	this.label = label;
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
    
    public String getParameters() {
    	return parameters;
    }

    
    public void setParameters(String parameters) {
    	this.parameters = parameters;
    }

	public Integer getTransformerId() {
		return transformerId;
	}

	public void setTransformerId(Integer transformerId) {
		this.transformerId = transformerId;
	}

	public String getPivotColumnName() {
		return pivotColumnName;
	}

	public void setPivotColumnName(String pivotColumnName) {
		this.pivotColumnName = pivotColumnName;
	}

	public String getPivotRowName() {
		return pivotRowName;
	}

	public void setPivotRowName(String pivotRowName) {
		this.pivotRowName = pivotRowName;
	}

	public String getPivotColumnValue() {
		return pivotColumnValue;
	}

	public void setPivotColumnValue(String pivotColumnValue) {
		this.pivotColumnValue = pivotColumnValue;
	}

	public Map getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map paramsMap) {
		this.paramsMap = paramsMap;
	}  
	
	public boolean hasBehaviour(String behaviourId) {
		return behaviours.containsKey(behaviourId);
	}
	
	public Object getBehaviour(String behaviourId) {
		return behaviours.get(behaviourId);
	}
	
	public void addBehaviour(IDataSetBehaviour behaviour) {
		behaviours.put(behaviour.getId(), behaviour);
	}

	public boolean hasDataStoreTransformer() {
		return getDataStoreTransformer() != null;
	}
	
	public void removeDataStoreTransformer() {
		setDataStoreTransformer(null);
	}

	public void setDataStoreTransformer(IDataStoreTransformer dataSetTransformer) {
		this.dataSetTransformer = dataSetTransformer;
	}
	
	public IDataStoreTransformer getDataStoreTransformer() {
		return this.dataSetTransformer;
	}

	
	public boolean isNumRows() {
		return numRows;
	}

	
	public void setNumRows(boolean numRows) {
		this.numRows = numRows;
	}
	
	public void loadData() throws EMFUserError, EMFInternalError {
		loadData(0, -1, -1);
	}
	
	public void loadData(int offset, int fetchSize, int maxResults) throws EMFUserError, EMFInternalError {
		throw new RuntimeException("Unsupported method");
	}
	
	public String getDsMetadata() {
		return dsMetadata;
	}

	public void setDsMetadata(String dsMetadata) {
		this.dsMetadata = dsMetadata;
	}
}
