package it.eng.spagobi.engines.kpi.service;

import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.AbstractSpagoBIAction;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;


public class ManageTrendAction extends AbstractSpagoBIAction{
	
	private static transient Logger logger = Logger.getLogger(ManageTrendAction.class);
	ExecutionInstance instance = null;
	
	@Override
	public void doService() {
		logger.debug("IN");
		try {
			String kpiInstanceID = this.getAttributeAsString("kpiInstId");
			String kpiBeginDate = this.getAttributeAsString("dateFrom");
			String kpiEndDate = this.getAttributeAsString("dateTo");
			
			//converts the params string in date type 
			String format  = GeneralUtilities.getServerDateFormat();
			logger.debug("Got Date format: "+(format!=null ? format : "null"));
			SimpleDateFormat f = new SimpleDateFormat();
			f.applyPattern(format);	
			Date dKpiBeginDate = new Date();
			dKpiBeginDate = f.parse(kpiBeginDate);
			Date dKpiEndDate = new Date();
			dKpiEndDate = f.parse(kpiEndDate);
			JSONObject jsonData = null;
			//gets the chart's data	
			if (kpiInstanceID!=null){
				jsonData = DAOFactory.getKpiDAO().getKpiTrendJSONResult(Integer.valueOf(kpiInstanceID), dKpiBeginDate, dKpiEndDate);
				if(jsonData!=null){
					try {
						writeBackToClient( new JSONSuccess( jsonData ) );
					} catch (IOException e) {
						throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
					}
				}else{
					throw new SpagoBIServiceException(SERVICE_NAME,"No data found");
				}
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new SpagoBIServiceException(SERVICE_NAME,"sbi.ds.testError", e);
		}
		logger.debug("OUT");
		
	}
	
	
}