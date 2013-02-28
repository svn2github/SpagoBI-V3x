/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

/**
 * Object name
 * 
 * [description]
 * 
 * Authors - Marco Cortella (marco.cortella@eng.it)
 */
Ext.ns("Sbi.engines");

Sbi.engines.EngineManagementPanel = function(config) {

	var defaultSettings = {
		singleSelection : true
	};

	if (Sbi.settings && Sbi.settings.engines
			&& Sbi.settings.engines.engineManagementPanel) {
		defaultSettings = Ext.apply(defaultSettings,
				Sbi.settings.engines.engineManagementPanel);
	}

	var c = Ext.apply(defaultSettings, config || {});
	c.configurationObject = this.initConfigObject();

	// Ext.apply(this, c);

	Sbi.engines.EngineManagementPanel.superclass.constructor.call(this, c);

	this.rowselModel.addListener('rowselect', function(sm, row, rec) {
		var record = this.rowselModel.getSelected();
		this.setValues(record);
		this.activateEngineDetailFields(null, rec, row);
		this.activateDataSourceCombo(null,rec);

	}, this);

};

Ext
.extend(
		Sbi.engines.EngineManagementPanel,
		Sbi.widgets.ListDetailForm,
				{
					// ---------------------------------------------------------------------------
					// object's members
					// ---------------------------------------------------------------------------
					configurationObject : null

					// ---------------------------------------------------------------------------
					// public methods
					// ---------------------------------------------------------------------------

					// ---------------------------------------------------------------------------
					// private methods
					// ---------------------------------------------------------------------------

					,
					initConfigObject : function() {

						this.configurationObject = this.configurationObject
								|| {};

						this.initMasterGridConf();

						this.initDataStoreServicesConf();
						this.initCrudServicesConf();
						this.initButtonsConf();
						this.initTabsConf();

						return this.configurationObject;
					}
					
					// DataStore Services initalization

					 , initDataStoreServicesConf: function() {
						 this.configurationObject = this.configurationObject ||
						 {};

						 this.configurationObject.getEngineTypesServiceUrl =
							 Sbi.config.serviceRegistry.getServiceUrl({
								 serviceName : 'DOMAIN_ACTION',
								 baseParams : {
									 MESSAGE_DET : "DOMAINS_FILTER", 
									 DOMAIN_TYPE: "ENGINE_TYPE"
								 }
							 });
						 
						 this.configurationObject.getDocumentTypesServiceUrl =
							 Sbi.config.serviceRegistry.getServiceUrl({
								 serviceName : 'DOMAIN_ACTION',
								 baseParams : {
									 MESSAGE_DET : "DOMAINS_FILTER", 
									 DOMAIN_TYPE: "BIOBJ_TYPE"
								 }
							 });
						
						 this.configurationObject.getDataSourcesServiceUrl =
							 Sbi.config.serviceRegistry.getServiceUrl({
								 serviceName : 'MANAGE_ENGINE_ACTION',
								 baseParams : {
									 MESSAGE_DET : "ENGINE_DATASOURCES", 
								 }
							 });

					 }

					,
					initCrudServicesConf : function() {
						this.configurationObject = this.configurationObject
								|| {};

						this.configurationObject.manageListService = Sbi.config.serviceRegistry
								.getServiceUrl({
									serviceName : 'MANAGE_ENGINE_ACTION',
									baseParams : {
										MESSAGE_DET : "ENGINE_LIST"
									}
								});

						this.configurationObject.saveItemService = Sbi.config.serviceRegistry
								.getServiceUrl({
									serviceName : 'MANAGE_ENGINE_ACTION',
									baseParams : {
										LIGHT_NAVIGATOR_DISABLED : 'TRUE',
										MESSAGE_DET : "ENGINE_INSERT"
									}
								});

						this.configurationObject.deleteItemService = Sbi.config.serviceRegistry
								.getServiceUrl({
									serviceName : 'MANAGE_ENGINE_ACTION',
									baseParams : {
										LIGHT_NAVIGATOR_DISABLED : 'TRUE',
										MESSAGE_DET : "ENGINE_DELETE"
									}
								});
					}

					,
					initMasterGridConf : function() {

						this.configurationObject = this.configurationObject
								|| {};

						this.configurationObject.fields = [ "id", "label",
								"name", "description", "documentType",
								"engineType", "useDataSet", "useDataSource",
								"class", "url", "driver", "secondaryUrl",
								"dataSourceId" ];

						this.configurationObject.emptyRecToAdd = new Ext.data.Record(
								{
									id : null,
									name : '',
									label : '',
									description : '',
									documentType : '',
									engineType : '',
									useDataSet : '',
									useDataSource : '',
									class : '',
									url : '',
									driver : '',
									secondaryUrl : '',
									dataSourceId : ''
								});

						this.configurationObject.gridColItems = [ {
							id : 'label',
							header : LN('sbi.generic.label'),
							width : 140,
							sortable : true,
							locked : false,
							dataIndex : 'label'
						}, {
							header : LN('sbi.generic.name'),
							width : 150,
							sortable : true,
							dataIndex : 'name'
						}, {
							header : LN('description'),
							width : 140,
							sortable : true,
							dataIndex : 'description'
						}

						];

						this.configurationObject.panelTitle = LN('sbi.ds.panelTitle');
						this.configurationObject.listTitle = LN('sbi.ds.listTitle');

						this.configurationObject.filter = true;
						this.configurationObject.columnName = [
								[ 'sbiDsConfig.label', LN('sbi.generic.label') ],
								[ 'sbiDsConfig.name', LN('sbi.generic.name') ] ];
						this.configurationObject.setCloneButton = true;
					}
					
					//Engine Combo listener
					,activateEngineDetailFields : function(combo, record, index) {
						//var engineSelected = record.get('engineType');
						var engineSelected = this.detailFieldEngineType.getRawValue();
						if (engineSelected != null
								&& engineSelected == 'EXT') {
							//Show External Engine properties
							
							this.detailFieldClass.setVisible(false);
							this.detailFieldClass.getEl().up('.x-form-item').setDisplayed(false);
							this.detailFieldUrl.setVisible(true);
							this.detailFieldUrl.getEl().up('.x-form-item').setDisplayed(true);
							this.detailFieldSecondaryUrl.setVisible(true);
							this.detailFieldSecondaryUrl.getEl().up('.x-form-item').setDisplayed(true);
							this.detailFieldDriverName.setVisible(true);
							this.detailFieldDriverName.getEl().up('.x-form-item').setDisplayed(true);
							
						} else {
							//Show Internal Engine properties
							this.detailFieldClass.setVisible(true);
							this.detailFieldClass.getEl().up('.x-form-item').setDisplayed(true);
							this.detailFieldUrl.setVisible(false);
							this.detailFieldUrl.getEl().up('.x-form-item').setDisplayed(false);
							this.detailFieldSecondaryUrl.setVisible(false);
							this.detailFieldSecondaryUrl.getEl().up('.x-form-item').setDisplayed(false);
							this.detailFieldDriverName.setVisible(false);
							this.detailFieldDriverName.getEl().up('.x-form-item').setDisplayed(false);

						}
					}
					
					//UseDatasource listener
					,activateDataSourceCombo : function(combo, checked) {
						var useDatasource = this.detailFieldUseDataSource.getValue();
						if (useDatasource){
							this.detailFieldDataSource.setVisible(true);
							this.detailFieldDataSource.getEl().up('.x-form-item').setDisplayed(true);
						} else {
							this.detailFieldDataSource.setVisible(false);
							this.detailFieldDataSource.getEl().up('.x-form-item').setDisplayed(false);
						}
					}
					

					,
					initButtonsConf : function() {
						this.configurationObject = this.configurationObject
								|| {};

						var tbButtonsArray = new Array();
						// this.tbProfAttrsButton = new Ext.Toolbar.Button({
						// text: LN('sbi.ds.pars'),
						// iconCls: 'icon-profattr',
						// handler: this.profileAttrs,
						// width: 30,
						// scope: this
						// });
						// tbButtonsArray.push(this.tbProfAttrsButton);

						// this.tbInfoButton = new Ext.Toolbar.Button({
						// text: LN('sbi.ds.help'),
						// iconCls: 'icon-info',
						// handler: this.info,
						// width: 30,
						// scope: this
						// });
						// tbButtonsArray.push(this.tbInfoButton);

						this.tbTransfInfoButton = new Ext.Toolbar.Button({
							text : LN('sbi.ds.help'),
							iconCls : 'icon-info',
							handler : this.transfInfo,
							width : 30,
							scope : this
						});
						tbButtonsArray.push(this.tbTransfInfoButton);
						this.configurationObject.tbButtonsArray = tbButtonsArray;
					}

					,
					initTabsConf : function() {
						this.configurationObject = this.configurationObject
								|| {};

						this.initDetailTab();
						this.configurationObject.tabItems = [ this.detailTab ];
					}

					,
					initDetailTab : function() {
						// this.profileAttributesStore = new
						// Ext.data.SimpleStore({
						// fields : [ 'profAttrs' ],
						// data : config.attrs,
						// autoLoad : false
						// });

						// Store of the combobox
						this.documentTypesStore = new Ext.data.JsonStore({
						    url: this.configurationObject.getDocumentTypesServiceUrl,
						    autoLoad : true,
						    root: 'domains',
						    fields: ['VALUE_CD','VALUE_ID']
						});
						//this.documentTypesStore.load();

						this.engineTypesStore = new Ext.data.JsonStore({
						    url: this.configurationObject.getEngineTypesServiceUrl,
						    autoLoad : true,
						    root: 'domains',
						    fields: ['VALUE_CD','VALUE_ID']
						});
						//this.engineTypesStore.load();
						
						this.dataSourcesStore = new Ext.data.JsonStore({
						    url: this.configurationObject.getDataSourcesServiceUrl,
						    autoLoad : true,
						    root: 'rows',
						    fields: ['DATASOURCE_LABEL','DATASOURCE_ID']
						});
						//this.dataSourcesStore.load();



						// START list of detail fields
						this.detailFieldName = new Ext.form.TextField ({
							maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : LN('sbi.generic.name'),
							allowBlank : false,
							validationEvent : true,
							name : 'name',
						});

						this.detailFieldLabel = new Ext.form.TextField ({
							maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString2'),
							fieldLabel : LN('sbi.generic.label'),
							allowBlank : false,
							validationEvent : true,
							name : 'label'
						});

						this.detailFieldDescr = new Ext.form.TextArea ({
							//xtype : 'textarea',
							width : 350,
							height : 80,
							maxLength : 160,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : LN('sbi.generic.descr'),
							validationEvent : true,
							name : 'description'
						});

						this.detailFieldDocumentType = new Ext.form.ComboBox ({
							name : 'documentType',
							store : this.documentTypesStore,
							width : 150,
							fieldLabel : 'Document Type',
							displayField : 'VALUE_CD',
							valueField : 'VALUE_ID',
							typeAhead : true,
							forceSelection : true,
							mode : 'local',
							triggerAction : 'all',
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							validationEvent : true,
							xtype : 'combo'
						});

						this.detailFieldEngineType = new Ext.form.ComboBox ({
							name : 'engineType',
							store : this.engineTypesStore,
							width : 150,
							fieldLabel : 'Engine Types',
							displayField : 'VALUE_CD',
							valueField : 'VALUE_ID',
							typeAhead : true,
							forceSelection : true,
							mode : 'local',
							triggerAction : 'all',
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							validationEvent : true
							//xtype : 'combo'
						});
						this.detailFieldEngineType.addListener('select',this.activateEngineDetailFields, this);


						this.detailFieldUseDataSet = new Ext.form.Checkbox ({
							
							fieldLabel : 'Use Data Set',
							name : 'useDataSet'

						});

						this.detailFieldUseDataSource = new Ext.form.Checkbox({
							fieldLabel : 'Use Data Source',
							name : 'useDataSource',
							triggerAction : 'all',
							validationEvent : true,
							listeners:{ check:this.activateDataSourceCombo }
						 
					     
	
						});
						//this.detailFieldUseDataSource.addListener('click',this.activateDataSourceCombo, this);
						

						this.detailFieldDataSource = new Ext.form.ComboBox ({
							name : 'dataSourceId',
							store : this.dataSourcesStore,
							width : 150,
							fieldLabel : 'Data Source',
							displayField : 'DATASOURCE_LABEL',
							valueField : 'DATASOURCE_ID',
							typeAhead : true,
							forceSelection : true,
							mode : 'local',
							triggerAction : 'all',
							selectOnFocus : true,
							editable : false,
							allowBlank : true,
							validationEvent : true,
						});
						
						this.detailFieldClass = new Ext.form.TextField ({
							//maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : 'Class',
							allowBlank : true,
							validationEvent : false,
							name : 'class'
						});


						this.detailFieldUrl = new Ext.form.TextField ({
							//maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : 'Url',
							allowBlank : true,
							validationEvent : false,
							name : 'url'
						});

						this.detailFieldSecondaryUrl = new Ext.form.TextField ({
							//maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : 'Secondary Url',
							allowBlank : true,
							validationEvent : false,
							name : 'secondaryUrl'
						});

						this.detailFieldDriverName = new Ext.form.TextField ({
							maxLength : 50,
							minLength : 1,
							width : 350,
							regexText : LN('sbi.roles.alfanumericString'),
							fieldLabel : 'Driver Name',
							allowBlank : true,
							validationEvent : false,
							name : 'driver'
						});
						// END list of detail fields

						var c = {};

						this.detailTab = new Ext.Panel(
								{
									title : LN('sbi.generic.details'),
									itemId : 'detail',
									width : 430,
									items : {
										id : 'items-detail',
										itemId : 'items-detail',
										columnWidth : 0.4,
										xtype : 'fieldset',
										labelWidth : 90,
										defaultType : 'textfield',
										autoHeight : true,
										autoScroll : true,
										bodyStyle : Ext.isIE ? 'padding:0 0 8px 10px;'
												: 'padding:8px 10px;',
										border : false,

										style : {
											"margin-left" : "8px",
											"margin-right" : Ext.isIE6 ? (Ext.isStrict ? "-5px"
													: "-8px")
													: "8"
										},
										items : [ this.detailFieldLabel,
												this.detailFieldName,
												this.detailFieldDescr,
												this.detailFieldDocumentType,
												this.detailFieldEngineType,
												this.detailFieldUseDataSet,
												this.detailFieldUseDataSource,
												this.detailFieldDataSource,
												this.detailFieldClass,
												this.detailFieldUrl,
												this.detailFieldSecondaryUrl,
												this.detailFieldDriverName
										// this.manageDsVersionsPanel ,
										// this.detailFieldUserIn,this.detailFieldDateIn,this.detailFieldVersNum,this.detailFieldVersId,this.detailFieldId
										]
									}
								});
					}

					// OVERRIDING METHOD
					,
					addNewItem : function() {
						this.newRecord = new Ext.data.Record({
							name : '',
							label : '',
							description : '',
							documentType : '',
							engineType : '',
							useDataSet : 'false',
							useDataSource : 'false',
							class : '',
							url : '',
							driver : '',
							secondaryUrl : '',
							dataSourceId : ''
						});
						this.setValues(this.newRecord);

						this.tabs.items.each(function(item) {
							item.doLayout();
						});

						if (this.newRecord != null
								&& this.newRecord != undefined) {
							this.mainElementsStore.add(this.newRecord);
							this.rowselModel.selectLastRow(true);
						}
						this.tabs.setActiveTab(0);
					}

					,
					cloneItem : function() {
						var values = this.getValues();
						var params = this.buildParamsToSendToServer(values);
						// var arrayPars = this.manageParsGrid.getParsArray();

						this.newRecord = this.buildNewRecordToSave(values);
						// this.newRecord.set('pars',arrayPars);
						this.setValues(this.newRecord);

						// if (arrayPars) {
						// this.manageParsGrid.loadItems(arrayPars);
						// }else{
						// this.manageParsGrid.loadItems([]);
						// }
						this.manageDsVersionsGrid.loadItems([]);

						this.tabs.items.each(function(item) {
							item.doLayout();
						});
						if (this.newRecord != null
								&& this.newRecord != undefined) {
							this.mainElementsStore.add(this.newRecord);
							this.rowselModel.selectLastRow(true);
						}
						this.tabs.setActiveTab(0);
					}

					,
					buildNewRecordToSave : function(values) {
						var newRec = new Ext.data.Record({
							id : null,
							name : values['name'],
							label : values['label'],
							description : values['description'],
							documentType : values['documentType'],
							engineType : values['engineType'],
							useDataSet : values['useDataSet'],
							useDataSource : values['useDataSource'],
							class : values['class'],
							url : values['url'],
							driver : values['driver'],
							secondaryUrl : values['secondaryUrl'],
							dataSourceId : values['dataSourceId']
						});
						return newRec;
					}

					,
					buildParamsToSendToServer : function(values) {
						var params = {
							id : values['id'],
							name : values['name'],
							label : values['label'],
							description : values['description'],
							documentType : values['documentType'],
							engineType : values['engineType'],
							useDataSet : values['useDataSet'],
							useDataSource : values['useDataSource'],
							class : values['class'],
							url : values['url'],
							driver : values['driver'],
							secondaryUrl : values['secondaryUrl'],
							dataSourceId : values['dataSourceId']
						};
						return params;
					}

					,
					updateNewRecord : function(record, values) {
						record.set('id', values['id']);
						record.set('label', values['label']);
						record.set('name', values['name']);
						record.set('description', values['description']);
						record.set('documentType', values['documentType']);
						record.set('engineType', values['engineType']);
						record.set('useDataSet', values['useDataSet']);
						record.set('class', values['class']);
						record.set('url', values['url']);
						record.set('driver', values['driver']);
						record.set('secondaryUrl', values['secondaryUrl']);
						record.set('dataSourceId', values['dataSourceId']);

						// if (arrayPars) {
						// record.set('pars',arrayPars);
						// }

//						if (customString) {
//							record.set('customData', customString);
//						}

					}

					,
					updateMainStore : function(idRec) {
						var values = this.getValues();
						var record;
						var length = this.mainElementsStore.getCount();
						for ( var i = 0; i < length; i++) {
							var tempRecord = this.mainElementsStore.getAt(i);
							if (tempRecord.data.id == idRec) {
								record = tempRecord;
							}
						}
						var params = this.buildParamsToSendToServer(values);
						// var arrayPars = this.manageParsGrid.getParsArray();
						this.updateNewRecord(record, values);
						this.mainElementsStore.commitChanges();
					}

					,
					updateDsVersionsOfMainStore : function(idRec) {
						var arrayVersions = this.manageDsVersionsGrid
								.getCurrentDsVersions();
						if (arrayVersions) {
							var record;
							var length = this.mainElementsStore.getCount();
							for ( var i = 0; i < length; i++) {
								var tempRecord = this.mainElementsStore
										.getAt(i);
								if (tempRecord.data.id == idRec) {
									record = tempRecord;
								}
							}
							record.set('dsVersions', arrayVersions);
							this.mainElementsStore.commitChanges();
						}
					}

					,
					getValues : function() {
						//var values = this.getForm().getFieldValues();
						
						//Manual setting of values
						var values = {};
						//values.id = "";
						values.id = this.detailFieldId;
						values.name = this.detailFieldName.getValue();
						values.label = this.detailFieldLabel.getValue();
						values.description = this.detailFieldDescr.getValue();
						values.documentType = this.detailFieldDocumentType.getValue();
						values.engineType = this.detailFieldEngineType.getValue();
						values.useDataSet = this.detailFieldUseDataSet.getValue();
						values.useDataSource = this.detailFieldUseDataSource.getValue();
						values.dataSource = this.detailFieldDataSource.getValue();
						values.class = this.detailFieldClass.getValue();						
						values.url = this.detailFieldUrl.getValue();
						values.secondaryUrl = this.detailFieldSecondaryUrl.getValue();
						values.driver = this.detailFieldDriverName.getValue();
							
						return values;

					}

					,
					setValues : function(rec) {
						//this.getForm().loadRecord(record);

						//Manual setting of values
						if (rec.get('id')){
							this.detailFieldId = rec.get('id');
						} else {
							this.detailFieldId = '';
						}
						this.detailFieldName.setValue( rec.get('name'));
						this.detailFieldLabel.setValue( rec.get('label'));
						this.detailFieldDescr.setValue( rec.get('description'));
						this.detailFieldDocumentType.setValue(rec.get('documentType'));
						this.detailFieldEngineType.setValue(rec.get('engineType'));
						this.detailFieldUseDataSet.setValue(rec.get('useDataSet'));
						this.detailFieldUseDataSource.setValue(rec.get('useDataSource'));
						this.detailFieldDataSource.setValue(rec.get('dataSourceId'));
						this.detailFieldUrl.setValue(rec.get('url'));
						this.detailFieldClass.setValue(rec.get('class'));						
						this.detailFieldSecondaryUrl.setValue(rec.get('secondaryUrl'));
						this.detailFieldDriverName.setValue(rec.get('driver'));
					}

					// OVERRIDING save method
					,
					save : function() {
						var values = this.getValues();
						var idRec = values['id'];
						if (idRec == 0 || idRec == null || idRec === '') {
							this.doSave("yes");
						} else {
							Ext.MessageBox
									.confirm(
											LN('sbi.ds.recalculatemetadataconfirm.title'),
											LN('sbi.ds.recalculatemetadataconfirm.msg'),
											this.doSave, this);
						}
					}

					,
					doSave : function(recalculateMetadata) {
						var values = this.getValues();

						var idRec = values['id'];
						var newRec;
						//var newDsVersion;
						var isNewRec = false;
						var params = this.buildParamsToSendToServer(values);
						params.recalculateMetadata = recalculateMetadata;
						// var arrayPars = this.manageParsGrid.getParsArray();
						//var customString = this.customDataGrid.getDataString();

						if (idRec == 0 || idRec == null || idRec === '') {
							this.updateNewRecord(this.newRecord, values);
							isNewRec = true;
						} else {
							var record;
							var oldType;
							var length = this.mainElementsStore.getCount();
							for ( var i = 0; i < length; i++) {
								var tempRecord = this.mainElementsStore
										.getAt(i);
								if (tempRecord.data.id == idRec) {
									record = tempRecord;
									//oldType = record.get('dsTypeCd');
								}
							}
							this.updateNewRecord(record, values);

//							newDsVersion = new Ext.data.Record({
//								dsId : values['id'],
//								dateIn : values['dateIn'],
//								userIn : values['userIn'],
//								versId : values['versId'],
//								type : oldType,
//								versNum : values['versNum']
//							});
						}

						// if (arrayPars) {
						// params.pars = Ext.util.JSON.encode(arrayPars);
						// }
//						if (customString) {
//							params.customData = Ext.util.JSON
//									.encode(customString);
//						}

						if (idRec) {
							params.id = idRec;
						}
						//Serialize form values to JSON Object
						params.engineValues = Ext.util.JSON.encode(values);

						Ext.Ajax
								.request({
									url : this.services['saveItemService'],
									params : params,
									method : 'POST',
									success : function(response, options) {
										if (response !== undefined) {
											if (response.responseText !== undefined) {

												var content = Ext.util.JSON
														.decode(response.responseText);
												if (content.responseText !== 'Operation succeded') {
													Ext.MessageBox
															.show({
																title : LN('sbi.generic.error'),
																msg : content,
																width : 150,
																buttons : Ext.MessageBox.OK
															});
												}
												else {
													var itemId = content.id;
													var dateIn = content.dateIn;
													var userIn = content.userIn;
													var versId = content.versId;
													var versNum = content.versNum;

													if (isNewRec
															&& itemId != null
															&& itemId !== '') {

														var record;
														var length = this.mainElementsStore
																.getCount();
														for ( var i = 0; i < length; i++) {
															var tempRecord = this.mainElementsStore
																	.getAt(i);
															if (tempRecord.data.id == 0) {
																tempRecord.set(
																		'id',
																		itemId);
																tempRecord
																		.set(
																				'dateIn',
																				dateIn);
																tempRecord
																		.set(
																				'userIn',
																				userIn);
																tempRecord
																		.set(
																				'versId',
																				versId);
																tempRecord
																		.set(
																				'versNum',
																				versNum);
																tempRecord
																		.commit();
																this.detailFieldId
																		.setValue(itemId);
																this.detailFieldUserIn
																		.setValue(userIn);
																this.detailFieldDateIn
																		.setValue(dateIn);
																this.detailFieldVersNum
																		.setValue(versNum);
																this.detailFieldVersId
																		.setValue(versId);
															}
														}
													} else {
//														if (newDsVersion != null
//																&& newDsVersion != undefined) {
//															this.manageDsVersionsGrid
//																	.getStore()
//																	.addSorted(
//																			newDsVersion);
//															this.manageDsVersionsGrid
//																	.getStore()
//																	.commitChanges();
//															var values = this
//																	.getValues();
//															this
//																	.updateDsVersionsOfMainStore(values['id']);
//														}
													}
													this.mainElementsStore
															.commitChanges();
													if (isNewRec
															&& itemId != null
															&& itemId !== '') {
														this.rowselModel
																.selectLastRow(true);
													}

													Ext.MessageBox
															.show({
																title : LN('sbi.generic.result'),
																msg : LN('sbi.generic.resultMsg'),
																width : 200,
																buttons : Ext.MessageBox.OK
															});
												}

											} else {
												Sbi.exception.ExceptionHandler
														.showErrorMessage(
																LN('sbi.generic.serviceResponseEmpty'),
																LN('sbi.generic.serviceError'));
											}
										} else {
											Sbi.exception.ExceptionHandler
													.showErrorMessage(
															LN('sbi.generic.savingItemError'),
															LN('sbi.generic.serviceError'));
										}
									},
									failure : Sbi.exception.ExceptionHandler.handleFailure,
									scope : this
								});
					}



//					,
//					jsonTriggerFieldHandler : function() {
//						var values = this.getValues();
//						var datasetId = values['id'];
//						var datasourceLabel = this.detailQbeDataSource
//								.getValue();
//						if (datasourceLabel == '') {
//							Ext.MessageBox
//									.show({
//										title : LN('sbi.generic.error'),
//										msg : LN('sbi.tools.managedatasets.errors.missingdatasource'),
//										width : 150,
//										buttons : Ext.MessageBox.OK
//									});
//							return;
//						}
//						if (datamart == '') {
//							Ext.MessageBox
//									.show({
//										title : LN('sbi.generic.error'),
//										msg : LN('sbi.tools.managedatasets.errors.missingdatamart'),
//										width : 150,
//										buttons : Ext.MessageBox.OK
//									});
//							return;
//						}
//						var datamart = this.qbeDatamarts.getValue();
//						this.initQbeDataSetBuilder(datasourceLabel, datamart,
//								datasetId);
//						this.qbeDataSetBuilder.show();
//					}

					// , initQbeDataSetBuilder: function(datasourceLabel,
					// datamart, datasetId) {
					// if (this.qbeDataSetBuilder === null) {
					// this.initNewQbeDataSetBuilder(datasourceLabel, datamart,
					// datasetId);
					// return;
					// }
					// if (this.mustRefreshQbeView(datasourceLabel, datamart,
					// datasetId)) {
					// this.qbeDataSetBuilder.destroy();
					// this.initNewQbeDataSetBuilder(datasourceLabel, datamart,
					// datasetId);
					// return;
					// }
					// }

					// , initNewQbeDataSetBuilder: function(datasourceLabel,
					// datamart, datasetId) {
					// this.qbeDataSetBuilder = new
					// Sbi.tools.dataset.QbeDatasetBuilder({
					// datasourceLabel : datasourceLabel,
					// datamart : datamart,
					// datasetId : datasetId,
					// jsonQuery : this.qbeJSONQuery.getValue(),
					// qbeParameters : this.manageParsGrid.getParsArray(),
					// modal : true,
					// width : this.getWidth() - 50,
					// height : this.getHeight() - 50,
					// listeners : {
					// hide :
					// {
					// fn : function(theQbeDatasetBuilder) {
					// theQbeDatasetBuilder.getQbeQuery(); // asynchronous
					// }
					// , scope: this
					// },
					// gotqbequery : {
					// fn: this.manageQbeQuery
					// , scope: this
					// }
					// }
					// });
					// }

					// , mustRefreshQbeView: function(datasourceLabel, datamart,
					// datasetId) {
					// if (datasourceLabel ==
					// this.qbeDataSetBuilder.getDatasourceLabel()
					// && datamart == this.qbeDataSetBuilder.getDatamart()
					// && datasetId == this.qbeDataSetBuilder.getDatasetId()) {
					// return false;
					// }
					// return true;
					// }

					// METHOD TO BE OVERRIDDEN IN EXTENDED ELEMENT!!!!!
					,
					info : function() {
						var win_info_2;
						if (!win_info_2) {
							win_info_2 = new Ext.Window({
								id : 'win_info_2',
								autoLoad : {
									url : Sbi.config.contextName + '/themes/'
											+ Sbi.config.currTheme
											+ '/html/dsrules.html'
								},
								layout : 'fit',
								width : 620,
								height : 410,
								autoScroll : true,
								closeAction : 'close',
								buttonAlign : 'left',
								plain : true,
								title : LN('sbi.ds.help')
							});
						}
						;
						win_info_2.show();
					}

				// ,transfInfo: function() {
				// var win_info_4;
				// if(!win_info_4){
				// win_info_4= new Ext.Window({
				// id:'win_info_4',
				// autoLoad: {url:
				// Sbi.config.contextName+'/themes/'+Sbi.config.currTheme+'/html/dsTrasformationHelp.html'},
				// layout:'fit',
				// width:760,
				// height:420,
				// autoScroll: true,
				// closeAction:'close',
				// buttonAlign : 'left',
				// plain: true,
				// title: LN('sbi.ds.help')
				// });
				// };
				// win_info_4.show();
				// }

				// ,profileAttrs: function() {
				// var win_info_3;
				// if(!win_info_3){
				// win_info_3= new Ext.Window({
				// id:'win_info_3',
				// layout:'fit',
				// width:220,
				// height:350,
				// closeAction:'close',
				// buttonAlign : 'left',
				// autoScroll: true,
				// plain: true,
				// items: {
				// xtype: 'grid',
				// border: false,
				// columns: [{header: LN('sbi.ds.pars'),width : 170}],
				// store: this.profileAttributesStore
				// }
				// });
				// };
				// win_info_3.show();
				// }

				});
