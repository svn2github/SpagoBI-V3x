ALTER TABLE SBI_LOV ADD DATASET_ID INTEGER; 
ALTER TABLE SBI_LOV ADD CONSTRAINT SBI_LOV_2 FOREIGN KEY  (DATASET_ID) REFERENCES SBI_DATA_SET (DS_ID);
	
INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS, USER_IN, TIME_IN)
    VALUES ((SELECT next_val FROM hibernate_sequences WHERE sequence_name = 'SBI_DOMAINS'),
    'DATASET','sbidomains.nm.dataset','INPUT_TYPE','Input mode and values','sbidomains.ds.dataset', 'server', current_timestamp);
update hibernate_sequences set next_val = next_val+1 where  sequence_name = 'SBI_DOMAINS';
commit;

ALTER TABLE SBI_EXT_ROLES ADD COLUMN EDIT_WORKSHEET SMALLINT DEFAULT 1;
commit;


DELETE FROM SBI_DOMAINS WHERE domain_cd = 'SCRIPT_TYPE' AND value_cd='ejs';
UPDATE SBI_DOMAINS SET value_cd='ECMAScript' WHERE domain_cd = 'SCRIPT_TYPE' AND value_cd='rhino-nonjdk';
commit;
ALTER TABLE SBI_DATA_SET_HISTORY ADD COLUMN QUERY_SCRIPT CLOB DEFAULT NULL;
ALTER TABLE SBI_DATA_SET_HISTORY ADD COLUMN QUERY_SCRIPT_LANGUAGE VARCHAR(100) DEFAULT NULL;
commit;

ALTER TABLE SBI_DOMAINS ADD CONSTRAINT XAK1SBI_DOMAINS UNIQUE (VALUE_CD, DOMAIN_CD);

ALTER TABLE SBI_DATA_SET_HISTORY MODIFY (PARAMS VARCHAR2(4000) );

--converts DS_METADATA from VARCHAR2 to CLOB
/*
ALTER TABLE SBI_DATA_SET_HISTORY ADD TMP_DS_METADATA CLOB;
UPDATE SBI_DATA_SET_HISTORY SET TMP_DS_METADATA = DS_METADATA;
ALTER TABLE SBI_DATA_SET_HISTORY DROP COLUMN DS_METADATA;
ALTER TABLE SBI_DATA_SET_HISTORY RENAME COLUMN TMP_DS_METADATA to DS_METADATA;
commit;
*/
