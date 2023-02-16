package org.digibooster.spring.batch.mdc.config;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.mdc.listener.JobExecutionMDCContextListener;
import org.digibooster.spring.batch.mdc.listener.MDCTaskExecutorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MDCConfiguration {

	@Bean
	public JobExecutionContextListener jobExecutionMDCContextListener() {
		return new JobExecutionMDCContextListener();
	}


	@Bean
	public MDCTaskExecutorListener mdcTaskExecutorListener(){
		return new MDCTaskExecutorListener();
	}
}
