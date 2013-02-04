/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.behaviouralmodel.lov.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.behaviour.UserProfileUtils;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class DatasetDetail implements ILovDetail {

	private static transient Logger logger = Logger.getLogger(DatasetDetail.class);

	private List visibleColumnNames = null;
	private String valueColumnName = "";
	private String descriptionColumnName = "";
	private List invisibleColumnNames = null;
	private List treeLevelsColumns = null;
	private String lovType = "simple";
	
	private String datasetId;
	private String datasetLabel;

	public DatasetDetail() {}
	
	/**
	 * constructor.
	 * 
	 * @param dataDefinition xml representation of the script lov
	 * 
	 * @throws SourceBeanException the source bean exception
	 */
	public DatasetDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML (dataDefinition);
	}
	
	/**
	 * @return the datasetId
	 */
	public String getDatasetId() {
		return datasetId;
	}


	/**
	 * @param datasetId the datasetId to set
	 */
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	
	/**
	 * @return the datasetLabel
	 */
	public String getDatasetLabel() {
		return datasetLabel;
	}


	/**
	 * @param datasetLabel the datasetLabel to set
	 */
	public void setDatasetLabel(String datasetLabel) {
		this.datasetLabel = datasetLabel;
	}



	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#toXML()
	 */
	public String toXML() {
		String XML = "<DATASET>" +
		"<ID>"+this.getDatasetId()+"</ID>" +
		"<LABEL>"+this.getDatasetLabel() + "</LABEL>" +
		"<VALUE-COLUMN>"+this.getValueColumnName()+"</VALUE-COLUMN>" +
		"<DESCRIPTION-COLUMN>"+this.getDescriptionColumnName()+"</DESCRIPTION-COLUMN>" +
		"<VISIBLE-COLUMNS>"+GeneralUtilities.fromListToString(this.getVisibleColumnNames(), ",")+"</VISIBLE-COLUMNS>" +
		"<INVISIBLE-COLUMNS>"+GeneralUtilities.fromListToString(this.getInvisibleColumnNames(), ",")+"</INVISIBLE-COLUMNS>" +
		"<LOVTYPE>"+this.getLovType() + "</LOVTYPE>" +
		"<TREE-LEVELS-COLUMNS>"+GeneralUtilities.fromListToString(this.getTreeLevelsColumns(), ",")+"</TREE-LEVELS-COLUMNS>" +
		"</DATASET>";
		return XML;
	}

	/**
	 * loads the lov from an xml string.
	 * 
	 * @param dataDefinition the xml definition of the lov
	 * 
	 * @throws SourceBeanException the source bean exception
	 */
	public void loadFromXML(String dataDefinition) throws SourceBeanException {
		logger.debug("IN");
		dataDefinition.trim();
		if(dataDefinition.indexOf("<ID>")!=-1) {
			int startInd = dataDefinition.indexOf("<ID>");
			int endId = dataDefinition.indexOf("</ID>");
			String dataset = dataDefinition.substring(startInd + 6, endId);
			dataset =dataset.trim();
			if(!dataset.startsWith("<![CDATA[")) {
				dataset = "<![CDATA[" + dataset  +  "]]>";
				dataDefinition = dataDefinition.substring(0, startInd+6) + dataset + dataDefinition.substring(endId); 
			}
		}

		
		
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		SourceBean idBean = (SourceBean)source.getAttribute("ID"); 
		String id =  idBean.getCharacters(); 
		SourceBean labelBean = (SourceBean)source.getAttribute("LABEL");
		String label = labelBean.getCharacters();
		SourceBean valCol = (SourceBean)source.getAttribute("VALUE-COLUMN");
		String valueColumn = valCol.getCharacters();
		SourceBean visCol = (SourceBean)source.getAttribute("VISIBLE-COLUMNS");
		String visibleColumns = visCol.getCharacters();
		SourceBean invisCol = (SourceBean)source.getAttribute("INVISIBLE-COLUMNS");
		String invisibleColumns = "";
		// compatibility control (versions till 1.9RC does not have invisible columns definition)
		if (invisCol != null) {
			invisibleColumns = invisCol.getCharacters();
			if(invisibleColumns==null) {
				invisibleColumns = "";
			}
		}
		SourceBean descCol = (SourceBean)source.getAttribute("DESCRIPTION-COLUMN");
		String descriptionColumn = null;
		// compatibility control (versions till 1.9.1 does not have description columns definition)
		if (descCol != null) { 
			descriptionColumn = descCol.getCharacters();
			if(descriptionColumn==null) {
				descriptionColumn = valueColumn;
			}
		} else descriptionColumn = valueColumn;
		
		// compatibility control (versions till 3.6 does not have TREE-LEVELS-COLUMN  definition)
		SourceBean treeLevelsColumnsBean = (SourceBean)source.getAttribute("TREE-LEVELS-COLUMNS");
		String treeLevelsColumnsString = null;
		if (treeLevelsColumnsBean != null) { 
			treeLevelsColumnsString = treeLevelsColumnsBean.getCharacters();
		}
		if( (treeLevelsColumnsString!=null) && !treeLevelsColumnsString.trim().equalsIgnoreCase("") ) {
			String[] treeLevelsColumnArr = treeLevelsColumnsString.split(",");
			this.treeLevelsColumns = Arrays.asList(treeLevelsColumnArr);
		}
		SourceBean lovTypeBean = (SourceBean)source.getAttribute("LOVTYPE"); 
		String lovType;
		if(lovTypeBean!=null){
			lovType =  lovTypeBean.getCharacters(); 
			this.lovType = lovType;
		}
		
		setDatasetId(id);
		setDatasetLabel(label);
		setValueColumnName(valueColumn);
		setDescriptionColumnName(descriptionColumn);
		List visColNames = new ArrayList();
		if( (visibleColumns!=null) && !visibleColumns.trim().equalsIgnoreCase("") ) {
			String[] visColArr = visibleColumns.split(",");
			visColNames = Arrays.asList(visColArr);
		}
		setVisibleColumnNames(visColNames);
		List invisColNames = new ArrayList();
		if( (invisibleColumns!=null) && !invisibleColumns.trim().equalsIgnoreCase("") ) {
			String[] invisColArr = invisibleColumns.split(",");
			invisColNames = Arrays.asList(invisColArr);
		}
		setInvisibleColumnNames(invisColNames);
		logger.debug("OUT");

	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getLovResult(it.eng.spago.security.IEngUserProfile, java.util.List, it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance)
	 */
	public String getLovResult(IEngUserProfile profile,
			List<ObjParuse> dependencies, ExecutionInstance executionInstance)
			throws Exception {
		//gets the dataset object informations
		IDataSet dataset = DAOFactory.getDataSetDAO().loadActiveIDataSetByID(new Integer(getDatasetId()));
		Map parameters = new HashMap(); 
		dataset.setParamsMap(parameters);
		dataset.setUserProfileAttributes(UserProfileUtils.getProfileAttributes(profile));
		dataset.loadData();
		IDataStore ids = dataset.getDataStore();

		String resultXml = ids.toXml();
		return resultXml;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#requireProfileAttributes()
	 */
	public boolean requireProfileAttributes() throws Exception {
		return false;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getProfileAttributeNames()
	 */
	public List getProfileAttributeNames() throws Exception {
		//Empty List because Profile Attributes are managed inside the Dataset logic
		return new ArrayList();
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getVisibleColumnNames()
	 */
	public List getVisibleColumnNames() {
		return visibleColumnNames;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getInvisibleColumnNames()
	 */
	public List getInvisibleColumnNames() {
		return invisibleColumnNames;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getDescriptionColumnName()
	 */
	public String getDescriptionColumnName() {
		return descriptionColumnName;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setVisibleColumnNames(java.util.List)
	 */
	public void setVisibleColumnNames(List visibleColumnNames) {
		this.visibleColumnNames = visibleColumnNames;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setInvisibleColumnNames(java.util.List)
	 */
	public void setInvisibleColumnNames(List invisibleColumnNames) {
		this.invisibleColumnNames = invisibleColumnNames;
	}



	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setDescriptionColumnName(java.lang.String)
	 */
	public void setDescriptionColumnName(String descriptionColumnName) {
		this.descriptionColumnName = descriptionColumnName;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getValueColumnName()
	 */
	public String getValueColumnName() {
		return valueColumnName;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setValueColumnName(java.lang.String)
	 */
	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>DatasetDetail</code> objects whom XML has been
	 * built.
	 * 
	 * @param dataDefinition The XML input String
	 * 
	 * @return The corrispondent <code>DatasetDetail</code> object
	 * 
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static DatasetDetail fromXML (String dataDefinition) throws SourceBeanException {
		return new DatasetDetail(dataDefinition);
	}

	public String getLovType() {
		return lovType;
	}

	public void setLovType(String lovType) {
		this.lovType = lovType;
	}

	public List getTreeLevelsColumns() {
		return treeLevelsColumns;
	}

	public void setTreeLevelsColumns(List treeLevelsColumns) {
		this.treeLevelsColumns = treeLevelsColumns;
	}

	
	
}
