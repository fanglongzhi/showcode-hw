package com.msb.join.demo;

import com.msb.join.demo.utils.Config;
import com.msb.join.demo.utils.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program:
 * @description: 应用启动成功后向注册表注册自己
 * @author: zhifanglong
 * @create: 2018-11-14 18:07
 */
@Component
@Order(1)
public class ApplicationStartup implements ApplicationRunner {
   @Autowired
   private Register register;
   @Autowired
   private Config config;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1.首先，获取配置信息
        config.fetchConfig();
        //2.然后，服务注册
       register.register();
    }
}