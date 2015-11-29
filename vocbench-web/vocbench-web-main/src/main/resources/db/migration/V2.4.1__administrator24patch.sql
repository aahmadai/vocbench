-- ------------------------------------------------------
-- VB version 2.4 SQL Script
-- ------------------------------------------------------

--
-- Table structure for table `users_groups_projects`
--

CREATE TABLE IF NOT EXISTS `users_groups_projects` (
  `users_id` int(7) NOT NULL DEFAULT '0',
  `users_group_id` int(2) NOT NULL DEFAULT '0',
  `project_id` int(7) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `users_groups_projects`
--
ALTER TABLE `users_groups_projects`
 ADD PRIMARY KEY (`users_id`,`users_group_id`,`project_id`);
 
 --
-- Table structure for table `users_language_projects`
--

CREATE TABLE IF NOT EXISTS `users_language_projects` (
  `user_id` int(7) NOT NULL DEFAULT '0',
  `language_code` varchar(2) NOT NULL,
  `project_id` int(7) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Indexes for table `users_language_projects`
--
ALTER TABLE `users_language_projects`
 ADD PRIMARY KEY (`user_id`,`language_code`,`project_id`);
 
 
 ALTER TABLE `users_preference`
  DROP PRIMARY KEY,
   ADD PRIMARY KEY(
     `user_id`,
     `ontology_id`);
     
--
-- Add User module permission to group 'Project Manager'
--     
INSERT IGNORE INTO `permission_group_map` (`users_groups_id`, `permission_id`) SELECT users_groups_id, '11' FROM users_groups WHERE users_groups_name='Project manager';
