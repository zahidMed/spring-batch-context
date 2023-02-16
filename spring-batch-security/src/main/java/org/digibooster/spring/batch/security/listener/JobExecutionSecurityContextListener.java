package org.digibooster.spring.batch.security.listener;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.batch.core.JobExecution;

import java.util.Map;

/**
 * This class restores spring-security context inside the Spring batch job
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class JobExecutionSecurityContextListener implements JobExecutionContextListener {

	private final Logger log = LoggerFactory.getLogger(JobExecutionSecurityContextListener.class);

	public static final String SECURITY_PARAM_NAME = "security-param";

	protected static final ThreadLocal<Authentication> ORIGINAL_CONTEXT = new ThreadLocal<>();

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the security context");
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			jobParametersBuilder.addParameter(SECURITY_PARAM_NAME,
					new SerializableJobParameter(authentication));
		}
	}

	@Override
	public void restoreContext(JobExecution jobExecution) {
		restoreContext(jobExecution.getJobParameters().getParameters(),true);
	}

	protected void restoreContext(Map<String, JobParameter> parameters, boolean keepCurrentContext){
		if (!parameters.containsKey(SECURITY_PARAM_NAME)) {
			log.error("Could not find parameter {} in order to restore the security context", SECURITY_PARAM_NAME);
			return;
		}
		SerializableJobParameter<Authentication> authentication = (SerializableJobParameter<Authentication>) parameters.get(SECURITY_PARAM_NAME);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(keepCurrentContext) {
			ORIGINAL_CONTEXT.set(securityContext.getAuthentication());
		}
		securityContext.setAuthentication(authentication.getValue());

	}

	@Override
	public void clearContext(JobExecution jobExecution) {
		log.debug("Clear the security context from Job: {}", jobExecution.getJobInstance().getJobName());
		SecurityContextHolder.clearContext();
		Authentication originalAuth = ORIGINAL_CONTEXT.get();
		if (originalAuth != null) {
			SecurityContextHolder.getContext().setAuthentication(originalAuth);
			ORIGINAL_CONTEXT.remove();
		}
	}

}
