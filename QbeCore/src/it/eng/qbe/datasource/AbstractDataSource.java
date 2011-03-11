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

import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.qbe.model.i18n.ModelI18NProperties;
import it.eng.qbe.model.i18n.QbeCacheManager;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.model.structure.builder.DataMartStructureBuilderFactory;
import it.eng.qbe.model.structure.builder.IDataMartStructureBuilder;
import it.eng.qbe.query.Query;
import it.eng.qbe.statment.IStatement;
import it.eng.qbe.statment.StatementFactory;

import java.util.List;
import java.util.Locale;

/**
 * @author Andrea Gioia
 */
public abstract class AbstractDataSource implements IDataSource {
	
	protected String name;
	protected IDataSourceConfiguration configuration;
	
	
	protected DataMartModelAccessModality dataMartModelAccessModality;
	protected DataMartModelStructure dataMartModelStructure;

		
	
	public IDataSourceConfiguration getConfiguration() {
		return configuration;
	}

	public DataMartModelStructure getDataMartModelStructure() {
		IDataMartStructureBuilder structureBuilder;
		if(dataMartModelStructure == null) {			
			structureBuilder = DataMartStructureBuilderFactory.getDataMartStructureBuilder(this);
			dataMartModelStructure = structureBuilder.build();
		}
		
		return dataMartModelStructure;
	}
	
	public IStatement createStatement(Query query) {
		return StatementFactory.createStatement(this, query);
	}
	
	public DataMartModelAccessModality getDataMartModelAccessModality() {
		return dataMartModelAccessModality;
	}

	public void setDataMartModelAccessModality(
			DataMartModelAccessModality dataMartModelAccessModality) {
		this.dataMartModelAccessModality = dataMartModelAccessModality;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ModelI18NProperties getModelI18NProperties(Locale locale) {
		ModelI18NProperties properties;
		properties = QbeCacheManager.getInstance().getLabels( this , locale );
		return properties;
	}
	
}
