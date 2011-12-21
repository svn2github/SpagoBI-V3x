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
package it.eng.spagobi.tools.dataset.bo;

import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.common.dataproxy.IDataProxy;
import it.eng.spagobi.tools.dataset.common.dataproxy.JavaClassDataProxy;
import it.eng.spagobi.tools.dataset.common.datareader.XmlDataReader;

import org.apache.log4j.Logger;

public class JavaClassDataSet extends ConfigurableDataSet {
	 
	public static String DS_TYPE = "SbiJClassDataSet";
	
	private static transient Logger logger = Logger.getLogger(JavaClassDataSet.class);
	 
	
	public JavaClassDataSet() {
		super();
		setDataProxy( new JavaClassDataProxy() );
		setDataReader( new XmlDataReader() );
	}
	
	public JavaClassDataSet(SpagoBiDataSet dataSetConfig) {
		super(dataSetConfig);
		setDataProxy( new JavaClassDataProxy() );
		setDataReader( new XmlDataReader() );		
		
		setClassName( dataSetConfig.getJavaClassName() );
	}
	
	public SpagoBiDataSet toSpagoBiDataSet() {
		SpagoBiDataSet sbd;
		
		sbd = super.toSpagoBiDataSet();
		
		sbd.setType( DS_TYPE );
				
		sbd.setJavaClassName( getClassName() );
		
		return sbd;
	}
	
	public JavaClassDataProxy getDataProxy() {
		IDataProxy dataProxy;
		
		dataProxy = super.getDataProxy();
		
		if(dataProxy == null) {
			setDataProxy( new JavaClassDataProxy() );
			dataProxy = getDataProxy();
		}
		
		if(!(dataProxy instanceof  JavaClassDataProxy)) throw new RuntimeException("DataProxy cannot be of type [" + 
				dataProxy.getClass().getName() + "] in FileDataSet");
		
		return (JavaClassDataProxy)dataProxy;
	}

	public void setClassName(String className) {
		getDataProxy().setClassName(className);
	}
	
	public String getClassName() {
		return getDataProxy().getClassName();
	}
	
	
	 
}
