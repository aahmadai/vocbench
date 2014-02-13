-- ------------------------------------------------------
-- VB version 2.0.1 SQL Script
-- ------------------------------------------------------

-- Add Dutch language
INSERT IGNORE INTO `language_interface` VALUES ('NL','Dutch','Nederlands');

-- Add SPARQL Module
INSERT IGNORE INTO `permissiontype` VALUES ('22', 'Sparql');
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('1', '22');