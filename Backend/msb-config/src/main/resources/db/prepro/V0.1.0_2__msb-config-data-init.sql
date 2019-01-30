-- MSB system config init data
SET NAMES utf8mb4 ;

-- 服务配置信息
LOCK TABLES `t_config` WRITE;
INSERT INTO t_config (id, application, profile, label, property_key, property_value, create_time)
VALUES
 ( 1 , 'msb-api', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 2 , 'msb-api', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 3 , 'msb-api', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 4 , 'msb-api', 'prepro', 'master', 'msb.api.maxPageSize', '1000000', sysdate()),
 ( 5 , 'msb-api', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 6 , 'msb-api', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 7 , 'msb-api', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 8 , 'msb-api', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 9 , 'msb-api', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 10 , 'msb-api', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 11 , 'msb-api', 'prepro', 'master', 'spring.redis.host', '188.4.12.53', sysdate()),
 ( 12 , 'msb-api', 'prepro', 'master', 'spring.redis.password', '{cipher}02d9240d988956b12e49a598d0bc7e4aed8a026d41e0f5d8df4d80ef6555a79b', sysdate()),
 ( 13 , 'msb-api', 'prepro', 'master', 'spring.redis.port', '16384', sysdate()),
 ( 14 , 'msb-api', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 15 , 'msb-api', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 16 , 'msb-auth', 'prepro', 'master', 'eureka.client.fetchRegistry', 'true', sysdate()),
 ( 17 , 'msb-auth', 'prepro', 'master', 'eureka.client.registerWithEureka', 'true', sysdate()),
 ( 18 , 'msb-auth', 'prepro', 'master', 'eureka.client.registry-fetch-interval-seconds', '10', sysdate()),
 ( 19 , 'msb-auth', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 20 , 'msb-auth', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 21 , 'msb-auth', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 22 , 'msb-auth', 'prepro', 'master', 'msb.auth.eomsServiceUrl', 'http://10.201.37.161/default/services/RestfulAdapter', sysdate()),
 ( 23 , 'msb-auth', 'prepro', 'master', 'msb.auth.msbFrontClientIds', '331524f7-3457-4016-a856-2d4ab84b19bc', sysdate()),
 ( 24 , 'msb-auth', 'prepro', 'master', 'msb.auth.ssoAuthServiceUrl', 'http://188.0.55.247/sso/oauth/token', sysdate()),
 ( 25 , 'msb-auth', 'prepro', 'master', 'msb.auth.ssoBlacklistServiceUrl', 'http://188.0.55.247/sso/token/blacklist', sysdate()),
 ( 26 , 'msb-auth', 'prepro', 'master', 'msb.autoLockDays', '90', sysdate()),
 ( 27 , 'msb-auth', 'prepro', 'master', 'ribbon.ConnectTimeout', '8000', sysdate()),
 ( 28 , 'msb-auth', 'prepro', 'master', 'ribbon.ReadTimeout', '8000', sysdate()),
 ( 29 , 'msb-auth', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 30 , 'msb-auth', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 31 , 'msb-auth', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 32 , 'msb-auth', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 33 , 'msb-auth', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 34 , 'msb-auth', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 35 , 'msb-auth', 'prepro', 'master', 'spring.redis.host', '188.4.12.53', sysdate()),
 ( 36 , 'msb-auth', 'prepro', 'master', 'spring.redis.password', '{cipher}02d9240d988956b12e49a598d0bc7e4aed8a026d41e0f5d8df4d80ef6555a79b', sysdate()),
 ( 37 , 'msb-auth', 'prepro', 'master', 'spring.redis.port', '16384', sysdate()),
 ( 38 , 'msb-auth', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 39 , 'msb-auth', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 40 , 'msb-break', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 41 , 'msb-break', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 42 , 'msb-break', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 43 , 'msb-break', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 44 , 'msb-break', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 45 , 'msb-break', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 46 , 'msb-break', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 47 , 'msb-break', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 48 , 'msb-break', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 49 , 'msb-break', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 50 , 'msb-break', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 51 , 'msb-fluid', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 52 , 'msb-fluid', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 53 , 'msb-fluid', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 54 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 55 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 56 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 57 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 58 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 59 , 'msb-fluid', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 60 , 'msb-fluid', 'prepro', 'master', 'spring.redis.host', '188.4.12.53', sysdate()),
 ( 61 , 'msb-fluid', 'prepro', 'master', 'spring.redis.password', '{cipher}02d9240d988956b12e49a598d0bc7e4aed8a026d41e0f5d8df4d80ef6555a79b', sysdate()),
 ( 62 , 'msb-fluid', 'prepro', 'master', 'spring.redis.port', '16384', sysdate()),
 ( 63 , 'msb-fluid', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 64 , 'msb-fluid', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 65 , 'msb-monitor', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 66 , 'msb-monitor', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 67 , 'msb-monitor', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 68 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 69 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.password', '{cipher}3e7f12bc3ece7d80dbc3858a1dd8e4a7af84d556f72d9ff55e3540700cf873be', sysdate()),
 ( 70 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 71 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3308/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 72 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 73 , 'msb-monitor', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 74 , 'msb-monitor', 'prepro', 'master', 'spring.redis.host', '188.4.12.53', sysdate()),
 ( 75 , 'msb-monitor', 'prepro', 'master', 'spring.redis.password', '{cipher}02d9240d988956b12e49a598d0bc7e4aed8a026d41e0f5d8df4d80ef6555a79b', sysdate()),
 ( 76 , 'msb-monitor', 'prepro', 'master', 'spring.redis.port', '16384', sysdate()),
 ( 77 , 'msb-monitor', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 78 , 'msb-monitor', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 79 , 'msb-route', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 80 , 'msb-route', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 81 , 'msb-route', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 82 , 'msb-route', 'prepro', 'master', 'msb.route.kongAdminUrl', 'http://msb-kong-admin-test:8001', sysdate()),
 ( 83 , 'msb-route', 'prepro', 'master', 'msb.route.zuulAddress', 'http://msb-zuul-test:10000', sysdate()),
 ( 84 , 'msb-route', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 85 , 'msb-route', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 86 , 'msb-route', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 87 , 'msb-route', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 88 , 'msb-route', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 89 , 'msb-route', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 90 , 'msb-route', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 91 , 'msb-route', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 92 , 'msb-service', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 93 , 'msb-service', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 94 , 'msb-service', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 95 , 'msb-service', 'prepro', 'master', 'msb.service.eurekaRegisterLogUrl', 'http://msb-eureka-prepro:8761/register_log', sysdate()),
 ( 96 , 'msb-service', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 97 , 'msb-service', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 98 , 'msb-service', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 99 , 'msb-service', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 100 , 'msb-service', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 101 , 'msb-service', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 102 , 'msb-service', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 103 , 'msb-service', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 104 , 'msb-system', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 105 , 'msb-system', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 106 , 'msb-system', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 107 , 'msb-system', 'prepro', 'master', 'msb.common.enabled', 'true', sysdate()),
 ( 108 , 'msb-system', 'prepro', 'master', 'msb.system.eomsServiceUrl', 'http://10.201.37.161/default/services/RestfulAdapter', sysdate()),
 ( 109 , 'msb-system', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 110 , 'msb-system', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 111 , 'msb-system', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 112 , 'msb-system', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 113 , 'msb-system', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 114 , 'msb-system', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 115 , 'msb-system', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 116 , 'msb-system', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 117 , 'msb-test', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 118 , 'msb-test', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 119 , 'msb-test', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 120 , 'msb-zuul', 'prepro', 'master', 'eureka.client.fetchRegistry', 'true', sysdate()),
 ( 121 , 'msb-zuul', 'prepro', 'master', 'eureka.client.registerWithEureka', 'true', sysdate()),
 ( 122 , 'msb-zuul', 'prepro', 'master', 'eureka.client.registry-fetch-interval-seconds', '10', sysdate()),
 ( 123 , 'msb-zuul', 'prepro', 'master', 'eureka.client.service-url.defaultZone', 'http://msb-eureka-prepro:8761/eureka', sysdate()),
 ( 124 , 'msb-zuul', 'prepro', 'master', 'eureka.instance.prefer-ip-address', 'true', sysdate()),
 ( 125 , 'msb-zuul', 'prepro', 'master', 'management.security.enabled', 'false', sysdate()),
 ( 126 , 'msb-zuul', 'prepro', 'master', 'msb.zuul.msbFrontClientIds', '331524f7-3457-4016-a856-2d4ab84b19bc', sysdate()),
 ( 127 , 'msb-zuul', 'prepro', 'master', 'msb.zuul.userTokenExpireSeconds', '1800', sysdate()),
 ( 128 , 'msb-zuul', 'prepro', 'master', 'rate.limit.api.h.default', '10800000', sysdate()),
 ( 129 , 'msb-zuul', 'prepro', 'master', 'rate.limit.api.m.default', '180000', sysdate()),
 ( 130 , 'msb-zuul', 'prepro', 'master', 'rate.limit.api.s.default', '3000', sysdate()),
 ( 131 , 'msb-zuul', 'prepro', 'master', 'rate.limit.app.h.default', '7200000', sysdate()),
 ( 132 , 'msb-zuul', 'prepro', 'master', 'rate.limit.app.m.default', '120000', sysdate()),
 ( 133 , 'msb-zuul', 'prepro', 'master', 'rate.limit.app.s.default', '2000', sysdate()),
 ( 134 , 'msb-zuul', 'prepro', 'master', 'ribbon.ConnectTimeout', '8000', sysdate()),
 ( 135 , 'msb-zuul', 'prepro', 'master', 'ribbon.ReadTimeout', '10000', sysdate()),
 ( 136 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.driver-class-name', 'com.mysql.jdbc.Driver', sysdate()),
 ( 137 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.password', '{cipher}a5cbc11a908df9095a89e0075480f9b990fe5e8bdac1eea49e7fa5770e318058', sysdate()),
 ( 138 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.test-on-borrow', 'true', sysdate()),
 ( 139 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.url', 'jdbc:mysql://188.4.15.216:3307/msb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false', sysdate()),
 ( 140 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.username', 'acnmsb', sysdate()),
 ( 141 , 'msb-zuul', 'prepro', 'master', 'spring.datasource.validation-query', 'select 1', sysdate()),
 ( 142 , 'msb-zuul', 'prepro', 'master', 'spring.jpa.show-sql', 'false', sysdate()),
 ( 143 , 'msb-zuul', 'prepro', 'master', 'spring.redis.host', '188.4.12.53', sysdate()),
 ( 144 , 'msb-zuul', 'prepro', 'master', 'spring.redis.password', '{cipher}02d9240d988956b12e49a598d0bc7e4aed8a026d41e0f5d8df4d80ef6555a79b', sysdate()),
 ( 145 , 'msb-zuul', 'prepro', 'master', 'spring.redis.port', '16384', sysdate()),
 ( 146 , 'msb-zuul', 'prepro', 'master', 'spring.sleuth.sampler.percentage', '1.0', sysdate()),
 ( 147 , 'msb-zuul', 'prepro', 'master', 'spring.zipkin.base-url', 'http://msb-zipkin-prepro:9411/', sysdate()),
 ( 148 , 'msb-zuul', 'prepro', 'master', 'zuul.ignored-services', 'msb-api,msb-zuul,msb-system,msb-service,msb-monitor,msb-route,msb-break,msb-config,msb-auth', sysdate());
 UNLOCK TABLES;

-- 服务密钥列表
LOCK TABLES `t_service_info` WRITE;
INSERT INTO `t_service_info` (`id`,`service_id`,`service_name`,`service_secret`) 
VALUES
(172,'msb-api','msb-api','msb-api'),
 (173,'msb-auth','msb-auth','msb-auth'),
 (174,'msb-break','msb-break','msb-break'),
 (175,'msb-config','msb-config','msb-config'),
 (176,'msb-fluid','流控策略管理服务','b4882c94cdc3e4711ff3f1ffb15492f0'),
 (177,'msb-monitor','msb-monitor','msb-monitor'),
 (178,'msb-route','msb-route','msb-route'),
 (179,'msb-service','msb-service','msb-service'),
 (180,'msb-system','msb-system','msb-system'),
 (181,'msb-zuul','msb-zuul','msb-zuul');
UNLOCK TABLES;