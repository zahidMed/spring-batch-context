package org.digibooster.spring.batch.mdc.listener;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for MDC context
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class MDCTaskExecutorListener implements TaskExecutorListener {

    private static final long serialVersionUID = 5974049945578697998L;

    @Override
    public void copyContext(Map<String, Object> memory) {
        memory.put(JobExecutionMDCContextListener.MDC_PARAM_NAME, MDC.getMDCAdapter().getCopyOfContextMap());
    }

    @Override
    public void restoreContext(Map<String, Object> memory) {

        if(memory.containsKey(JobExecutionMDCContextListener.MDC_PARAM_NAME)) {
            for (Map.Entry<String, String> entry : ((HashMap<String, String>) memory.get(JobExecutionMDCContextListener.MDC_PARAM_NAME)).entrySet()) {
                MDC.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void cleanContext() {
        MDC.clear();
    }
}
