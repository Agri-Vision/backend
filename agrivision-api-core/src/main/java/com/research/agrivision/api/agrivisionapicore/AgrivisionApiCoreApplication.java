package com.research.agrivision.api.agrivisionapicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class AgrivisionApiCoreApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(AgrivisionApiCoreApplication.class, args);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
