package org.digibooster.spring.batch.locale.listener;

import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.batch.core.JobInstance;
import java.util.Locale;

public class JobExecutionLocaleContextListenerTest {

    @Test
    public void testInsertContextInfo(){
        JobExecutionLocaleContextListener listener= new JobExecutionLocaleContextListener();
        Locale locale = new Locale("AR","MA");
        LocaleContextHolder.setLocale(locale);
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        SerializableJobParameter<Locale> serializableJobParameter = (SerializableJobParameter<Locale>) parametersBuilder.toJobParameters().getParameters().get(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME);
        Assert.assertEquals(locale,serializableJobParameter.getValue());
    }

    @Test
    public void testRestoreAndCleanContextForJob(){
        JobExecutionLocaleContextListener listener= new JobExecutionLocaleContextListener();
        Locale defaultLocale = new Locale("EN","USA");
        LocaleContextHolder.setLocale(defaultLocale);
        Locale locale = new Locale("AR","MA");
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        parametersBuilder.addParameter(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME, new SerializableJobParameter(locale));
        JobExecution jobExecution= new JobExecution(new JobInstance(1L,"Job"),parametersBuilder.toJobParameters(),"");
        listener.restoreContext(jobExecution);
        Assert.assertEquals(locale,LocaleContextHolder.getLocale());
        listener.clearContext(jobExecution);
        Assert.assertEquals(defaultLocale,LocaleContextHolder.getLocale());
    }
}
