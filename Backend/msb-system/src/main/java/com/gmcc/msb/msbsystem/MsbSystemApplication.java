package com.gmcc.msb.msbsystem;

import com.gmcc.msb.common.config.CommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@Import(value = CommonConfiguration.class)
public class MsbSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsbSystemApplication.class, args);
	}
}
