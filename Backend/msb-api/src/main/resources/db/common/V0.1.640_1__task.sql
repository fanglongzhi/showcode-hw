CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) DEFAULT NULL,
  `task_key` varchar(200) DEFAULT NULL,
  `task_name` varchar(100) DEFAULT NULL,
  `last_run_time` datetime DEFAULT NULL,
  `last_run_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_task_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL,
  `run_time` datetime DEFAULT NULL,
  `run_status` tinyint(1) DEFAULT NULL COMMENT '0：成功，1：失败',
  `error_message` text COMMENT '错误信息',
  `elasped` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `t_module` (`id`,`parent_id`,`module_name`,`m_key`,`apis`,`create_time`,`create_by`,`update_time`,`update_by`,`status`,`org_id`)
VALUES
(134, 74, '定时任务', 'system/task', NULL, NULL, NULL, NULL, NULL, '0', NULL),
(135, 134, '定时任务-列表', 'system/task:list', '10104', NULL, NULL, NULL, NULL, '0', NULL),
(136, 134, '定时任务-详细日志', 'system/task:logs', '10105', NULL, NULL, NULL, NULL, '0', NULL);



INSERT INTO `t_service_api` (`id`,`service_id`,`api_name`,`path`,`method`,`status`,`is_annoymous_access`,`create_time`,`create_by`,`update_time`,`update_by`,`org_id`)
VALUES
('10104', 'msb-system', '定时任务列表', '/task/list/', 'GET', 2,0,NULL,NULL,NULL,NULL,1),
('10105', 'msb-system', '定时任务日志', '/tasklogs/{taskId}/', 'GET', 2,0,NULL,NULL,NULL,NULL,1);

INSERT INTO `t_role_auth` (`role_id`,`auth_id`,`create_time`,`create_by`)
VALUES
(1,134,NULL,NULL),
(1,135,NULL,NULL),
(1,136,NULL,NULL);

