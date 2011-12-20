/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.model.properties.initializer;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.hibernate.IHibernateDataSource;
import it.eng.qbe.datasource.jpa.IJpaDataSource;
import it.eng.qbe.datasource.jpa.JPADataSource;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class ModelStructurePropertiesInitializerFactory { 
	
	public static IModelStructurePropertiesInitializer getDataMartStructurePropertiesInitializer(IDataSource dataSource) {
		IModelStructurePropertiesInitializer initializer;
		
		initializer = null;
		
		if(dataSource instanceof IHibernateDataSource) {
			initializer = new SimpleModelStructurePropertiesInitializer((IHibernateDataSource)dataSource);
		} else if (dataSource instanceof JPADataSource) {
			initializer = new SimpleModelStructurePropertiesInitializer((IJpaDataSource)dataSource);
		} else {
			throw new RuntimeException("Impossible to load datamart structure from a datasource of type [" + dataSource.getClass().getName() + "]");
		}
		
		
		return initializer;		
	}
}
