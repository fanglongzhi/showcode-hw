delete from t_error_code where code = '0010-10001';
delete from t_error_code where code = '0010-10002';
delete from t_error_code where code = '0010-10003';
delete from t_error_code where code = '0010-10004';
delete from t_error_code where code = '0010-10005';
delete from t_error_code where code = '0010-10006';



INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10001', '不支持该流控类型', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10002', 'APP不存在', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10003', 'API不存在', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10004', '流控策略已经存在', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10005', '流控策略不存在', '');
INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-fluid', '0010-10006', '秒，分，时 必须设置一个', '');