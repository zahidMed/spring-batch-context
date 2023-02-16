package org.digibooster.spring.batch.locale.listener;

import java.util.Locale;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class restores the Locale context inside the Spring batch job
 * 
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class JobExecutionLocaleContextListener implements JobExecutionContextListener {

	private final Logger log = LoggerFactory.getLogger(JobExecutionLocaleContextListener.class);

	public static final String LOCALE_PARAM_NAME = "locale-param";

	private static final ThreadLocal<Locale> ORIGINAL_CONTEXT = new ThreadLocal<>();

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the Locale values");
		Locale locale = LocaleContextHolder.getLocale();
		jobParametersBuilder.addParameter(LOCALE_PARAM_NAME, new SerializableJobParameter(locale));

	}


	@Override
	public void restoreContext(JobExecution jobExecution) {
		restoreContext(jobExecution.getJobParameters().getParameters(),true);
	}

	protected void restoreContext(Map<String, JobParameter> parameters, boolean keepCurrentContext) {
		if(!parameters.containsKey(LOCALE_PARAM_NAME)){
			log.error("Could not find key {} in the job execution context", LOCALE_PARAM_NAME);
			return;
		}
		SerializableJobParameter<Locale> localeParam = (SerializableJobParameter<Locale>) parameters.get(LOCALE_PARAM_NAME);
		Locale locale = localeParam.getValue();
		if(keepCurrentContext) {
			Locale originalLocale = LocaleContextHolder.getLocale();
			ORIGINAL_CONTEXT.set(originalLocale);
		}
		LocaleContextHolder.setLocale(locale, true);
	}

	@Override
	public void clearContext(JobExecution jobExecution) {
		log.debug("Clear the locale context from Job: {}", jobExecution.getJobInstance().getJobName());
		Locale originalLocale = ORIGINAL_CONTEXT.get();
		if (originalLocale != null) {
			LocaleContextHolder.setLocale(originalLocale, true);
			ORIGINAL_CONTEXT.remove();
		}
	}

}