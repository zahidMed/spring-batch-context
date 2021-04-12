package org.digibooster.spring.batch.aop;

import org.digibooster.spring.batch.aop.JobExecutionAspect;
import org.digibooster.spring.batch.config.TestConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@TestPropertySource(locations = "classpath:application.properties")
public class JobExecutionAspectTest {

	JobLauncher aspectJobLauncher;

	TestJobLauncher jobLauncher = new TestJobLauncher();

	@Autowired
	JobExecutionAspect jobExecutionAspect;

	@Before
	public void setUp() {

		AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(jobLauncher);
		aspectJProxyFactory.addAspect(jobExecutionAspect);

		DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
		AopProxy aopProxy = proxyFactory.createAopProxy(aspectJProxyFactory);

		aspectJobLauncher = (JobLauncher) aopProxy.getProxy();
	}

	@Test
	public void testBeforeRun() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
		JobParametersBuilder expectedParametersBuilder = new JobParametersBuilder();
		expectedParametersBuilder.addString("originalParam", "originalParam");
		expectedParametersBuilder.addString("Param1", "textParam1");
		expectedParametersBuilder.addLong("Param2", 12L);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("originalParam", "originalParam");
		aspectJobLauncher.run(null, jobParametersBuilder.toJobParameters());

		Assert.assertEquals(expectedParametersBuilder.toJobParameters(), jobLauncher.getJobParameters());
	}

	public static class TestJobLauncher implements JobLauncher {

		private JobParameters jobParameters;

		public JobParameters getJobParameters() {
			return jobParameters;
		}

		@Override
		public JobExecution run(Job job, JobParameters jobParameters) throws JobExecutionAlreadyRunningException,
				JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
			this.jobParameters = jobParameters;
			return null;
		}
	}

}
