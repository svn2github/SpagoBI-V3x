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
package it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata;

import it.eng.spagobi.commons.metadata.SbiHibernateModel;

public class SbiObjParuse extends SbiHibernateModel {


	private SbiObjParuseId id;
	private Integer prog;
	private String filterColumn;
	private String preCondition;
	private String postCondition;
	private String logicOperator;

	// Constructors

	/**
	 * default constructor.
	 */
	public SbiObjParuse() {
	}

	/**
	 * constructor with id.
	 * 
	 * @param id the id
	 */
	public SbiObjParuse(SbiObjParuseId id) {
		this.id = id;
	}

	/**
	 * Gets the filter column.
	 * 
	 * @return the filter column
	 */
	public String getFilterColumn() {
		return filterColumn;
	}

	/**
	 * Sets the filter column.
	 * 
	 * @param filterColumn the new filter column
	 */
	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	/**
	 * Gets the logic operator.
	 * 
	 * @return the logic operator
	 */
	public String getLogicOperator() {
		return logicOperator;
	}

	/**
	 * Sets the logic operator.
	 * 
	 * @param logicOperator the new logic operator
	 */
	public void setLogicOperator(String logicOperator) {
		this.logicOperator = logicOperator;
	}

	/**
	 * Gets the post condition.
	 * 
	 * @return the post condition
	 */
	public String getPostCondition() {
		return postCondition;
	}

	/**
	 * Sets the post condition.
	 * 
	 * @param postCondition the new post condition
	 */
	public void setPostCondition(String postCondition) {
		this.postCondition = postCondition;
	}

	/**
	 * Gets the pre condition.
	 * 
	 * @return the pre condition
	 */
	public String getPreCondition() {
		return preCondition;
	}

	/**
	 * Sets the pre condition.
	 * 
	 * @param preCondition the new pre condition
	 */
	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	/**
	 * Gets the prog.
	 * 
	 * @return the prog
	 */
	public Integer getProg() {
		return prog;
	}

	/**
	 * Sets the prog.
	 * 
	 * @param prog the new prog
	 */
	public void setProg(Integer prog) {
		this.prog = prog;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public SbiObjParuseId getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param objparuseId the new id
	 */
	public void setId(SbiObjParuseId objparuseId) {
		this.id = objparuseId;
	}



}
