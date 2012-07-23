/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.commons.utilities;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.UserFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.metadata.SbiTenant;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class UserUtilities {

    static Logger logger = Logger.getLogger(UserUtilities.class);

    public static String getSchema(String ente,RequestContainer aRequestContainer){
    	
    	logger.debug("Ente: "+ente);
    	SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
    	SessionContainer permanentSession = aSessionContainer.getPermanentContainer();

    	IEngUserProfile userProfile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    	
    	if (userProfile!=null){
    		try {
				return (String) userProfile.getUserAttribute(ente);
			} catch (EMFInternalError e) {
				logger.error("User profile is NULL!!!!");
			}
    	}else {
    		logger.warn("User profile is NULL!!!!");
    	}
    	return null;
    }
    
    public static String getSchema(String ente,IEngUserProfile userProfile){
    	logger.debug("Ente: "+ente);
    	if (userProfile!=null){
    		try {
				return (String) userProfile.getUserAttribute(ente);
			} catch (EMFInternalError e) {
				logger.error("User profile is NULL!!!!");
			}
    	}else {
    		logger.warn("User profile is NULL!!!!");
    	}
    	return null;
    }
    
    
    
    /**
     * Gets the user profile.
     * 
     * @return the user profile
     * 
     * @throws Exception the exception
     */
	public static IEngUserProfile getUserProfile() throws Exception {
		RequestContainer aRequestContainer = RequestContainer
				.getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer
				.getSessionContainer();
		SessionContainer permanentSession = aSessionContainer
				.getPermanentContainer();

		IEngUserProfile userProfile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		if (userProfile == null) {

			String userId = null;
			PortletRequest portletRequest = PortletUtilities
					.getPortletRequest();
			Principal principal = portletRequest.getUserPrincipal();
			userId = principal.getName();
			logger.debug("got userId from Principal=" + userId);

			userProfile = UserUtilities.getUserProfile(userId);

			logger.debug("userProfile created.UserID= "
					+ (String) userProfile.getUserUniqueIdentifier());
			logger.debug("Attributes name of the user profile: "
					+ userProfile.getUserAttributeNames());
			logger.debug("Functionalities of the user profile: "
					+ userProfile.getFunctionalities());
			logger.debug("Roles of the user profile: " + userProfile.getRoles());

			permanentSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE,
					userProfile);

			// String username = (String) userProfile.getUserUniqueIdentifier();
			String username = ((UserProfile) userProfile).getUserId().toString();
			if (!UserUtilities.userFunctionalityRootExists(username)) {
				UserUtilities.createUserFunctionalityRoot(userProfile);
			}

		}

		return userProfile;
	}

	public static IEngUserProfile getUserProfile(HttpServletRequest req)
			throws Exception {
		logger.debug("IN");
		try {
			SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
			String userId = userProxy.readUserIdentifier(req);
			return UserUtilities.getUserProfile(userId);
		} finally {
			logger.debug("OUT");
		}
	}

	public static IEngUserProfile getUserProfile(String userId)
			throws Exception {
		logger.debug("IN.userId=" + userId);
		if (userId == null)
			return null;
		ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory
				.createISecurityServiceSupplier();
		try {
			SpagoBIUserProfile user = supplier.createUserProfile(userId);
			if (user == null)
				return null;
			checkTenant(user);
			user.setFunctions(readFunctionality(user.getRoles(),
					user.getOrganization()));
			return new UserProfile(user);
		} catch (Exception e) {
			logger.error("Exception while creating user profile", e);
			throw new SecurityException(
					"Exception while creating user profile", e);
		} finally {
			logger.debug("OUT");
		}
	}  

    /**
     * User functionality root exists.
     * 
     * @param username the username
     * 
     * @return true, if successful
     * 
     * @throws Exception the exception
     */
    public static boolean userFunctionalityRootExists(String username) throws Exception {
	boolean exists = false;
	try {
		logger.debug("****  username checked: " + username);
	    ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
	    exists = functdao.checkUserRootExists(username);
	} catch (Exception e) {
		logger.error("Error while checking user functionality root existence", e);
	    throw new Exception("Unable to check user functionality existence", e);
	}
	return exists;
    }

    /**
     * User functionality root exists.
     * 
     * @param userProfile the user profile
     * 
     * @return true, if successful
     * 
     * @throws Exception the exception
     */
    public static boolean userFunctionalityRootExists(UserProfile userProfile) {
    	Assert.assertNotNull(userProfile, "User profile in input is null");
    	boolean toReturn = false;
    	String userName = (String) userProfile.getUserName();
    	try {
    		toReturn = userFunctionalityRootExists(userName);
    	} catch (Exception e) {
    		throw new SpagoBIRuntimeException("Cannot find if user functionality exists for user [" + userName + "]", e);
    	}
    	return toReturn;
    }
    
    /**
     * User functionality root exists.
     * 
     * @param userProfile the user profile
     * 
     * @return true, if successful
     * 
     * @throws Exception the exception
     */
    public static LowFunctionality loadUserFunctionalityRoot(UserProfile userProfile) {
    	Assert.assertNotNull(userProfile, "User profile in input is null");
    	String userId = (String) userProfile.getUserId();
    	LowFunctionality lf = null;
    	try {
    		lf = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath("/" + userId, false);
    	} catch (Exception e) {
    		throw new SpagoBIRuntimeException("Cannot load user functionality for user with id [" + userId + "]", e);
    	}
    	return lf;
    }
    
    /**
     * Creates the user functionality root.
     * 
     * @param userProfile the user profile
     * 
     * @throws Exception the exception
     */
    public static void createUserFunctionalityRoot(IEngUserProfile userProfile) throws Exception {
    	logger.debug("IN");
	try {
	    String userId = (String) ((UserProfile)userProfile).getUserId();
	    logger.debug("userId: " + userId);
	    Collection roleStrs = ((UserProfile)userProfile).getRolesForUse();
	    Iterator roleIter = roleStrs.iterator();
	    List roles = new ArrayList();
	    logger.debug("Roles's number: " + roleStrs.size());
	    while (roleIter.hasNext()) {
	    	String rolename = (String) roleIter.next();
	    	logger.debug("Rolename: " + rolename);
	    	Role role = DAOFactory.getRoleDAO().loadByName(rolename);
	    	if (role!=null)  {
	    		roles.add(role);
	    		logger.debug("Add Rolename ( " + rolename +") ");
	    	}
	    	else logger.debug("Rolename ( " + rolename +") doesn't exist in EXT_ROLES");
	    }
	    Role[] rolesArr = new Role[roles.size()];
	    rolesArr = (Role[]) roles.toArray(rolesArr);

	    UserFunctionality userFunct = new UserFunctionality();
	    userFunct.setCode("ufr_" + userId);
	    userFunct.setDescription("User Functionality Root");
	    userFunct.setName(userId);
	    userFunct.setPath("/" + userId);
	    //userFunct.setExecRoles(rolesArr);
	    ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
	    functdao.insertUserFunctionality(userFunct);
	} catch (Exception e) {
	   logger.error("Error while creating user functionality root", e);
	    throw new Exception("Unable to create user functionality root", e);
	}finally{
		logger.debug("OUT");
	}
    }


    public static String[] readFunctionality(String[] roles, String organization) {
		logger.debug("IN");
		try {
		    it.eng.spagobi.commons.dao.IUserFunctionalityDAO dao = DAOFactory.getUserFunctionalityDAO();
		    dao.setTenant(organization);
		    String[] functionalities = dao.readUserFunctionality(roles);
		    logger.debug("Functionalities retrieved: " + functionalities == null ? "" : functionalities.toString());
		    
		    List<String> roleFunctionalities = new ArrayList<String>();
		    Role virtualRole = getVirtualRole(roles, organization);
		    
			if (virtualRole.isAbleToSaveSubobjects()) {
				roleFunctionalities.add(SpagoBIConstants.SAVE_SUBOBJECT_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSeeSubobjects()) {
				roleFunctionalities.add(SpagoBIConstants.SEE_SUBOBJECTS_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSeeSnapshots()) {
				roleFunctionalities.add(SpagoBIConstants.SEE_SNAPSHOTS_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSeeViewpoints()) {
				roleFunctionalities.add(SpagoBIConstants.SEE_VIEWPOINTS_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSeeNotes()) {
				roleFunctionalities.add(SpagoBIConstants.SEE_NOTES_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSendMail()) {
				roleFunctionalities.add(SpagoBIConstants.SEND_MAIL_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSaveIntoPersonalFolder()) {
				roleFunctionalities.add(SpagoBIConstants.SAVE_INTO_FOLDER_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSaveRememberMe()) {
				roleFunctionalities.add(SpagoBIConstants.SAVE_REMEMBER_ME_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSeeMetadata()) {
				roleFunctionalities.add(SpagoBIConstants.SEE_METADATA_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToSaveMetadata()) {
				roleFunctionalities.add(SpagoBIConstants.SAVE_METADATA_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToBuildQbeQuery()) {
				roleFunctionalities.add(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToDoMassiveExport()) {
				roleFunctionalities.add(SpagoBIConstants.DO_MASSIVE_EXPORT_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToEditWorksheet()) {
				roleFunctionalities.add(SpagoBIConstants.EDIT_WORKSHEET_FUNCTIONALITY);
			}
			if (virtualRole.isAbleToManageUsers()) {
				roleFunctionalities.add(SpagoBIConstants.FINAL_USERS_MANAGEMENT);
			}
			
			if (!roleFunctionalities.isEmpty()) {
				List<String> roleTypeFunctionalities = Arrays.asList(functionalities);
				roleFunctionalities.addAll(roleTypeFunctionalities);
				String[] a = new String[]{""};
				functionalities = roleFunctionalities.toArray(a);
			}
		    
		    return functionalities;
		} catch (Exception e) {
		    logger.error("Exception", e);
		    throw new RuntimeException("Error while loading functionalities", e);
		} finally {
		    logger.debug("OUT");
		}
		
    }
    
    public static String getUserId(HttpServletRequest req){
        logger.debug("IN");
        SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
        String userId = userProxy.readUserIdentifier(req);
        logger.debug("OUT,userId:"+userId);
        return userId;
    }
    
	private static Role getVirtualRole(String[] roles, String organization) throws Exception {
		logger.debug("IN");
		Role virtualRole = new Role("", "");
		virtualRole.setIsAbleToSaveSubobjects(false);
		virtualRole.setIsAbleToSeeSubobjects(false);
		virtualRole.setIsAbleToSeeSnapshots(false);
		virtualRole.setIsAbleToSeeViewpoints(false);
		virtualRole.setIsAbleToSeeMetadata(false);
		virtualRole.setIsAbleToSaveMetadata(false);
		virtualRole.setIsAbleToSendMail(false);
		virtualRole.setIsAbleToSeeNotes(false);
		virtualRole.setIsAbleToSaveRememberMe(false);
		virtualRole.setIsAbleToSaveIntoPersonalFolder(false);
		virtualRole.setIsAbleToBuildQbeQuery(false);
		virtualRole.setIsAbleToDoMassiveExport(false);
		virtualRole.setIsAbleToManageUsers(false);
		if (roles != null) {
			for (int i = 0; i < roles.length; i++) {
				String roleName = roles[i];
				logger.debug("RoleName="+roleName);
				IRoleDAO roleDAO = DAOFactory.getRoleDAO();
				roleDAO.setTenant(organization);
				Role anotherRole = roleDAO.loadByName(roleName);
				if (anotherRole!=null) { 
					if (anotherRole.isAbleToSaveSubobjects()) {
						logger.debug("User has role " + roleName + " that is able to save subobjects.");
						virtualRole.setIsAbleToSaveSubobjects(true);
					}
					if (anotherRole.isAbleToSeeSubobjects()) {
						logger.debug("User has role " + roleName + " that is able to see subobjects.");
						virtualRole.setIsAbleToSeeSubobjects(true);
					}
					if (anotherRole.isAbleToSeeViewpoints()) {
						logger.debug("User has role " + roleName + " that is able to see viewpoints.");
						virtualRole.setIsAbleToSeeViewpoints(true);
					}
					if (anotherRole.isAbleToSeeSnapshots()) {
						logger.debug("User has role " + roleName + " that is able to see snapshots.");
						virtualRole.setIsAbleToSeeSnapshots(true);
					}
					if (anotherRole.isAbleToSeeMetadata()) {
						logger.debug("User has role " + roleName + " that is able to see metadata.");
						virtualRole.setIsAbleToSeeMetadata(true);
					}
					if (anotherRole.isAbleToSaveMetadata()) {
						logger.debug("User has role " + roleName + " that is able to save metadata.");
						virtualRole.setIsAbleToSaveMetadata(true);
					}
					if (anotherRole.isAbleToSendMail()) {
						logger.debug("User has role " + roleName + " that is able to send mail.");
						virtualRole.setIsAbleToSendMail(true);
					}
					if (anotherRole.isAbleToSeeNotes()) {
						logger.debug("User has role " + roleName + " that is able to see notes.");
						virtualRole.setIsAbleToSeeNotes(true);
					}
					if (anotherRole.isAbleToSaveRememberMe()) {
						logger.debug("User has role " + roleName + " that is able to save remember me.");
						virtualRole.setIsAbleToSaveRememberMe(true);
					}
					if (anotherRole.isAbleToSaveIntoPersonalFolder()) {
						logger.debug("User has role " + roleName + " that is able to save into personal folder.");
						virtualRole.setIsAbleToSaveIntoPersonalFolder(true);
					}
					if (anotherRole.isAbleToBuildQbeQuery()) {
						logger.debug("User has role " + roleName + " that is able to build QBE queries.");
						virtualRole.setIsAbleToBuildQbeQuery(true);
					}
					if (anotherRole.isAbleToDoMassiveExport()) {
						logger.debug("User has role " + roleName + " that is able to do massive export.");
						virtualRole.setIsAbleToDoMassiveExport(true);
					}
					if (anotherRole.isAbleToEditWorksheet()) {
						logger.debug("User has role " + roleName + " that is able to edit worksheet documents.");
						virtualRole.setIsAbleToEditWorksheet(true);
					}
					if (anotherRole.isAbleToManageUsers()) {
						logger.debug("User has role " + roleName + " that is able to manage users.");
						virtualRole.setIsAbleToManageUsers(true);
					}
				}
			}
		}
		logger.debug("OUT");
		return virtualRole;
	}
	
	private static void checkTenant(SpagoBIUserProfile profile) {
		if (profile.getOrganization() == null) {
			logger.warn("User profile [" + profile.getUserId()
					+ "] has no organization/tenant set!!!");
			List<SbiTenant> tenants = DAOFactory.getTenantsDAO()
					.loadAllTenants();
			if (tenants == null || tenants.size() == 0) {
				throw new SpagoBIRuntimeException(
						"No tenants found on database");
			}
			if (tenants.size() > 1) {
				throw new SpagoBIRuntimeException(
						"Tenants are more than one, cannot associate input user profile ["
								+ profile.getUserId() + "] to a single tenant!!!");
			}
			SbiTenant tenant = tenants.get(0);
			logger.warn("Associating user profile ["
								+ profile.getUserId() + "] to tenant [" + tenant.getName() + "]");
			profile.setOrganization(tenant.getName());
		}
	}


}
