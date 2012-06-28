/* SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This program is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, either version 2.1 
 * of the License, or (at your option) any later version. This program is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU  General Public License for more details. You should have received a copy of the GNU  General Public License along with 
 * this program. If not, see: http://www.gnu.org/licenses/. */
package it.eng.spagobi.engines.weka.services.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.eng.spagobi.engines.weka.WekaEngine;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DownloadOutputFileServlet extends HttpServlet {

	private static transient Logger logger = Logger.getLogger(DownloadOutputFileServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.debug("IN");
		try {
			String outputFileName = request.getParameter("outputFileName");
			File outputFilesDir = WekaEngine.getConfig().getEngineOutputFilesDir();
			File outputFile = new File(outputFilesDir, outputFileName);
			
			// setup response header
			if(response instanceof HttpServletResponse) {
				response.setHeader("Content-Disposition", "attachment" + "; filename=\"" + outputFile.getName() + "\";");
			}
			
			response.setContentType("text/plain");
			response.setStatus(200);
			
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(outputFile));
			
			int b = -1;
			int contentLength = 0;
			byte[] buf = new byte[1024];
			while((b = in.read(buf)) != -1) {
				response.getOutputStream().write(buf, 0, b);
				contentLength += b;
			}	
			response.setContentLength( contentLength );
			response.getOutputStream().flush();
			
			in.close();
		} catch(Throwable t) {
			
		} finally {
			logger.debug("IN");
		}
	}
}
