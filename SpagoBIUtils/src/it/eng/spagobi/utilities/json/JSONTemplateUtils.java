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
package it.eng.spagobi.utilities.json;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.container.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 * 
 * Utility Class to convert xml template in json template
 */

public class JSONTemplateUtils {
	
	//template constants

	public static final String WIDTH = "WIDTH";
	public static final String HEIGHT = "HEIGHT";
	public static final String HIGH_NUMCHARTS = "NUMCHARTS";
	public static final String HIGH_SUBTYPE = "SUBTYPE";
	
	private static transient Logger logger = Logger.getLogger(JSONTemplateUtils.class);
	
	private String divWidth = "100%";
	private String divHeight = "100%";
	private String theme = "";
	private Integer numCharts = new Integer ("1");
	private String subType = "";
	private boolean firstBlock = true;
	private JSONArray parametersJSON = null;
	/**
	 * Returns a JSONObject with the input configuration (xml format). 
	 * 
	 * @param xmlTemplate the template in xml language
	 * @param
	 * 
	 * @return JSONObject the same template in json format (because highcharts uses json format)
	 */
	public JSONObject getJSONTemplateFromXml(SourceBean xmlTemplate, JSONArray parsJSON) throws JSONException {
		JSONObject toReturn = null;
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    OutputStreamWriter ow = new OutputStreamWriter(out);
	    parametersJSON = parsJSON;
	    try{
		    //the begin of all...
		    ow.write("{\n");
			
			//dimension definition
			setDivWidth((String)xmlTemplate.getAttribute(WIDTH));
			setDivHeight((String)xmlTemplate.getAttribute(HEIGHT));
			//number of chart definition (for highchart lib)
			//setNumCharts((xmlTemplate.getAttribute(HIGH_NUMCHARTS)!=null)?Integer.valueOf((String)xmlTemplate.getAttribute(HIGH_NUMCHARTS)):1);
			//subtype for master/detail chart
			//setSubType((xmlTemplate.getAttribute(HIGH_CHART+"."+ HIGH_SUBTYPE)!=null)?(String)xmlTemplate.getAttribute(HIGH_CHART+"."+ HIGH_SUBTYPE):"");
			
			xmlTemplate.delAttribute(WIDTH);
			xmlTemplate.delAttribute(HEIGHT);
			//xmlTemplate.delAttribute(HIGH_NUMCHARTS);
			//xmlTemplate.delAttribute(HIGH_SUBTYPE);

			ow = getPropertiesDetail(xmlTemplate, ow);
			ow.write("}\n");
			ow.flush();			
			//System.out.println("*** template: " + out.toString());
			logger.debug("ChartConfig: " + out.toString());
			
	    }catch (IOException ioe){
	    	logger.error("Error while defining json chart template: " + ioe.getMessage());
	    }catch (Exception e){
	    	logger.error("Error while defining json chart template: " + e.getMessage());
	    }finally{
	    	try{
	    		ow.close();
	    	}catch (IOException ioe2){
		    	logger.error("Error while closing the output writer object: " + ioe2.getMessage());
		    }
	    	
	    }
	    //replace dublicate , charachter
	    String json = out.toString().replaceAll(", ,", ",");
	    toReturn =  ObjectUtils.toJSONObject(json);
		
		return toReturn;
	}

	/**
	 * @return the divWidth
	 */
	public String getDivWidth() {
		return divWidth;
	}

	/**
	 * @param divWidth the divWidth to set
	 */
	public void setDivWidth(String divWidth) {
		this.divWidth = divWidth;
	}

	/**
	 * @return the divHeight
	 */
	public String getDivHeight() {
		return divHeight;
	}

	/**
	 * @param divHeight the divHeight to set
	 */
	public void setDivHeight(String divHeight) {
		this.divHeight = divHeight;
	}
	
	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the numCharts
	 */
	public Integer getNumCharts() {
		return numCharts;
	}

	/**
	 * @param numCharts the numCharts to set
	 */
	public void setNumCharts(Integer numCharts) {
		this.numCharts = numCharts;
	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @param subType the subType to set
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * @return the firstBlock
	 */
	public boolean isFirstBlock() {
		return firstBlock;
	}

	
	/**
	 * @param firstBlock the firstBlock to set
	 */
	public void setFirstBlock(boolean firstBlock) {
		this.firstBlock = firstBlock;
	}

	/**
	 * Returns an  OutputStreamWriter with the json template
	 * 
	 * @param sbConfig the sourcebean with the xml configuration 
	 * @param ow the current OutputStreamWriter
	 * @return
	 */
	private  OutputStreamWriter getPropertiesDetail (Object sbConfig, OutputStreamWriter ow ){
		//template complete
		OutputStreamWriter toReturn = ow;
		
		if (sbConfig == null) return toReturn;
		
	    try{
	    	List atts = ((SourceBean)sbConfig).getContainedAttributes();
	    	for (int i=0; i< atts.size();i++) {

				SourceBeanAttribute object = (SourceBeanAttribute) atts.get(i);
				
				//object.getValue();
				String key=(String)object.getKey();
				if(key.endsWith("_LIST")){
					String arrayKey = key.substring(0, key.indexOf("_LIST"));
					toReturn.write("      " + convertKeyString(arrayKey) +": [ \n");	
					toReturn = getAllArrayAttributes(object, toReturn);
					toReturn.write("       ]\n");
				}else{
					if (object.getValue() instanceof SourceBean){
						toReturn.write("      " + convertKeyString(key) +": { \n");	
						toReturn = getAllAttributes(object, ow);
						toReturn.write("       }\n");
					}
					else {
						//only for root node attributes
						toReturn.write("      " + convertKeyString(key) +": '");	
						toReturn = getAllAttributes(object, ow);
						toReturn.write("'\n");
					}
				}
				if(i != atts.size()-1){
					toReturn.write(", ");		
				}
			}
	    	
	    }catch (Exception e){
	    	logger.error("Error while defining json chart template: " + e.getMessage());
	    }	 
	    return toReturn;
	}
	

	/**
	 * Returns an OutputStreamWriter with all details about a single key (ie. CHART tag)
	 * @param key
	 * @param sb
	 * @param ow
	 * @return
	 */
	private OutputStreamWriter getAllAttributes(SourceBeanAttribute sb, OutputStreamWriter ow){
		OutputStreamWriter toReturn = ow;
		
		try{
			if (sb.getValue() instanceof SourceBean){
                //toReturn.write("      " + convertKeyString(sb.getKey()) +": { \n");    
				SourceBean sbSubConfig = (SourceBean)sb.getValue();
				List subAtts = sbSubConfig.getContainedAttributes();
				List containedSB = sbSubConfig.getContainedSourceBeanAttributes();
				int numberOfSb = containedSB.size();
				int sbCounter = 1;
				//standard tag attributes
				for(int i =0; i< subAtts.size(); i++){
					SourceBeanAttribute object = (SourceBeanAttribute)subAtts.get(i);
					if (object.getValue() instanceof SourceBean){
						
						String key=(String)object.getKey();

						if(key.endsWith("_LIST")){
							String arrayKey = key.substring(0, key.indexOf("_LIST"));
							toReturn.write("      " + convertKeyString(arrayKey) +": [ \n");	
							toReturn = getAllArrayAttributes(object, toReturn);
							toReturn.write("       ]\n");
						}else{
							toReturn.write("      " + convertKeyString(key) +": { \n");	
							toReturn = getAllAttributes(object, toReturn);
							toReturn.write("       }\n");
						}
						if(i != subAtts.size()-1){
							toReturn.write("       , ");
						}
						sbCounter++;
					}else{
						SourceBeanAttribute subObject2 = (SourceBeanAttribute) subAtts.get(i);
						toReturn = writeTagAttribute(subObject2, toReturn, false);
						if(i != subAtts.size()-1){
							toReturn.write("       , ");
						}
					}
				}
                //toReturn.write("       }\n");
            }else{
                //puts the simple value attribute
                toReturn.write(String.valueOf(sb.getValue()));
            }
				
		}catch (IOException ioe){
	    	logger.error("Error while defining json chart template: " + ioe.getMessage());
	    }catch (Exception e){
	    	logger.error("Error while defining json chart template: " + e.getMessage());
	    }
		return toReturn;
	}
	private OutputStreamWriter getAllArrayAttributes(SourceBeanAttribute sb, OutputStreamWriter ow){
		OutputStreamWriter toReturn = ow;
		
		try{
			if (sb.getValue() instanceof SourceBean){
				SourceBean sbSubConfig = (SourceBean)sb.getValue();

				List containedSB = sbSubConfig.getContainedSourceBeanAttributes();
				int numberOfSb = containedSB.size();
				int sbCounter = 1;
				//standard tag attributes
				for(int i =0; i< containedSB.size(); i++){
					SourceBeanAttribute object = (SourceBeanAttribute)containedSB.get(i);
					Object o = object.getValue();
					SourceBean sb1 = SourceBean.fromXMLString(o.toString());
					String v = sb1.getCharacters();
					if(v!= null){
						toReturn.write(v + "\n" );

					}else{
						//attributes

						toReturn.write("{ 	\n" );
				    	List atts = ((SourceBean)sb1).getContainedAttributes();
						toReturn = getAllAttributes(object, toReturn);
				    	toReturn.write("} 	\n" );
					}
					if(i != containedSB.size()-1){
						toReturn.write("       , ");
					}
				}
			
			}	
		}catch (Exception e){
	    	logger.error("Error while defining json chart template: " + e.getMessage());
	    }
		return toReturn;
	}
	/**
	 * Returns an object (String or Integer) with the value of the property.
	 * @param key the attribute key
	 * @param sbAttr the soureBeanAttribute to looking for the value of the key
	 * @return
	 */
	private Object getAttributeValue(String key, SourceBeanAttribute sbAttr){
		String value = new String((String)sbAttr.getValue());
		Object finalValue = "";
		if(value != null){
			try{
				//checks if the value is a number
				finalValue =Long.valueOf(value);
			}catch (Exception e){
					//checks if the value is a boolean
					if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false") //boolean
							&& ! value.startsWith("[")//not an array example for categories...
							//&& ! value.trim().startsWith("function")//not an array example for categories...
						){
						//replace parameters
						if(value.contains("$P{")){
							finalValue = replaceParametersInValue(value);
							finalValue = "'" + finalValue + "'";						
						}else{
							//the value is a string!
							finalValue = "'" + value + "'";
						}
					}else{
						//the value is not a string
						finalValue = value;
					}
			}
		}
		return finalValue;
	}
	
	private String replaceParametersInValue(String valueString){
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(valueString);
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.indexOf("$P{") != -1){
				String parName = tok.substring(tok.indexOf("$P{")+3, tok.indexOf("}"));				
				if(!parName.equals("")){					
					for(int i=0; i<parametersJSON.length(); i++){
						try {
							JSONObject objPar = (JSONObject)parametersJSON.get(i);								
							if(((String)objPar.get("name")).equals(parName)){
								String val = ((String)objPar.get("value")).replaceAll("'", "");
								sb.append(val);
								sb.append(" ");
							}
						} catch (JSONException e1) {
							logger.error("Error while replacing parameters in value: " + e1.getMessage());
						}
					}
				}
			}else{
				sb.append(tok);
				sb.append(" ");
			}
		}

		return sb.toString(); 
	}
	/**
	 * @param sb
	 * @param toReturn
	 * @return
	 * @throws IOException
	 */
	private OutputStreamWriter writeTagAttribute(SourceBeanAttribute sb, OutputStreamWriter toReturn, boolean isTag) throws IOException{

		Object subValue = getAttributeValue(sb.getKey(), sb);
		if (subValue != null){
			if(isTag){
				toReturn.write("      " + convertKeyString(sb.getKey()) + ": " + subValue + "\n" );	
			}else{				
				toReturn.write("      " + sb.getKey() + ": " + subValue + "\n" );	
			}

		}
		return toReturn;
	}
	private String convertKeyString(String xmlTag){
		String jsonKey = xmlTag.toLowerCase();
		StringBuffer sb = new StringBuffer();
		int count = 0;
	    for (String s : xmlTag.split("_")) {
	    	if(count == 0){
	    		sb.append(Character.toLowerCase(s.charAt(0)));
	    	}else{
	    		sb.append(Character.toUpperCase(s.charAt(0)));
	    	}
	        if (s.length() > 1) {
	            sb.append(s.substring(1, s.length()).toLowerCase());
	        }
	        count++;
	    }

	    if(!sb.toString().equals("")){
	    	jsonKey = sb.toString();
	    }
	    
	    return jsonKey;

	}
	
	
}
