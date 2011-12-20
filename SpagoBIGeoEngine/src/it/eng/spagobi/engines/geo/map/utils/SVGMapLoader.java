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
package it.eng.spagobi.engines.geo.map.utils;

import it.eng.spagobi.engines.geo.map.provider.SOMapProvider;
import it.eng.spagobi.services.content.bo.Content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

import sun.misc.BASE64Decoder;
import sun.rmi.runtime.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class SVGMapLoader.
 * 
 * @author Andrea Gioia
 */
public class SVGMapLoader {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SVGMapLoader.class);
	
	private static final BASE64Decoder DECODER = new BASE64Decoder();
	
	
	/** The document factory. */
	private SAXSVGDocumentFactory documentFactory;
	
	private String parser = XMLResourceDescriptor.getXMLParserClassName();
	/** The xml input factory. */
	private static XMLInputFactory xmlInputFactory;
	
	static {
		xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);     
		xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
	}
	
	
	/**
	 * Load map as document.
	 * 
	 * @param file the file
	 * 
	 * @return the sVG document
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SVGDocument loadMapAsDocument(File file) throws IOException {
		String url;		
		url = file.toURI().toURL().toString();
		return loadMapAsDocument(url);
	}
	
	
	public SVGDocument loadMapAsDocument(Content map) throws IOException {
		String mapContent;
		
		mapContent = new String( DECODER.decodeBuffer(map.getContent()) );
		documentFactory = new SAXSVGDocumentFactory(parser);
		return (SVGDocument)documentFactory.createDocument( null, new StringReader(mapContent) );
	}
	
	/**
	 * Load map as document.
	 * 
	 * @param url the url
	 * 
	 * @return the sVG document
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SVGDocument loadMapAsDocument(String url) throws IOException {
		logger.debug(url);
		SVGDocument svgDoc = null;
		try{
			documentFactory = new SAXSVGDocumentFactory(parser);
			svgDoc = (SVGDocument)documentFactory.createDocument(url);
		}catch (Throwable e) {
			logger.error(e);
		}
	    return svgDoc;
	}
	
	/**
	 * Gets the map as stream.
	 * 
	 * @param file the file
	 * 
	 * @return the map as stream
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws XMLStreamException the XML stream exception
	 */
	public static XMLStreamReader getMapAsStream(File file) throws FileNotFoundException, XMLStreamException {
		return  xmlInputFactory.createXMLStreamReader(new FileInputStream(file));	
	}
	
	/**
	 * Gets the map as stream.
	 * 
	 * @param url the url
	 * 
	 * @return the map as stream
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws XMLStreamException the XML stream exception
	 */
	public static XMLStreamReader getMapAsStream(String url) throws FileNotFoundException, XMLStreamException {
		return  xmlInputFactory.createXMLStreamReader(new FileInputStream(url));	
	}
}
