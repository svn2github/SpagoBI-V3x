/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.
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
  * Singleton object that handle all errors generated on the client side
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors - Alberto Ghedin (alberto.ghedin@eng.it)
  */


Ext.ns("Sbi.worksheet.runtime");

Sbi.worksheet.runtime.RuntimeChartFactory = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
 
    // public space
	return {
	
		init : function() {
		},
		
		
        createLineChart : function(config) {
        	var chartLib = 'highcharts';
        	if (Sbi.settings && Sbi.settings.worksheet && Sbi.settings.worksheet.chartlib) {
        		chartLib = Sbi.settings.worksheet.chartlib;
        	}
        	chartLib = chartLib.toLowerCase();
    		switch (chartLib) {
		        case 'ext3':
		        	return new Sbi.worksheet.runtime.RuntimeLineChartPanelExt3(config);
		        default: 
		        	return new Sbi.worksheet.runtime.RuntimeLineChartPanelHighcharts(config);
			}       	
        },

        createBarChart : function(config) {
        	var chartLib = 'highcharts';
        	if (Sbi.settings && Sbi.settings.worksheet && Sbi.settings.worksheet.chartlib) {
        		chartLib = Sbi.settings.worksheet.chartlib;
        	}
        	chartLib = chartLib.toLowerCase();
    		switch (chartLib) {
		        case 'ext3':
		        	return new Sbi.worksheet.runtime.RuntimeBarChartPanelExt3(config);
		        default: 
		        	return new Sbi.worksheet.runtime.RuntimeBarChartPanelHighcharts(config);
			}       	
        },
        
        createPieChart : function(config) {
        	var chartLib = 'highcharts';
        	if (Sbi.settings && Sbi.settings.worksheet && Sbi.settings.worksheet.chartlib) {
        		chartLib = Sbi.settings.worksheet.chartlib;
        	}
        	chartLib = chartLib.toLowerCase();
    		switch (chartLib) {
		        case 'ext3':
		        	return new Sbi.worksheet.runtime.RuntimePieChartPanelExt3(config);
		        default: 
		        	return new Sbi.worksheet.runtime.RuntimePieChartPanelHighcharts(config);
			}       	
        }

	};
}();