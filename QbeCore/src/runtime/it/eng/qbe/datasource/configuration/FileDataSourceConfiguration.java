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


import it.eng.qbe.datasource.configuration.dao.ICalculatedFieldsDAO;
import it.eng.qbe.datasource.configuration.dao.IModelI18NPropertiesDAO;
import it.eng.qbe.datasource.configuration.dao.IModelPropertiesDAO;
import it.eng.qbe.datasource.configuration.dao.IViewsDAO;
import it.eng.qbe.datasource.configuration.dao.fileimpl.CalculatedFieldsDAOFileImpl;
import it.eng.qbe.datasource.configuration.dao.fileimpl.ModelI18NPropertiesDAOFileImpl;
import it.eng.qbe.datasource.configuration.dao.fileimpl.ModelPropertiesDAOFileImpl;
import it.eng.qbe.datasource.configuration.dao.fileimpl.ViewsDAOFileImpl;
import it.eng.qbe.model.properties.SimpleModelProperties;
import it.eng.qbe.model.structure.ModelCalculatedField;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class FileDataSourceConfiguration implements IDataSourceConfiguration {
	
	String modelName;
	Map<String,Object> dataSourceProperties;
	
	File file;

	IModelPropertiesDAO modelPropertiesDAO;
	ICalculatedFieldsDAO calculatedFieldsDAO;
	IModelI18NPropertiesDAO modelLabelsDAOFileImpl;
	IViewsDAO viewsDAO;
	
	public FileDataSourceConfiguration(String modelName, File file) {
		this.modelName = modelName;
		this.file = file;
		this.dataSourceProperties = new HashMap<String,Object>();
		this.modelPropertiesDAO = new ModelPropertiesDAOFileImpl(file);
		this.modelLabelsDAOFileImpl = new ModelI18NPropertiesDAOFileImpl(file);
		this.calculatedFieldsDAO = new CalculatedFieldsDAOFileImpl(file);	
		this.viewsDAO = new ViewsDAOFileImpl(file);	
	}
	
	public File getFile() {
		return file;
	}

	public String getModelName() {
		return modelName;
	}
	
	public SimpleModelProperties loadModelProperties() {
		return modelPropertiesDAO.loadModelProperties();
	}
	
	public SimpleModelProperties loadModelI18NProperties() {
		return loadModelI18NProperties(null);
	}
	public SimpleModelProperties loadModelI18NProperties(Locale locale) {
		SimpleModelProperties properties = modelLabelsDAOFileImpl.loadProperties(locale);
		return properties;
	}

	public Map loadCalculatedFields() {
		return calculatedFieldsDAO.loadCalculatedFields();
	}

	public void saveCalculatedFields(Map<String, List<ModelCalculatedField>> calculatedFields) {
		calculatedFieldsDAO.saveCalculatedFields( calculatedFields );
	}

	public Map<String, Object> loadDataSourceProperties() {
		return dataSourceProperties;
	}

	public List loadViews() {
		List views = new ArrayList();
		Object o =  viewsDAO.loadModelViews();
		views.add(o);
		return views;
	}
}
