-- ------------------------------------------------------
-- VB version 2.4 SQL Script
-- ------------------------------------------------------

-- Add Projects Module
INSERT IGNORE INTO `permissiontype` VALUES ('26', 'Projects');

-- Add Project Manager as new user group
INSERT IGNORE INTO `users_groups` (`users_groups_name`, `users_groups_desc`) VALUES ('Project manager', 'Project Manager can assign already existing users to a project');

-- Add Project module permission to Project manager
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) SELECT users_groups_id, '26' FROM users_groups WHERE users_groups_name='Project manager';
