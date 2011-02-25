/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
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
  * [description]
  * 
  * 
  * Public Functions
  * 
  *  [list]
  * 
  * 
  * Authors
  * 
  * - Andrea Gioia (adrea.gioia@eng.it), Fabio D'Ovidio (f.dovidio@inovaos.it)
  */

Ext.ns("Sbi.georeport");

Sbi.georeport.LayerFactory = function(){
 
	return {
		
		createLayer : function( layerConf ){
			var layer;
			if(layerConf.type === 'WMS') {
				layer = new OpenLayers.Layer.WMS(
					layerConf.name, layerConf.url, 
					layerConf.params, layerConf.options
				);
			} else if(layerConf.type === 'TMS') {
				layerConf.options.getURL = Sbi.georeport.GeoReportUtils.osm_getTileURL;
				layer = new OpenLayers.Layer.TMS(
					layerConf.name, layerConf.url, layerConf.options
				);
			} else if(layerConf.type === 'Google') {
				layer = new OpenLayers.Layer.Google(
					layerConf.name, layerConf.options
				);
			} else if(layerConf.type === 'OSM') { 
				layer = new OpenLayers.Layer.OSM.Mapnik('OSM');
			}else {
				Sbi.exception.ExceptionHandler.showErrorMessage(
					'Layer type [' + layerConf.type + '] not supported'
				);
			}
			return layer;
		}
	};
	
}();







	