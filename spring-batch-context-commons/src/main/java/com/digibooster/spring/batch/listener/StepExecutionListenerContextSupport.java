package com.digibooster.spring.batch.listener;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.util.CollectionUtils;

/**
 * Listener that call the registered {@link JobExecutionContextListener} before
 * and after running the batch steps.
 * 
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class StepExecutionListenerContextSupport implements StepExecutionListener {

	protected List<JobExecutionContextListener> jobExecutionContextListeners;

	public StepExecutionListenerContextSupport(List<JobExecutionContextListener> jobExecutionContextListeners) {
		this.jobExecutionContextListeners = jobExecutionContextListeners;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		if (!CollectionUtils.isEmpty(jobExecutionContextListeners)) {
			Iterator<JobExecutionContextListener> iter = jobExecutionContextListeners.iterator();
			while (iter.hasNext()) {
				iter.next().restoreContext(stepExecution);
			}
		}

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!CollectionUtils.isEmpty(jobExecutionContextListeners)) {
			Iterator<JobExecutionContextListener> iter = jobExecutionContextListeners.iterator();
			while (iter.hasNext()) {
				iter.next().clearContext(stepExecution);
			}
		}
		return stepExecution.getExitStatus();
	}

}
