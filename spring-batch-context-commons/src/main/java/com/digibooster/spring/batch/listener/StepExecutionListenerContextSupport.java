package com.digibooster.spring.batch.listener;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.util.CollectionUtils;

public class StepExecutionListenerContextSupport implements StepExecutionListener{

	
protected List<JobExecutionContextListener> jobExecutionContextListeners;
	
	public StepExecutionListenerContextSupport(List<JobExecutionContextListener> jobExecutionContextListeners){
		this.jobExecutionContextListeners= jobExecutionContextListeners;
	}
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		if(!CollectionUtils.isEmpty(jobExecutionContextListeners)){
			Iterator<JobExecutionContextListener> iter= jobExecutionContextListeners.iterator();
			while(iter.hasNext()){
				iter.next().restoreContext(stepExecution);
			}
		}
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if(!CollectionUtils.isEmpty(jobExecutionContextListeners)){
			Iterator<JobExecutionContextListener> iter= jobExecutionContextListeners.iterator();
			while(iter.hasNext()){
				iter.next().clearContext(stepExecution);
			}
		}
		return stepExecution.getExitStatus();
	}

}
