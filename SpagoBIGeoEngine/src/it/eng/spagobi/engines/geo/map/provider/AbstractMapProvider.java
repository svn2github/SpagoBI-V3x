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
package it.eng.spagobi.engines.geo.map.provider;

import it.eng.spagobi.engines.geo.GeoEngineException;
import it.eng.spagobi.engines.geo.component.AbstractGeoEngineComponent;
import it.eng.spagobi.engines.geo.map.provider.configurator.AbstractMapProviderConfigurator;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractMapProvider.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class AbstractMapProvider extends AbstractGeoEngineComponent implements IMapProvider {

	/** The selected map name. */
	private String selectedMapName;
		
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(AbstractMapProvider.class);
	
	
	/**
	 * Instantiates a new abstract map provider.
	 */
	public AbstractMapProvider() {
        super();
    }
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.AbstractGeoEngineComponent#init(java.lang.Object)
	 */
	public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		AbstractMapProviderConfigurator.configure( this, getConf() );
	}
	
   
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getSVGMapStreamReader()
	 */
	public XMLStreamReader getSVGMapStreamReader() throws GeoEngineException {
    	return getSVGMapStreamReader(selectedMapName);
    }
	
    /* (non-Javadoc)
     * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getSVGMapStreamReader(java.lang.String)
     */
    public XMLStreamReader getSVGMapStreamReader(String mapName) throws GeoEngineException {
    	return null;
    }
    
    /* (non-Javadoc)
     * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getSVGMapDOMDocument()
     */
    public SVGDocument getSVGMapDOMDocument() throws GeoEngineException {
		return getSVGMapDOMDocument(selectedMapName);
	}
    
	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getSVGMapDOMDocument(java.lang.String)
	 */
	public SVGDocument getSVGMapDOMDocument(String mapName) throws GeoEngineException {
		return null;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getSelectedMapName()
	 */
	public String getSelectedMapName() {
		return selectedMapName;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#setSelectedMapName(java.lang.String)
	 */
	public void setSelectedMapName(String selectedMapName) {
		this.selectedMapName = selectedMapName;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getMapNamesByFeature(java.lang.String)
	 */
	public List getMapNamesByFeature(String featureName) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.engines.geo.map.provider.IMapProvider#getFeatureNamesInMap(java.lang.String)
	 */
	public List getFeatureNamesInMap(String mapName) throws Exception {
		return null;
	}

	
   

}
