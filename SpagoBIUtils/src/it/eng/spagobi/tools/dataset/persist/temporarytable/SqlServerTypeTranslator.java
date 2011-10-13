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
package it.eng.spagobi.tools.dataset.persist.temporarytable;

import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class SqlServerTypeTranslator implements INativeDBTypeable{
	
	private static Logger logger = Logger.getLogger("SqlServerTypeTranslator");
	private static final Integer MAX_CHAR_SIZE = 8000;
	
	private static Map<String, String> sqlServerTypeMapping;
	static{
		sqlServerTypeMapping = new HashMap<String, String>();
		sqlServerTypeMapping.put("java.lang.Integer", "int");//no param
		sqlServerTypeMapping.put("java.lang.String", "char");//  oppure varchar  [ ( n | max ) ]  Dati di tipo carattere a lunghezza variabile non Unicode. n pu� essere un valore compreso tra 1 e 8.000. max indica che le dimensioni massime dello spazio di archiviazione sono 2^31-1 byte.
		//sqlServerTypeMapping("java.lang.String4001", "CLOB");
		sqlServerTypeMapping.put("java.lang.Boolean", "bit");//1,0 o null
		sqlServerTypeMapping.put("java.lang.Float", "float");//n pu� non essere specificato Dove n � il numero di bit utilizzato per archiviare la mantissa del numero float nella notazione scientifica
		sqlServerTypeMapping.put("java.lang.Double", "real");//no param
		sqlServerTypeMapping.put("java.util.Date", "date");//no param
		sqlServerTypeMapping.put("java.sql.Date", "date");//no param
		sqlServerTypeMapping.put("java.sql.Timestamp", "datetime");//oppure datetime2 AAAA-MM-GG hh:mm:ss[se datetime2 .secondi frazionari] Definisce una data costituita dalla combinazione con un'ora del giorno espressa nel formato 24 ore. datetime2 pu� essere considerato un'estensione del tipo datetime esistente con un pi� ampio intervallo di date, una maggiore precisione frazionaria predefinita e una precisione specificata dall'utente facoltativa.
		sqlServerTypeMapping.put("java.math.BigDecimal", "decimal");// facoltativi (p,s) p (precisione) Numero massimo totale di cifre decimali che � possibile archiviare, sia a destra che a sinistra del separatore decimale.s (scala) Numero massimo di cifre decimali che � possibile archiviare a destra del separatore decimale. La scala deve essere un valore compreso tra 0 e p.
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


		typeSQL = sqlServerTypeMapping.get(typeJavaName);


		// write Type
		queryType +=" "+typeSQL+""; 

		if(typeJavaName.equalsIgnoreCase(String.class.getName())){
			if(size>MAX_CHAR_SIZE){
				logger.error("For Sqlserver the max size of a char column must be < "+MAX_CHAR_SIZE+". The size you've specified is "+size);
				throw new SpagoBIRuntimeException("For Sqlserver the max size of a char column must be < "+MAX_CHAR_SIZE+". The size you've specified is "+size);
			}
			if( size != null && size!= 0){
				queryType +="("+size+")";
			}
		}else if(typeJavaName.equalsIgnoreCase(BigDecimal.class.getName()) && (precision != null && scale != null)){
			queryType+="("+precision+","+scale+")";
		}else if(typeJavaName.equalsIgnoreCase(Float.class.getName()) && (precision != null)){//mantissa
			queryType+="("+precision+")";
		}
		logger.debug("The translated sql server type is "+queryType);
		queryType+=" ";
		return queryType;
	}

}
