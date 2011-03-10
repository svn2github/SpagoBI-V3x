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
package it.eng.qbe.datasource.configuration;


import it.eng.qbe.model.i18n.ModelLabels;
import it.eng.qbe.model.structure.DataMartCalculatedField;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class FileDataSourceConfiguration implements IDataSourceConfiguration {
	
	String modelName;
	File file;
	Map<String,Object> dataSourceProperties;

	IModelPropertiesDAO modelPropertiesDAO;
	ICalculatedFieldsDAO calculatedFieldsDAO;
	IModelLabelsDAO modelLabelsDAOFileImpl;
	
	public FileDataSourceConfiguration(String modelName, File file) {
		this.modelName = modelName;
		this.file = file;
		this.dataSourceProperties = new HashMap<String,Object>();
		this.modelPropertiesDAO = new ModelPropertiesDAOFileImpl(file);
		this.modelLabelsDAOFileImpl = new ModelLabelsDAOFileImpl(file);
		this.calculatedFieldsDAO = new CalculatedFieldsDAOFileImpl(file);		
	}
	
	public File getFile() {
		return file;
	}

	public String getModelName() {
		return modelName;
	}
	
	public Properties getModelProperties() {
		return modelPropertiesDAO.loadModelProperties();
	}
	
	public ModelLabels getModelLabels() {
		return modelLabelsDAOFileImpl.loadDatamartLabels();
	}

	public ModelLabels getModelLabels(Locale locale) {
		return modelLabelsDAOFileImpl.loadDatamartLabels(locale);
	}

	public Map getCalculatedFields() {
		return calculatedFieldsDAO.loadCalculatedFields();
	}

	public void setCalculatedFields(Map<String, List<DataMartCalculatedField>> calculatedFields) {
		calculatedFieldsDAO.saveCalculatedFields(calculatedFields);
	}

	public Map<String, Object> getDataSourceProperties() {
		return dataSourceProperties;
	}
}
