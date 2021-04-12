package org.digibooster.spring.batch.aop;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.digibooster.spring.batch.listener.JobExecutionContextListener;

/**
 * Aspect that intercepts the call of job run method in order to allow the
 * {@link JobExecutionContextListener} to insert the parameters they need for
 * restoring their contexts.
 * 
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
@Aspect
@Service
public class JobExecutionAspect {

	protected List<JobExecutionContextListener> jobExecutionContextListeners;

	public JobExecutionAspect(List<JobExecutionContextListener> jobExecutionContextListeners) {
		this.jobExecutionContextListeners = jobExecutionContextListeners;
	}

	@Around("execution(* org.springframework.batch.core.launch.JobLauncher+.run(..))")
	public JobExecution beforeRun(ProceedingJoinPoint joinPoint) throws Throwable {
		JobParameters jobParameters = (JobParameters) joinPoint.getArgs()[1];
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(jobParameters);
		if (!CollectionUtils.isEmpty(jobExecutionContextListeners)) {
			Iterator<JobExecutionContextListener> iter = jobExecutionContextListeners.iterator();
			while (iter.hasNext()) {
				iter.next().insertContextInfo(jobParametersBuilder);
			}
		}
		return (JobExecution) joinPoint
				.proceed(new Object[] { joinPoint.getArgs()[0], jobParametersBuilder.toJobParameters() });

	}

}
