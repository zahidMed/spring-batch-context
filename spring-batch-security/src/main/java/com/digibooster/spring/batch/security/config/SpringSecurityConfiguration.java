package com.digibooster.spring.batch.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;
import com.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;

@Configuration
public class SpringSecurityConfiguration {
	
	@Bean
	public JobExecutionContextListener jobExecutionMDCContextListener() {
		return new JobExecutionSecurityContextListener();
	}

}
