package com.digibooster.spring.batch.security.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;
import com.digibooster.spring.batch.util.CustomJobParameter;

/**
 * This class restores spring-security context inside the Spring batch job
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class JobExecutionSecurityContextListener implements JobExecutionContextListener{

	private final Logger log = LoggerFactory.getLogger(JobExecutionSecurityContextListener.class);
	
	private static final String SECURITY_PARAM_NAME="security-param";
	
	private static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();
	
	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the security context");
		SecurityContext securityContext= SecurityContextHolder.getContext();
		Authentication authentication= securityContext.getAuthentication();
		if(authentication!=null) {
			jobParametersBuilder.addParameter(SECURITY_PARAM_NAME, new CustomJobParameter<Authentication>(authentication));
		}
	}

	@Override
	public void fillJobExecutionContext(JobExecution jobExecution) {
		log.debug("Restore the scurity context");
		CustomJobParameter<Authentication> authentication=(CustomJobParameter<Authentication>) jobExecution.getJobParameters().getParameters().get(SECURITY_PARAM_NAME);
		if(authentication!=null) {
			jobExecution.getExecutionContext().put(SECURITY_PARAM_NAME,(Authentication)authentication.getValue());
		}
		else {
			log.error("Could not find parameter {} in order to restore the security context",SECURITY_PARAM_NAME);
		}
		
	}

	@Override
	public void removeFromJobExecutionContext(JobExecution jobExecution) {
		jobExecution.getExecutionContext().remove(SECURITY_PARAM_NAME);
		
	}

	@Override
	public void restoreContext(StepExecution stepExecution) {
		if(stepExecution.getJobExecution().getExecutionContext().containsKey(SECURITY_PARAM_NAME)) {
			log.debug("Restore the security context");
			Authentication authentication=(Authentication) stepExecution.getJobExecution().getExecutionContext().get(SECURITY_PARAM_NAME);
			SecurityContext securityContext= SecurityContextHolder.getContext();
			ORIGINAL_CONTEXT.set(securityContext.getAuthentication());
			securityContext.setAuthentication(authentication);
		}
		else {
			log.error("Could not find key {} in the job execution context",SECURITY_PARAM_NAME);
		}
	}

	@Override
	public void clearContext(StepExecution stepExecution) {
		log.debug("Clear the security context");
		SecurityContextHolder.clearContext();
		Authentication originalAuth = ORIGINAL_CONTEXT.get();
		if(originalAuth!=null) {
			SecurityContextHolder.getContext().setAuthentication(originalAuth);
			ORIGINAL_CONTEXT.remove();
		}
	}

}
