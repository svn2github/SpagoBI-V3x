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
package it.eng.spagobi.behaviouralmodel.analyticaldriver.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads the parameters lookup list
 * 
 * @author Andrea Gioia
 */

public class ListLookupReportsModule extends AbstractBasicListModule {
	
	public static final String MODULE_PAGE = "ReportsLookupPage";
	
	/**
	 * Class Constructor.
	 */
	public ListLookupReportsModule() {
		super();
	} 
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		SessionContainer session = this.getRequestContainer().getSessionContainer();
		
		String message = (String) session.getAttribute("MESSAGE");		
		if(message!=null){
			request.setAttribute("MESSAGE", message);
			session.delAttribute("MESSAGE");
		}
		
		String page = (String) session.getAttribute("LIST_PAGE");
		if(page!=null){
			request.setAttribute("LIST_PAGE", page);
			session.delAttribute("LIST_PAGE");
		}
		super.service(request, response); 
	}
	  
	
	/**
	 * Gets the list.
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * 
	 * @return ListIFace
	 * 
	 * @throws Exception the exception
	 */
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		
		
		
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
	            "DetailBIObjectPublisher", 
	            "getPublisherName", 
	            "REQ: " + request);
		
		getSubreportsId(request);
		
		
		
		return DelegatedHibernateConnectionListService.getList(this, request, response);
	}
	
	private List getSubreportsId(SourceBean request){
		List results = new ArrayList();
		List attrs = request.getContainedAttributes();
		for(int i = 0; i < attrs.size(); i++){
			SourceBeanAttribute attr = (SourceBeanAttribute)attrs.get(i);
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "ListLookupReportsModule","service", " ATTR -> " + attr.getKey() + "=" + attr.getValue());
			String key = (String)attr.getKey();
			if(key.startsWith("checkbox")) {
				String id = key.substring(key.indexOf(':')+1, key.length());
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "ListLookupReportsModule","service", " ATTR [OK] " + id);
				results.add(new Integer(id));
			}
		}
		
		return results;
	}
	
}
