package com.gmcc.msb.api;

import com.gmcc.msb.common.config.CommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author Yuan Chunhai
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import(value = CommonConfiguration.class)
public class ServiceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApiApplication.class, args);
    }
}
