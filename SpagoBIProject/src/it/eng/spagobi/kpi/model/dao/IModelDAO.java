package it.eng.spagobi.kpi.model.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.model.bo.Model;

import java.util.List;

public interface IModelDAO {
	
	/**
	 * Returns the Model without its children of the referred id
	 * 
	 * @param id of the Model
	 * @return Model of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */	
	public Model loadModelWithoutChildrenById(Integer id) throws EMFUserError;
	
	/**
	 * Returns the Model wit its children of the referred id
	 * 
	 * @param id of the Model
	 * @return Model of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */
	public Model loadModelWithChildrenById(Integer id) throws EMFUserError;
	
	/**
	 * Modify model.
	 * 
	 * @param aModel the a model
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public void modifyModel(Model aModel) throws EMFUserError;

	/**
	 * Insert a model and return the new model.
	 * 
	 * @param aModel the model to create
	 * @param modelTypeId the id of the type of the model
	 * @return the id of the model created
	 * @throws EMFUserError the EMF user error
	 */
	public Integer insertModel(Model aModel, Integer modelTypeId) throws EMFUserError;
	/**
	 * Insert a model and return the new model.
	 * 
	 * @param aModel the model to create
	 * @return the id of the model created
	 * @throws EMFUserError the EMF user error
	 */
	public Integer insertModel(Model aModel) throws EMFUserError;
	/**
	 * 
	 * Returns the List of Model Root.
	 * 
	 * @return List of Model Root.
	 * @throws EMFUserError If an Exception occurred.
	 */
	public List loadModelsRoot() throws EMFUserError;
	
	/**
	 * Delete a Model (children and attributes).
	 * @param modelId id of the model to delete.
	 * @return Return true if the model is deleted.
	 * @throws EMFUSEMFUserError If an Exception occurred.
	 */
	public boolean deleteModel(Integer modelId) throws EMFUserError;

	public List loadModelsRoot(String fieldOrder, String typeOrder)throws EMFUserError;

//	/**
//	 * Check if a Model or its children are associated with KPI.
//	 * @param modelId the id of the to check.
//	 * @return true if the Model or its children are associated with KPI.
//	 * @throws EMFUserError
//	 */
//	public boolean hasKpi(Integer modelId)throws EMFUserError;
}
