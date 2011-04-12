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
package it.eng.qbe.datasource.jpa.views;

import it.eng.qbe.AbstractQbeTestCase;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.hibernate.HibernateDataSource;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.qbe.datasource.jpa.impl.StandardJPAELinkImplDataSourceTestCase;
import it.eng.qbe.datasource.jpa.impl.StandardJPAHibernateImplDataSourceTestCase;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.IModelViewEntityDescriptor;
import it.eng.qbe.model.structure.ModelViewEntity;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SimpleKeyJpaDataSourceTestCase extends AbstractViewJpaDataSourceTestCase {
	
	private static final String QBE_FILE = "test-resources/jpa/views/simpleKey/dist/datamart.jar";
	
	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;
		
		modelName = "My Model";  
		
		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
	}
	
	public void testQbeWithView() {
		doTests() ;
	}
	
	public void doTests() {
		super.doTests();
		// add custom tests here
	}
	
	
}
