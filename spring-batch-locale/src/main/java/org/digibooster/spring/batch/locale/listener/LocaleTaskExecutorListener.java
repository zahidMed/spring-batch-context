package org.digibooster.spring.batch.locale.listener;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Map;

/**
 * Implementation for Locale context
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class LocaleTaskExecutorListener implements TaskExecutorListener {
    private static final long serialVersionUID = 3175946582156201599L;

    @Override
    public void copyContext(Map<String, Object> memory) {
        memory.put(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME, LocaleContextHolder.getLocale());
    }

    @Override
    public void restoreContext(Map<String, Object> memory) {
        if(memory.containsKey(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME)){
            LocaleContextHolder.setLocale((Locale) memory.get(JobExecutionLocaleContextListener.LOCALE_PARAM_NAME));
        }
    }

    @Override
    public void cleanContext() {
        LocaleContextHolder.resetLocaleContext();
    }
}
