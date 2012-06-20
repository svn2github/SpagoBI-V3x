ALTER TABLE SBI_LOV ADD DATASET_ID INTEGER;\p\g 
ALTER TABLE SBI_LOV ADD CONSTRAINT SBI_LOV_2 FOREIGN KEY  (DATASET_ID) REFERENCES SBI_DATA_SET (DS_ID);\p\g

INSERT INTO SBI_DOMAINS (VALUE_ID, VALUE_CD,VALUE_NM,DOMAIN_CD,DOMAIN_NM,VALUE_DS, USER_IN, TIME_IN)
    VALUES ((SELECT next_val FROM hibernate_sequences WHERE sequence_name = 'SBI_DOMAINS'),
    'DATASET','sbidomains.nm.dataset','INPUT_TYPE','Input mode and values','sbidomains.ds.dataset', 'server', current_timestamp);\p\g
update hibernate_sequences set next_val = next_val+1 where  sequence_name = 'SBI_DOMAINS';\p\g
commit;\p\g

ALTER TABLE SBI_EXT_ROLES ADD COLUMN EDIT_WORKSHEET TINYINT DEFAULT 1;\p\g


DELETE FROM SBI_DOMAINS WHERE domain_cd = 'SCRIPT_TYPE' AND value_cd='ejs';\p\g
UPDATE SBI_DOMAINS SET value_cd='ECMAScript' WHERE domain_cd = 'SCRIPT_TYPE' AND value_cd='rhino-nonjdk';\p\g
commit;\p\g
ALTER TABLE SBI_DATA_SET_HISTORY ADD COLUMN QUERY_SCRIPT NVARCHAR DEFAULT NULL;\p\g
ALTER TABLE SBI_DATA_SET_HISTORY ADD COLUMN QUERY_SCRIPT_LANGUAGE VARCHAR(100) DEFAULT NULL;\p\g
commit;\p\g

ALTER TABLE SBI_DOMAINS ADD CONSTRAINT XAK1SBI_DOMAINS UNIQUE (VALUE_CD, DOMAIN_CD);\p\g

ALTER TABLE SBI_DATA_SET_HISTORY ALTER COLUMN PARAMS  VARCHAR(4000);\p\g

ALTER TABLE SBI_PARUSE ADD COLUMN MAXIMIZER_ENABLED SMALLINT DEFAULT 0;\p\g
UPDATE SBI_PARUSE SET MAXIMIZER_ENABLED = 0;\p\g
commit;\p\g