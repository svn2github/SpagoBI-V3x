/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.qbe.datasource.jpa.query;

import it.eng.qbe.datasource.AbstractDataSourceTestCase;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.IJpaDataSource;
import it.eng.qbe.datasource.jpa.JPADriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QueryDataSourceTestCase extends AbstractDataSourceTestCase {
	
	private static final String QBE_FILE = "test-resources/jpa/query/datamart.jar";
	
	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;
		
		modelName = "foodmart_1307532444533";  
		
		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration, false);
	}
	
	public void testQbeWithKeys() {
		doTests() ;
	}
	
	public void doTests() {
		super.doTests();
		doTestCustomQuery();
	}

	private void doTestCustomQuery() {
		
		javax.persistence.Query jpqlQuery;
		String statementStr = "SELECT t_0.storeId.storeCountry FROM  SalesFact1998 t_0 WHERE  t_0.timeId=t_0.timeId ";
		EntityManager entityManager = ((IJpaDataSource)dataSource).getEntityManager();
		
		try {
			jpqlQuery = entityManager.createQuery( statementStr );
		} catch (Throwable t) {
			System.err.println("statementStr: " + statementStr);
			System.err.println(t.getMessage());
			throw new RuntimeException("Impossible to compile query statement [" + statementStr + "]", t);
		}
			
		
		List result = null;

		try {
			result = jpqlQuery.getResultList();
		} catch (Throwable t) {
			System.err.println("statementStr: " + statementStr);
			System.err.println("statementStr: " + t.getMessage());
			throw new RuntimeException("Impossible to execute statement [" + statementStr + "]", t);
		}
		
	}
}
