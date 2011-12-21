/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.jpivotaddins.util;

import it.eng.spagobi.jpivotaddins.bean.AnalysisBean;

import com.tonbeller.jpivot.chart.ChartComponent;

public class ChartCustomizer {

	public static void customizeChart(AnalysisBean analysis, ChartComponent chart) {
		
		chart.setChartHeight(analysis.getChartHeight());
		chart.setChartWidth(analysis.getChartWidth());
		chart.setChartTitle(analysis.getChartTitle());
		chart.setChartType(analysis.getChartType());
		chart.setFontName(analysis.getFontName());
		chart.setFontStyle(analysis.getFontStyle());
		chart.setFontSize(analysis.getFontSize());

		// legend
		chart.setShowLegend(analysis.isShowLegend());
		// if legend is visible, set properties
		if (analysis.isShowLegend() == true) {
			chart.setLegendFontName(analysis.getLegendFontName());
			chart.setLegendFontStyle(analysis.getLegendFontStyle());
			chart.setLegendFontSize(analysis.getLegendFontSize());
			chart.setLegendPosition(analysis.getLegendPosition());
		}

		// slicer
		chart.setShowSlicer(analysis.isShowSlicer());
		// if slicer is visible, set properties
		if (analysis.isShowSlicer() == true) {
			chart.setSlicerPosition(analysis.getSlicerPosition());
			chart.setSlicerAlignment(analysis.getSlicerAlignment());
			chart.setSlicerFontName(analysis.getSlicerFontName());
			chart.setSlicerFontStyle(analysis.getSlicerFontStyle());
			chart.setSlicerFontSize(analysis.getSlicerFontSize());
		}

		// axes
		chart.setAxisFontName(analysis.getAxisFontName());
		chart.setAxisFontStyle(analysis.getAxisFontStyle());
		chart.setAxisFontSize(analysis.getAxisFontSize());
		chart.setHorizAxisLabel(analysis.getHorizAxisLabel());
		chart.setVertAxisLabel(analysis.getVertAxisLabel());
		chart.setAxisTickFontName(analysis.getAxisTickFontName());
		chart.setAxisTickFontStyle(analysis.getAxisTickFontStyle());
		chart.setAxisTickFontSize(analysis.getAxisTickFontSize());

		chart.setDrillThroughEnabled(analysis.isDrillThroughEnabled());
		chart.setTickLabelRotate(analysis.getTickLabelRotate());

		// set chart visible status
		chart.setVisible(analysis.isShowChart());

		// background color
		chart.setBgColorB(analysis.getBgColorB());
		chart.setBgColorG(analysis.getBgColorG());
		chart.setBgColorR(analysis.getBgColorR());
	

	}
	
}
