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
package it.eng.spagobi.services.content.service;


import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.util.HashMap;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class ContentServiceImpl extends AbstractServiceImpl{

    static private Logger logger = Logger.getLogger(ContentServiceImpl.class);
    

    /**
     * Instantiates a new content service impl.
     */
    public ContentServiceImpl(){
	super();
    }
    
    /**
     * Read template.
     * 
     * @param token the token
     * @param user the user
     * @param document the document
     * 
     * @return the content
     */
    public Content readTemplate(String token, String user, String document,HashMap attributes) {

	Monitor monitor =MonitorFactory.start("spagobi.service.content.readTemplate");
        logger.debug("IN");
        try {
            validateTicket(token,user);
            ContentServiceImplSupplier c=new ContentServiceImplSupplier();
	    return c.readTemplate(user, document, attributes);
	} catch (Exception e) {
	    logger.error("Exception",e); 
	    return null;
	}finally{
	    monitor.stop();
	    logger.debug("OUT");
	}        
    }

    /**
     * Read template by label.
     * 
     * @param token the token
     * @param user the user
     * @param document the document
     * 
     * @return the content
     */
    public Content readTemplateByLabel(String token, String user, String label, HashMap attributes) {

	Monitor monitor =MonitorFactory.start("spagobi.service.content.readTemplate");
        logger.debug("IN");
        try {
            validateTicket(token,user);
            ContentServiceImplSupplier c=new ContentServiceImplSupplier();
	    return c.readTemplateByLabel(user, label, attributes);
	} catch (Exception e) {
	    logger.error("Exception",e); 
	    return null;
	}finally{
	    monitor.stop();
	    logger.debug("OUT");
	}        
    }

    
    /**
     * Read sub object content.
     * 
     * @param token the token
     * @param user the user
     * @param subObjectId the sub object id
     * 
     * @return the content
     */
    public Content readSubObjectContent(String token,String user,String subObjectId){
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.content.readSubObjectContent");
        try {
            validateTicket(token,user);
            return readSubObjectContent(user,subObjectId);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    monitor.stop();	    
	    logger.debug("OUT");
	} 

    }
    /**
     * Read sub object content.
     * 
     * @param token the token
     * @param user the user
     * @param subObjectName the sub object name
     * @param objId the object id
     * 
     * @return the content
     */
    public Content readSubObjectContent(String token,String user,String subObjectName, Integer objId){
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.content.readSubObjectContent");
        try {
            validateTicket(token,user);
            return readSubObjectContent(user, subObjectName, objId);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    monitor.stop();	    
	    logger.debug("OUT");
	} 

    } 
    /**
     * Save sub object.
     * 
     * @param token the token
     * @param user the user
     * @param documentiId the documenti id
     * @param analysisName the analysis name
     * @param analysisDescription the analysis description
     * @param visibilityBoolean the visibility boolean
     * @param content the content
     * 
     * @return the string
     */
    public String saveSubObject(String token,String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.content.saveSubObject");
        try {
            validateTicket(token,user);
            IEngUserProfile profile = GeneralUtilities.createNewUserProfile(user);
            if (!profile.getFunctionalities().contains(SpagoBIConstants.SAVE_SUBOBJECT_FUNCTIONALITY)) {
            	logger.debug("KO - User " + user + " cannot save subobjects");
            	return "KO - You cannot save subobjects";
            }
            String userId = ((UserProfile)profile).getUserId().toString();
            //return saveSubObject(user,documentiId,analysisName,analysisDescription,visibilityBoolean,content);
            return saveSubObject(userId,documentiId,analysisName,analysisDescription,visibilityBoolean,content);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	} catch (Exception e) {
	    logger.error(e);
	    return null;
	}finally{
	    monitor.stop();	   
	    logger.debug("OUT");
	}	

    }

	/**
     * Save object template.
     * 
     * @param token the token
     * @param user the user
     * @param documentiId the documenti id
     * @param templateName the template name
     * @param content the content
     * 
     * @return the string
     */
    public String saveObjectTemplate(String token,String user,String documentiId,String templateName,String content){
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.content.saveObjectTemplate");
        try {
            validateTicket(token,user);
            return saveObjectTemplate(user,documentiId,templateName,content);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return null;
	}finally{
	    monitor.stop();	   
	    logger.debug("OUT");
	}

    }
    
    /**
     * Download all.
     * 
     * @param token the token
     * @param user the user
     * @param biobjectId the biobject id
     * @param fileName the file name
     * 
     * @return the content
     */
    public Content downloadAll(String token,String user,String biobjectId,String fileName){
	return null;
    }
    
    // PRIVATE METHOD
    
    private Content readSubObjectContent(String user,String subObjectId){
    	logger.debug("IN");
    	Content content=new Content();
    	try {
    	    Integer id = new Integer(subObjectId);
    	    ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
    	    SubObject subobj = subdao.getSubObject(id);
    	    byte[] cont = subobj.getContent();
    	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
    	    content.setContent(bASE64Encoder.encode(cont));
    	    content.setFileName(subobj.getName());
    	    return content;
    	} catch (NumberFormatException e) {
    	    logger.error("NumberFormatException",e);
    	} catch (EMFUserError e) {
    	    logger.error("EMFUserError",e);
    	} catch (EMFInternalError e) {
    		logger.error("EMFInternalError",e);
		} 
    	logger.debug("OUT");
    	return null;	
    }
  
    private Content readSubObjectContent(String user,String subObjectName, Integer objId){
    	logger.debug("IN");
    	Content content=new Content();
    	try {
    	    ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
    	    SubObject subobj = subdao.getSubObjectByNameAndBIObjectId(subObjectName, objId);
    	    byte[] cont = subobj.getContent();
    	    BASE64Encoder bASE64Encoder = new BASE64Encoder();
    	    content.setContent(bASE64Encoder.encode(cont));
    	    content.setFileName(subobj.getName());
    	    return content;
    	} catch (NumberFormatException e) {
    	    logger.error("NumberFormatException",e);
    	} catch (EMFUserError e) {
    	    logger.error("EMFUserError",e);
    	} catch (EMFInternalError e) {
    		logger.error("EMFInternalError",e);
		} 
    	logger.debug("OUT");
    	return null;	
    }
    
    private String saveSubObject(String user,String documentiId,String analysisName,String analysisDescription,String visibilityBoolean,String content){
	logger.debug("IN");
	try {
	    ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
	    subdao.setUserID(user);
	    Integer docId = new Integer(documentiId);
	    boolean subobjExists = false;
	    
	    //gets subobj if yet presents
	    Integer id = null;
	    SubObject objSub = subdao.getSubObjectByNameAndBIObjectId(analysisName, docId);
	    if (objSub != null){
	    	id = objSub.getId();
	    	//check ability to modify:
		    if (!user.equals(objSub.getOwner())) {
	        	logger.debug("KO - User " + user + " cannot modify subobjects");
	        	return "KO - You cannot modify subobjects";
	        }
	    	subobjExists = true;
	    }
	    else 
	    	objSub = new SubObject();
	    
	    objSub.setDescription(analysisDescription);
	    
	    if (visibilityBoolean!=null && visibilityBoolean.equals("true")){
	    	objSub.setIsPublic(new Boolean(true));
	    }else{
	    	objSub.setIsPublic(new Boolean(false));
	    }
	    objSub.setOwner(user);
	    objSub.setName(analysisName);
	    objSub.setContent(content.getBytes());
	    
	    //if subobject doesn't exist, it will be created
	    if (!subobjExists)
	    	id = subdao.saveSubObject(docId, objSub);
	    else 
	    	//update the subobject
	    	id = subdao.modifySubObject(docId, objSub);;
	    
	    String toReturn = "OK - " + id.toString();
	    return toReturn;
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException",e);
	    return "KO";
	} catch (EMFUserError e) {
	    logger.error("EMFUserError",e);
	    return "KO";
	}finally{
	    logger.debug("OUT");
	}
    }
    
    private String saveObjectTemplate(String user,String documentiId,String templateName,String content){
	logger.debug("IN");
	try {
	    IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
	    objdao.setUserID(user);
	    Integer docId = new Integer(documentiId);
	    BIObject biobj = objdao.loadBIObjectById(docId);
	    ObjTemplate objTemp = new ObjTemplate();
	    objTemp.setBiobjId(biobj.getId());
	    objTemp.setActive(new Boolean(true));
	    objTemp.setContent(content.getBytes());
	    objTemp.setName(templateName);
	    objdao.modifyBIObject(biobj, objTemp);
	} catch (NumberFormatException e) {
	    logger.error("NumberFormatException",e);
	    return "KO";
	} catch (EMFUserError e) {
	    logger.error("EMFUserError",e);
	    return "KO";
	}finally{
	    logger.debug("OUT");
	}
	return "OK";
    }
    
    private Content downloadAll(String user,String biobjectId,String fileName){
	return null;
    }    
    
    


    

    
    
    
    
    
}
