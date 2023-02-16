package org.digibooster.spring.batch.listener;

import org.digibooster.spring.batch.aop.JobExecutionAspect;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;

/**
 * Allow the restoring the context of the thread that runs the job inside the
 * job itself.
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public interface JobExecutionContextListener {

	/**
	 * Serializes the current context information and inserts it as a job
	 * parameter. This method called by the Aspect {@link JobExecutionAspect}
	 * 
	 * @param jobParametersBuilder
	 */
	void insertContextInfo(JobParametersBuilder jobParametersBuilder);

	/**
	 * Restores the context information inside the Job thread.
	 * This method is called by the Job listener.
	 * @param jobExecution
	 */
	void restoreContext(JobExecution jobExecution);


	/**
	 * Removes the context information when the job ends and restore the previous one.
	 * @param jobExecution
	 */
	void clearContext(JobExecution jobExecution);

}
