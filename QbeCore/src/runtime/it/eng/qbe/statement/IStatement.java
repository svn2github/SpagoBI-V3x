/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.Query;

import java.util.Map;
import java.util.Set;

/**
 * The Interface IStatement.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public interface IStatement {
	
	public IDataSource getDataSource();
	
	public void setQuery(Query query);	
	public Query getQuery();
	
	/*
	 *  the number of the selected entities depends on the statement type and not on the abstract query
	 *  
	 *  For example the following is the same query expressed in SQL and HQL ...
	 *  
	 *  ->	select f.unit_sales, p.brand_name from fact_sales f, product p where f.id_product = p.id_product
	 *  
	 *  -> select f.unit_sales, f.product.brand_name from sales f
	 *  
	 *  the first (SQL) have two selected entities the latter only 1
	 *  
	 */
	public Set getSelectedEntities();
	
	public void prepare();		
	public String getQueryString();
	public String getSqlQueryString();
	
	public int getOffset();
	public void setOffset(int offset);
	public int getFetchSize();	
	public void setFetchSize(int fetchSize);	
	public int getMaxResults();	
	public void setMaxResults(int maxResults);
	
	public Map getParameters();
	public void setParameters(Map parameters);
	
	
	
}
