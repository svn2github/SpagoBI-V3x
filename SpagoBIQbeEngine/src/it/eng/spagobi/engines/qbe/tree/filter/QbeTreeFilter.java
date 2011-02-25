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
 * The Class QbeTreeFilter.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QbeTreeFilter {
	
	/** The entity filter. */
	private IQbeTreeEntityFilter entityFilter;
	
	/** The field filter. */
	private IQbeTreeFieldFilter fieldFilter;
	
	/**
	 * Instantiates a new qbe tree filter.
	 * 
	 * @param entityFilter the entity filter
	 * @param fieldFilter the field filter
	 */
	public QbeTreeFilter(IQbeTreeEntityFilter entityFilter, IQbeTreeFieldFilter fieldFilter) {
		setEntityFilter(entityFilter);
		setFieldFilter(fieldFilter);
	}
	
	/**
	 * Filter entities.
	 * 
	 * @param datamartModel the datamart model
	 * @param entities the entities
	 * 
	 * @return the list
	 */
	public List filterEntities(IDataMartModel datamartModel, List entities) {
		return getEntityFilter().filterEntities(datamartModel, entities);
	}
	
	/**
	 * Filter fields.
	 * 
	 * @param datamartModel the datamart model
	 * @param fields the fields
	 * 
	 * @return the list
	 */
	public List filterFields(IDataMartModel datamartModel, List fields) {
		return getFieldFilter().filterFields(datamartModel, fields);
	}

	/**
	 * Gets the entity filter.
	 * 
	 * @return the entity filter
	 */
	protected IQbeTreeEntityFilter getEntityFilter() {
		return entityFilter;
	}

	/**
	 * Sets the entity filter.
	 * 
	 * @param entityFilter the new entity filter
	 */
	protected void setEntityFilter(IQbeTreeEntityFilter entityFilter) {
		this.entityFilter = entityFilter;
	}

	/**
	 * Gets the field filter.
	 * 
	 * @return the field filter
	 */
	protected IQbeTreeFieldFilter getFieldFilter() {
		return fieldFilter;
	}

	/**
	 * Sets the field filter.
	 * 
	 * @param fieldFilter the new field filter
	 */
	protected void setFieldFilter(IQbeTreeFieldFilter fieldFilter) {
		this.fieldFilter = fieldFilter;
	}
}
