/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
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
		
		timestamp: {
			dateFormat: 'd/m/Y H:i:s',
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
Sbi.locale.ln['sbi.console.messagewin.warning.title'] = 'Warning';
Sbi.locale.ln['sbi.console.messagewin.error.title'] = 'Messaggio di Errore';
Sbi.locale.ln['sbi.console.messagewin.info.title'] = 'Informazione';

Sbi.locale.ln['sbi.console.detailpage.title'] = 'Dettaglio Pagina';
Sbi.locale.ln['sbi.console.consolepanel.title'] = 'Console';

//error / alarms window
Sbi.locale.ln['sbi.console.error.btnClose'] = 'Chiudi';
Sbi.locale.ln['sbi.console.error.btnSetChecked'] = 'Visionato';
Sbi.locale.ln['sbi.console.error.btnSetNotChecked'] = 'Non visionato';

	//downalod window
Sbi.locale.ln['sbi.console.downloadlogs.title'] = 'Specifica i parametri per il download dei files:';
Sbi.locale.ln['sbi.console.downloadlogs.initialDate'] = 'Data inizio';
Sbi.locale.ln['sbi.console.downloadlogs.finalDate'] = 'Data fine';
Sbi.locale.ln['sbi.console.downloadlogs.initialTime'] = 'Ora inizio';
Sbi.locale.ln['sbi.console.downloadlogs.finalTime'] = 'Ora fine';
Sbi.locale.ln['sbi.console.downloadlogs.path'] = 'Path';
Sbi.locale.ln['sbi.console.downloadlogs.btnClose'] = 'Chiudi';
Sbi.locale.ln['sbi.console.downloadlogs.btnDownload'] = 'Download';
Sbi.locale.ln['sbi.console.downloadlogs.initialDateMandatory'] = 'Data inizio obbligatoria';
Sbi.locale.ln['sbi.console.downloadlogs.finalDateMandatory'] = 'Data fine obbligatoria';
Sbi.locale.ln['sbi.console.downloadlogs.initialTimeMandatory'] = 'Ora inizio obbligatoria';
Sbi.locale.ln['sbi.console.downloadlogs.finalTimeMandatory'] = 'Ora fine obbligatoria';
Sbi.locale.ln['sbi.console.downloadlogs.pathsMandatory'] = 'Path obbligatorio'
Sbi.locale.ln['sbi.console.downloadlogs.rangeInvalid'] = 'Intervallo date errato';

//propmtables window
Sbi.locale.ln['sbi.console.promptables.btnOK'] = 'OK';
Sbi.locale.ln['sbi.console.promptables.btnClose'] = 'Chiudi';
Sbi.locale.ln['sbi.console.promptables.lookup.Annulla'] = 'Annulla';
Sbi.locale.ln['sbi.console.promptables.lookup.Confirm'] = 'Conferma';
Sbi.locale.ln['sbi.console.promptables.lookup.Select'] = 'Seleziona';

Sbi.locale.ln['sbi.console.localization.columnsKO'] = 'Errore durante la internazionalizzazione. Controllare il dataset!';

