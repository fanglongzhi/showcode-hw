package com.gmcc.msb.msbservice;

import com.gmcc.msb.common.config.CommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author zhi fanglong
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import(CommonConfiguration.class)
public class MsbServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsbServiceApplication.class, args);
    }
}
