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
Ext.ns("Sbi.kpi");

Sbi.kpi.KpiAccordionPanel =  function(config) {
		
		var defaultSettings = {};

		if (Sbi.settings && Sbi.settings.kpi && Sbi.settings.kpi.kpiAccordinPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.kpi.kpiAccordinPanel);
		}

		var c = Ext.apply(defaultSettings, config || {});

		Ext.apply(this, c);
		

	    var item1 = new Ext.Panel({
	        title: 'Accordion Item 1',
	        html: '&lt;empty panel&gt;',
	        cls:'empty'
	    });

	    var item2 = new Ext.Panel({
	        title: 'Accordion Item 2',
	        html: '&lt;empty panel&gt;',
	        cls:'empty'
	    });

	    var item3 = new Ext.Panel({
	        title: 'Accordion Item 3',
	        html: '&lt;empty panel&gt;',
	        cls:'empty'
	    });

	    var item4 = new Ext.Panel({
	        title: 'Accordion Item 4',
	        html: '&lt;empty panel&gt;',
	        cls:'empty'
	    });

	    var item5 = new Ext.Panel({
	        title: 'Accordion Item 5',
	        html: '&lt;empty panel&gt;',
	        cls:'empty'
	    });

	    c = {
	        region:'east',
	        fill: true,
	        split:true,
	        width: 200,
	        minSize: 175,
	        maxSize: 400,
	        collapsible: true,
	        layout:'accordion',
	        items: [item1, item2, item3, item4, item5]
	    };
		
   
		Sbi.kpi.KpiAccordionPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.kpi.KpiAccordionPanel , Ext.Panel, {});