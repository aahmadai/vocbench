-- ------------------------------------------------------
-- VB version 2.3 SQL Script
-- ------------------------------------------------------

-- Add Scheme permission to other groups 
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('2', '21');
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('3', '21');
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('4', '21');
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('5', '21');


-- Add Refactor Module
INSERT IGNORE INTO `permissiontype` VALUES ('25', 'Refactor');
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) VALUES ('1', '25');
