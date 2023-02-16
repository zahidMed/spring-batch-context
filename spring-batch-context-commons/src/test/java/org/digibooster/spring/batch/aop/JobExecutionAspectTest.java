package org.digibooster.spring.batch.aop;


import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.Arrays;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@TestExecutionListeners(DirtiesContextTestExecutionListener.class)
public class JobExecutionAspectTest {

	JobLauncher aspectJobLauncher;

	TestJobLauncher jobLauncher = new TestJobLauncher();

	JobExecutionAspect jobExecutionAspect = new JobExecutionAspect(Arrays.asList((JobExecutionContextListener)new JobExecutionContextListenerTest()));

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

	public static class JobExecutionContextListenerTest implements JobExecutionContextListener {

		@Override
		public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
			jobParametersBuilder.addString("Param1", "textParam1");
			jobParametersBuilder.addLong("Param2", 12L);
		}


		@Override
		public void restoreContext(JobExecution jobExecution) {

		}


		@Override
		public void clearContext(JobExecution stepExecution) {

		}

	}

}
