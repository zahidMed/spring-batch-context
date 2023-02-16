package org.digibooster.spring.batch.sleuth.listener;

import org.digibooster.spring.batch.listener.JobExecutionContextListener;
import org.digibooster.spring.batch.sleuth.SpanInfoHolder;
import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Map;

/**
 * This class restores sleuth context inside the Spring batch job
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 */
public class JobExecutionSleuthContextListener implements JobExecutionContextListener {

    private final Logger log = LoggerFactory.getLogger(JobExecutionSleuthContextListener.class);
    public static final String SLEUTH_PARAM_NAME = "sleuth-param";

    private final Tracer tracer;

    protected static final ThreadLocal<Span> ORIGINAL_CONTEXT = new ThreadLocal<>();

    public JobExecutionSleuthContextListener(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void insertContextInfo(JobParametersBuilder jobParametersBuilder) {
        log.debug("Save the sleuth context");
        Span currentSpan = tracer.getCurrentSpan();
        if(currentSpan==null)
            return;
        SpanInfoHolder infoHolder = new SpanInfoHolder();
        infoHolder.setName(currentSpan.getName());
        infoHolder.setTraceIdHigh(currentSpan.getTraceIdHigh());
        infoHolder.setTraceId(currentSpan.getTraceId());
        infoHolder.setParents(currentSpan.getParents());
        infoHolder.setSpanId(currentSpan.getSpanId());
        infoHolder.setRemote(currentSpan.isRemote());
        infoHolder.setExportable(currentSpan.isExportable());
        infoHolder.setTags(currentSpan.tags());
        infoHolder.setProcessId(currentSpan.getProcessId());
        infoHolder.setBaggage(currentSpan.getBaggage());
        jobParametersBuilder.addParameter(SLEUTH_PARAM_NAME, new SerializableJobParameter<SpanInfoHolder>(infoHolder));
    }

    @Override
    public void restoreContext(JobExecution jobExecution) {
        restoreContext(jobExecution.getJobParameters().getParameters(), true);
    }

    protected void restoreContext(Map<String, JobParameter> parameters, boolean keepCurrentContext) {
        if (!parameters.containsKey(SLEUTH_PARAM_NAME)) {
            log.error("Could not find the key {} in job parameters", SLEUTH_PARAM_NAME);
            return;
        }

        SerializableJobParameter<SpanInfoHolder> spanParameter = (SerializableJobParameter<SpanInfoHolder>) parameters.get(SLEUTH_PARAM_NAME);
        SpanInfoHolder infoHolder = spanParameter.getValue();
        Span newSpan = Span //
                .builder() //
                .name(infoHolder.getName()) //
                .traceIdHigh(infoHolder.getTraceIdHigh()) //
                .traceId(infoHolder.getTraceId()) //
                .parents(infoHolder.getParents()) //
                .spanId(infoHolder.getSpanId()) //
                .remote(infoHolder.isRemote()) //
                .exportable(infoHolder.isExportable()) //
                .tags(infoHolder.getTags()) //
                .processId(infoHolder.getProcessId()) //
                .baggage(infoHolder.getBaggage()) //
                .build(); //
        if (keepCurrentContext) {
            Span originalSpan = tracer.getCurrentSpan();
            ORIGINAL_CONTEXT.set(originalSpan);
        }
        tracer.continueSpan(newSpan);
    }


    @Override
    public void clearContext(JobExecution jobExecution) {
        Span originalSpan = ORIGINAL_CONTEXT.get();
        if (originalSpan != null) {
            tracer.continueSpan(originalSpan);
            ORIGINAL_CONTEXT.remove();
        }
    }

}
