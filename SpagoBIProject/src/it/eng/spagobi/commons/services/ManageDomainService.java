/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/

/**
 * Object name
 * 
 * ManageDomains
 * 
 * 
 * Public Properties
 * 
 * [list]
 * 
 * 
 * Public Methods
 * 
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors
 * 
 * Monia Spinelli (monia.spinelli@eng.it)
 */
package it.eng.spagobi.commons.services;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.kpi.model.dao.IModelInstanceDAO;
import it.eng.spagobi.kpi.model.dao.IModelResourceDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONFailure;
import it.eng.spagobi.utilities.service.JSONSuccess;

public class ManageDomainService extends AbstractSpagoBIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// logger component
	private static Logger logger = Logger.getLogger(ManageDomainService.class);

	// Service parameter
	private final String MESSAGE_DET = "MESSAGE_DET";

	private static final String DOMAIN_LIST = "DOMAIN_LIST";
	private static final String DOMAIN_DELETE = "DOMAIN_DELETE";
	private static final String DOMAIN_SAVE = "DOMAIN_SAVE";

	@Override
	public void doService() {
		IDomainDAO domainDao;
		String serviceType;

		logger.debug("IN");

		try {
			domainDao = DAOFactory.getDomainDAO();
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME, "Error occurred");
		}

		serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Parameter [" + MESSAGE_DET + "] is equal to ["
				+ serviceType + "]");

		if (serviceType != null) {
			if (serviceType.equalsIgnoreCase(DOMAIN_LIST)) {
				doDomainList();
			} else if (serviceType.equalsIgnoreCase(DOMAIN_DELETE)) {
				doDelete();
			} else if (serviceType.equalsIgnoreCase(DOMAIN_SAVE)) {
				doSave();
			} else {
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Unable to execute service [" + serviceType + "]");
			}
		}

		logger.debug("OUT");

	}

	public void doSave() {

		logger.debug("IN");

		try {

			logger.debug("Save domain");

			Domain domain = this.setDomain();
			DAOFactory.getDomainDAO().saveDomain(domain);
			JSONObject response = new JSONObject();
			response.put("VALUE_ID", domain.getValueId());
			writeBackToClient(new JSONSuccess(response));

		} catch (Throwable e) {
			throw new SpagoBIServiceException(SERVICE_NAME,
					"Impossible to save domain", e);
		} finally {
			logger.debug("OUT");
		}
	}

	public void doDelete() {
		try {
			// domainDao.delete(codeDomain, codeValue);
			writeBackToClient(new JSONAcknowledge("Operation succeded"));

		} catch (Throwable e) {
			logger.error("Exception occurred while retrieving domain data", e);
			throw new SpagoBIServiceException(SERVICE_NAME,
					"Exception occurred while retrieving domain data", e);
		}
	}

	public void doDomainList() {
		try {
			logger.debug("Loaded domain list");

			List<Domain> domainList = DAOFactory.getDomainDAO().loadListDomains();
			JSONArray domainListJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(domainList,
							this.getLocale());
			JSONObject response = new JSONObject();
			response.put("response", domainListJSON);

			writeBackToClient(new JSONSuccess(response));

			// PaginatorIFace paginator = new GenericPaginator();
			// Alla fine di ogni iterazione inserire: paginator.addRow(rowSB);
			// list.setPaginator(paginator);
			// writeBackToClient(new JSONAcknowledge("Operation succeded"));
		} catch (Throwable e) {
			logger.error("Exception occurred while retrieving domain data", e);
			throw new SpagoBIServiceException(SERVICE_NAME,
					"Exception occurred while retrieving domain data", e);
		}
	}

	public Domain setDomain() {
		Domain domain = new Domain();

		domain.setValueId(this.getAttributeAsInteger("VALUE_ID"));
		domain.setValueCd(this.getAttributeAsString("VALUE_CD"));
		domain.setValueName(this.getAttributeAsString("VALUE_NM"));
		domain.setDomainCode(this.getAttributeAsString("DOMAIN_CD"));
		domain.setDomainName(this.getAttributeAsString("DOMAIN_NM"));
		domain.setValueDescription(this.getAttributeAsString("VALUE_DS"));

		return domain;

	}

}
