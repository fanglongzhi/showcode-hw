INSERT INTO `t_module` (`id`,`parent_id`,`module_name`,`m_key`,`apis`,`create_time`,`create_by`,`update_time`,`update_by`,`status`,`org_id`)
VALUES 
(137, 111, '熔断操作日志', 'monitor/break-manage-log', NULL, NULL, NULL, NULL, NULL, '0', NULL),
(138, 132, '熔断操作日志-查询', 'monitor/break-manage-log:list', '10103', NULL, NULL, NULL, NULL, '0', NULL);



INSERT INTO `t_service_api` (`id`,`service_id`,`api_name`,`path`,`method`,`status`,`is_annoymous_access`,`create_time`,`create_by`,`update_time`,`update_by`,`org_id`)
VALUES 
('10106', 'msb-break', '熔断策略日志获取接口', '/break/log/', 'GET', 2,0,NULL,NULL,NULL,NULL,1);

