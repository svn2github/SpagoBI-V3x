/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
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
package it.eng.qbe.datasource;


import java.util.List;
import java.util.Map;

import it.eng.qbe.datasource.hibernate.DBConnection;

// TODO: Auto-generated Javadoc
/**
 * The Interface DataSourceManager.
 * 
 * @author Andrea Gioia
 */
public interface DataSourceManager {
	
	/**
	 * Gets the data source.
	 * 
	 * @param dataMartNames the data mart names
	 * @param connection the connection
	 * 
	 * @return the data source
	 */
	IDataSource getDataSource(List dataMartNames, DBConnection connection);
	
	/**
	 * Gets the data source.
	 * 
	 * @param dataMartNames the data mart names
	 * @param dblinkMap the dblink map
	 * @param connection the connection
	 * 
	 * @return the data source
	 */
	IDataSource getDataSource(List dataMartNames, Map dblinkMap, DBConnection connection);
}
