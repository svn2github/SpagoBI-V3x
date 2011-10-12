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
package it.eng.spagobi.tools.dataset.functionalities.temporarytable;

import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class DB2TypeTranslator implements INativeDBTypeable{

	private static Logger logger = Logger.getLogger("DB2TypeTranslator");
	private static final int MAX_CHAR_SIZE = 254;
	private static Map<String, String> db2TypeMapping;
	static{
		db2TypeMapping = new HashMap<String, String>();
		db2TypeMapping.put("java.lang.Integer", "INTEGER");//no param
		db2TypeMapping.put("java.lang.String", "CHAR");// (n)n<32672  CHAR(n) n<=254
		//db2TypeMapping.put("java.lang.String4001", "CLOB");
		db2TypeMapping.put("java.lang.Boolean", "SMALLINT");//no param
		db2TypeMapping.put("java.lang.Float", "REAL");//no param
		db2TypeMapping.put("java.lang.Double", "DOUBLE");//no param
		db2TypeMapping.put("java.util.Date", "DATE");//no param
		db2TypeMapping.put("java.sql.Date", "DATE");//no param
		db2TypeMapping.put("java.sql.Timestamp", "TIMESTAMP");//no param
		db2TypeMapping.put("java.math.BigDecimal", "DECIMAL");//DECIMAL(p,s)
	}
	
	@SuppressWarnings("rawtypes")
	public String getNativeTypeString(String typeJavaName, Map properties) {
		logger.debug("Translating java type "+typeJavaName+" with properties "+properties);
		// convert java type in SQL type
		String queryType ="";
		String typeSQL ="";

		// proeprties
		Integer size = null;
		Integer precision = null;
		Integer scale = null;

		if(properties.get(SIZE) != null) 
			size = Integer.valueOf(properties.get(SIZE).toString());
		if(properties.get(PRECISION) != null) 
			precision = Integer.valueOf(properties.get(PRECISION).toString());
		if(properties.get(SCALE) != null) 
			scale = Integer.valueOf(properties.get(SCALE).toString());


		typeSQL = db2TypeMapping.get(typeJavaName);


		// write Type
		queryType +=" "+typeSQL+""; 

		if(typeJavaName.equalsIgnoreCase(String.class.getName())){
			if(size>MAX_CHAR_SIZE){
				logger.error("For DB2 the max size of a char column must be < "+MAX_CHAR_SIZE+". The size you've specified is "+size);
				throw new SpagoBIRuntimeException("For DB2 the max size of a char column must be < 254. The size you've specified is "+size);
			}
			if( size != null && size!= 0){
				queryType +="("+size+")";
			}
		}else if(typeJavaName.equalsIgnoreCase(BigDecimal.class.getName()) && (precision != null && scale != null)){
			queryType+="("+precision+","+scale+")";
		}

		queryType+=" ";
		
		logger.debug("The translated DB2 type is "+queryType);
		return queryType;
	}

}
