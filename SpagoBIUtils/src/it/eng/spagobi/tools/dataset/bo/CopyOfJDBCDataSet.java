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

import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.QuerableBehaviour;
import it.eng.spagobi.tools.dataset.common.dataproxy.IDataProxy;
import it.eng.spagobi.tools.dataset.common.dataproxy.JDBCDataProxy;
import it.eng.spagobi.tools.dataset.common.datareader.JDBCDataReader;
import it.eng.spagobi.tools.datasource.bo.DataSourceFactory;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * @authors
 * Angelo Bernabei
 *         angelo.bernabei@eng.it
 * Giulio Gavardi
 *     giulio.gavardi@eng.it
 *  Andrea Gioia
 *         andrea.gioia@eng.it
 *         
 *  @deprecated
 */
public class CopyOfJDBCDataSet extends ConfigurableDataSet {
	
	public static String DS_TYPE = "SbiQueryDataSet";
	
	private static transient Logger logger = Logger.getLogger(CopyOfJDBCDataSet.class);
    
	
	/**
     * Instantiates a new empty JDBC data set.
     */
    public CopyOfJDBCDataSet() {
		super();
		setDataProxy( new JDBCDataProxy() );
		setDataReader( new JDBCDataReader() );
		addBehaviour( new QuerableBehaviour(this) );
	}
    
    public CopyOfJDBCDataSet(SpagoBiDataSet dataSetConfig) {
		super(dataSetConfig);
		
		setDataProxy( new JDBCDataProxy() );
		setDataReader( new JDBCDataReader() );
		
		try{
			setDataSource( DataSourceFactory.getDataSource( dataSetConfig.getDataSource() ) );
		}
		catch (Exception e) {
			throw new RuntimeException("missing right exstension", e);
		}
	
		setQuery( dataSetConfig.getQuery() );
		
		addBehaviour( new QuerableBehaviour(this) );
	}
    
    
    
    /**
     * Redefined for set schema name
     * 
     */
	public void setUserProfileAttributes(Map userProfile)  {
		this.userProfileParameters = userProfile;
		if (getDataSource().checkIsMultiSchema()){
			String schema=null;
			try {
				schema = (String)userProfile.get(getDataSource().getSchemaAttribute());
				((JDBCDataProxy)dataProxy).setSchema(schema);
				logger.debug("Set UP Schema="+schema);
			} catch (Throwable t) {
				throw new SpagoBIRuntimeException("An error occurred while reading schema name from user profile", t);	
			}	
		}
	}
	

	
	public SpagoBiDataSet toSpagoBiDataSet() {
		SpagoBiDataSet sbd;
		JDBCDataProxy dataProxy;
		
		sbd = super.toSpagoBiDataSet();
		
		sbd.setType( DS_TYPE );
			
		dataProxy = (JDBCDataProxy)this.getDataProxy();
		sbd.setDataSource(dataProxy.getDataSource().toSpagoBiDataSource());
		if(query!=null){
		sbd.setQuery(query.toString());
		}
		return sbd;
	}

	
	public JDBCDataProxy getDataProxy() {
		IDataProxy dataProxy;
		
		dataProxy = super.getDataProxy();
		
		if(dataProxy == null) {
			setDataProxy( new JDBCDataProxy() );
			dataProxy = getDataProxy();
		}
		
		if(!(dataProxy instanceof  JDBCDataProxy)) throw new RuntimeException("DataProxy cannot be of type [" + 
				dataProxy.getClass().getName() + "] in JDBCDataSet");
		
		return (JDBCDataProxy)dataProxy;
	}
	
	public void setDataSource(IDataSource dataSource) {
		getDataProxy().setDataSource(dataSource);
	}
	
	public IDataSource getDataSource() {
		return getDataProxy().getDataSource();
	}
	    
}
