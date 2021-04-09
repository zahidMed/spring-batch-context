package com.digibooster.spring.batch.locale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.digibooster.spring.batch.locale.listener.JobExecutionLocaleContextListener;

@Configuration
public class LocaleConfiguration {

	@Bean
	public JobExecutionLocaleContextListener jobExecutionLocaleContextListener() {
		return new JobExecutionLocaleContextListener();
	}
}
