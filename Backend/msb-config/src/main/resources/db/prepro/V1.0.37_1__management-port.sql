
delete from t_config where application in ( 'msb-api','msb-auth','msb-break','msb-fluid','msb-monitor','msb-route','msb-service','msb-system','msb-zuul') and property_key = 'management.port' ;

INSERT INTO t_config (application, profile, label, property_key, property_value, create_time)
VALUES
 (  'msb-api', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-auth', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-break', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-fluid', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-monitor', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-route', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-service', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-system', 'prepro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-zuul', 'prepro', 'master', 'management.port', '8932', sysdate())
;