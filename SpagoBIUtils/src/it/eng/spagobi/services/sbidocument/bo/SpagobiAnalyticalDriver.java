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

package it.eng.spagobi.services.sbidocument.bo;

public class SpagobiAnalyticalDriver  implements java.io.Serializable {


    private java.lang.Integer id;

    private java.lang.String label;

    private java.lang.String type;

    private java.lang.String urlName;

    private java.lang.Object[] values;

    public SpagobiAnalyticalDriver() {
    }

    

    /**
     * Gets the id value for this SDKDocumentParameter.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this SDKDocumentParameter.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the label value for this SDKDocumentParameter.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this SDKDocumentParameter.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the type value for this SDKDocumentParameter.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this SDKDocumentParameter.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the urlName value for this SDKDocumentParameter.
     * 
     * @return urlName
     */
    public java.lang.String getUrlName() {
        return urlName;
    }


    /**
     * Sets the urlName value for this SDKDocumentParameter.
     * 
     * @param urlName
     */
    public void setUrlName(java.lang.String urlName) {
        this.urlName = urlName;
    }


    /**
     * Gets the values value for this SDKDocumentParameter.
     * 
     * @return values
     */
    public java.lang.Object[] getValues() {
        return values;
    }


    /**
     * Sets the values value for this SDKDocumentParameter.
     * 
     * @param values
     */
    public void setValues(java.lang.Object[] values) {
        this.values = values;
    }



	public SpagobiAnalyticalDriver(Integer id, String label, String type,
			String urlName, Object[] values) {
		super();
		this.id = id;
		this.label = label;
		this.type = type;
		this.urlName = urlName;
		this.values = values;
	}
    
    
    
    
    
}
