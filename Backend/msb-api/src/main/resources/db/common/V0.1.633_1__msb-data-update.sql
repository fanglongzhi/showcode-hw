INSERT INTO `t_module` (`id`,`parent_id`,`module_name`,`m_key`,`apis`,`create_time`,`create_by`,`update_time`,`update_by`,`status`,`org_id`)
VALUES 
(132, 111, '流控操作日志', 'monitor/manage-log', NULL, NULL, NULL, NULL, NULL, '0', NULL),
(133, 132, '流控操作日志-查询', 'monitor/manage-log:list', '10103', NULL, NULL, NULL, NULL, '0', NULL);



INSERT INTO `t_service_api` (`id`,`service_id`,`api_name`,`path`,`method`,`status`,`is_annoymous_access`,`create_time`,`create_by`,`update_time`,`update_by`,`org_id`) 
VALUES 
('10103', 'msb-fluid', '流控策略日志获取接口', '/strategy/log/page/', 'GET', 2,0,NULL,NULL,NULL,NULL,1);

