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

package it.eng.spagobi.commons.presentation;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.presentation.PublisherDispatcherIFace;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
/**
 * A particular publisher used to save configuration.
 * 
 * @author sulis
 */
public class SaveConfigurationPublisher implements PublisherDispatcherIFace {

	/**
	 * Given the request at input, gets the name of the reference publisher,driving
	 * the execution into the correct jsp page, or jsp error page, if any error occurred.
	 * 
	 * @param requestContainer The object containing all request information
	 * @param responseContainer The object containing all response information
	 * 
	 * @return A string representing the name of the correct publisher, which will
	 * call the correct jsp reference.
	 */
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {

		//SourceBean serviceRequest = requestContainer.getServiceRequest();
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		if (errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			PortletRequest portletRequest = PortletAccess.getPortletRequest();
			PortletMode mode = portletRequest.getPortletMode(); 
			if (PortletMode.EDIT.equals(mode)) return "saveConfiguration";
			if (PortletMode.HELP.equals(mode)) return "saveConfiguration";
			else return "saveConfigurationLoop";
		}
		else
			return new String("error");
	}

}
