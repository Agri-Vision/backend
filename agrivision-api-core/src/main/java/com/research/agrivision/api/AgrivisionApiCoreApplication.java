package com.research.agrivision.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableAsync
public class AgrivisionApiCoreApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(AgrivisionApiCoreApplication.class, args);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("JDAsync-");
		executor.initialize();
		return executor;
	}

}
