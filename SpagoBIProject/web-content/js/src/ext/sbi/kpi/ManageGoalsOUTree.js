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
 * Authors - Monica Franceschini
 */
Ext.ns("Sbi.kpi");

Sbi.kpi.ManageGoalsOUTree = function(config, ref) { 
	
	this.addEvents();
	var paramsOUChildList = {LIGHT_NAVIGATOR_DISABLED: 'TRUE',MESSAGE_DET: "OU_CHILDS_LIST"};
	this.configurationObject = {};

	this.configurationObject.manageTreeService = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'MANAGE_OUS_ACTION'
			, baseParams: paramsOUChildList
	});	

	//reference to viewport container
	this.referencedCmp = ref;
	this.initConfigObject();
	
	config.configurationObject = this.configurationObject;
	
	var c = Ext.apply({}, config || {}, {});
	Sbi.kpi.ManageGoalsOUTree.superclass.constructor.call(this, c);	 	
	this.on('render', function(){this.getRootNode().expand(false, /*no anim*/false);}, this)
};

Ext.extend(Sbi.kpi.ManageGoalsOUTree, Sbi.widgets.SimpleTreePanel, {
	
	configurationObject: null
	, gridForm:null
	, mainElementsStore:null
	, root:null
	, referencedCmp : null

	,initConfigObject: function(){

		var thisPanel = this;
		this.treeLoader=new Ext.tree.TreeLoader({
			nodeParameter: 'nodeId',
			dataUrl: thisPanel.configurationObject.manageTreeService,
			createNode: function(attr) {
				
				if (attr.nodeId) {
					attr.id = attr.nodeId;
				}
				if (attr.ou!=null) {
					attr.text = attr.ou.name;
					attr.qtip = attr.ou.name;
				}
				var node = Ext.tree.TreeLoader.prototype.createNode.call(this, attr);
				return node;
			}
		}); 
    }


});
