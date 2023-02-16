package org.digibooster.spring.batch.sleuth.listener;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Map;

/**
 * Implementation for sleuth context propagation
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class SleuthTaskExecutorListener implements TaskExecutorListener {
    private static final long serialVersionUID = 3332190137176159854L;

    private final Tracer tracer;

    public SleuthTaskExecutorListener(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void copyContext(Map<String, Object> memory) {
        Span currentSpan= tracer.getCurrentSpan();
        if(currentSpan!=null) {
            memory.put(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME, currentSpan);
        }
    }

    @Override
    public void restoreContext(Map<String, Object> memory) {
        if(memory.containsKey(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME)){
            Span span= tracer.createSpan(Thread.currentThread().getName(), (Span) memory.get(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME));
            tracer.continueSpan(span);
        }
    }

    @Override
    public void cleanContext() {
        Span span= tracer.getCurrentSpan();
        if(span!=null){
            tracer.close(span);
        }
    }
}
