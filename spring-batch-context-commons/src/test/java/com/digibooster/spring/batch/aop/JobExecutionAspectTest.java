package com.digibooster.spring.batch.aop;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.digibooster.spring.batch.config.JobExecutionContextListenerTest;
import com.digibooster.spring.batch.config.TestConfiguration;
import com.digibooster.spring.batch.config.TestContextValue;
import com.digibooster.spring.batch.listener.JobExecutionContextListener;


//@RunWith(SpringRunner.class)
//@SpringBootTest
//@SpringBatchTest
//@EnableAutoConfiguration
//@ContextConfiguration(classes = { TestConfiguration.class })
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
//  DirtiesContextTestExecutionListener.class})
//@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
//@TestPropertySource(locations = "classpath:application.properties")
public class JobExecutionAspectTest {

	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	JobExecutionContextListenerTest contextListenerTest;
	
	@Autowired
	Job job;

	
	//@Test
	public void test() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException{
		contextListenerTest.setInputValue(new TestContextValue("test value",10));
		jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
		Thread.sleep(20000);
		Assert.assertEquals(new TestContextValue("test value",10), contextListenerTest.getOutputValue());
		
	}

}
