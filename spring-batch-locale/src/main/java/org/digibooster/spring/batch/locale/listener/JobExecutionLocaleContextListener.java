package org.digibooster.spring.batch.locale.listener;

import java.util.Locale;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
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

	private static final String LOCALE_PARAM_NAME = "locale-param";

	private static final ThreadLocal<Locale> ORIGINAL_CONTEXT = new ThreadLocal<>();

	@Override
	public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
		log.debug("Insert the Locale values");
		Locale locale = LocaleContextHolder.getLocale();
		jobParametersBuilder.addParameter(LOCALE_PARAM_NAME, new SerializableJobParameter<Locale>(locale));

	}

	@Override
	public void fillJobExecutionContext(JobExecution jobExecution) {
		log.debug("Restore the locale context");
		SerializableJobParameter<Locale> locale = (SerializableJobParameter<Locale>) jobExecution.getJobParameters()
				.getParameters().get(LOCALE_PARAM_NAME);
		if (locale != null) {
			jobExecution.getExecutionContext().put(LOCALE_PARAM_NAME, (Locale) locale.getValue());
		} else {
			log.error("Could not find parameter {} in order to restore the locale context", LOCALE_PARAM_NAME);
		}

	}

	@Override
	public void removeFromJobExecutionContext(JobExecution jobExecution) {
		jobExecution.getExecutionContext().remove(LOCALE_PARAM_NAME);

	}

	@Override
	public void restoreContext(StepExecution stepExecution) {
		if (stepExecution.getJobExecution().getExecutionContext().containsKey(LOCALE_PARAM_NAME)) {
			log.debug("Restore the locale context");
			Locale locale = (Locale) stepExecution.getJobExecution().getExecutionContext().get(LOCALE_PARAM_NAME);
			Locale originalLocale = LocaleContextHolder.getLocale();
			ORIGINAL_CONTEXT.set(originalLocale);
			LocaleContextHolder.setLocale(locale, true);

		} else {
			log.error("Could not find key {} in the job execution context", LOCALE_PARAM_NAME);
		}
	}

	@Override
	public void clearContext(StepExecution stepExecution) {
		log.debug("Clear the locale context");
		Locale originalLocale = ORIGINAL_CONTEXT.get();
		if (originalLocale != null) {
			LocaleContextHolder.setLocale(originalLocale, true);
			ORIGINAL_CONTEXT.remove();
		}

	}

}