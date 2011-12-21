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

package it.eng.spagobi.services.sbidocument.stub;

import it.eng.spagobi.services.sbidocument.service.SbiDocumentServiceImpl;

public class SbiDocumentServiceSoapBindingImpl implements it.eng.spagobi.services.sbidocument.stub.SbiDocumentService{
    public it.eng.spagobi.services.sbidocument.bo.SpagobiAnalyticalDriver[] getDocumentAnalyticalDrivers(java.lang.String in0, java.lang.String in1, java.lang.Integer in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException {
        SbiDocumentServiceImpl impl = new SbiDocumentServiceImpl();
        return impl.getDocumentAnalyticalDrivers(in0, in1, in2, in3, in4);
    }

    public java.lang.String getDocumentAnalyticalDriversJSON(java.lang.String in0, java.lang.String in1, java.lang.Integer in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException {
        SbiDocumentServiceImpl impl = new SbiDocumentServiceImpl();
        return impl.getDocumentAnalyticalDriversJSON(in0, in1, in2, in3, in4);
    }

}
