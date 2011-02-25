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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.execution");

Sbi.execution.ViewpointsPanel = function(config, doc) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getViewpointsService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_VIEWPOINTS_ACTION'
		, baseParams: params
	});
	this.services['deleteViewpointService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_VIEWPOINTS_ACTION'
		, baseParams: params
	});
	
	this.addEvents('executionrequest', 'applyviewpoint');
	 
    this.store = new Ext.data.JsonStore({
        root: 'results'
        , idProperty: 'name'
        , fields: ['name', 'owner', 'description', 'scope', 
                   {name:'creationDate', type:'date', dateFormat: Sbi.config.clientServerTimestampFormat}, 
                   'parameters'
                   ]
		, url: this.services['getViewpointsService']
    }); 
    this.store.on('loadexception', function(store, options, response, e){
    	Sbi.exception.ExceptionHandler.handleFailure(response, options);
    }, this);
    
    this.execColumn = new Ext.grid.ButtonColumn({
	       header:  '',
	       dataIndex: 'execute',
	       iconCls: 'icon-execute',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.store.getAt(index);
	          var viewpointId = selectedRecord.get('id');
	          this.grid.fireEvent('executionrequest', selectedRecord.get('parameters'));
	       },
	       width: 25,
	       renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
	});
    
    this.applyColumn = new Ext.grid.ButtonColumn({
	       header:  '',
	       dataIndex: 'apply',
	       iconCls: 'icon-apply-viewpoint',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.store.getAt(index);
	          var viewpointId = selectedRecord.get('id');
	          this.grid.fireEvent('applyviewpoint', selectedRecord.get('parameters'));
	       },
	       width: 25,
	       renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
	});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    
    this.shortcutsHiddenPreference = config.shortcutsHidden !== undefined ? config.shortcutsHidden : false;
    
	this.isHidden = !Sbi.user.functionalities.contains('SeeViewpointsFunctionality')
	        		||
    				this.shortcutsHiddenPreference;

	var c = Ext.apply({}, config, {
        store: this.store
        , columns: [
              {header: LN('sbi.execution.viewpoints.name'), sortable: true, width: 50, dataIndex: 'name'}
            , {header: LN('sbi.execution.viewpoints.description'), sortable: true, width: 50, dataIndex: 'description'}
            , {header: LN('sbi.execution.viewpoints.owner'), sortable: true, width: 50,dataIndex: 'owner'}
            , {header: LN('sbi.execution.viewpoints.scope'), sortable: true, width: 50, dataIndex: 'scope'}
            , {header: LN('sbi.execution.viewpoints.creationDate'), sortable: true, width: 50, dataIndex: 'creationDate', renderer: Ext.util.Format.dateRenderer(Sbi.config.localizedDateFormat)} 
            , this.applyColumn
            , this.execColumn
            , sm
        ]
        , plugins: [ this.applyColumn, this.execColumn ]
		, viewConfig: {
        	forceFit: true
        	, emptyText: LN('sbi.execution.viewpoints.emptyText')
		}
        , tbar:[
           '->'
           , {
        	   text: LN('sbi.execution.subobjects.deleteSelected')
        	   , tooltip: LN('sbi.execution.subobjects.deleteSelectedTooltip')
        	   , iconCls:'icon-remove'
        	   , scope: this
        	   , handler : this.deleteSelectedSubObjects
           	}
        ]
        , collapsible: false
        , title: LN('sbi.execution.viewpoints.title')
        , autoScroll: true
        , sm : sm
        , height: 200
        , width:260
        , hidden: this.isHidden
	});   
	
	// constructor
    Sbi.execution.ViewpointsPanel.superclass.constructor.call(this, c);
    
    this.on('rowdblclick', this.onRowDblClick, this);
    
   
    
};

Ext.extend(Sbi.execution.ViewpointsPanel, Ext.grid.GridPanel, {
	
	services: null
	, executionInstance: null
	, store: null
	 
    // public methods
	
	, synchronize: function( executionInstance ) {
		this.executionInstance = executionInstance;
		if (this.isHidden === false) {
			this.store.load({params: this.executionInstance});
		}
	}

	, addViewpoints: function( viewpoints ) {
		if(viewpoints instanceof Array) {
			// it's ok	
		} else {
			viewpoints = [viewpoints];
		}
		
		var records = [];
		for(var i = 0; i < viewpoints.length; i++) {
			records[i] = new this.store.recordType( viewpoints[i] );
		}
		this.store.add(records);
	}

	, deleteSelectedSubObjects: function() {
		var recordsSelected = this.getSelectionModel().getSelections();
		if (recordsSelected && recordsSelected.length > 0) {
			var ids = new Array();
			for (var count = 0; count < recordsSelected.length; count++) {
				ids[count] = recordsSelected[count].get('name');
			}
			var idsJoined = ids.join(',');
			
			Ext.Ajax.request({
		        url: this.services['deleteViewpointService'],
		        params: {'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID, 'viewpoint_ids': idsJoined},
		        callback : function(options , success, response) {
		  	  		if(success) {
		  	  			// removes the subobjects from the store
		  	  			for (var count = 0; count < recordsSelected.length; count++) {
		  	  				this.store.remove(recordsSelected[count]);
		  	  			}
		  	  		} else { 
		  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot detele customized views', 'Service Error');
		  	  		}
		        },
		        scope: this,
				failure: Sbi.exception.ExceptionHandler.handleFailure      
			});
			
		} else {
			Sbi.exception.ExceptionHandler.showWarningMessage(LN('sbi.execution.subobjects.noSubObjectsSelected'), 'Warning');
		}
	}
	
	, onRowDblClick: function (grid, rowIndex, event) {
	    	var selectedRecord =  grid.getStore().getAt(rowIndex);
	    	var viewpointId = selectedRecord.get('id');
	    	this.fireEvent('executionrequest', selectedRecord.get('parameters'));
	    }
	
});