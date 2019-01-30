
update t_config set property_value = 'http://msb-eureka-peer1-prepro:8761/register_log' where application = 'msb-service' and property_key = 'msb.service.eurekaRegisterLogUrl';