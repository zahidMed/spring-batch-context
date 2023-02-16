package org.digibooster.spring.batch.security.config;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.digibooster.spring.batch.security.listener.SecurityTaskExecutorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringSecurityConfiguration {

	@Bean
	public JobExecutionContextListener jobExecutionSecurityContextListener() {
		return new JobExecutionSecurityContextListener();
	}

	@Bean
	public SecurityTaskExecutorListener securityTaskExecutorListener(){
		return new SecurityTaskExecutorListener();
	}

}
