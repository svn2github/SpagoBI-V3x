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

Sbi.kpi.KpiGridPanel =  function(config) {
	
		var json = config.json;

		var defaultSettings = {
	        title: config.title,
	        region: 'center',
	        enableDD: true
			,loader: new Ext.tree.TreeLoader() // Note: no dataurl, register a TreeLoader to make use of createNode()
			,root: new Ext.tree.AsyncTreeNode({
				text: 'KPI root',
				id:'name',
				children: json
				
			}),
			rootVisible:false
				
		};

		if (Sbi.settings && Sbi.settings.kpi && Sbi.settings.kpi.kpiGridPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.kpi.kpiGridPanel);
		}

		var c = Ext.apply(defaultSettings, config || {});

		Ext.apply(this, c);
		
		this.initGrid();
		
		Sbi.kpi.KpiGridPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.kpi.KpiGridPanel ,Ext.ux.tree.TreeGrid, {
	columns: null
	
	, initGrid: function(){
		var kpiColumns = new Array();
		
		var col = {header:'Model Instance',
		dataIndex:'name',
		width:200};
		kpiColumns.push(col);
		
		var col1 = {header:'Actual',
		dataIndex: 'actual',
		width: 100};
		kpiColumns.push(col1);	
		
		var col2 = {header:'Target',
		dataIndex:'target',
		width: 70};
		kpiColumns.push(col2);
		
		var col3 = {header:'Status',
		dataIndex:'status',
		//xtype:'templatecolumn', 
		width:70};
		kpiColumns.push(col3);
		
		var col4 = {header:'Trend',
		dataIndex:'trend',
		width:70};
		kpiColumns.push(col4);
		
		this.columns = kpiColumns;


	}
	
});