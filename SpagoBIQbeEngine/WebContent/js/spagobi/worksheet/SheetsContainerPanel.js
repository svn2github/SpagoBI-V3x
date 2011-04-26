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

Sbi.worksheet.SheetsContainerPanel = function(config) { 
	this.index = 0;
	this.addEvents();
	this.addPanel = {
			id: 'addTab',
            title: '<br>',
            iconCls: 'newTabIcon',
        	};
	
	var c ={
			tabPosition: 'bottom',        
	        enableTabScroll:true,
	        defaults: {autoScroll:true},
	        items: [this.addPanel]
	};
	
	this.on('render',function(){this.addTab();},this);
	
	this.initPanel();
	Sbi.worksheet.SheetsContainerPanel.superclass.constructor.call(this, c);	 		

};

Ext.extend(Sbi.worksheet.SheetsContainerPanel, Ext.TabPanel, {
	index: null,
	
	initPanel: function(){
	    this.on('tabchange',function(tabPanel, tab){
	    	if(tab==null || tab.id=='addTab'){
	    		this.addTab();
	    		tabPanel.setActiveTab(tabPanel.items.length-2);
	    	}
	    },this);
	},

	addTab: function(){
		this.suspendEvents();
		this.remove('addTab');
		var sheet = new Sbi.worksheet.SheetPanel({
	        title: 'Sheet ' + (++this.index),
	        closable:true
	    }); 
		
	    var tab = this.add(sheet);
	    this.add(this.addPanel);
	    if(this.getActiveTab()==null){
	    	this.setActiveTab(0);
	    }
	    this.resumeEvents();
	    
	    tab.on('beforeClose',function(panel){
			Ext.MessageBox.confirm(
					LN('sbi.worksheet.msg.deletetab.title'),
					LN('sbi.worksheet.msg.deletetab.msg'),            
		            function(btn, text) {
		                if (btn=='yes') {
		                	this.remove(panel);
		                }
		            },
		            this
				);
			return false;
	    },this);
	}

	
});