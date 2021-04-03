package com.digibooster.spring.batch.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;

public class JobExecutionContextListenerTest implements JobExecutionContextListener {

	private TestContextValue inputValue;
	
	public TestContextValue getOutputValue() {
		return outputValue;
	}

	public void setInputValue(TestContextValue inputValue) {
		this.inputValue = inputValue;
	}

	private TestContextValue outputValue;
	

	public void beforeJobSaving(ExecutionContext executionContext) {
		executionContext.put("textVal",inputValue);

	}

	public void beforeJobExecution(ExecutionContext executionContext) {
		outputValue= (TestContextValue) executionContext.get("textVal");
	}

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		// TODO Auto-generated method stub
		
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