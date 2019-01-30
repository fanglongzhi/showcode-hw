delete from t_error_code where code = '0003-10003';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10003', '该记录不存在', '');
delete from t_error_code where code = '0007-10001';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10001', '数据组已经存在', '');
delete from t_error_code where code = '0007-10002';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10002', '父组不存在', '');
delete from t_error_code where code = '0007-10003';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10003', '组不存在', '');