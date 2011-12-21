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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datareader.IDataReader;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.scripting.ScriptManager;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ScriptDataProxy extends AbstractDataProxy {

	String script;
	String languageScript;

	private static transient Logger logger = Logger.getLogger(ScriptDataProxy.class);


	public ScriptDataProxy() {

	}

	public ScriptDataProxy(String _script, String _languageScript) {
		setScript( _script );
		setLanguageScript(_languageScript);
	}


	public IDataStore load(String statement, IDataReader dataReader) throws EMFUserError {
		if(statement != null) {
			setScript(statement);
		}
		return load(dataReader);
	}


	public IDataStore load(IDataReader dataReader) {
		logger.debug("IN");
		String data = null;
		IDataStore dataStore = null;
		ScriptManager sm = new ScriptManager();
		if(predefinedGroovyScriptFileName!=null && !predefinedGroovyScriptFileName.equals("")){
			sm.setPredefinedGroovyScriptFileName(predefinedGroovyScriptFileName);
		}
		if(predefinedJsScriptFileName!=null && !predefinedJsScriptFileName.equals("")){
			sm.setPredefinedJsScriptFileName(predefinedJsScriptFileName);
		}
		
		try {
			if(statement != null){
				logger.debug("Statement "+statement);
				data = sm.runScript(statement, languageScript);
			}
			else{
				logger.debug("Use script (no parameters) "+script);
				data = sm.runScript(script, languageScript);
			}
			// check if the result must be converted into the right xml sintax
			boolean toconvert = checkSintax(data);
			if(toconvert) { 
				data = convertResult(data);
			}
			dataStore = dataReader.read(data);
		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("Impossible to load store", t);
		}
		logger.debug("OUT");

		return dataStore;
	}

	private boolean checkSintax(String result) {

		logger.debug("IN");
		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";

		boolean toconvert = false;
	
			SourceBean source = null;
			try {
				source = SourceBean.fromXMLString(result);
			} catch (SourceBeanException e) {
				logger.error("SourceBean Exception");
				toconvert = true;
			}catch (NullPointerException n) {
				logger.error("NullPointerException");
				toconvert = false;
			}
			if(source!=null){
				if(!source.getName().equalsIgnoreCase("ROWS")) {
					toconvert = true;
				} else {
					List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
					if( (rowsList==null) || (rowsList.size()==0) ) {
						toconvert = true;
					} else {
						// TODO this part can be moved to the import transformer
						// RESOLVES RETROCOMPATIBILITY PROBLEMS
						// finds the name of the first attribute of the rows if exists 
						String defaultName = "";
						SourceBean rowSB = (SourceBean) rowsList.get(0);
						List attributes = rowSB.getContainedAttributes();
						if (attributes != null && attributes.size() > 0) {
							SourceBeanAttribute attribute = (SourceBeanAttribute) attributes.get(0);
							defaultName = attribute.getKey();
						}
						// if a value column is specified, it is considered
						SourceBean valueColumnSB = (SourceBean) source.getAttribute("VALUE-COLUMN");
						if (valueColumnSB != null) {
							String valueColumn = valueColumnSB.getCharacters();
							if (valueColumn != null) {
								valueColumnName = valueColumn;
							}
						} else {
							valueColumnName = defaultName;
						}
						SourceBean visibleColumnsSB = (SourceBean) source.getAttribute("VISIBLE-COLUMNS");
						if (visibleColumnsSB != null) {
							String allcolumns = visibleColumnsSB.getCharacters();
							if (allcolumns != null) {
								String[] columns = allcolumns.split(",");
								visibleColumnNames = Arrays.asList(columns);
							}
						} else {
							String[] columns = new String[] {defaultName};
							visibleColumnNames = Arrays.asList(columns);
						}
						SourceBean descriptionColumnSB = (SourceBean) source.getAttribute("DESCRIPTION-COLUMN");
						if (descriptionColumnSB != null) {
							String descriptionColumn = descriptionColumnSB.getCharacters();
							if (descriptionColumn != null) {
								descriptionColumnName = descriptionColumn;
							}
						} else {
							descriptionColumnName = defaultName;
						}
					}
				}
			}else{
				logger.error("the result of the dataset is not formatted with the right structure so it will be wrapped inside an xml envelope");
			}
		logger.debug("OUT");
		return toconvert;
	}

	private String convertResult(String result) {

		logger.debug("IN");

		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result +"\"/>");
		sb.append("</ROWS>");
		descriptionColumnName = "VALUE";
		valueColumnName = "VALUE";
		String [] visibleColumnNamesArray = new String [] {"VALUE"};
		visibleColumnNames = Arrays.asList(visibleColumnNamesArray);

		logger.debug("OUT");
		return sb.toString();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getLanguageScript() {
		return languageScript;
	}

	public void setLanguageScript(String languageScript) {
		this.languageScript = languageScript;
	}



}
