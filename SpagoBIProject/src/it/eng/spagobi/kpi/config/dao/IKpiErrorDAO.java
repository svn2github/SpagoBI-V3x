package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.config.bo.KpiError;
import it.eng.spagobi.kpi.config.metadata.SbiKpiError;
import it.eng.spagobi.tools.dataset.exceptions.DatasetException;

import java.util.List;

public interface IKpiErrorDAO {


	/**
	 * @param kpiError
	 * @return
	 * @throws EMFUserError
	 */
	public Integer insertKpiError(SbiKpiError kpiError) throws EMFUserError;

	/**	
	 * @return
	 * @throws EMFUserError
	 */
	public List<KpiError> loadAllKpiErrors() throws EMFUserError;

	/**
	 * 
	 * @return
	 * @throws EMFUserError
	 */
	public KpiError loadKpiErrorById(Integer id) throws EMFUserError;

	/**
	 * @param kpiError
	 * @return
	 * @throws EMFUserError
	 */
	public void updateKpiError(SbiKpiError kpiError) throws EMFUserError;

	/**
	 * 
	 * @param exception
	 * @param modelInstanceId
	 * @param resourceName
	 * @return
	 * @throws EMFUserError
	 */
	public Integer insertKpiError(	DatasetException exception, Integer modelInstanceId, String resourceName) throws EMFUserError;


}
