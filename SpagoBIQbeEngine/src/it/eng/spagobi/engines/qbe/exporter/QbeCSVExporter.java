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
package it.eng.spagobi.engines.qbe.exporter;

import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class QbeCSVExporter {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeCSVExporter.class);
    
    /**
     * The values separator string
     */
    public static final String VALUES_SEPARATOR = "\t";
    
	public QbeCSVExporter() {
		super();
	}

	public void export(File outputFile, Connection connection, String sqlStatement) {
		try {
			FileWriter writer = new FileWriter(outputFile);
			Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = stmt.executeQuery( sqlStatement );
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				writeRecordInfoCsvFile(writer, resultSet, columnCount);
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("Error exporting in CSV", e);
			throw new SpagoBIEngineRuntimeException("Error exporting in CSV", e);
		}
	}
	
	private void writeRecordInfoCsvFile(FileWriter writer, ResultSet resultSet,
			int columnCount) throws SQLException, IOException {
		for (int i = 1; i <= columnCount; i++) {
			Object temp = resultSet.getObject(i);
			if (temp != null) {
				writer.append(temp.toString());
			}
			writer.append( VALUES_SEPARATOR );
		}
		writer.append("\n");
	}
	
}
