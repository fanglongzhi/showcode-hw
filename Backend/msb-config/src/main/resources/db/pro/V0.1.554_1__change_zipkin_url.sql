
update t_config set property_value = 'http://msb-zipkin-pro:9411/' where property_key = 'spring.zipkin.base-url' and application in ('msb-api','msb-auth','msb-break','msb-fluid','msb-monitor','msb-route','msb-service','msb-system','msb-zuul');
