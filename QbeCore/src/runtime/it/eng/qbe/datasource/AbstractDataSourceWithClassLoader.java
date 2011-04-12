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
package it.eng.qbe.datasource;

import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.hibernate.HibernateDataSourceWithClassLoader;

import it.eng.qbe.model.accessmodality.IModelAccessModality;
import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;

import java.util.Locale;
/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 * 
 * This Data source wraps a IDataSource and it's class loader..
 * I'ts useful when you have different models working in the same moment
 * 
 * It calls all the methods of the wrapped data source but before call them
 * it sets the myClassLoader as thread class loader
 */

public abstract class AbstractDataSourceWithClassLoader implements IDataSource{
	
	protected static ClassLoader defoutlClassLoader;
	
	protected ClassLoader myClassLoader;

	//The wrapped data source
	protected IDataSource wrappedDataSource;
	
	public AbstractDataSourceWithClassLoader(IDataSource wrappedDataSource){
		if(defoutlClassLoader==null){
			defoutlClassLoader = Thread.currentThread().getContextClassLoader();
		}else{
			Thread.currentThread().setContextClassLoader(defoutlClassLoader);
		}
		myClassLoader = defoutlClassLoader;
		if(wrappedDataSource instanceof AbstractDataSourceWithClassLoader){
			this.wrappedDataSource = ((AbstractDataSourceWithClassLoader) wrappedDataSource).getWrappedDataSource();
		}
		this.wrappedDataSource = wrappedDataSource;
	}
	
	public String getName() {
		return wrappedDataSource.getName();
	}

	public IDataSourceConfiguration getConfiguration() {
		return wrappedDataSource.getConfiguration();
	}

	public IModelStructure getModelStructure() {
		Thread.currentThread().setContextClassLoader(myClassLoader);
		return wrappedDataSource.getModelStructure();
	}

	public IModelAccessModality getModelAccessModality() {
		return wrappedDataSource.getModelAccessModality();
	}

	public void setDataMartModelAccessModality(
			IModelAccessModality modelAccessModality) {
		wrappedDataSource.setDataMartModelAccessModality(modelAccessModality);
		
	}

	public IModelProperties getModelI18NProperties(Locale locale) {
		return wrappedDataSource.getModelI18NProperties(locale);
	}

	public boolean isOpen() {
		return wrappedDataSource.isOpen();
	}

	public void close() {
		wrappedDataSource.close();
	}

	public IStatement createStatement(Query query) {
		Thread.currentThread().setContextClassLoader(myClassLoader);
		return wrappedDataSource.createStatement(query);
	}

	public IDataSource getWrappedDataSource() {
		return wrappedDataSource;
	}

	public void setWrappedDataSource(IDataSource wrappedDataSource) {
		this.wrappedDataSource = wrappedDataSource;
	}
		
	
	
}