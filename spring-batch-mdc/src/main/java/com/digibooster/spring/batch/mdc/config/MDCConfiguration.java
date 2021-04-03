package com.digibooster.spring.batch.mdc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;
import com.digibooster.spring.batch.mdc.listener.JobExecutionMDCContextListener;

@Configuration
public class MDCConfiguration {
	
	@Bean
	public JobExecutionContextListener jobExecutionMDCContextListener() {
		return new JobExecutionMDCContextListener();
	}

}
