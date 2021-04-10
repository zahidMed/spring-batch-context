package com.digibooster.spring.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.digibooster.spring.batch.aop" })
public class TestConfiguration {

	@Bean
	public JobExecutionContextListenerTest jobExecutionContextListener() {
		return new JobExecutionContextListenerTest();
	}

}
