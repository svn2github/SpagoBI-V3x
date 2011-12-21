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
package it.eng.spagobi.utilities.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.SingletonConfig;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;

public class ScriptManager {

	static private Logger logger = Logger.getLogger(ScriptManager.class);

	private String predefinedScriptFileName = null;
	private String predefinedGroovyScriptFileName = null;
	private String predefinedJsScriptFileName = null;

	private String scriptLanguage = null;
	private String defaultEngine = null;



	public ScriptManager() {
	}

	/**
	 * Run a script. (Deprecated)
	 * 
	 * @param script the script to run
	 * @param bind the bindings for script variables
	 * 
	 * @return the result of the script
	 * 
	 * @throws Exception the exception
	 */
	public String runScript(String script, Binding bind) throws Exception {
		String result = run(script, bind);
		return result;
	}

	public String getPredefinedGroovyScriptFileName() {
		return predefinedGroovyScriptFileName;
	}

	public void setPredefinedGroovyScriptFileName(
			String predefinedGroovyScriptFileName) {
		this.predefinedGroovyScriptFileName = predefinedGroovyScriptFileName;
	}

	public String getPredefinedJsScriptFileName() {
		return predefinedJsScriptFileName;
	}

	public void setPredefinedJsScriptFileName(String predefinedJsScriptFileName) {
		this.predefinedJsScriptFileName = predefinedJsScriptFileName;
	}


	
	/**
	 * Run a script. (Deprecated)
	 * 
	 * @param script the script to run
	 * 
	 * @return the result of the script
	 * 
	 * @throws Exception the exception
	 */
	public String runScript(String script) throws Exception {
		String result = run(script, null);
		return result;
	}



	/**
	 * Run a script.
	 * 
	 * @param script the script to run
	 * 
	 * @return the result of the script
	 * 
	 * @throws Exception the exception
	 */
	public String runScript(String script, String languageScript) throws Exception {
		String result = run(script, null, languageScript);
		return result;
	}

	/**
	 * Run a script.
	 * 
	 * @param script the script to run
	 * @param bind 
	 * @param languageScript the language of the script to run
	 * 
	 * @return the result of the script
	 * 
	 * @throws Exception the exception
	 */
	public String runScript(String script, Binding bind, String languageScript) throws Exception {
		String result = run(script, bind, languageScript);
		return result;
	}


	/**
	 * oLD METHOD RUNNING a script
	 * @param script the script to run 
	 * @param bind the bindings for script variables
	 * @return the result of the script
	 * @throws Exception
	 */
	private String run(String script, Binding bind) throws Exception {
		String result = "";
		if(defaultEngine==null || defaultEngine.equals("")){
			defaultEngine =SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE_DEFAULT");
		}
		
		// get the name of the default script language
		if(scriptLanguage==null || scriptLanguage.equals("")){
			scriptLanguage = SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE."+defaultEngine+".name");
		}
		
		// the only language supported now is groovy so if the default script isn't groovy
		// throw an exception and return an empty string
		if(!scriptLanguage.equalsIgnoreCase("groovy")) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
					"run", "The only script language supported is groovy, " +
			"the configuration file has no configuration for groovy");
			return "";
		}
		// load predefined script file
		if(predefinedScriptFileName==null || predefinedScriptFileName.equals("")){
			predefinedScriptFileName = SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE."+defaultEngine+".predefinedScriptFile");
		}
		
		if(predefinedScriptFileName != null && !predefinedScriptFileName.trim().equals("")) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
					"run", "Trying to load predefined script file '" + predefinedScriptFileName + "'.");
			InputStream is = null;
			try {
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(predefinedScriptFileName);
				StringBuffer servbuf = new StringBuffer();
				int arrayLength = 1024;
				byte[] bufferbyte = new byte[arrayLength];
				char[] bufferchar = new char[arrayLength];
				int len;
				while ((len = is.read(bufferbyte)) >= 0) {
					for (int i = 0; i < arrayLength; i++) {
						bufferchar[i] = (char) bufferbyte[i];
					}
					servbuf.append(bufferchar, 0, len);
				}
				is.close();
				script = servbuf.toString() + script;
			} catch (Exception e) {
				SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
						"run", "The predefined script file '" + predefinedScriptFileName + "' was not properly loaded.");
			} finally {
				if (is != null) is.close();
			}
		}

		// create shell instance
		GroovyShell shell = null;
		if(bind==null) {
			shell = new GroovyShell();
		} else {
			shell = new GroovyShell(bind);
		}
		// execute the script
		Object value = shell.evaluate(script);
		result = value.toString();
		// return the result
		return result;
	} 


	public String getPredefinedScriptFileName() {
		return predefinedScriptFileName;
	}

	public void setPredefinedScriptFileName(String predefinedScriptFileName) {
		this.predefinedScriptFileName = predefinedScriptFileName;
	}

	public String getScriptLanguage() {
		return scriptLanguage;
	}

	public void setScriptLanguage(String scriptLanguage) {
		this.scriptLanguage = scriptLanguage;
	}

	/**
	 * Fill a groovy binding with attributes of an hashmap.
	 * 
	 * @param attrs Map of attibutes to load into binding
	 * 
	 * @return the groovy binding object
	 */
	public static Binding fillBinding(HashMap attrs) {
		Binding bind = new Binding();
		Set setattrs = attrs.keySet();
		Iterator iterattrs = setattrs.iterator();
		String key = null;
		Object value = null;
		while(iterattrs.hasNext()) {
			key = iterattrs.next().toString();
			value = attrs.get(key);
			if (value != null) {
				bind.setVariable(key, value);
			} else {
				logger.warn("Variable [" + key + "] is null!!!");
			}
		}
		return bind;
	}

	/**
	 * Fill a groovy binding with attributes of a user profile.
	 * 
	 * @param profile the profile
	 * 
	 * @return the groovy binding object
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
	public static Binding fillBinding(IEngUserProfile profile) throws EMFInternalError {
		HashMap allAttrs = ScriptUtilities.getAllProfileAttributes(profile);
		if (allAttrs == null) return null;
		return fillBinding(allAttrs);
	}






	/**
	 * Run a script specifying the language
	 * @param script the script to run 
	 * @param bind the bindings for script variables
	 * @param languageScript language of the script
	 * @return the result of the script
	 * @throws Exception
	 */
	private String run(String script, Binding bind, String languageScript) throws Exception {
		logger.debug("IN");
		String result = "";

		ScriptEngineManager scriptManager = new ScriptEngineManager();

		ScriptEngine scriptEngine = scriptManager.getEngineByName(languageScript);

		// if groouvy  TODO: unify the add of utilities file among all languages
		if(languageScript.equalsIgnoreCase("groovy")){
			script=addGroovyPredefined(script);
		}
		if(languageScript.equalsIgnoreCase("ejs") || languageScript.equalsIgnoreCase("rhino-nonjdk")){
			script=addJavascriptPredefined(script);
		}		

		if(scriptEngine!=null){
			logger.debug("Found engine "+scriptEngine.NAME);
			Object returning=scriptEngine.eval(script);
			if(returning!=null){
				logger.debug("Result found");
				result=returning.toString();
			}
			else{
				logger.error("Result not found");
			}
		}else {
			logger.error("Could not find engine for alias: "+languageScript);

		}
		logger.debug("OUT");
		return result;
	}



	 public String addGroovyPredefined(String script) throws IOException{

		// load predefined script file
		if(predefinedGroovyScriptFileName==null || predefinedGroovyScriptFileName.equals("")){
			predefinedGroovyScriptFileName = SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE.groovy.predefinedScriptFile");
		}
		if(predefinedGroovyScriptFileName != null && !predefinedGroovyScriptFileName.trim().equals("")) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
					"run", "Trying to load predefined script file '" + predefinedGroovyScriptFileName + "'.");
			InputStream is = null;
			try {
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(predefinedGroovyScriptFileName);
				StringBuffer servbuf = new StringBuffer();
				int arrayLength = 1024;
				byte[] bufferbyte = new byte[arrayLength];
				char[] bufferchar = new char[arrayLength];
				int len;
				while ((len = is.read(bufferbyte)) >= 0) {
					for (int i = 0; i < arrayLength; i++) {
						bufferchar[i] = (char) bufferbyte[i];
					}
					servbuf.append(bufferchar, 0, len);
				}
				is.close();
				script = servbuf.toString() + script;
			} catch (Exception e) {
				SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
						"run", "The predefined script file '" + predefinedGroovyScriptFileName + "' was not properly loaded.");
			} finally {
				if (is != null) is.close();
			}
		}
	return script;
	}



	public String addJavascriptPredefined(String script) throws IOException{

		// load predefined script file
		if(predefinedJsScriptFileName==null || predefinedJsScriptFileName.equals("")){
			predefinedJsScriptFileName = SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE.javascript.predefinedScriptFile");
		}
		if(predefinedJsScriptFileName != null && !predefinedJsScriptFileName.trim().equals("")) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
					"run", "Trying to load predefined script file '" + predefinedJsScriptFileName + "'.");
			InputStream is = null;
			try {
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(predefinedJsScriptFileName);
				StringBuffer servbuf = new StringBuffer();
				int arrayLength = 1024;
				byte[] bufferbyte = new byte[arrayLength];
				char[] bufferchar = new char[arrayLength];
				int len;
				while ((len = is.read(bufferbyte)) >= 0) {
					for (int i = 0; i < arrayLength; i++) {
						bufferchar[i] = (char) bufferbyte[i];
					}
					servbuf.append(bufferchar, 0, len);
				}
				is.close();
				script = servbuf.toString() + script;
			} catch (Exception e) {
				SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, ScriptManager.class.getName(), 
						"run", "The predefined script file '" + predefinedJsScriptFileName + "' was not properly loaded.");
			} finally {
				if (is != null) is.close();
			}
		}
	return script;
	}
	


}




/*
 * implementation with bsf, seems not possible to laucha groovy expression with Bindings
 * so we use groovy directly
 * 
	String name = (String)scriptLangSB.getAttribute("name");
	String engclass = (String)scriptLangSB.getAttribute("engineclass");
	String id = (String)scriptLangSB.getAttribute("identifier");
	String shortid = (String)scriptLangSB.getAttribute("shortidentifier");
	BSFManager.registerScriptingEngine(name, engclass, new String[] { id, shortid }	);
    BSFManager manager = new BSFManager(); 
    try {
    	Object answer = manager.eval(name, "Test1.groovy", 0, 0, script);
    	result = answer.toString();
    } catch (BSFException e1) {
    	e1.printStackTrace();
    }
 */	
