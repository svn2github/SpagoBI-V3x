package it.eng.spagobi.tools.dataset.common.dataproxy;

import java.util.Map;

public abstract class AbstractDataProxy implements IDataProxy {

	Map parameters;
	Map profile;
	int offset;
	int fetchSize;
	int maxResults;
	boolean calculateResultNumberOnLoad;
	String statement;
	String resPath;
	
	String predefinedGroovyScriptFileName = null;
	String predefinedJsScriptFileName = null;

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

	public String getResPath() {
		return resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public Map getProfile() {
		return profile;
	}

	public void setProfile(Map profile) {
		this.profile = profile;
	}

	public boolean isPaginationSupported() {
		return isOffsetSupported() && isMaxResultsSupported();
	}
	
	public boolean isOffsetSupported() {
		return false;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isFetchSizeSupported() {
		return false;
	}
	
	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public boolean isMaxResultsSupported() {
		return true;
	}
	
	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	public boolean isCalculateResultNumberOnLoadEnabled() {
		return calculateResultNumberOnLoad;
	}
	
	public void setCalculateResultNumberOnLoad(boolean enabled) {
		calculateResultNumberOnLoad = enabled;
	}
	
	public long getResultNumber() {
		return -1;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
}
