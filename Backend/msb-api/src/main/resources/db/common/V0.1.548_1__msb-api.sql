DELETE FROM `t_service_api` WHERE (`id` = '10100');
DELETE FROM `t_service_api` WHERE (`id` = '10101');

INSERT INTO `t_service_api` (`id`, `service_id`, `api_name`, `path`, `method`, `status`, `is_annoymous_access`, `create_time`, `create_by`, `update_time`, `org_id`) VALUES ('10100', 'msb-api', 'API批量取消订阅', '/api/order/unsubscribe/', 'POST', '2', '0', '2018-11-20 17:02:30', 'dwhuzhiwen2', '2018-11-20 17:03:36', '1');
INSERT INTO `t_service_api` (`id`, `service_id`, `api_name`, `path`, `method`, `status`, `is_annoymous_access`, `create_time`, `create_by`, `update_time`, `org_id`) VALUES ('10101', 'msb-api', 'API批量删除订阅', '/api/order/delete/list/', 'POST', '2', '0', '2018-11-20 17:03:19', 'dwhuzhiwen2', '2018-11-20 17:03:39', '1');
