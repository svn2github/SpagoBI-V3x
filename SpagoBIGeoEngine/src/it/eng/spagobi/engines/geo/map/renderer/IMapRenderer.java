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
package it.eng.spagobi.engines.geo.map.renderer;

import java.io.File;

import it.eng.spagobi.engines.geo.GeoEngineException;
import it.eng.spagobi.engines.geo.component.IGeoEngineComponent;
import it.eng.spagobi.engines.geo.datamart.provider.IDataMartProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;

// TODO: Auto-generated Javadoc
/**
 * The Interface IMapRenderer.
 * 
 * @author Andrea Gioia
 */
public interface IMapRenderer  extends IGeoEngineComponent {
	
	/**
	 * Render map.
	 * 
	 * @param mapProvider the map provider
	 * @param datamartProvider the datamart provider
	 * @param outputFormat the output format
	 * 
	 * @return the file
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public File renderMap(IMapProvider mapProvider, 
			IDataMartProvider datamartProvider,
			  String outputFormat) throws GeoEngineException;
			  
	
	/**
	 * Render map.
	 * 
	 * @param mapProvider the map provider
	 * @param datamartProvider the datamart provider
	 * 
	 * @return the file
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	File renderMap(IMapProvider mapProvider, IDataMartProvider datamartProvider) throws GeoEngineException;
	
	/**
	 * Gets the layer names.
	 * 
	 * @return the layer names
	 */
	public String[] getLayerNames();
	
	/**
	 * Gets the layer.
	 * 
	 * @param layerName the layer name
	 * 
	 * @return the layer
	 */
	public Layer getLayer(String layerName);
	
	/**
	 * Adds the layer.
	 * 
	 * @param layer the layer
	 */
	public void addLayer(Layer layer);
	
	/**
	 * Clear layers.
	 */
	void clearLayers();
	
	void setSelectedMeasureName(String selectedMeasureName);
}
