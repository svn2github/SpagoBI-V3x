/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.talend;

import it.eng.spagobi.utilities.engines.EngineVersion;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TalendEngineVersion extends EngineVersion {
	
	public static final String ENGINE_NAME = "SpagoBITalendEngine";
    public static final String AUTHOR = "Engineering Ingegneria Informatica S.p.a.";
    public static final String WEB = "http://spagobi.eng.it/";
    
    public static final String MAJOR = "2";
    public static final String MINOR = "0";
    public static final String REVISION = "0";
    public static final String CODENAME = "Stable";
    
    public static final String CLIENT_COMPLIANCE_VERSION = "0.5.0";
    
	
	private static TalendEngineVersion instance;
	
	public static TalendEngineVersion getInstance() {
		if(instance == null) {
			instance = new TalendEngineVersion(MAJOR, MINOR, REVISION, CODENAME);
		}
		
		return instance;
	}
	
	private TalendEngineVersion(String major, String minor, String revision, String codename) {
		super(major, minor, revision, codename);
	}
	
	
	public String getFullName() {
		return ENGINE_NAME + "-" + this.toString();
	}
	    
	
	public String getInfo() {
		return getFullName() + " [ " + WEB +" ]";
	}
	
	/**
	 * Gets the compliance version.
	 * 
	 * @return the compliance version
	 */
	public String getComplianceVersion() {
		return CLIENT_COMPLIANCE_VERSION;
	}

}
