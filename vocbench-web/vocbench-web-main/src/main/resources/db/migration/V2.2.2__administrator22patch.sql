-- ------------------------------------------------------
-- VB version 2.2.2 SQL Script
-- ------------------------------------------------------

--
-- Insert data for table `permissiontype`
--

INSERT IGNORE INTO `permissiontype` (`permission_id`, `permission`) VALUES (23, 'ICV'), (24, 'Sheet2RDF');
--
-- Insert data for table `permission_group_map`
--

INSERT IGNORE INTO `permission_group_map` VALUES(1, 23);
INSERT IGNORE INTO `permission_group_map` VALUES(1, 24);