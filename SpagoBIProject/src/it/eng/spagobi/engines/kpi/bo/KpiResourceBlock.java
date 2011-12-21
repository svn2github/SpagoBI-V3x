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
package it.eng.spagobi.engines.kpi.bo;

import it.eng.spago.base.RequestContainer;
import it.eng.spagobi.kpi.model.bo.Resource;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class KpiResourceBlock implements Serializable{
	
	private static transient Logger logger = Logger.getLogger(KpiResourceBlock.class);
	
	Resource r = null;
	KpiLine root = null;
	Date d = null;
	String title = "";
	String subtitle = "";
	protected HashMap parMap;
	protected String currTheme="";
	protected RequestContainer requestContainer = null;
	KpiLineVisibilityOptions options = new KpiLineVisibilityOptions();
	
	public KpiResourceBlock(Resource r, KpiLine root, Date d, HashMap parMap,KpiLineVisibilityOptions options ) {
		super();
		this.r = r;
		this.root = root;
		this.d = d;
		this.parMap = parMap;
		this.options = options;
	}
	
	public KpiResourceBlock() {
		super();
		this.d = new Date();
		this.parMap = new HashMap();
	}

	public Resource getR() {
		return r;
	}
	
	public void setR(Resource r) {
		this.r = r;
	}
	
	public KpiLine getRoot() {
		return root;
	}
	
	public KpiLineVisibilityOptions getOptions() {
		return options;
	}

	public void setOptions(KpiLineVisibilityOptions options) {
		this.options = options;
	}

	public void setRoot(KpiLine root) {
		this.root = root;
	}

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}
	
	public HashMap getParMap() {
		return parMap;
	}

	public void setParMap(HashMap parMap) {
		this.parMap = parMap;
	}	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	
}
