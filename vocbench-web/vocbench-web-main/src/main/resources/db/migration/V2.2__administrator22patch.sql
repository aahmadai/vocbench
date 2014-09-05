-- ------------------------------------------------------
-- VB version 2.2 SQL Script
-- ------------------------------------------------------

-- Add missing Visitor record
INSERT IGNORE INTO `users_groups` (`users_groups_id`, `users_groups_name`, `users_groups_desc`) VALUES (12, 'Visitor', 'Users can view only.');

CREATE TABLE IF NOT EXISTS `st_instances` (
  `st_name` varchar(255) NOT NULL,
  `st_domain` varchar(255) NOT NULL,
  `st_port` varchar(7) NOT NULL,
  PRIMARY KEY (`st_name`,`st_domain`,`st_port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `st_instances`
--

INSERT IGNORE INTO `st_instances` VALUES('localhost-1979', 'localhost', '1979');

UPDATE ontology_info dest, (SELECT ontology_id, SUBSTRING_INDEX(db_driver, '/', 3) AS domain FROM ontology_info) src SET dest.db_driver = src.domain WHERE dest.ontology_id=src.ontology_id
