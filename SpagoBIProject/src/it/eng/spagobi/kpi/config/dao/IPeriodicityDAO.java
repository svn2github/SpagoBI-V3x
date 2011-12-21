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
package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.ISpagoBIDao;
import it.eng.spagobi.kpi.config.bo.Periodicity;

import java.util.List;

public interface IPeriodicityDAO extends ISpagoBIDao{
	
	/**
	 * Returns the Periodicity of the referred id
	 * 
	 * @param id of the Periodicity
	 * @return Periodicity of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */
	public Periodicity loadPeriodicityById(Integer id) throws EMFUserError;

	/**
	 * Returns the list of Periodicity.
	 * 
	 * @return the list of all Periodicity.
	 * @throws EMFUserError if an Exception occurs
	 */
	public List loadPeriodicityList() throws EMFUserError;
	
	public void deletePeriodicity(Integer perId) throws EMFUserError;
	
	public void modifyPeriodicity(Periodicity per) throws EMFUserError;
	
	public Integer insertPeriodicity(Periodicity per) throws EMFUserError;
	
	public Integer getPeriodicitySeconds(Integer periodicityId)
	throws EMFUserError;
}
