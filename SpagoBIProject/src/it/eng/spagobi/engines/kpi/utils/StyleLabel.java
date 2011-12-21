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
package it.eng.spagobi.engines.kpi.utils;

import java.awt.Color;
import java.awt.Font;

public class StyleLabel {

	private Font font;
	private String fontName;
	private int size;
	private Color color;
	private String orientation;
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public StyleLabel(String fontName, int size, Color color, String orientation) {
		super();
		this.fontName = fontName;
		this.size = size;
		this.color = color;
		this.orientation = orientation;
		font=new Font(fontName,Font.BOLD,size);
	}
	public StyleLabel(String fontName, int size, Color color) {
		super();
		this.fontName = fontName;
		this.size = size;
		this.color = color;
		font=new Font(fontName,Font.BOLD,size);
	}
	

	
	
}
