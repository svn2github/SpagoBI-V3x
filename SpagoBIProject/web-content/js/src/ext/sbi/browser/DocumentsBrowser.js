/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

Ext.ns("Sbi.browser");

Sbi.browser.DocumentsBrowser = function(config) {    
   
	// sub-components   
	
	
	this.rootFolderId = config.rootFolderId || null;
	this.selectedFolderId = this.rootFolderId;
	
	this.treePanel = new Sbi.browser.DocumentsTree({
        border: true
        , rootNodeId: this.selectedFolderId 
    });
	
	this.filterPanel = new Sbi.browser.FilterPanel({
        title: LN('sbi.browser.filtrpanel.title')
        , border:true
        , metaFolder: config.metaFolder
        , metaDocument: config.metaDocument	
    });
	
	if (Sbi.user.functionalities.contains('DoMassiveExportFunctionality')) {
		this.progressPanel = new Sbi.browser.ProgressPanel({
			title: LN('sbi.browser.progresspanel.title')
			, border:true
			, metaFolder: config.metaFolder
			, metaDocument: config.metaDocument	
		});
	}
	
	
	this.searchPanel = new Sbi.browser.SearchPanel({
        title: LN('sbi.browser.searchpanel.title')
        , border:true
        , metaDocument: config.metaDocument	
    });
		
	this.westRegionContainer = new Ext.Panel({
	       id:'westRegionContainer',
	       split:true,
	       border:true,
	       frame:true,
	       collapsible: true,
	       //margins:'0 0 0 15',
	       layout:'accordion',
	       layoutConfig:{
	          animate:true
	       },
	       items: [
	               this.treePanel
	               , this.filterPanel
	               , this.searchPanel
	       ]
	});

	if(this.progressPanel){
	// defined and added only if user has massive export functionality	
		this.westRegionContainer.add(this.progressPanel);
	}
	
	
	this.detailPanel = new Sbi.browser.FolderDetailPanel({ 
		layout: 'fit'
        , metaFolder: config.metaFolder
        , metaDocument: config.metaDocument	
        , folderId: this.selectedFolderId
        //, closable:false
        //, title: 'Browser'
    });
    
	
	this.mainTab = new Ext.Panel({ 
		layout: 'fit'
        , title: 'Browser'
        , iconCls: 'icon-browser-tab'
        , items: [this.detailPanel]
    });
	
	
	
	this.centerContainerPanel = new Ext.TabPanel({
		 region: 'center'
		 
		 , resizeTabs:true
		 , minTabWidth: 115
		 , tabWidth:135
		 , enableTabScroll:true
		 , defaults: {autoScroll:true}
		 , activeItem: 0
			 
		 , items: [this.mainTab]
	});
	
	// if browser is IE, re-inject parent.execCrossNavigation function in order to solve parent variable conflict that occurs when 
	// more iframes are built and the same function in injected: it is a workaround that let cross navigation work properly
	if (Ext.isIE) {
		this.centerContainerPanel.on(
				'tabchange',
				function () {
					var anActiveTab = this.centerContainerPanel.getActiveTab();
					if (anActiveTab.activeDocument !== undefined) {
						try {
							var documentPage = anActiveTab.activeDocument.documentExecutionPage;
							if (documentPage.isVisible()) {
								var scriptFn = 	"parent.execCrossNavigation = function(d,l,p,s,ti,t) {" +
												"	sendMessage({'label': l, parameters: p, windowName: d, subobject: s, target: t, title: ti},'crossnavigation');" +
												"};";
								documentPage.miframe.iframe.execScript(scriptFn, true);
							}
						//} catch (e) {alert(e);}
						} catch (e) {}
					}
				}
				, this
		);
	}
	
	//send messages about enable or disable datastore refreh action (for console engine) 
	this.centerContainerPanel.on(
	   'beforetabchange',
	   function (tabPanel, newTab, currentTab ) {
	//	   alert('tabPanel: ' + tabPanel + ' newTab: ' + newTab + ' currentTab: ' + currentTab);
		    if(currentTab && currentTab.activeDocument && currentTab.activeDocument.documentExecutionPage) {
		    	currentTab.activeDocument.documentExecutionPage.miframe.sendMessage('Disable datastore', 'hide');
		    }
		    if(newTab.activeDocument && newTab.activeDocument.documentExecutionPage){
		    	newTab.activeDocument.documentExecutionPage.miframe.sendMessage('Enable datastore', 'show');
		    }
	   }
	   , this
	);
	
	config.baseLayout = config.baseLayout || {}; 	
	var c = Ext.apply({}, config.baseLayout, {
		layout: 'border',
	    border: false,
	    items: [ 
	            // CENTER REGION ---------------------------------------------------------
	            this.centerContainerPanel, 
	            // WEST REGION -----------------------------------------------------------
	            new Ext.Panel({               
	                region: 'west',
	                border: false,
	                frame: false,
	                //margins: '0 0 3 3',
	                collapsible: true,
	                collapsed: false,
	                hideCollapseTool: true,
	                titleCollapse: true,
	                collapseMode: 'mini',
	                split: true,
	                autoScroll: false,
	                width: 280,
	                minWidth: 280,
	                layout: 'fit',
	                items: [this.westRegionContainer]
	              })
	            // NORTH HREGION -----------------------------------------------------------
	            /*
	          	,new Sbi.browser.Toolbar({
	            	region: 'north',
	            	margins: '3 3 3 3',
	            	autoScroll: false,
	            	height: 30,
	            	layout: 'fit'
	          	})
	          	*/
	        ]
	});   
    
    Sbi.browser.DocumentsBrowser.superclass.constructor.call(this, c);
    
    this.treePanel.addListener('click', this.onTreeNodeClick, this);
   
    
    this.detailPanel.addListener('onfolderload', this.onFolderLoad, this);
    this.detailPanel.addListener('ondocumentclick', this.onDocumentClick, this);
    
    this.detailPanel.addListener('onfolderclick', this.onFolderClick, this);
    this.detailPanel.addListener('onbreadcrumbclick', this.onBreadCrumbClick, this);
    
    this.searchPanel.addListener('onsearch', this.onSearch, this);
    this.searchPanel.addListener('onreset', this.onReset, this);
    
    this.filterPanel.addListener('onsort', this.onSort, this);
    this.filterPanel.addListener('ongroup', this.onGroup, this);
    this.filterPanel.addListener('onfilter', this.onFilter, this);
   
    if(this.progressPanel){ 
    	this.progressPanel.addListener('click', this.onTreeNodeClick, this);
    }
    
   
    
    
}




Ext.extend(Sbi.browser.DocumentsBrowser, Ext.Panel, {
    	
	rootFolderId: null
    , selectedFolderId: null
    
	, westRegionContainer: null
    , treePanel: null
    , filterPanel: null
    , searchPanel: null
    , progressPanel: null
    
    , centerRegionContainer: null
    , detailPanel: null
    , executionPanel: null
    
    
    , selectFolder: function(folderId) {
		this.detailPanel.loadFolder(folderId, this.rootFolderId);
		this.selectedFolderId = folderId;
		this.searchPanel.selectedFolderId = folderId;
	}
	
	, onFolderLoad: function(panel) {
		if(this.centerContainerPanel.getActiveTab() != this.mainTab) {
			this.centerContainerPanel.setActiveTab(this.mainTab);
		}
		
	}
    
    
    , onTreeNodeClick: function(node, e) {
		this.selectFolder(node.id);
	}
	,onCrossNavigation: function(config){
		this.onCrossNavigationDocumentClick(config);
		return false;
	}
	
	, onCrossNavigationDocumentClick: function(r) {
		
		var config = Ext.apply({
			title: r.document.title !== undefined ? r.document.title : r.document.name
			, closable: true
		}, r);
		
		var executionPanel = new Sbi.execution.ExecutionPanel(config, r.document);
		
		executionPanel.addListener('crossnavigationonothertab', this.onCrossNavigation, this);
		
		this.centerContainerPanel.add(executionPanel).show();
		
		executionPanel.execute();
	}

	, onDocumentClick: function(panel, doc) {
		
		var executionPanel = new Sbi.execution.ExecutionPanel({
			title: doc.title !== undefined ? doc.title : doc.name
			, closable: true
		}, doc);
		
		executionPanel.addListener('crossnavigationonothertab', this.onCrossNavigation, this);
		
		this.centerContainerPanel.add(executionPanel).show();
		
		executionPanel.execute();
	}
	
	, onFolderClick: function(panel, r) {
		this.selectFolder(r.id);
	}
	
	
	, onBreadCrumbClick: function(panel, b) {
		this.selectFolder(b.id);
	}
	
	, onSearch: function(panel, q) {
		if(this.rootFolderId) q.rootFolderId = this.rootFolderId;
		this.detailPanel.searchFolder(q);
	}
	
	, onSort: function(panel, cb) {
		this.detailPanel.sort('Documents', cb.inputValue);
	}
	
	, onReset: function(panel, cb) {
		this.selectFolder(this.selectedFolderId);
	}
	
	, onGroup: function(panel, cb) {
		this.detailPanel.group('Documents', cb.inputValue);
	}
	
	, onFilter: function(panel, cb) {
		this.detailPanel.filter(cb.inputValue);
	}
});