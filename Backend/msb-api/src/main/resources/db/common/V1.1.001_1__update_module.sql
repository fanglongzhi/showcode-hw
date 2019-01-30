UPDATE `t_module` SET `id` = '1000' WHERE (`id` = '73');
UPDATE `t_module` SET `id` = '1001' WHERE (`id` = '72');
UPDATE `t_module` SET `id` = '1002' WHERE (`id` = '70');
UPDATE `t_module` SET `id` = '1003' WHERE (`id` = '71');
UPDATE `t_module` SET `id` = '1004' WHERE (`id` = '69');
UPDATE `t_module` SET `id` = '1005' WHERE (`id` = '111');
UPDATE `t_module` SET `id` = '1006' WHERE (`id` = '74');

UPDATE `t_module` SET `parent_id` = '1000' WHERE (`parent_id` = '73');
UPDATE `t_module` SET `parent_id` = '1001' WHERE (`parent_id` = '72');
UPDATE `t_module` SET `parent_id` = '1002' WHERE (`parent_id` = '70');
UPDATE `t_module` SET `parent_id` = '1003' WHERE (`parent_id` = '71');
UPDATE `t_module` SET `parent_id` = '1004' WHERE (`parent_id` = '69');
UPDATE `t_module` SET `parent_id` = '1005' WHERE (`parent_id` = '111');
UPDATE `t_module` SET `parent_id` = '1006' WHERE (`parent_id` = '74');


UPDATE `t_role_auth` SET `auth_id` = '1000' WHERE (`auth_id` = '73');
UPDATE `t_role_auth` SET `auth_id` = '1001' WHERE (`auth_id` = '72');
UPDATE `t_role_auth` SET `auth_id` = '1002' WHERE (`auth_id` = '70');
UPDATE `t_role_auth` SET `auth_id` = '1003' WHERE (`auth_id` = '71');
UPDATE `t_role_auth` SET `auth_id` = '1004' WHERE (`auth_id` = '69');
UPDATE `t_role_auth` SET `auth_id` = '1005' WHERE (`auth_id` = '111');
UPDATE `t_role_auth` SET `auth_id` = '1006' WHERE (`auth_id` = '74');

INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`, `status`) VALUES ('146', '139', '网关监控-查询', 'monitor/gateway:list', '20035,20036,20037', '0');
UPDATE `t_module` SET `id` = '139', `apis` = '' WHERE (`id` = '127');
INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`, `status`) VALUES ('147', '142', '熔断监控-查询', 'monitor/hystrix-dashboard:list', '10003', '0');
UPDATE `t_module` SET `id` = '142', `m_key` = 'monitor/hystrix-dashboard', `apis` = '' WHERE (`id` = '110');
INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `status`) VALUES ('148', '141', '服务调用链-查询', 'monitor/zipkin-dashboard:list', '0');
UPDATE `t_module` SET `id` = '141', `m_key` = 'monitor/zipkin-dashboard' WHERE (`id` = '112');
INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`, `status`) VALUES ('149', '140', 'API调用统计-查询列表', 'monitor/api-count:list', '10003,20041,20042,20043', '0');
UPDATE `t_module` SET `id` = '140' WHERE (`id` = '128');
UPDATE `t_module` SET `id` = '143' WHERE (`id` = '116');
UPDATE `t_module` SET `id` = '144' WHERE (`id` = '132');
UPDATE `t_module` SET `id` = '145' WHERE (`id` = '137');

UPDATE `t_module` SET `parent_id` = '143' WHERE (`id` = '117');
UPDATE `t_module` SET `parent_id` = '144' WHERE (`id` = '133');
UPDATE `t_module` SET `parent_id` = '145' WHERE (`id` = '136');


INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '139');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '140');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '141');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '142');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '143');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '144');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '145');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '146');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '147');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '148');
INSERT INTO `t_role_auth` (`role_id`, `auth_id`) VALUES ('1', '149');


