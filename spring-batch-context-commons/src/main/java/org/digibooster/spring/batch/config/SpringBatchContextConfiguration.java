package org.digibooster.spring.batch.config;

import java.util.List;

import org.digibooster.spring.batch.aop.JobExecutionAspect;
import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.listener.JobExecutionListenerContextSupport;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Job configuration.
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
@Configuration
@ComponentScan(basePackages = { "org.digibooster.spring.batch.aop" })
@EnableAspectJAutoProxy
public class SpringBatchContextConfiguration {

	@Bean
	public JobExecutionListenerContextSupport jobExecutionListenerContextSupport(
			@Autowired List<JobExecutionContextListener> jobExecutionContextListeners) {
		return new JobExecutionListenerContextSupport(jobExecutionContextListeners);
	}


	/**
	 * Bean pre-processing that registers job and step listener for the created job
	 * in order to recover the context information injected as job parameters by the
	 * {@link JobExecutionAspect}
	 * 
	 * @param jobExecutionListener
	 * @return
	 */
	@Bean
	public BeanPostProcessor jobPostProcessor(@Autowired final JobExecutionListenerContextSupport jobExecutionListener) {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof AbstractJob) {
					AbstractJob job = (AbstractJob) bean;
					job.registerJobExecutionListener(jobExecutionListener);
				}
				return bean;
			}
		};
	}

}
