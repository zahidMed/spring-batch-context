package org.digibooster.spring.batch.locale.listener;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleTaskExecutorListenerTest {

    @Test
    public void testLocaleTaskExecutorListener(){
        Locale locale = new Locale("AR","MA");
        LocaleContextHolder.setLocale(locale);
        LocaleTaskExecutorListener listener= new LocaleTaskExecutorListener();
        Map<String,Object> memory= new HashMap();
        listener.copyContext(memory);
        Assert.assertEquals(locale,memory.get(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME));
        listener.cleanContext();
        Assert.assertEquals(Locale.getDefault(), LocaleContextHolder.getLocale());
        listener.restoreContext(memory);
        Assert.assertEquals(locale,LocaleContextHolder.getLocale());
    }
}
