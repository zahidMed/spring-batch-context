package org.digibooster.spring.batch.sleuth.config;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.sleuth.listener.JobExecutionSleuthContextListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfiguration {

	@Bean
	public JobExecutionContextListener jobExecutionSleuthContextListener(@Autowired Tracer tracer) {
		return new JobExecutionSleuthContextListener(tracer);
	}

}
