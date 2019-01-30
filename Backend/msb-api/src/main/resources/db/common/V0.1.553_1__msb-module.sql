DELETE FROM `t_module` WHERE (`id` = '131');
DELETE FROM `t_service_api` WHERE (`id` = '10020');

INSERT INTO `t_service_api` (`id`, `service_id`, `api_name`, `path`, `method`, `status`, `is_annoymous_access`, `org_id`) VALUES ('10020', 'msb-api', '修改API订阅', '/api/order/', 'PUT', '2', '0', '1');
INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`, `status`) VALUES ('131', '5', 'API订阅-修改订阅', 'api-manage/api-subscribe:update', '10020', '0');
