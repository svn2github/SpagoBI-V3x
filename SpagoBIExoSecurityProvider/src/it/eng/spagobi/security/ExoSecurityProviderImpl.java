/* SpagoBI, the Open Source Business Intelligence suite

* � 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this file,
* You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.security;

import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Implements the ISecurityInfoProvider interface defining method to get the 
 * system and user roles.
 */
public class ExoSecurityProviderImpl implements ISecurityInfoProvider {
	
	
	/** 
	 * Get all the portal roles 
	 * @return List of the portal roles (list of it it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		List roles = new ArrayList();
		PortalContainer container = PortalContainer.getInstance();	
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		try {
			Collection groups = service.getGroupHandler().getAllGroups();
		    Iterator iter = groups.iterator();
		    Group group = null;
		    while(iter.hasNext()) {
		    	group = (Group)iter.next();
		    	SecurityProviderUtilities.debug(this.getClass(), "getRoles", " Find a Role With Name [" + group.getGroupName() +"]");
		    	add(group, service, roles);
		    }
		} catch (Exception e) {
			SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
					               this.getClass().getName(),
					               "getRoles()",
					               " Exception while retrieving roles", e);
		}
		return roles;
	}
	
	
	/**
	 * Add the current group(role) and it's child to the roles list
	 * @param group Group of the portal
	 * @param orgService OrganizationService of the portal
	 * @param roles List of roles (list of it it.eng.spagobi.bo.Role)
	 */
	private void add(Group group, OrganizationService orgService, List roles){
		Role role = new Role(group.getId(), group.getDescription());
    	roles.add(role);
    	try{
    		Collection children = orgService.getGroupHandler().findGroups(group);
    		if ((children == null) || (children.size() == 0)){
    			// End recursion
    			return;
    		}else{
    			Iterator it = children.iterator();
    			while (it.hasNext()){
    				add((Group)it.next(), orgService, roles);
    			}
    		}
    	}catch(Exception e){
    		SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
    				               this.getClass().getName(),
    				               "add()",
    				               " Exception when retrieving child of group "+group.getId(), e);
    	}
	}


	/**
	 * Get the names of all the profile attributes defined
	 * @return a list containig the names of all the profile attributes defined
	 */
	public List getAllProfileAttributesNames() {
		List toReturn = null;
		try {
			toReturn = SecurityProviderUtilities.getAllProfileAtributesNames();
		} catch (EMFInternalError e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		               			   "getAllProfileAttributesNames()",
		               			   "Error while retrieving the list of all profile attributes names", e);
			return new ArrayList();
		}
		return toReturn;
	}

}
