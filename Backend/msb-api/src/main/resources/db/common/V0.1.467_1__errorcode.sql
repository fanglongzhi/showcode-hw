delete from t_error_code where code = '0008-31146';
delete from t_error_code where code = '0008-10015';

INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-zuul', '0008-31146', '表不存在', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-zuul', '0008-10015', '未匹配服务路由', '');