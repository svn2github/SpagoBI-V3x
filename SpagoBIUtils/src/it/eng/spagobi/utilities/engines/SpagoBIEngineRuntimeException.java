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

package it.eng.spagobi.utilities.engines;

import java.util.ArrayList;
import java.util.List;

public class SpagoBIEngineRuntimeException extends RuntimeException {
    
	/* 
	 * User oriented description of the exception. It is usually prompted to the user.
	 * Instead the message passed to the constructor is developer oriented and it should be just logged. 
	 */
	private String description;
	
	/*
	 * A list of possible solutions to the problem that have caused the exception
	 */
	private List hints;
	
	private IEngineInstance engineInstance;
	
	/**
	 * Builds a <code>SpagoBIRuntimeException</code>.
	 * 
	 * @param message Text of the exception
	 */
    public SpagoBIEngineRuntimeException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>SpagoBIRuntimeException</code>.
     * 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public SpagoBIEngineRuntimeException(String message, Throwable ex) {
    	super(message, ex);
    }
    
    /**
     * Builds a <code>SpagoBIRuntimeException</code>.
     * 
     * @param ex previous Throwable object
     */
    public SpagoBIEngineRuntimeException(Throwable ex) {
    	super(ex);
    }
    
    public Throwable getRootException() {
    	Throwable rootException;
		
		rootException = this;
		while(rootException.getCause() != null) {
			rootException = rootException.getCause();
		}
		
		return rootException;
    }
    
    public String getRootCause() {
		String rootCause;		
		Throwable rootException;
		
		rootException = getRootException();
		
		rootCause = rootException.getMessage()!=null
			? rootException.getMessage()
			: rootException.getClass().getName();
		
		return rootCause;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHints(List hints) {
		this.hints = hints;
	}
	
	public List getHints() {
		if(hints == null) {
			hints = new ArrayList();
		}
		return hints;
	}

	public void addHint(String hint) {
		getHints().add(hint);
	}

	public IEngineInstance getEngineInstance() {
		return engineInstance;
	}

	public void setEngineInstance(IEngineInstance engineInstance) {
		this.engineInstance = engineInstance;
	}

}

