/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.common.behaviour;

import it.eng.spagobi.tools.dataset.bo.IDataSet;

import java.util.List;
import java.util.Map;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class FilteringBehaviour extends AbstractDataSetBehaviour {
	
	private Map<String, List<String>> filters = null;
	
	public FilteringBehaviour(IDataSet targetDataSet) {
		super(FilteringBehaviour.class.getName(), targetDataSet);
	}

	public void setFilters(Map<String, List<String>> filters) {
		this.filters = filters;
	}
	
	public Map<String, List<String>> getFilters() {
		return this.filters;
	}
	
}
