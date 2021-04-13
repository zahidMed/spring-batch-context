package org.digibooster.spring.batch.listener;

import org.digibooster.spring.batch.aop.JobExecutionAspect;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;

/**
 * Allow the restoring the context of the thread that runs the job inside the
 * job it self.
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public interface JobExecutionContextListener {

	/**
	 * Serializes the current context information and puts it in the the job
	 * parameter. This method called by the Aspect {@link JobExecutionAspect}
	 * 
	 * @param jobParametersBuilder
	 */
	void insertContextInfo(JobParametersBuilder jobParametersBuilder);

	/**
	 * Deserializes the context information from the job parameters and inserts it
	 * in the Job execution context. This method is called by the Job listener
	 * {@link JobExecutionListenerContextSupport}
	 * 
	 * @param jobExecution
	 */
	void fillJobExecutionContext(JobExecution jobExecution);

	/**
	 * Removes the context information from job execution context when the job ends
	 * This method is called by the Job listener
	 * {@link JobExecutionListenerContextSupport}
	 * 
	 * @param jobExecution
	 */
	void removeFromJobExecutionContext(JobExecution jobExecution);

	/**
	 * Restore the context information from the job execution context before each
	 * step This method is called by the Step listener
	 * {@link StepExecutionListenerContextSupport}
	 * 
	 * @param stepExecution
	 */
	void restoreContext(StepExecution stepExecution);

	/**
	 * Remove the context information when the step ends This method is called by
	 * the Step listener {@link StepExecutionListenerContextSupport}
	 * 
	 * @param stepExecution
	 */
	void clearContext(StepExecution stepExecution);

}
