
delete from t_config where application in ( 'msb-api','msb-auth','msb-break','msb-fluid','msb-monitor','msb-route','msb-service','msb-system','msb-zuul') and property_key = 'management.port' ;

INSERT INTO t_config (application, profile, label, property_key, property_value, create_time)
VALUES
 (  'msb-api', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-auth', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-break', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-fluid', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-monitor', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-route', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-service', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-system', 'test', 'master', 'management.port', '8932', sysdate()),
 (  'msb-zuul', 'test', 'master', 'management.port', '8932', sysdate())
;