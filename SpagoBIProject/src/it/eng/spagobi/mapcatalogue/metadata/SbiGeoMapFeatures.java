/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.mapcatalogue.metadata;

import it.eng.spagobi.commons.metadata.SbiHibernateModel;



// Generated 31-mag-2007 14.53.27 by Hibernate Tools 3.2.0.beta8

/**
 * SbiGeoMapFeatures generated by hbm2java
 */
public class SbiGeoMapFeatures extends SbiHibernateModel{

	// Fields    

	private SbiGeoMapFeaturesId id;

	private SbiGeoFeatures sbiGeoFeatures;

	private SbiGeoMaps sbiGeoMaps;

	private String svgGroup;

	private String visibleFlag;

	// Constructors

	/**
	 * default constructor.
	 */
	public SbiGeoMapFeatures() {
	}

	/**
	 * minimal constructor.
	 * 
	 * @param id the id
	 * @param sbiGeoFeatures the sbi geo features
	 * @param sbiGeoMaps the sbi geo maps
	 */
	public SbiGeoMapFeatures(SbiGeoMapFeaturesId id,
			SbiGeoFeatures sbiGeoFeatures, SbiGeoMaps sbiGeoMaps) {
		this.id = id;
		this.sbiGeoFeatures = sbiGeoFeatures;
		this.sbiGeoMaps = sbiGeoMaps;
	}

	/**
	 * full constructor.
	 * 
	 * @param id the id
	 * @param sbiGeoFeatures the sbi geo features
	 * @param sbiGeoMaps the sbi geo maps
	 * @param svgGroup the svg group
	 * @param visibleFlag the visible flag
	 */
	public SbiGeoMapFeatures(SbiGeoMapFeaturesId id,
			SbiGeoFeatures sbiGeoFeatures, SbiGeoMaps sbiGeoMaps,
			String svgGroup, String visibleFlag) {
		this.id = id;
		this.sbiGeoFeatures = sbiGeoFeatures;
		this.sbiGeoMaps = sbiGeoMaps;
		this.svgGroup = svgGroup;
		this.visibleFlag = visibleFlag;
	}

	// Property accessors
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public SbiGeoMapFeaturesId getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(SbiGeoMapFeaturesId id) {
		this.id = id;
	}

	/**
	 * Gets the sbi geo features.
	 * 
	 * @return the sbi geo features
	 */
	public SbiGeoFeatures getSbiGeoFeatures() {
		return this.sbiGeoFeatures;
	}

	/**
	 * Sets the sbi geo features.
	 * 
	 * @param sbiGeoFeatures the new sbi geo features
	 */
	public void setSbiGeoFeatures(SbiGeoFeatures sbiGeoFeatures) {
		this.sbiGeoFeatures = sbiGeoFeatures;
	}

	/**
	 * Gets the sbi geo maps.
	 * 
	 * @return the sbi geo maps
	 */
	public SbiGeoMaps getSbiGeoMaps() {
		return this.sbiGeoMaps;
	}

	/**
	 * Sets the sbi geo maps.
	 * 
	 * @param sbiGeoMaps the new sbi geo maps
	 */
	public void setSbiGeoMaps(SbiGeoMaps sbiGeoMaps) {
		this.sbiGeoMaps = sbiGeoMaps;
	}

	/**
	 * Gets the svg group.
	 * 
	 * @return the svg group
	 */
	public String getSvgGroup() {
		return this.svgGroup;
	}

	/**
	 * Sets the svg group.
	 * 
	 * @param svgGroup the new svg group
	 */
	public void setSvgGroup(String svgGroup) {
		this.svgGroup = svgGroup;
	}

	/**
	 * Gets the visible flag.
	 * 
	 * @return the visible flag
	 */
	public String getVisibleFlag() {
		return this.visibleFlag;
	}

	/**
	 * Sets the visible flag.
	 * 
	 * @param visibleFlag the new visible flag
	 */
	public void setVisibleFlag(String visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

}
