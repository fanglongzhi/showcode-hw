DELETE FROM `t_module` WHERE (`id` = '129');
DELETE FROM `t_module` WHERE (`id` = '130');

INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`) VALUES ('129', '4', '我的应用-批量删除订阅', 'api-manage/my-app:batchDelete', '10101');
INSERT INTO `t_module` (`id`, `parent_id`, `module_name`, `m_key`, `apis`) VALUES ('130', '4', '我的应用-批量取消订阅', 'api-manage/my-app:batchCancel', '10100');