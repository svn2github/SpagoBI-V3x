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
package it.eng.spagobi.engines.worksheet;

import it.eng.spagobi.engines.qbe.QbeEngineException;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;
import it.eng.spagobi.engines.worksheet.template.WorksheetTemplate;
import it.eng.spagobi.engines.worksheet.template.WorksheetTemplateParser;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.persist.IDataSetTableDescriptor;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.engines.AbstractEngineInstance;
import it.eng.spagobi.utilities.engines.IEngineAnalysisState;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class WorksheetEngineInstance extends AbstractEngineInstance {
		
		WorksheetTemplate template;
		IDataSetTableDescriptor dataSetTableDescriptor;

		/** Logger component. */
	    public static transient Logger logger = Logger.getLogger(QbeEngineInstance.class);
		

		protected WorksheetEngineInstance(Object template, Map env) throws WorksheetEngineException {
			this( WorksheetTemplateParser.getInstance().parse(template), env );
		}
		
		protected WorksheetEngineInstance(WorksheetTemplate template, Map env) throws WorksheetEngineException {
			super( env );
			logger.debug("IN");
			this.template = template;
			logger.debug("OUT");
		}

		public void validate() throws QbeEngineException {
			return;
		}

		public WorksheetTemplate getTemplate() {
			return template;
		}

		/* (non-Javadoc)
		 * @see it.eng.spagobi.utilities.engines.IEngineInstance#getAnalysisState()
		 */
		@Override
		public IEngineAnalysisState getAnalysisState() {
			return this.getTemplate().getWorkSheetDefinition();
		}

		/* (non-Javadoc)
		 * @see it.eng.spagobi.utilities.engines.IEngineInstance#setAnalysisState(it.eng.spagobi.utilities.engines.IEngineAnalysisState)
		 */
		@Override
		public void setAnalysisState(IEngineAnalysisState analysisState) {
			// TODO Auto-generated method stub
		}

		public IDataSet getDataSet() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public IDataSource getDataSource() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public void setDataSource(IDataSource dataSource) {
			// TODO Auto-generated method stub
			return;
		}
		
		public IDataSetTableDescriptor getLastDataSetTableDescriptor() {
			return this.dataSetTableDescriptor;
		}
		
		public void setLastDataSetTableDescriptor(IDataSetTableDescriptor dataSetTableDescriptor) {
			this.dataSetTableDescriptor = dataSetTableDescriptor;
		}
	
}
