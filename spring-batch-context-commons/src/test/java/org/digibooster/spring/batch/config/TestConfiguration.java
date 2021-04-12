package org.digibooster.spring.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.digibooster.spring.batch.aop" })
public class TestConfiguration {

	@Bean
	public JobExecutionContextListenerTest jobExecutionContextListener() {
		return new JobExecutionContextListenerTest();
	}

}
