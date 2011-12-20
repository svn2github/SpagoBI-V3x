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

package it.eng.spagobi.commons.presentation.tags;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Builds and presents all objects list for all admin 
 * SpagoBI's list modules. Once a list module has been executed, 
 * the list tag builds all the correspondent jsp page and gives the results
 */
public class ListBIParametersTag extends ListTag
{
    
	/**
	 * Starting from the module <code>buttonsSB</code> object, 
	 * creates all buttons for the jsp list. 
	 * 
	 * @throws JspException If any exception occurs.
	 */
	
	protected StringBuffer makeButton() throws JspException {

		StringBuffer htmlStream = new StringBuffer();
		SourceBean buttonsSB = (SourceBean)_layout.getAttribute("BUTTONS");
		List buttons = buttonsSB.getContainedSourceBeanAttributes();
		Iterator iter = buttons.listIterator();
		while(iter.hasNext()) {
			SourceBeanAttribute buttonSBA = (SourceBeanAttribute)iter.next();
			SourceBean buttonSB = (SourceBean)buttonSBA.getValue();
			List parameters = buttonSB.getAttributeAsList("PARAMETER");
			HashMap paramsMap = getParametersMap(parameters, null);
			String img = (String)buttonSB.getAttribute("image");
			String labelCode = (String)buttonSB.getAttribute("label");
			String label = msgBuilder.getMessage(labelCode, "messages", httpRequest);
			label = StringEscapeUtils.escapeHtml(label);
			htmlStream.append("<form action='"+urlBuilder.getUrl(httpRequest, new HashMap())+"' id='form"+label+"'  method='POST' >\n");
			htmlStream.append("	<td class=\"header-button-column-portlet-section\">\n");
			Set paramsKeys = paramsMap.keySet();
			Iterator iterpar = paramsKeys.iterator();
			while(iterpar.hasNext()) {
				String paramKey = (String)iterpar.next();
				String paramValue = (String)paramsMap.get(paramKey);
				while(paramValue.indexOf("%20") != -1) {
					paramValue = paramValue.replaceAll("%20", " ");
				}
				htmlStream.append("	  <input type='hidden' name='"+paramKey+"' value='"+paramValue+"' /> \n");
			}
			htmlStream.append("		<a href='javascript:document.getElementById(\"form"+label+"\").submit()'><img class=\"header-button-image-portlet-section\" title='" + label + "' alt='" + label + "' src='"+urlBuilder.getResourceLinkByTheme(httpRequest, img, currTheme)+"' /></a>\n");
			htmlStream.append("	</td>\n");
			htmlStream.append("</form>\n");
		}	
		return htmlStream;
	} 



}
	
	



