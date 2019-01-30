delete from t_error_code where code = '0001-10005';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10005', '该状态不能执行此操作', '');
delete from t_error_code where code = '0006-00002';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-service', '0006-00002', '服务名或者服务ID不能重复', '');