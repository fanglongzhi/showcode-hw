delete from t_error_code where code = '0001-10020';

INSERT INTO t_error_code (service_id, code, message, description) VALUES ( 'msb-api', '0001-10020', '应用已经设置了流控策略', '');
