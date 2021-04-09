package com.digibooster.spring.batch.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;

public class JobExecutionContextListenerTest implements JobExecutionContextListener {

	public void beforeJobSaving(ExecutionContext executionContext) {

	}

	public void beforeJobExecution(ExecutionContext executionContext) {
	}

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		jobParametersBuilder.addString("Param1", "textParam1");
		jobParametersBuilder.addLong("Param2", 12L);
	}

	@Override
	public void fillJobExecutionContext(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromJobExecutionContext(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restoreContext(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearContext(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}

}