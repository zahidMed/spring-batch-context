package org.digibooster.spring.batch.locale.config;

import org.digibooster.spring.batch.locale.listener.JobExecutionLocaleContextListener;
import org.digibooster.spring.batch.locale.listener.LocaleTaskExecutorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocaleConfiguration {

	@Bean
	public JobExecutionLocaleContextListener jobExecutionLocaleContextListener() {
		return new JobExecutionLocaleContextListener();
	}

	@Bean
	public LocaleTaskExecutorListener localeTaskExecutorListener(){
		return new LocaleTaskExecutorListener();
	}
}
