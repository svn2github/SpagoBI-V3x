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
package it.eng.spagobi.engines.qbe.analysisstateloaders.worksheet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 */
public class WorksheetStateLoaderFactory {
	
	private static Map loaderRegistry;
	
	static {
		loaderRegistry = new HashMap();
		
		loaderRegistry.put("0", 
			new Version0WorksheetStateLoader()
		);
		
		loaderRegistry.put("1", 
				new Version1WorksheetStateLoader()
		);
	}
	
	private static WorksheetStateLoaderFactory instance;
	public static WorksheetStateLoaderFactory getInstance() {
		if(instance == null) {
			instance = new WorksheetStateLoaderFactory();
		}
		return instance;
	}
	
	private WorksheetStateLoaderFactory() {}
	
	public IWorksheetStateLoader getLoader(String encodingFormatVersion) {
		return (IWorksheetStateLoader) loaderRegistry.get(encodingFormatVersion);
	}
}
