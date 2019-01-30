delete from t_error_code where code = '0007-10004';
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-system', '0007-10004', '数据组中含有用户，不能删除', '');