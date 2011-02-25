package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;

import java.util.List;

public interface IKpiInstPeriodDAO {

	/**
	 * Load couples by Kpi Instance Id .
	 * 
	 * @param modelId
	 *            the id of modelInstance to check.

	 * @return list of modelResource Id
	 * 
	 * @throws EMFUserError
	 */
	List loadKpiInstPeriodId(Integer kpiInstId) throws EMFUserError;

	
	
}
