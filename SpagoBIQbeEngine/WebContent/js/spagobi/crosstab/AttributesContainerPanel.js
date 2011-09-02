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

Ext.ns("Sbi.crosstab");

Sbi.crosstab.AttributesContainerPanel = function(config) {
	
	var defaultSettings = {
	};
	
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.attributesContainerPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.attributesContainerPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c); // this operation should overwrite this.initialData content, that is initial grid's content
	
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE'};
	this.services = this.services || new Array();
	
	this.services['getValues'] = this.services['getValues'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_VALUES_FOR_CROSSTAB_ATTRIBUTES_ACTION'
		, baseParams: params
	});
	
	this.init(c);
	
	Ext.apply(c, {
        store: this.store
        , cm: this.cm
        , enableDragDrop: true
        , ddGroup: this.ddGroup || 'crosstabDesignerDDGroup'
	    , layout: 'fit'
	    , viewConfig: {
	    	forceFit: true
	    }
		, tools: [
	          {
	        	  id: 'close'
	        	, handler: this.removeAllAttributes
	          	, scope: this
	          	, qtip: LN('sbi.crosstab.attributescontainerpanel.tools.tt.removeall')
	          }
		]
        , listeners: {
			render: function(grid) { // hide the grid header
				grid.getView().el.select('.x-grid3-header').setStyle('display', 'none');
    		}
        	, keydown: function(e) { 
        		if (e.keyCode === 46) {
        			this.removeSelectedAttributes();
      	      	}      
      	    }
        	, mouseover: function(e, t) {
        		this.targetRow = t; // for Drag&Drop
        	}
        	, mouseout: function(e, t) {
        		this.targetRow = undefined;
        	}
		}
        , scope: this
        , type: 'attributesContainerPanel'
	});	
	
	// constructor
    Sbi.crosstab.AttributesContainerPanel.superclass.constructor.call(this, c);
    
    this.on('render', this.initDropTarget, this);
    
    this.on('rowdblclick', this.rowDblClickHandler, this);
    
};

Ext.extend(Sbi.crosstab.AttributesContainerPanel, Ext.grid.GridPanel, {
	
	initialData: undefined
	, targetRow: null
	, calculateTotalsCheckbox: null
	, calculateSubtotalsCheckbox: null
	, Record: Ext.data.Record.create([
	      {name: 'id', type: 'string'}
	      , {name: 'alias', type: 'string'}
	      , {name: 'funct', type: 'string'}
	      , {name: 'iconCls', type: 'string'}
	      , {name: 'nature', type: 'string'}
	      , {name: 'values', type: 'string'}
	])
	
	, init: function(c) {
		this.initStore(c);
		this.initColumnModel(c);
	}
	
	, initStore: function(c) {
		this.store =  new Ext.data.ArrayStore({
	        fields: ['id', 'alias', 'funct', 'iconCls', 'nature', 'values']
		});
		// if there are initialData, load them into the store
		if (this.initialData !== undefined) {
			for (i = 0; i < this.initialData.length; i++) {
				var record = new this.Record(this.initialData[i]);
	  			this.store.add(record);
			}
		}
	}
	
	, initColumnModel: function(c) {
        this.template = new Ext.Template( // see Ext.Button.buttonTemplate and Button's onRender method
        		// margin auto in order to have button center alignment
                '<table style="margin-left: auto; margin-right: auto;" id="{4}" cellspacing="0" class="x-btn {3}"><tbody class="{1}">',
                '<tr><td class="x-btn-tl"><i>&#160;</i></td><td class="x-btn-tc"></td><td class="x-btn-tr"><i>&#160;</i></td></tr>',
                '<tr><td class="x-btn-ml"><i>&#160;</i></td><td class="x-btn-mc"><button type="{0}" class=" x-btn-text {5}"></button>{6}</td><td class="x-btn-mr"><i>&#160;</i></td></tr>',
                '<tr><td class="x-btn-bl"><i>&#160;</i></td><td class="x-btn-bc"></td><td class="x-btn-br"><i>&#160;</i></td></tr>',
                '</tbody></table>');
        
        this.template.compile();
		
	    var fieldColumn = new Ext.grid.Column({
	    	header:  ''
	    	, dataIndex: 'alias'
	    	, hideable: false
	    	, hidden: false	
	    	, sortable: false
	   	    , renderer : function(value, metaData, record, rowIndex, colIndex, store){
	        	return this.template.apply(
	        			['button', 'x-btn-small x-btn-icon-small-left', '', 'x-btn-text-icon', Ext.id(), record.data.iconCls, record.data.alias]		
	        	);
	    	}
	        , scope: this
	    });
	    this.cm = new Ext.grid.ColumnModel([fieldColumn]);
	}

	, initDropTarget: function() {
		this.removeListener('render', this.initDropTarget, this);
		var dropTarget = new Sbi.widgets.GenericDropTarget(this, {
			ddGroup: this.ddGroup || 'crosstabDesignerDDGroup'
			, onFieldDrop: this.onFieldDrop
		});
	}

	, onFieldDrop: function(ddSource) {
		
		if (ddSource.grid && ddSource.grid.type && ddSource.grid.type === 'queryFieldsPanel') {
			// dragging from QueryFieldsPanel
			this.notifyDropFromQueryFieldsPanel(ddSource);
		} else if (ddSource.grid && ddSource.grid.type && ddSource.grid.type === 'attributesContainerPanel') {
			// dragging from AttributesContainerPanel
			this.notifyDropFromAttributesContainerPanel(ddSource);
		} else if (ddSource.grid && ddSource.grid.type && ddSource.grid.type === 'measuresContainerPanel') {
			Ext.Msg.show({
				   title: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.title'),
				   msg: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.measures'),
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.WARNING
			});
		}
		
	}
	
	, notifyDropFromQueryFieldsPanel: function(ddSource) {
		var rows = ddSource.dragData.selections;
		for (var i = 0; i < rows.length; i++) {
			var aRow = rows[i];
			// if the attribute is already present show a warning
			if (this.store.find('id', aRow.data.id) !== -1) {
				Ext.Msg.show({
					   title: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.title'),
					   msg: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.attributealreadypresent'),
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.WARNING
				});
				return;
			}
			// if the field is a measure show a warning
			if (aRow.data.nature === 'measure') {
				Ext.Msg.show({
					   title: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.title'),
					   msg: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.measures'),
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.WARNING
				});
				return;
			}
			// if the field is a postLineCalculated show an error
			if (aRow.data.nature === 'postLineCalculated') {
				Ext.Msg.show({
					   title: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.title'),
					   msg: LN('sbi.crosstab.attributescontainerpanel.cannotdrophere.postlinecalculated'),
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.ERROR
				});
				return;
			}
			var newRecord = new this.Record(aRow.data);
			newRecord.data.values = '[]'; // init the 'values' property as empty
			this.store.add([newRecord]);
		}
	}
	
	, notifyDropFromAttributesContainerPanel: function(ddSource) {
		if (ddSource.grid.id === this.id) {
			// DD on the same AttributesContainerPanel --> re-order the fields
			var rows = ddSource.dragData.selections;
			if (rows.length > 1) {
				Ext.Msg.show({
					   title:'Drop not allowed',
					   msg: 'You can move only one field at a time',
					   buttons: Ext.Msg.OK,
					   icon: Ext.MessageBox.WARNING
				});
			} else {
				var row = rows[0];
				var rowIndex; // the row index on which the field has been dropped on
				if(this.targetRow) {
					rowIndex = this.getView().findRowIndex( this.targetRow );
				}
				if (rowIndex == undefined || rowIndex === false) {
					rowIndex = undefined;
				}
		           
	         	var rowData = this.store.getById(row.id);
            	this.store.remove(this.store.getById(row.id));
                if (rowIndex != undefined) {
                	this.store.insert(rowIndex, rowData);
                } else {
                	this.store.add(rowData);
                }
		         
		        this.getView().refresh();
				
			}
		} else {
			// DD on another AttributesContainerPanel --> moving the fields from rows to columns or from columns to rows
			var rows = ddSource.dragData.selections;
			ddSource.grid.store.remove(rows);
			this.store.add(rows);
		}
	}
	
	, getContainedAttributes: function () {
		var attributes = [];
		for(i = 0; i < this.store.getCount(); i++) {
			var record = this.store.getAt(i);
			attributes.push(record.data);
		}
		return attributes;
	}
	
	, setAttributes: function (attributes) {
		this.removeAllAttributes();
		for (var i = 0; i < attributes.length; i++) {
  			var attribute = attributes[i];
  			var record = new this.Record(attribute);
  			this.store.add(record); 
  		}
	}
	
	, removeSelectedAttributes: function() {
        var sm = this.getSelectionModel();
        var rows = sm.getSelections();
        this.store.remove(rows);
	}
	
	, removeAllAttributes: function() {
		this.store.removeAll(false);
	}
	
	, rowDblClickHandler: function(grid, rowIndex, event) {
		var store = this.createLookupStore(grid, rowIndex);
		var record = grid.store.getAt(rowIndex);
		var alias = record.get('alias');
     	var chooserWindow = new Sbi.widgets.SimpleValuesChooserWindow({
     		store : store
     		, columnHeader : "Values"
     		, columnName : "Values"
     	});
 		chooserWindow.on('beforeclose', this.updateValues.createDelegate(this, [record, chooserWindow], true), this);
 		chooserWindow.show();
 		store.on('load', this.selectValues.createDelegate(this, [record, chooserWindow], true), this);
 		var params = {
 			ALIAS : alias
 		};
 		// a global variable Sbi.formviewer.formEnginePanel is defined, it is the form state. Send it to the server
 		if (Sbi.formviewer && Sbi.formviewer.formEnginePanel) {
 			var formState = Sbi.formviewer.formEnginePanel.getFormState();
 			params.formState = Ext.encode(formState);
 		}
 		store.load({params: params});
	}
	
	, createLookupStore : function (grid, rowIndex) {
		var store = new Ext.data.JsonStore({
			url: this.services['getValues']
		});
		store.on('loadexception', function(store, options, response, e) {
			Sbi.exception.ExceptionHandler.handleFailure(response, options);
		});
		return store;	
	}
	
	, updateValues : function ( theWindow, record, chooserWindow ) {
		record.data.values = Ext.encode(chooserWindow.getSelectedValues());
	}
	
	, selectValues : function ( store, records, options, record, chooserWindow ) {
		chooserWindow.select(Ext.decode(record.data.values));
	}

});