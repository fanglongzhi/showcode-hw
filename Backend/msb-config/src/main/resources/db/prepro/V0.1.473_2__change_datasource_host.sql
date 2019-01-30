update t_config set property_value = 'jdbc:mysql://192.169.17.52:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false' where property_key = 'spring.datasource.url' and application in ('msb-api','msb-auth','msb-break','msb-fluid','msb-route','msb-service','msb-system','msb-zuul');

update t_config set property_value = 'jdbc:mysql://192.169.17.52:3308/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false' where property_key = 'spring.datasource.url' and application = 'msb-monitor';
