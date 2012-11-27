Ext.ns("Sbi.locale");
Sbi.locale.ln = Sbi.locale.ln || new Array();

Sbi.locale.formats = {
		/*
		number: {
			decimalSeparator: ',',
			decimalPrecision: 2,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '�',
			nullValue: ''
		},
		*/
		float: {
			decimalSeparator: ',',
			decimalPrecision: 2,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '�',
			nullValue: ''
		},
		int: {
			decimalSeparator: ',',
			decimalPrecision: 0,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '�',
			nullValue: ''
		},
		string: {
			trim: true,
    		maxLength: null,
    		ellipsis: true,
    		changeCase: null, // null | 'capitalize' | 'uppercase' | 'lowercase'
    		//prefix: '',
    		//suffix: '',
    		nullValue: ''
		},
		
		date: {
			dateFormat: 'd/m/Y',
    		nullValue: ''
		},
		
		boolean: {
			trueSymbol: 'vero',
    		falseSymbol: 'false',
    		nullValue: ''
		}
};

//===================================================================
// MESSAGE WINDOW
//===================================================================
Sbi.locale.ln['sbi.qbe.messagewin.warning.title'] = 'Warning';
Sbi.locale.ln['sbi.qbe.messagewin.error.title'] = 'Messaggio di Errore';
Sbi.locale.ln['sbi.qbe.messagewin.info.title'] = 'Informazione';



//===================================================================
//QUERY EDITOR PANEL
//===================================================================
Sbi.locale.ln['sbi.qbe.queryeditor.title'] = 'Query';
Sbi.locale.ln['sbi.qbe.queryeditor.westregion.title'] = 'Schema';
Sbi.locale.ln['sbi.qbe.queryeditor.westregion.tools.expand'] = 'Espandi tutto'; 
Sbi.locale.ln['sbi.qbe.queryeditor.westregion.tools.collapse'] = 'Collassa tutto'; 
Sbi.locale.ln['sbi.qbe.queryeditor.westregion.tools.flat'] = 'Flat view'; 
Sbi.locale.ln['sbi.qbe.queryeditor.westregion.tools.addcalculated'] = 'Aggiungi campo calcolato'; 

Sbi.locale.ln['sbi.qbe.queryeditor.savequery'] = 'Salvare la query ...';
Sbi.locale.ln['sbi.qbe.queryeditor.querysaved'] = 'Query salvata';
Sbi.locale.ln['sbi.qbe.queryeditor.querysavedsucc'] = 'Query salvata correttamente';
Sbi.locale.ln['sbi.qbe.queryeditor.msgwarning'] = 'La query � incorreta; vuoi salvarla lo stesso?';
Sbi.locale.ln['sbi.qbe.queryeditor.saveqasview'] = 'Salvare la query come vista...';

Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.title'] = 'Editor delle query';
Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.tools.save'] = 'Salva come vista customizzata';
Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.tools.view'] = 'Salva come vista database';
Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.tools.execute'] = 'Esegui la query';
Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.tools.validate'] = 'Valida la  query';
Sbi.locale.ln['sbi.qbe.queryeditor.centerregion.tools.help'] = 'Help me please';

Sbi.locale.ln['sbi.qbe.queryeditor.eastregion.title'] = 'Catalogo delle query';
Sbi.locale.ln['sbi.qbe.queryeditor.eastregion.tools.delete'] = 'Cancella';
Sbi.locale.ln['sbi.qbe.queryeditor.eastregion.tools.add'] = 'Aggiungi query';
Sbi.locale.ln['sbi.qbe.queryeditor.eastregion.tools.insert'] = 'Aggiungi query secondaria';

//===================================================================
//EXPRESSION EDITOR
//===================================================================
Sbi.locale.ln['sbi.qbe.expreditor.title'] = 'Editor delle Espressioni';
Sbi.locale.ln['sbi.qbe.expreditor.items'] = 'Elementi Espr.';
Sbi.locale.ln['sbi.qbe.expreditor.operands'] = 'Operandi';
Sbi.locale.ln['sbi.qbe.expreditor.operators'] = 'Operatori';
Sbi.locale.ln['sbi.qbe.expreditor.structure'] = 'Struttura Espr.';
Sbi.locale.ln['sbi.qbe.expreditor.clear'] = 'Pulisci Tutto';
Sbi.locale.ln['sbi.qbe.expreditor.expression'] = 'Espressione';
Sbi.locale.ln['sbi.qbe.expreditor.log'] =  'Log';
Sbi.locale.ln['sbi.qbe.expreditor.refresh'] =  'Aggiorna struttura espressione';
Sbi.locale.ln['sbi.qbe.expreditor.clearttp'] =  'Pulisci tutti i campi selezionati';
Sbi.locale.ln['sbi.qbe.expreditor.filterdesc'] = 'La descrizione del Filtro va qui';
Sbi.locale.ln['sbi.qbe.expreditor.operatordesc'] =  'La descrizione dell\'Operatore va qui';

//===================================================================
//DATASTORE PANEL
//===================================================================
Sbi.locale.ln['sbi.qbe.datastorepanel.title'] = 'Risultati';

Sbi.locale.ln['sbi.qbe.datastorepanel.grid.displaymsg'] = 'Visualizza {0} - {1} of {2}';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.emptymsg'] = 'Nessun risultato';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.emptywarningmsg'] = 'La query non ha restituito valori';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.beforeoverflow'] = 'Soglia massima del numero di record';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.afteroverflow'] = 'superata';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.beforepagetext'] = 'Pagina';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.afterpagetext'] = 'di {0}';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.firsttext'] = 'Prima Pagina';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.prevtext'] = 'Pagina precedente';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.nexttext'] = 'Pagina successiva';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.lasttext'] = 'Pagina successiva';
Sbi.locale.ln['sbi.qbe.datastorepanel.grid.refreshtext'] = 'Aggiorna';

Sbi.locale.ln['sbi.qbe.datastorepanel.button.tt.exportto'] = 'Esporta a';

//LookupField 
Sbi.locale.ln['sbi.lookup.Confirm'] = 'Conferma';
Sbi.locale.ln['sbi.lookup.Annulla'] = 'Annulla';
Sbi.locale.ln['sbi.lookup.Select']= 'Selezionare Valore ...';
Sbi.locale.ln['sbi.lookup.ValueOfColumn'] ='Il valore della colonna';
Sbi.locale.ln['sbi.lookup.asA'] = 'inteso come';

//===================================================================
//SAVE WINDOW
//===================================================================
Sbi.locale.ln['sbi.qbe.savewindow.desc'] = 'Descrizione';
Sbi.locale.ln['sbi.qbe.savewindow.name'] = 'Nome' ;
Sbi.locale.ln['sbi.qbe.savewindow.saveas'] = 'Salva come ...' ;
Sbi.locale.ln['sbi.qbe.savewindow.selectscope'] = 'Selezionare scope...' ;
Sbi.locale.ln['sbi.qbe.savewindow.scope'] = 'Scope';
Sbi.locale.ln['sbi.qbe.savewindow.save'] = 'Salvare';
Sbi.locale.ln['sbi.qbe.savewindow.cancel'] = 'Cancellare';
Sbi.locale.ln['sbi.qbe.savewindow.public'] = 'Pubblico';
Sbi.locale.ln['sbi.qbe.savewindow.private'] = 'Privato';
Sbi.locale.ln['sbi.qbe.savewindow.publicdesc'] = 'Tutti coloro che possono eseguire questo documento vedranno il tuo subobject salvato';
Sbi.locale.ln['sbi.qbe.savewindow.privatedesc'] = 'Il subobject salvato sar� visibile solo da te';

//===================================================================
//FILTER GRID
//===================================================================
Sbi.locale.ln['sbi.qbe.filtergridpanel.title'] = 'Filtri';

//column headers
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.delete'] = 'Elimina tutti';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.name'] = 'Nome';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.entity'] = 'Entita\'';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.field'] = 'Campo';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.operator'] = 'Operatore';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.value'] = 'Operando';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.isfree'] = 'Campo a richiesta';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.type'] = 'Tipo Operando';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.boperator'] = 'Connettore';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.desc'] = 'Descrizione filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.lotype'] = 'Tipo espressione sinistra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.rotype'] = 'Tipo espressione destra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.lodesc'] = 'Descrizione elemento';
Sbi.locale.ln['sbi.qbe.filtergridpanel.headers.rodesc'] = 'Descrizione elemento';
//column tooltip
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.notdef'] = 'Help tooltip non ancora definito';

Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.name'] = 'Nome univoco della condizione';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.desc'] = 'Descrizione della condizione';

Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.loval'] = 'Valore dell\'operando di sinistra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.lodesc'] = 'Operando di sinistra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.lotype'] = 'Tipo dell\'operando di sinistra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.lodef'] = 'Valore di default dell\'operando di sinistra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.lolast'] = 'Ultimo valore dell\'operando di sinistra';

Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.operator'] = 'Operator';

Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.roval'] = 'Valore dell\'operando di destra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.rodesc'] = 'Operando di destra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.rotype'] = 'Tipo dell\'operando di destra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.rodef'] = 'Valore di default dell\'operando di destra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.tooltip.rolast'] = 'Ultimo valore dell\'operando di destra';

//boolean operators
Sbi.locale.ln['sbi.qbe.filtergridpanel.boperators.name.and'] = 'AND';
Sbi.locale.ln['sbi.qbe.filtergridpanel.boperators.name.or'] = 'OR';

Sbi.locale.ln['sbi.qbe.filtergridpanel.boperators.desc.and'] = 'Collega questo filtro al successivo usando l\'operatore AND';
Sbi.locale.ln['sbi.qbe.filtergridpanel.boperators.desc.or'] = 'Collega questo filtro al successivo usando l\'operatore OR';

Sbi.locale.ln['sbi.qbe.filtergridpanel.boperators.editor.emptymsg'] = 'Seleziona un operatore...';


//filter operators

Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.none'] = 'none';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.eq'] = 'uguale a';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.noteq'] = 'non uguale a';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.gt'] = 'maggiore di';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.eqgt'] = 'maggiore o uguale a';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.lt'] = 'minore di';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.eqlt'] = 'minore o uguale di';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.starts'] = 'inizia con';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notstarts'] = 'non inizia con';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.ends'] = 'finisce con';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notends'] = 'non finisce con';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.contains'] = 'contiene';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notcontains'] = 'non contiene';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.between'] = 'compreso tra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notbetween'] = 'non compreso tra';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.in'] = 'in';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notin'] = 'non in';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.notnull'] = 'non nullo';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.name.isnull'] = 'nullo';

Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.none'] = 'nessun filtro applicato';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.eq'] = 'verificato se il valore del campo e\' uguale al valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.noteq'] = 'verificato se il valore del campo non e\' diverso dal valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.gt'] = 'verificato se il valore del campo e\' maggiore del valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.eqgt'] = 'verificato se il valore del campo e\' maggiore o uguale al valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.lt'] = 'verificato se il valore del campo e\' minore del valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.eqlt'] = 'verificato se il valore del campo inizia con il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.starts'] = 'verificato se il valore del campo e\' minore o uguale al valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notstarts'] = 'verificato se il valore del campo non inizia con il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.ends'] = 'verificato se il valore del campo termina con il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notends'] = 'verificato se il valore del campo non termina con il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.contains'] = 'verificato se il valore del campo contiene il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notcontains'] = 'verificato se il valore del campo non contiene il valore del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.between'] = 'verificato se il valore del campo e\' compreso nell\'intervallo specificato';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notbetween'] = 'verificato se il valore del campo non e\' compreso nell\'intervallo specificato';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.in'] = 'verificato se il valore del campo e\' uguale ad uno dei valori del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notin'] = 'verificato se il valore del campo non e\' uguale a nessuno dei valori del filtro';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.notnull'] = 'verificato se il valore del campo e\' nullo';
Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.desc.isnull'] = 'verificato se il valore del campo non e\' nullo';

Sbi.locale.ln['sbi.qbe.filtergridpanel.foperators.editor.emptymsg'] = 'Seleziona un operatore...';

//buttons 
Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.text.delete'] = 'Elimina tutto';
Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.tt.delete'] = 'Elimina tutto';

Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.text.wizard'] = 'Espressioni';
Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.tt.wizard'] = 'Espressioni';
Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.text.add'] = 'Nuova condizione';
Sbi.locale.ln['sbi.qbe.filtergridpanel.buttons.tt.add'] = 'Crea una nuova condizione';
// ===================================================================
//	SELECT GRID
// ===================================================================
Sbi.locale.ln['sbi.qbe.selectgridpanel.title'] = 'Seleziona Campo';

// column headers
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.include'] = 'Includi';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.having'] = 'Condizione su raggruppamento';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.visible'] = 'Visibile';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.group'] = 'Raggruppa';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.filter'] = 'Filtri';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.entity'] = 'Entita\'';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.alias'] = 'Alias';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.order'] = 'Ordinamento';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.group'] = 'Raggruppa';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.function'] = 'Funzione';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.field'] = 'Campo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.headers.delete'] = 'Elimina tutti';

//aggregation functions
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.none'] = 'nessuno';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.sum'] = 'somma';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.avg'] = 'media';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.max'] = 'massimo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.min'] = 'minimo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.name.count'] = 'conteggio';

Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.none'] = 'Nessuna modalita\' d\'aggregazione attivata';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.sum'] = 'Restituisce la somma dei valori nel gruppo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.avg'] = 'Restituisce la media dei valori nel gruppo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.max'] = 'Restituisce il massimo dei valori nel gruppo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.min'] = 'Restituisce il minimo dei valori nel gruppo';
Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.desc.count'] = 'Restituisce il conteggio dei valori nel gruppo';

Sbi.locale.ln['sbi.qbe.selectgridpanel.aggfunc.editor.emptymsg'] = 'Seleziona una funzione...';


// sorting functions
Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.name.none'] = 'nessuna';
Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.name.asc'] = 'ascendente';
Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.name.desc'] = 'discendenti';

Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.desc.none'] = 'No ordering applied to the given colunm';
Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.desc.asc'] = 'Order values of the given column in asecnding way';
Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.desc.desc'] = 'Order values of the given column in descending way';

Sbi.locale.ln['sbi.qbe.selectgridpanel.sortfunc.editor.emptymsg'] = 'Seleziona la modalita\' d\'ordinamento...';

//buttons 
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.hide'] = 'Nascondi i campi non-visibili';
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.tt.hide'] = 'Nascondi tutti i campi non-visibili';

Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.group'] = 'Raggruppa per entita\'';
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.tt.group'] = 'Raggruppa per entita\' padre';

Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.add'] = 'Aggiungi campo calcolato';
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.tt.add'] = 'Add an ad-hoc calculated field (i.e. valid only for this query)';

Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.delete'] = 'Elimina tutti';
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.tt.delete'] = 'Elimina campi selezionati';

Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.deleteall'] = 'Elimina tutto';
Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.tt.deleteall'] = 'Elimina tutti i campi selezionati';

Sbi.locale.ln['sbi.qbe.selectgridpanel.buttons.text.distinct'] = 'Valori distinti';

Sbi.locale.ln['sbi.qbe.freeconditionswindow.title'] = 'Inserisci condizioni ...';
Sbi.locale.ln['sbi.qbe.freeconditionswindow.buttons.text.apply'] = 'Attiva';
Sbi.locale.ln['sbi.qbe.freeconditionswindow.buttons.text.cancel'] = 'Elimina tutti';
Sbi.locale.ln['sbi.qbe.freeconditionswindow.buttons.text.restoredefaults'] = 'Riprista valori pedefiniti';
Sbi.locale.ln['sbi.qbe.freeconditionswindow.buttons.text.saveasdefaults'] = 'Sala come predefinito';
Sbi.locale.ln['sbi.qbe.freeconditionswindow.buttons.text.restorelast'] = 'Ripristina precedente';

//===================================================================
//QUERY CATALOGUE PANEL
//===================================================================
Sbi.locale.ln['sbi.qbe.cataloguepanel.title'] = 'Catalogo delle query';


//===================================================================
//HAVING CLAUSE PANEL
//===================================================================
Sbi.locale.ln['sbi.qbe.havinggridpanel.title'] = 'Condizioni di raggruppamento';

Sbi.locale.ln['sbi.qbe.operandchooserwindow.title'] = 'Selezione operando da query padre';


//===================================================================
//DOCUMENT PARAMETERS PANEL
//===================================================================
Sbi.locale.ln['sbi.qbe.documentparametersgridpanel.title'] = 'Driver analitici del documento';
Sbi.locale.ln['sbi.qbe.documentparametersgridpanel.emptytext'] = 'Questo documento non ha driver analitici';
Sbi.locale.ln['sbi.qbe.documentparametersgridpanel.headers.label'] = 'Intestazione';
Sbi.locale.ln['sbi.qbe.documentparametersgridpanel.parameterreference'] = 'Driver analitico';