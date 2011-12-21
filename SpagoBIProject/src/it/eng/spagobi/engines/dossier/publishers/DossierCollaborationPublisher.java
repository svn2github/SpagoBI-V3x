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

package it.eng.spagobi.engines.dossier.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.dossier.constants.DossierConstants;

public class DossierCollaborationPublisher implements PublisherDispatcherIFace {

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
		// GET THE MODULE RESPONSE
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		SourceBean serviceResp = responseContainer.getServiceResponse();
		SourceBean moduleResponse = (SourceBean)serviceResp.getAttribute(DossierConstants.DOSSIER_COLLABORATION_MODULE);
		if(moduleResponse==null) {
			SpagoBITracer.major(DossierConstants.NAME_MODULE, this.getClass().getName(), 
					            "getPublisherName", "Module response null");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_dossier_messages");
			errorHandler.addError(error);
			return "error";
		}
		// GET THE PUBLISHER NAME
		String pubName = (String)moduleResponse.getAttribute(DossierConstants.PUBLISHER_NAME);
		if((pubName==null) || pubName.trim().equals("")){
			SpagoBITracer.major(DossierConstants.NAME_MODULE, this.getClass().getName(), 
		                        "getPublisherName", "attribute "+DossierConstants.PUBLISHER_NAME+" " +
		                        "not found in response or empty");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_dossier_messages");
			errorHandler.addError(error);
			return "error";
		}
		// RETURN PUBLISHER NAME OR ERROR PUBLISHER
		if(errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			return pubName;
		} else {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
				return pubName;
			} else {
				return new String("error");
			}
		}
	}

}
