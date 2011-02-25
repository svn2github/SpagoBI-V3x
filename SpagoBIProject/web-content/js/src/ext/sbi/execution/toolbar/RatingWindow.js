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
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.execution.toolbar");

Sbi.execution.toolbar.RatingWindow = function(config) {
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['showRatingService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'RATING_ACTION'
		, baseParams: params
	}) + '&MESSAGEDET=GOTO_DOCUMENT_RATE&OBJECT_ID=' + config.OBJECT_ID;
	
	this.buddy = undefined;
	
	var c = Ext.apply({}, config, {
		id:'win_metadata',
		bodyCfg: {
			tag:'div',
			cls:'x-panel-body',
			children:[{
				tag:'iframe',
			    name: 'ratingIFrame', 
        		id: 'ratingIFrame', 
  				src: this.services['showRatingService'],
  				frameBorder:0,
  				width:'100%',
  				height:'100%',
  				style: {overflow:'auto'}  
			}]
		},
		layout:'fit',
		width:230,
		height:360,
		//closeAction:'hide',
		plain: true,
		title: LN('sbi.execution.rating'),
		scripts: true, 
		buttons: [
		     {
		    	 text: LN('sbi.execution.rating.vote'), 
		    	 handler: function() {
		    	 	ratingIFrame.saveDL(); 
		     	 }
		     } 
		], 
		buttonAlign : 'left'
	});   
	
	// constructor
    Sbi.execution.toolbar.RatingWindow.superclass.constructor.call(this, c);
    
    if (this.buddy === undefined) {
    	this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
    }
    
};

Ext.extend(Sbi.execution.toolbar.RatingWindow, Ext.Window, {});