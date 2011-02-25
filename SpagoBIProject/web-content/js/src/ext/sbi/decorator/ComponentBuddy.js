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
  * ComponentBuddy 
  * 
  * follows the twin element making it always rendered 
  * on top of otherones in the page
  * 
  * 
  * Public Properties
  * 
  * - buddy: the twin element
  * 
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.commons.ComponentBuddy");

Sbi.commons.ComponentBuddy = function(config) {
    
    this.buddy = undefined;
    
    Ext.apply(this, config);
	
	//this.addEvents();	
    
    // sub-components    
   
    var el =  Ext.get(Ext.DomHelper.append( Ext.getBody(), {
                            tag : 'iframe',
                            frameborder : 0,
                            html : 'Inline frames are NOT enabled\/supported by your browser.',
                            src : (Ext.isIE && Ext.isSecure)? Ext.SSL_SECURE_URL: 'about:blank'}));
                    
    el.applyStyles({
      position: 'absolute',
      'background-color': 'white',
      'z-index': 1000,
      width: '400px',
	  height: '400px',
      opacity: 1
      
    });
    
    Sbi.commons.ComponentBuddy.superclass.constructor.call(this, el.dom.id, true);
    
    this.buddy.fixEl = this;
        
    this.buddy.addListener('render', function(){this.keepInTouchWithBuddy('render');}, this);
    this.buddy.addListener('show', function(){this.keepInTouchWithBuddy('show');}, this);
    this.buddy.addListener('move', function(){this.keepInTouchWithBuddy('move');}, this);
    this.buddy.addListener('hide', function(){this.keepInTouchWithBuddy('hide');}, this);
    
    
    
    if(this.buddy.dd) {
    	this.buddy.dd.onDrag = function(e) {
        	this.buddy.dd.alignElWithMouse(this.buddy.dd.proxy, e.getPageX(), e.getPageY());
            this.buddy.dd.alignElWithMouse(this, e.getPageX()-1, e.getPageY());
        };
    }
    
     
}




Ext.extend(Sbi.commons.ComponentBuddy, Ext.Element, {
    
    // static contens and methods definitions
    keepInTouchWithBuddy : function(eventName){
      
      var box = this.buddy.getBox();
      box.width += 3;
      box.height += 3;
      this.setBox( box );
      
      if(eventName === 'hide') {
      	this.hide();
      } else if (eventName === 'show') {
      	this.show();
      	if(this.buddy.dd) {
	    	this.buddy.dd.fixEl = this;
	    	this.buddy.dd.onDrag = function(e) {
	        	this.alignElWithMouse(this.proxy, e.getPageX(), e.getPageY());
	            this.alignElWithMouse(this.fixEl, e.getPageX()-1, e.getPageY());
	        };
    	  }
      } else if (eventName === 'render') {
      	 if(this.buddy.dd) {
	    	this.buddy.dd.onDrag = function(e) {
	        	this.buddy.dd.alignElWithMouse(this.buddy.dd.proxy, e.getPageX(), e.getPageY());
	            this.buddy.dd.alignElWithMouse(this, e.getPageX()-1, e.getPageY());
	        };
    	  }
      }
    }
    
});

