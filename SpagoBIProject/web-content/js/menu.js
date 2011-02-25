/*
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

//***************************************************************
//******** Functions for nested menu class **********************
//***************************************************************  
 var contextName = "";
 
 function openmenuNM(idmenu) {
	  	try {
	  		status = $(idmenu).style.display;
	  		menuopened=getCookie('menuopened');
	  		if(status=='inline') {
	  			$(idmenu).style.display = 'none';
	  			if(menuopened!=null) {
					newmenuopened = '';
					idmenusop = menuopened.split(",");
					for(i=0; i<idmenusop.length; i++) {
						idmenuop = idmenusop[i];
						if( (idmenuop!=idmenu) && (idmenuop!='') ) {
							newmenuopened = newmenuopened + idmenuop + ",";
						}
					}
					setCookie('menuopened',newmenuopened,365)
				}
	  		} else {
	  			$(idmenu).style.display = 'inline';
				if (menuopened==null) {
					setCookie('menuopened',(idmenu+','),365)
				} else {
		    		menuopened = menuopened + idmenu + ',';
		    		setCookie('menuopened',menuopened,365)
				}
	  		}
	  	} catch (e) {
			alert('Cannot open menu ...');
      	}
  }
	
	
 function openmenusNMFunct() {
    	menuopened=getCookie('menuopened');
    	if(menuopened!=null) {
    		idmenus = menuopened.split(',');
    		for(i=0; i<idmenus.length; i++) {
    			idmenu = idmenus[i];
    			try {
    				$(idmenu).style.display = 'inline';
    			} catch (err) {
    				/* ignore: the menu is not in this pager (maybe another tab) */
    			}
    		}
    	}
 }
  
  
//***************************************************************
//******** Functions for slide show menu class **********************
//***************************************************************  
  
  var tCloseAll = null;
  
  
  function closeAll() {
    for(i=0; i<menulevels.length; i++) {
      menulevel = menulevels[i];
      idMenu = 'menu_' + menulevel[0];
      $(idMenu).style.display='none';
    } 
  }
  
 	function overHandler(obj, level, e, idfolder) {
      window.clearTimeout(tCloseAll);
	    for(i=0; i<menulevels.length; i++) {
	      menulevel = menulevels[i];
	      if(menulevel[0] != idfolder) {
		      deepMenu = menulevel[1];
		      idMenu = 'menu_' + menulevel[0];
		      if(parseInt(deepMenu)>parseInt(level)) {
		        $(idMenu).style.display='none';
		      }
		   }   
	    } 
		obj.className = 'menuItemOver';
	}
	
	
	function outHandler(obj, e) {
		obj.className = 'menuItem';
		tCloseAll = setTimeout('closeAll()', 500);
 	}
	
	function openmenu(idfolder, e) {
	  	try {
          var idmenu = 'menu_' + idfolder;
    	  	var idmenulink = 'menu_link_' + idfolder;
    	  	var idmenulinklast = 'menu_link_last_' + idfolder;
    	  	var trlink = $(idmenulink);
    	  	if(trlink==null) return;
    	  	var tdlinklast = $(idmenulinklast);
    	  	if(tdlinklast==null) return;
    	    var postrlink = findPos(trlink);
    	    if(postrlink==null) return;
    	    var postrlinklast = findPos(tdlinklast);
    		  var wid = postrlinklast[0] - postrlink[0] + 25;
          $(idmenu).style.left= wid + postrlink[0] + 4 + 'px';
          $(idmenu).style.top= (postrlink[1]-15) + 'px';
          $(idmenu).style.display= 'inline';
      } catch (e) {

      }
	}
	
	function checkclosemenu(idfolder, e) {  
    	var idmenu = 'menu_' + idfolder;
	  	var idmenulink = 'menu_link_' + idfolder;
	  	var idmenulinklast = 'menu_link_last_' + idfolder;
		  var trlink = $(idmenulink);
	  	var tdlinklast = $(idmenulinklast);
	    var postrlink = findPos(trlink);
	    var postrlinklast = findPos(tdlinklast);
		  var wid = postrlinklast[0] - postrlink[0] + 25;
		  hei = 30;	  
      if( (e.clientY <= (postrlink[1]+3)) || (e.clientY > (postrlink[1] + hei - 3)) || (e.clientX <= (postrlink[0]+3)) ) {
            $(idmenu).style.display= 'none';
      }
	}
	

//***************************************************************
//******** Functions for execution document menu **********************
//*************************************************************** 
function setContextName(context){
		contextName = context;
}
 
function closeLoadingDocumentMenu() {
 	var load = document.getElementById('loadingDocumentMenu');
 	load.style.display='none';
 	var frame = document.getElementById('frameDocumentMenu');
 	frame.style.visibility='visible';
 }
 winDocumentMenu=null;
 function open_win_DocumentMenu(src) { 
 	if(winDocumentMenu==null) { 
		winDocumentMenu = new Window('winDocumentMenu', {className: "alphacube", title: "", resizable:true, destroyOnClose:false, width:800, height:500});
		winDocumentMenu.getContent().innerHTML="<div style='position:absolute;top:30px;left:30px;' id='loadingDocumentMenu'><center><br/><br/><span style='font-size:13pt;font-weight:bold;color:darkblue;'>Loading</span><br/><br/><img src='/SpagoBI/themes/sbi_default/img/wapp/loading.gif' /></center></div><iframe id='frameDocumentMenu' onload='parent.closeLoadingDocumentMenu()' style='width:800px;height:500px;visibility:hidden;' frameborder='0' scrolling='auto' noresize  src='"+src+"' />";
		winDocumentMenu.showCenter(false);
		observerResizeDocumentMenu = {
			onResize: function(eventName, win) {
				if(win == winDocumentMenu) { 
					var heightwin = win.getSize().height;
				 	var widthwin = win.getSize().width;
				 	var frameapp = document.getElementById('frameDocumentMenu');
				 	frameapp.style.height=heightwin + 'px';
				 	frameapp.style.width=widthwin + 'px'; 
				}
			}
		}
		observerMaximizeDocumentMenu = {
			onMaximize: function(eventName, win) {
				if(win == winDocumentMenu) { 
					var heightwin = win.getSize().height;
				 	var widthwin = win.getSize().width;
					if(win.isMaximized()) {
						if(isMoz()) {
					    	heightwin = (top.innerHeight)-60;
				 	    	widthwin = (top.innerWidth)-40;
				    	}
						if(isIE7()) {
					    	heightwin = heightwin-80;
				 	    	widthwin = widthwin-20;
				    	}
				    }
				 	win.setSize(widthwin, heightwin);
				 	var frameapp = document.getElementById('frameDocumentMenu');
				 	frameapp.style.height=heightwin + 'px';
				 	frameapp.style.width=widthwin + 'px'; 
		            win.showCenter();
				}
			}
		}
		observerMinimizeDocumentMenu = {
			onMinimize: function(eventName, win) {
				if(win == winDocumentMenu) { 
					var heightwin = win.getSize().height;
				 	var widthwin = win.getSize().width;
					if(win.isMinimized()) {
				 		var frameapp = document.getElementById('frameDocumentMenu');
				 		frameapp.style.display='none';
				 	} else {
				 		var frameapp = document.getElementById('frameDocumentMenu');
				 		frameapp.style.display='inline';
				 	}
				}
			}
		}
		observerCloseDocumentMenu = {
			onClose: function(eventName, win) {
				if(win == winDocumentMenu) { 
 					win.destroy();
 					winDocumentMenu=null;
				}
			}
		}
		Windows.addObserver(observerResizeDocumentMenu);
		Windows.addObserver(observerMaximizeDocumentMenu);
		Windows.addObserver(observerMinimizeDocumentMenu);
		Windows.addObserver(observerCloseDocumentMenu);
	} else {
		winDocumentMenu.show(false);
	}
}