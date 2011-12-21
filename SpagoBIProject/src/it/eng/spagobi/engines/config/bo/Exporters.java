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

package it.eng.spagobi.engines.config.bo;

import java.io.Serializable;

public class Exporters implements Serializable{


		Integer domainId;
		Integer engineId;
		boolean defaultValue;
		
		
		public Integer getDomainId() {
			return domainId;
		}
		public void setDomainId(Integer domainId) {
			this.domainId = domainId;
		}
		public Integer getEngineId() {
			return engineId;
		}
		public void setEngineId(Integer engineId) {
			this.engineId = engineId;
		}
		public boolean isDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(boolean defaultValue) {
			this.defaultValue = defaultValue;
		}
		
		

		


}
