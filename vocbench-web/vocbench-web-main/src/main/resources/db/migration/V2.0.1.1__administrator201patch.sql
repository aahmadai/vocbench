-- ------------------------------------------------------
-- VB version 2.0.1 SQL Script
-- ------------------------------------------------------

-- Add indexing column
ALTER TABLE  `ontology_info` ADD  `indexing` TINYINT( 1 ) NOT NULL DEFAULT  '0'