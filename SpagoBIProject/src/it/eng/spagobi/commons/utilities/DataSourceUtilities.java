/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.utilities;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.datasource.service.DataSourceSupplier;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class DataSourceUtilities {
	private static transient Logger logger = Logger.getLogger(DataSourceUtilities.class);
	
	/**
	 * This method, based on the data sources table, gets a
	 * database connection and return it.
	 * 
	 * @param dsLabel the ds label
	 * 
	 * @return the database connection
	 * 
	 * N.B. You MUST use this method only when RequestContainer is OK.
	 */
	public Connection getConnection(RequestContainer requestContainer,String dsLabel) {
		Connection connection =  null;
		//calls implementation for gets data source object
		
		DataSourceSupplier supplierDS = new DataSourceSupplier();		
		SpagoBiDataSource ds = supplierDS.getDataSourceByLabel(dsLabel);
		logger.debug("Schema Attributes:"+ ds.getSchemaAttribute());
		String schema=UserUtilities.getSchema(ds.getSchemaAttribute(),requestContainer);
		logger.debug("Schema:"+ schema);
		
		try {
			connection = ds.readConnection(schema);
		} catch (NamingException e) {
			logger.error("JNDI error", e);
		} catch (SQLException e) {
			logger.error("Cannot retrive connection", e);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		}
		
		return connection;
	}
	
	/**
	 * use this method in service implementation. If RequestContainer isn't correct.
	 * @param profile
	 * @param dsLabel
	 * @return
	 */
	public Connection getConnection(IEngUserProfile profile,String dsLabel) {
		Connection connection =  null;
		//calls implementation for gets data source object
		
		
		DataSourceSupplier supplierDS = new DataSourceSupplier();		
		SpagoBiDataSource ds = supplierDS.getDataSourceByLabel(dsLabel);
		logger.debug("Schema Attribute:"+ ds.getSchemaAttribute());
		String schema=null;
		if (profile!=null){
			schema=UserUtilities.getSchema(ds.getSchemaAttribute(),profile);
			logger.debug("Schema:"+ schema);
		}
		try {
			connection = ds.readConnection(schema);
		} catch (NamingException e) {
			logger.error("JNDI error", e);
		} catch (SQLException e) {
			logger.error("Cannot retrive connection", e);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		}
		
		return connection;
	}	
	/**
	 * Creates a ago DataConnection object starting from a sql connection.
	 * 
	 * @param con Connection to the export database
	 * 
	 * @return The Spago DataConnection Object
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
	public DataConnection getDataConnection(Connection con) throws EMFInternalError {
		DataConnection dataCon = null;
		try {
			Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
			SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
			dataCon = new DataConnection(con, "2.1", sqlMapper);
		} catch(Exception e) {
			logger.error("Error while getting Data Source " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build spago DataConnection object");
		}
		return dataCon;
	}
	
	

}
