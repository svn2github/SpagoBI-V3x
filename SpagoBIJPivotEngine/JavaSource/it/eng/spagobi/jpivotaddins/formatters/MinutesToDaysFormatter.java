/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.formatters;

import org.apache.log4j.Logger;

import mondrian.olap.CellFormatter;

public class MinutesToDaysFormatter implements CellFormatter {

	private static transient Logger logger = Logger.getLogger(MinutesToDaysFormatter.class);
	
	public String formatCell(Object value) {
		logger.debug("IN");
		if (value == null) {
			logger.warn("Value in input is null");
			return "";
		}
		logger.debug(value.getClass().getName());
		String toReturn = null;
		if (value instanceof Number) {
			Number doubleObj = (Number) value;
			double d = doubleObj.doubleValue();
			int days = (int) Math.floor(d/(60*24));
			double remainingMinutes = d % (60*24);
			int hours = (int) Math.round(remainingMinutes/60);
			toReturn = days + "d " + hours + "h";
		} else {
			toReturn = value.toString();
		}
		logger.debug("OUT: returning " + toReturn);
		return toReturn;
	}
	
}
