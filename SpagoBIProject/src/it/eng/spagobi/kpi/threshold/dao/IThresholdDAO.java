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
package it.eng.spagobi.kpi.threshold.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.ISpagoBIDao;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;

import java.util.List;

public interface IThresholdDAO extends ISpagoBIDao{
	
	/**
	 * Returns the Threshold of the referred id
	 * 
	 * @param id of the Threshold
	 * @return Threshold of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */
	public Threshold loadThresholdById(Integer id) throws EMFUserError ;
	
	public Threshold loadThresholdByCode(String code) throws EMFUserError ;
	
	/**
	 * Returns the list of Thresholds.
	 * @param typeOrder DESC or ASC.
	 * @param fieldOrder Name of the column in the view to Order.
	 * @return the list of Thresholds.
	 * @throws EMFUserError If an Exception occurred.
	 */
	public List loadThresholdList(String fieldOrder, String typeOrder) throws EMFUserError ;
	
	public List loadThresholdList() throws EMFUserError ;
	
	public List loadPagedThresholdList(Integer offset, Integer fetchSize)throws EMFUserError ;
	
	public Integer countThresholds()throws EMFUserError ;

	public void modifyThreshold(Threshold threshold) throws EMFUserError ;

	public Integer insertThreshold(Threshold toCreate)throws EMFUserError ;

	public boolean deleteThreshold(Integer thresholdId)throws EMFUserError;
	
	public Threshold toThreshold(SbiThreshold t) throws EMFUserError; 

	public List loadThresholdListFiltered(String hsql, Integer offset, Integer fetchSize)throws EMFUserError ;

}
