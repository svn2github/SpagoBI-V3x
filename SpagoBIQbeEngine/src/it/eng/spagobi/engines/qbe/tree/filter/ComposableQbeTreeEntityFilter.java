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
package it.eng.spagobi.engines.qbe.tree.filter;

import java.util.List;

import it.eng.qbe.model.IDataMartModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ComposableQbeTreeEntityFilter.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class ComposableQbeTreeEntityFilter implements IQbeTreeEntityFilter {
	
	/** The parent filter. */
	private IQbeTreeEntityFilter parentFilter;
	
	/**
	 * Instantiates a new composable qbe tree entity filter.
	 */
	public ComposableQbeTreeEntityFilter() {
		parentFilter = null;
	}
	
	/**
	 * Instantiates a new composable qbe tree entity filter.
	 * 
	 * @param parentFilter the parent filter
	 */
	public ComposableQbeTreeEntityFilter(IQbeTreeEntityFilter parentFilter) {
		setParentFilter(parentFilter);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.tree.filter.IQbeTreeEntityFilter#filterEntities(it.eng.qbe.model.IDataMartModel, java.util.List)
	 */
	public List filterEntities(IDataMartModel datamartModel, List entities) {
		
		if( getParentFilter() != null) {
			entities = getParentFilter().filterEntities(datamartModel, entities);
		}
		
		return filter( datamartModel, entities );
	}
	
	/**
	 * Filter.
	 * 
	 * @param datamartModel the datamart model
	 * @param fields the fields
	 * 
	 * @return the list
	 */
	public abstract List filter(IDataMartModel datamartModel, List fields);

	/**
	 * Gets the parent filter.
	 * 
	 * @return the parent filter
	 */
	protected IQbeTreeEntityFilter getParentFilter() {
		return parentFilter;
	}

	/**
	 * Sets the parent filter.
	 * 
	 * @param parentFilter the new parent filter
	 */
	protected void setParentFilter(IQbeTreeEntityFilter parentFilter) {
		this.parentFilter = parentFilter;
	}

}
