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
package it.eng.spagobi.commons.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spagobi.commons.utilities.PortletUtilities;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

/**
 * Defines a particular module for saving configuration.
 * 
 * @author sulis
 */
public class SaveConfigurationModule extends AbstractModule {
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.module.AbstractModule#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
	}
	
	/**
	 * Service.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws Exception the exception
	 * 
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */

	public void service(SourceBean request, SourceBean response) throws Exception {
        
		List attributes = request.getContainedAttributes();
		PortletRequest portReq = PortletUtilities.getPortletRequest();
		PortletPreferences pref = portReq.getPreferences();
		String prefPrefix = "PORTLET_PREF_";
		Iterator it = attributes.iterator();
		while (it.hasNext()) {
			SourceBeanAttribute attribute = (SourceBeanAttribute) it.next();
			String key = attribute.getKey();
			if (key != null && key.startsWith(prefPrefix)) {
				String prefName = key.substring(prefPrefix.length());
				String prefValues = (String) attribute.getValue();
				String[] values = prefValues.split(",");
				pref.setValues(prefName, values);
			}
		}
		pref.store();
		/*
		SessionContainer session = getRequestContainer().getSessionContainer();
		RequestContainer requestContainer = this.getRequestContainer();
		PortletRequest portReq = PortletUtilities.getPortletRequest();
		PortletPreferences pref = portReq.getPreferences();
		String lang = (String)request.getAttribute("language");
		String[] codesLang = lang.split(",");
		pref.setValues("language", codesLang);
		pref.store();
		SessionContainer permanentContainer = session.getPermanentContainer();
		permanentContainer.setAttribute("AF_LANGUAGE", codesLang[0]);
		permanentContainer.setAttribute("AF_COUNTRY", codesLang[1]);
		*/
	}


}




