DELETE FROM `t_service_api` WHERE (`id` = '10102');

INSERT INTO `t_service_api` (`id`, `service_id`, `api_name`, `path`, `method`, `status`, `is_annoymous_access`, `create_time`, `create_by`, `update_time`, `org_id`)
VALUES ('10102', 'msb-api', '首页统计数据', '/index_data/', 'GET', '2', '1', '2018-11-28 00:00:00', 'dwhuzhiwen2', '2018-11-20 17:03:36', '1');

INSERT INTO `t_app_order_api` (`id`,`app_id`,`api_id`,`status`,`start_date`,`end_date`,`create_date`,`create_by`,`update_by`,`update_date`)
VALUES (null ,75,10102,2,'2018-09-28 15:12:29','2050-01-06 15:12:29',NULL,NULL,NULL,NULL);
