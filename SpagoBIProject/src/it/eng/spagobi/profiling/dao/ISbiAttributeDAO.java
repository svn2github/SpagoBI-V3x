/* SpagoBI, the Open Source Business Intelligence suite

* � 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this file,
* You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.ISpagoBIDao;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;

import java.util.HashMap;
import java.util.List;

public interface ISbiAttributeDAO extends ISpagoBIDao{
	
	public List<SbiUserAttributes> loadSbiAttributesById(Integer id) throws EMFUserError;
	
	public HashMap<Integer, String> loadSbiAttributesByIds(List<String> ids) throws EMFUserError;
	
	public SbiAttribute loadSbiAttributeByName(String name) throws EMFUserError;
	
	public List<SbiAttribute> loadSbiAttributes() throws EMFUserError;
	
	public Integer saveSbiAttribute(SbiAttribute attribute) throws EMFUserError;
	
	public Integer saveOrUpdateSbiAttribute(SbiAttribute attribute) throws EMFUserError;

	public SbiUserAttributes loadSbiAttributesByUserAndId(Integer userId, Integer id)	throws EMFUserError;
	
	public void deleteSbiAttributeById(Integer id) throws EMFUserError;

}
