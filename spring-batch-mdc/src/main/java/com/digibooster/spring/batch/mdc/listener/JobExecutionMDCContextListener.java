package com.digibooster.spring.batch.mdc.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;

import com.digibooster.spring.batch.listener.JobExecutionContextListener;
import com.digibooster.spring.batch.util.SerializationUtils;

/**
 * This class restores MDC stored information context inside the Spring batch job
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class JobExecutionMDCContextListener implements JobExecutionContextListener {

	private final Logger log = LoggerFactory.getLogger(JobExecutionMDCContextListener.class);

	private static final String MDC_PARAM_NAME = "mdc-param";

	private static final ThreadLocal<Map> ORIGINAL_CONTEXT = new ThreadLocal<>();

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the MDC values");
		Map mdc = MDC.getCopyOfContextMap();
		if (mdc != null) {
			jobParametersBuilder.addString(MDC_PARAM_NAME, SerializationUtils.serialize(new HashMap(mdc)));
		}

	}

	@Override
	public void fillJobExecutionContext(JobExecution jobExecution) {
		log.debug("Restore the MC context");
		String mdcValue = jobExecution.getJobParameters().getString(MDC_PARAM_NAME);
		if (mdcValue != null) {
			HashMap mdc = SerializationUtils.deserialize(mdcValue, HashMap.class);
			jobExecution.getExecutionContext().put(MDC_PARAM_NAME, mdc);
		} else {
			log.error("Could not find parameter {} in order to restore the MDC context", MDC_PARAM_NAME);
		}

	}

	@Override
	public void removeFromJobExecutionContext(JobExecution jobExecution) {
		jobExecution.getExecutionContext().remove(MDC_PARAM_NAME);

	}

	@Override
	public void restoreContext(StepExecution stepExecution) {
		if (stepExecution.getJobExecution().getExecutionContext().containsKey(MDC_PARAM_NAME)) {
			log.debug("Restore the MDC context");
			HashMap<String,String> mdc = (HashMap) stepExecution.getJobExecution().getExecutionContext().get(MDC_PARAM_NAME);
			Map originalMdc = MDC.getMDCAdapter().getCopyOfContextMap();
			ORIGINAL_CONTEXT.set(originalMdc);
			MDC.clear();
			for(Map.Entry<String,String> entry:mdc.entrySet()) {
				MDC.put(entry.getKey(), entry.getValue());
			}
			
		} else {
			log.error("Could not find key {} in the job execution context", MDC_PARAM_NAME);
		}
	}

	@Override
	public void clearContext(StepExecution stepExecution) {
		log.debug("Clear the MDC context");
		MDC.clear();
		Map originalMdc = ORIGINAL_CONTEXT.get();
		if (originalMdc != null) {
			MDC.setContextMap(originalMdc);
			ORIGINAL_CONTEXT.remove();
		}

	}

}
