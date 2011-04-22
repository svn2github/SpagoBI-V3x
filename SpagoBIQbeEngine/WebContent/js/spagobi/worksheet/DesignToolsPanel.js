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
 * Public Properties
 * 
 * [list]
 * 
 * 
 * Public Methods
 * 
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Alberto Ghedin
 */
Ext.ns("Sbi.worksheet");

Sbi.worksheet.DesignToolsPanel = function(config) { 

	this.initPanels();
	var c ={
            layout: {
                type:'vbox',
                align:'stretch'
            },
            items:[this.designToolsFieldsPanel, this.designToolsPallettePanel, this.designToolsLayoutPanel]
	}
	Sbi.worksheet.DesignToolsPanel.superclass.constructor.call(this, c);	 		

};

Ext.extend(Sbi.worksheet.DesignToolsPanel, Ext.Panel, {
	designToolsFieldsPanel: null,
	designToolsPallettePanel: null,
	designToolsLayoutPanel: null,

	initPanels: function(){
		this.designToolsFieldsPanel = new Sbi.worksheet.DesignToolsFieldsPanel({
	        gridConfig: {
				ddGroup: 'worksheetDesignerDDGroup'
	        	, type: 'queryFieldsPanel'
	        }
		});
		this.designToolsPallettePanel = new Sbi.worksheet.DesignToolsPallettePanel();
		this.designToolsLayoutPanel = new Sbi.worksheet.DesignToolsLayoutPanel();
		this.designToolsFieldsPanel.flex = 1;
		this.designToolsPallettePanel.flex = 1;
		this.designToolsLayoutPanel.flex = 1;
	}
});
