
delete from t_error_code where code = '0001-00023';
delete from t_error_code where code = '0001-00024';
delete from t_error_code where code = '0001-00025';
delete from t_error_code where code = '0001-00026';
delete from t_error_code where code = '0001-00027';

delete from t_error_code where code = '0001-10136';
delete from t_error_code where code = '0001-10137';
delete from t_error_code where code = '0001-10138';
delete from t_error_code where code = '0001-10139';


INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-00023', '开始时间不能为空', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-00024', '结束时间不能为空', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-00025', '开始时间需小于结束时间', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-00026', '请求内容不能为空', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-00027', 'ID不能为空', '');


INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10136', '订阅记录时间重叠', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10137', '未找到记录', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10138', '状态不正确', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10139', '订阅记录时间重叠', '');
