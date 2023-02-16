package org.digibooster.spring.batch.listener;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

/**
 * Listener that call the registered {@link JobExecutionContextListener} before
 * and after running the batch jobs.
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
public class JobExecutionListenerContextSupport implements JobExecutionListener {

	protected List<JobExecutionContextListener> jobExecutionContextListeners;

	public JobExecutionListenerContextSupport(List<JobExecutionContextListener> jobExecutionContextListeners) {
		this.jobExecutionContextListeners = jobExecutionContextListeners;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (!CollectionUtils.isEmpty(jobExecutionContextListeners)) {
			Iterator<JobExecutionContextListener> iter = jobExecutionContextListeners.iterator();
			while (iter.hasNext()) {
				iter.next().restoreContext(jobExecution);
			}
		}

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (!CollectionUtils.isEmpty(jobExecutionContextListeners)) {
			Iterator<JobExecutionContextListener> iter = jobExecutionContextListeners.iterator();
			while (iter.hasNext()) {
				iter.next().clearContext(jobExecution);
			}
		}



	}

}
