
DELETE FROM `msb`.`t_user` WHERE (`id` = '43');
INSERT INTO `msb`.`t_user` (`id`, `operator_id`, `name`, `status`, `is_lock`, `org_id`, `login_id`, `user_id`, `person_card_no`, `email`, `mobile`, `create_time`, `update_time`, `org_name`) VALUES ('43', '209527', '微服务与网管能力开放平台超级管理员', '0', '0', '104869', 'msbadmin', 'msbadmin', '', '', '13902220057', '2018-09-27 16:51:17', '2018-12-14 09:29:08', '网管支撑室');
DELETE FROM `msb`.`t_data_org_user` WHERE (`org_id` = '1') and (`user_id` = 'msbadmin');
INSERT INTO `msb`.`t_data_org_user` (`org_id`, `user_id`, `create_time`, `main_flag`) VALUES ('1', 'msbadmin', '2018-12-12 14:37:37', '1');

INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '129', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '130', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '131', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '132', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '133', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '137', '2018-11-12 11:09:12', 'duanxin');
INSERT INTO `msb`.`t_role_auth` (`role_id`, `auth_id`, `create_time`, `create_by`) VALUES ('1', '138', '2018-11-12 11:09:12', 'duanxin');


