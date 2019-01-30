
delete from t_config where application in ( 'msb-api','msb-auth','msb-break','msb-fluid','msb-monitor','msb-route','msb-service','msb-system','msb-zuul') and property_key = 'management.port' ;

INSERT INTO t_config (application, profile, label, property_key, property_value, create_time)
VALUES
 (  'msb-api', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-auth', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-break', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-fluid', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-monitor', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-route', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-service', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-system', 'pro', 'master', 'management.port', '8932', sysdate()),
 (  'msb-zuul', 'pro', 'master', 'management.port', '8932', sysdate())
;