delete from t_error_code where code = '0003-10013';
delete from t_error_code where code = '0003-10014';
delete from t_error_code where code = '0003-10015';
delete from t_error_code where code = '0003-10016';
delete from t_error_code where code = '0003-10017';



INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10013', '刷新签名key请求失败', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10014', '刷新签名key请求返回失败', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10015', '刷新签名key网络错误', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10016', '刷新签名key失败，无实例', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-config', '0003-10017', '未获取到微服务信息，无法同步配置', '');