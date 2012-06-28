/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.datasource.hibernate.IHibernateDataSource;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.hibernate.HQLStatement;
import it.eng.qbe.statement.jpa.JPQLStatement;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class StatementFactory {
	public static IStatement createStatement(IDataSource dataSource, Query query) {
		IStatement statement;
		
		statement = null;
		
		if(dataSource instanceof IHibernateDataSource) {
			statement = new HQLStatement((IHibernateDataSource)dataSource, query);
		} else if (dataSource instanceof JPADataSource) {
			statement = new JPQLStatement((JPADataSource)dataSource, query);
		} else {
			throw new RuntimeException("Impossible to create statement from a datasource of type [" + dataSource.getClass().getName() + "]");
		}
		
		return statement;
	}
}
