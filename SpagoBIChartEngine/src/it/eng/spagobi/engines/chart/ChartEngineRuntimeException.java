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

package it.eng.spagobi.engines.chart;

import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */

/**
 * The Class ChartEngineException.
 */
public class ChartEngineRuntimeException extends SpagoBIEngineRuntimeException {
    
	/** The hints. 
	List hints;
	*/
	
	ChartEngineInstance engineInstance;
	
	
	/**
	 * Builds a <code>ChartException</code>.
	 * 
	 * @param message Text of the exception
	 */
    public ChartEngineRuntimeException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>ChartEngineException</code>.
     * 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public ChartEngineRuntimeException(String message, Throwable ex) {
    	super(message, ex);
    }
    
    public ChartEngineInstance getEngineInstance() {
		return engineInstance;
	}

	public void setEngineInstance(ChartEngineInstance engineInstance) {
		this.engineInstance = engineInstance;
	}
    
   


}

