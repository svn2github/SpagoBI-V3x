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
package it.eng.spagobi.jpivotaddins.util;

import javax.servlet.http.HttpSession;

public class SessionObjectRemoval {

	public static void removeSessionObjects(HttpSession session) {
		
		// remove following objects from session.
		if (session.getAttribute("query01") != null) {
			session.removeAttribute("query01");
		}

		if (session.getAttribute("table01") != null) {
			session.removeAttribute("table01");
		}

		if (session.getAttribute("minichartform01") != null) {
			session.removeAttribute("minichartform01");
		}

		if (session.getAttribute("mdxedit01") != null) {
			session.removeAttribute("mdxedit01");
		}

		if (session.getAttribute("navi01") != null) {
			session.removeAttribute("navi01");
		}

		if (session.getAttribute("print01") != null) {
			session.removeAttribute("print01");
		}

		if (session.getAttribute("sortform01") != null) {
			session.removeAttribute("sortform01");
		}

		if (session.getAttribute("printform01") != null) {
			session.removeAttribute("printform01");
		}

		if (session.getAttribute("chartform01") != null) {
			session.removeAttribute("chartform01");
		}

		if (session.getAttribute("query01.drillthroughtable") != null) {
			session.removeAttribute("query01.drillthroughtable");
		}
		
		if (session.getAttribute("chart01") != null) {
			session.removeAttribute("chart01");
		}
		
		if (session.getAttribute("analysisBean") != null) {
			session.removeAttribute("analysisBean");
		}
		
		if (session.getAttribute("save01") != null) {
			session.removeAttribute("save01");
		}
		
		if (session.getAttribute("saveAnalysis01") != null) {
			session.removeAttribute("saveAnalysis01");
		}
		
		if (session.getAttribute("schemas") != null) {
			session.removeAttribute("schemas");
		}
		
		if (session.getAttribute("connections") != null) {
			session.removeAttribute("connections");
		}
		
		if (session.getAttribute("MondrianCubes") != null) {
			session.removeAttribute("MondrianCubes");
		}
		
		if (session.getAttribute("MondrianVirtualCubes") != null) {
			session.removeAttribute("MondrianVirtualCubes");
		}
		
		if (session.getAttribute("saveTemplate01") != null) {
			session.removeAttribute("saveTemplate01");
		}

		if (session.getAttribute("saveTemplateForm01") != null) {
			session.removeAttribute("saveTemplateForm01");
		}
		
		if (session.getAttribute("toolbar01") != null) {
			session.removeAttribute("toolbar01");
		}
		
		if (session.getAttribute("selectedSchemaNode") != null) {
			session.removeAttribute("selectedSchemaNode");
		}
		
		if (session.getAttribute("catalogUri") != null) {
			session.removeAttribute("catalogUri");
		}
		
		if (session.getAttribute("connection") != null) {
			session.removeAttribute("connection");
		}
		
		if (session.getAttribute("parameters") != null) {
			session.removeAttribute("parameters");
		}
		
		if (session.getAttribute("queryWithParameters") != null) {
			session.removeAttribute("queryWithParameters");
		}
		
		if (session.getAttribute("initialQueryWithParameters") != null) {
			session.removeAttribute("initialQueryWithParameters");
		}
		
		if (session.getAttribute("initialMondrianQuery") != null) {
			session.removeAttribute("initialMondrianQuery");
		}
		
		if (session.getAttribute("selectedConnection") != null) {
			session.removeAttribute("selectedConnection");
		}
		
		if (session.getAttribute("selectedSchema") != null) {
			session.removeAttribute("selectedSchema");
		}
		
		if (session.getAttribute("selectedCube") != null) {
			session.removeAttribute("selectedCube");
		}
	}
	
}
