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
package it.eng.spagobi.engines.qbe.worksheet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class WorkSheet{
	
	private String name;
	private JSONObject header;
	private JSONArray filters;
	private JSONObject content;
	private JSONObject footer;
	
	/**
	 * @param name
	 * @param header
	 * @param filters
	 * @param content
	 * @param footer
	 */
	public WorkSheet(String name, JSONObject header, JSONArray filters,
			JSONObject content, JSONObject footer) {
		super();
		this.name = name;
		this.header = header;
		this.filters = filters;
		this.content = content;
		this.footer = footer;
	}
	public JSONObject getHeader() {
		return header;
	}
	public void setHeader(JSONObject header) {
		this.header = header;
	}
	public JSONArray getFilters() {
		return filters;
	}
	public void setFilters(JSONArray filters) {
		this.filters = filters;
	}
	public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
	public JSONObject getFooter() {
		return footer;
	}
	public void setFooter(JSONObject footer) {
		this.footer = footer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
