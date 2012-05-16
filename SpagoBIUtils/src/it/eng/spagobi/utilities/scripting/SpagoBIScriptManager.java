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

import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;

public class SpagoBIScriptManager {

	static private Logger logger = Logger.getLogger(SpagoBIScriptManager.class);

	
	public Object runScript(String script, String language) {
		return runScript(script, language, null, null);
	}
	
	public Object runScript(String script, String language, Map<String, Object> bindings)  {
		return runScript(script, language, bindings, null);
	}

	
	public Object runScript(String script, String language, Map<String, Object> bindings, List<File> imports) {
		
		Object results;
		
		logger.debug("IN");
		
		results = null;
		try {
			ScriptEngine scriptEngine = getScriptEngine(language);
			
			if(scriptEngine == null) {
				throw new RuntimeException("No engine available to execute scripts of type [" + language + "]");
			} else {
				logger.debug("Found engine [" + scriptEngine.NAME + "]");
			}
	
			if(imports != null) {
				StringBuffer importsBuffer = new StringBuffer();
				for(File scriptFile: imports) {
					importsBuffer.append(this.getImportedScript(scriptFile) + "\n");
				}
				script = importsBuffer.toString() + script;
			}
			
			ScriptContext scriptContext = new SimpleScriptContext();
			if(bindings != null) {
				Bindings scriptBindings = new SimpleBindings(bindings);
				scriptContext.setBindings(scriptBindings, ScriptContext.ENGINE_SCOPE);
			}			
			scriptEngine.setContext(scriptContext);
			
			results = scriptEngine.eval(script);
		} catch(Throwable t) {
			throw new SpagoBIRuntimeException("An unexpected error occured while executing script", t);
		} finally {
			logger.debug("OUT");
		}
		
		return results;
	}

	public boolean isEngineSupported(String name) {
		return getScriptEngine(name) != null;
	}

	private ScriptEngine getScriptEngine(String name) {
		ScriptEngine scriptEngine;
		scriptEngine = null;
		scriptEngine = getScriptEngineByLanguage(name);
		if(scriptEngine == null) {
			scriptEngine = getScriptEngineByName(name);
		}
		return scriptEngine;
	} 
	
	private ScriptEngine getScriptEngineByLanguage(String language) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		List<ScriptEngineFactory> scriptEngineFactories = scriptEngineManager.getEngineFactories();
	
		for (ScriptEngineFactory scriptEngineFactory: scriptEngineFactories) {
			if(scriptEngineFactory.getLanguageName().equals(language)) {
				return scriptEngineFactory.getScriptEngine();
			}
		}
		return null;
	} 
	
	private ScriptEngine getScriptEngineByName(String name) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		List<ScriptEngineFactory> scriptEngineFactories = scriptEngineManager.getEngineFactories();
	
		for (ScriptEngineFactory scriptEngineFactory: scriptEngineFactories) {
			if(scriptEngineFactory.getNames().contains(name)) {
				return scriptEngineFactory.getScriptEngine();
			}
		}
		return null;
	} 
	
	
	
	
	/**
	 * @return A list containing the names of all scripting languages supported
	 */
	public Set<String> getSupportedLanguages() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		List<ScriptEngineFactory> scriptEngineFactories = scriptEngineManager.getEngineFactories();
		
		Set<String> languages = new HashSet<String>();
		for (ScriptEngineFactory scriptEngineFactory: scriptEngineFactories) {
			languages.add( scriptEngineFactory.getLanguageName() );
		}
		return languages;
	}
	
	/**
	 * @return A list containing the short names of all scripting engines supported. An engie can have
	 * multiple names so in general the number of engine names is greather than the number of actual
	 * engines registered into the platform
	 */
	public Set<String> getSuportedEngineNames() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		List<ScriptEngineFactory> scriptEngineFactories = scriptEngineManager.getEngineFactories();
		
		Set<String> engineNames = new HashSet<String>();
		for (ScriptEngineFactory scriptEngineFactory: scriptEngineFactories) {
			engineNames.addAll( scriptEngineFactory.getNames() );
		}
		return engineNames;
	}
	
	
	// load predefined script file
	//	if(predefinedJsScriptFileName==null || predefinedJsScriptFileName.equals("")){
	//		predefinedJsScriptFileName = SingletonConfig.getInstance().getConfigValue("SCRIPT_LANGUAGE.javascript.predefinedScriptFile");
	//	}
	// InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(predefinedJsScriptFileName);
	
	private String getImportedScript(File scriptFile) {
		String importedScript;
		InputStream is;
		
		importedScript = null;
		is = null;
		try {	
			is = new FileInputStream(scriptFile);
			importedScript = getImportedScript(is);
		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("An unexpected error occured while importing script from file [" + scriptFile + "]", t);	
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException t) {
					logger.warn("Impossible to close inpust stream associated to file [" + scriptFile + "]", t);
				}
			}
		}
		
		return importedScript;
	}

	private String getImportedScript(InputStream is) {
		String importedScript;
		
		importedScript = null;
		
		try {
			StringBuffer buffer = new StringBuffer();
			int arrayLength = 1024;
			byte[] bufferbyte = new byte[arrayLength];
			char[] bufferchar = new char[arrayLength];
			int len;
			while ((len = is.read(bufferbyte)) >= 0) {
				for (int i = 0; i < arrayLength; i++) {
					bufferchar[i] = (char) bufferbyte[i];
				}
				buffer.append(bufferchar, 0, len);
			}
			importedScript = buffer.toString();
		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("An unexpected error occured while importing script from stream", t);	
		} 		
	
		return importedScript;
	}
	
	public void printInfo() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		List<ScriptEngineFactory> factories = mgr.getEngineFactories();

		for (ScriptEngineFactory factory: factories) {
			System.out.println("ScriptEngineFactory Info");
			String engName = factory.getEngineName();
			String engVersion = factory.getEngineVersion();
			String langName = factory.getLanguageName();
			String langVersion = factory.getLanguageVersion();
			System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);
			List<String> engNames = factory.getNames();
			for(String name: engNames) {
				System.out.printf("\tEngine Alias: %s\n", name);
			}
			System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
		}   
	}
}
