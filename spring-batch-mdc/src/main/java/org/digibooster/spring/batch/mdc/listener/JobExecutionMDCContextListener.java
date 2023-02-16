package org.digibooster.spring.batch.mdc.listener;

import java.util.HashMap;
import java.util.Map;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.*;

/**
 * This class restores MDC stored information context inside the Spring batch
 * job
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class JobExecutionMDCContextListener implements JobExecutionContextListener {

	private final Logger log = LoggerFactory.getLogger(JobExecutionMDCContextListener.class);

	public static final String MDC_PARAM_NAME = "mdc-param";

	protected static final ThreadLocal<Map<Long,Map>> ORIGINAL_CONTEXT = new ThreadLocal<>();

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the MDC values");
		Map mdc = MDC.getCopyOfContextMap();
		if (mdc != null) {
			jobParametersBuilder.addParameter(MDC_PARAM_NAME, new SerializableJobParameter(new HashMap(mdc)));
		}

	}




	@Override
	public void restoreContext(JobExecution jobExecution) {
		restoreContext(jobExecution.getJobParameters().getParameters(),true);
	}

	protected void restoreContext(Map<String, JobParameter> parameters, boolean keepCurrentContext){
		if(!parameters.containsKey(MDC_PARAM_NAME)){
			log.error("Could not find parameter {} in order to restore the MDC context", MDC_PARAM_NAME);
			return;
		}
		SerializableJobParameter<HashMap> mdcParam = (SerializableJobParameter<HashMap>) parameters.get(MDC_PARAM_NAME);
		HashMap<String, String> mdc = mdcParam.getValue();
		if(keepCurrentContext) {
			Map originalMdc = MDC.getMDCAdapter().getCopyOfContextMap();
			ORIGINAL_CONTEXT.set(originalMdc);
		}
		MDC.clear();
		for (Map.Entry<String, String> entry : mdc.entrySet()) {
			MDC.put(entry.getKey(), entry.getValue());
		}
	}


	@Override
	public void clearContext(JobExecution jobExecution) {
		log.debug("Clear the MDC context from Job: {}", jobExecution.getJobInstance().getJobName());
		MDC.clear();
		Map originalMdc = ORIGINAL_CONTEXT.get();
		if (originalMdc != null) {
			MDC.setContextMap(originalMdc);
			ORIGINAL_CONTEXT.remove();
		}
	}

}
