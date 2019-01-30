delete from t_error_code where code = '0007-10008';
delete from t_error_code where code = '0007-10009';

INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10008', '数据组中存在应用，不能删除', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10009', '数据组中存在服务，不能删除', '');