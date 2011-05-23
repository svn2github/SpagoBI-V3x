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
 * Public Methods
 * 
 * getLayoutValue(): returns a string with the selected layout. 
 * 			Available values:
 * 				'layout-headerfooter' (default)
 * 				'layout-header'
 * 				'layout-footer'
 * 				'layout-content'
 * 
 * 
 * setLayoutValue(value): sets the layout value..
 * 			The available values are the same of getLayoutValue
 * 
 * Authors - Alberto Ghedin
 */
Ext.ns("Sbi.worksheet.designer");

Sbi.worksheet.designer.DesignToolsLayoutPanel = function(config) { 
	
	var defaultSettings = {
		title:  LN('sbi.worksheet.designer.designtoolslayoutpanel.title'),
		border: false,
		bodyStyle: 'padding-top: 15px; padding-left: 15px'
	};
			
	if (Sbi.settings && Sbi.settings.worksheet && Sbi.settings.worksheet.designer && Sbi.settings.worksheet.designer.designToolsLayoutPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.worksheet.designer.designToolsLayoutPanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
			
	this.layoutRadioGroup = new Ext.form.RadioGroup({
		hideLabel: true,
		columns: 2,
		items: [
		        {name: 'layout', height: 40, id:'layout-headerfooter', ctCls:'layout-headerfooter',inputValue: 'layout-headerfooter', checked: true},
		        {name: 'layout', height: 40, id:'layout-header', ctCls:'layout-header',inputValue: 'layout-header'},
		        {name: 'layout', height: 40, id:'layout-footer', ctCls:'layout-footer', inputValue: 'layout-footer'},
		        {name: 'layout', height: 40, id:'layout-content', ctCls:'layout-content', inputValue: 'layout-content'}
		        ]
	});
	
	c = {
		items: [this.layoutRadioGroup]
	};

	this.layoutRadioGroup.on('change', this.updateSheetLayout, this);
	
	Sbi.worksheet.designer.DesignToolsLayoutPanel.superclass.constructor.call(this, c);	

	this.on('afterLayout',this.addToolTips,this);


};

Ext.extend(Sbi.worksheet.designer.DesignToolsLayoutPanel, Ext.FormPanel, {
	layoutRadioGroup: null,

	addToolTips: function(){

		var sharedConf ={anchor: 'top',width:200,trackMouse:true};

		new Ext.ToolTip(Ext.apply({
			target: 'x-form-el-layout-headerfooter',
			html: LN('sbi.worksheet.designer.designtoolslayoutpanel.tooltip.headerfooter'),
		},sharedConf));
		new Ext.ToolTip(Ext.apply({
			target: 'x-form-el-layout-header',
			html: LN('sbi.worksheet.designer.designtoolslayoutpanel.tooltip.header')
		},sharedConf));
		new Ext.ToolTip(Ext.apply({
			target: 'x-form-el-layout-footer',
			html: LN('sbi.worksheet.designer.designtoolslayoutpanel.tooltip.footer')
		},sharedConf));
		new Ext.ToolTip(Ext.apply({
			target: 'x-form-el-layout-content',
			html: LN('sbi.worksheet.designer.designtoolslayoutpanel.tooltip.content')
		},sharedConf));
		this.on('afterLayout',this.addToolTips,this);
	},

	//returns a string with the selected layout (for the available values look the..
	//.. class comment)
	getLayoutValue: function(){
		if(this.layoutRadioGroup!==null && this.layoutRadioGroup.getValue()!==null && this.layoutRadioGroup.getValue().getGroupValue()!==null){
			return this.layoutRadioGroup.getValue().getGroupValue();
		}else{
			this.layoutRadioGroup.setValue('layout-headerfooter');
			return 'layout-headerfooter';
		}
	},

	//set the layout (for the available values look the..
	//.. class comment)
	setLayoutValue: function(value){
		this.layoutRadioGroup.setValue(value);
	},
	
	updateSheetLayout: function(){
		this.fireEvent('layoutchange',this.getLayoutValue());
	}


});