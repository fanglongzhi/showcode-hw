package com.gmcc.msb.msbbreak;

import com.gmcc.msb.common.config.CommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(CommonConfiguration.class)
public class MsbBreakApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsbBreakApplication.class, args);
	}
}
