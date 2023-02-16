package org.digibooster.spring.batch.sleuth.listener;

import org.digibooster.spring.batch.sleuth.SpanInfoHolder;
import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.cloud.sleuth.*;
import org.springframework.cloud.sleuth.log.Slf4jSpanLogger;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.sleuth.trace.DefaultTracer;

import java.util.Random;

public class JobExecutionSleuthContextListenerTest {

    @Test
    public void testInsertContextInfo(){
        Tracer tracer = new DefaultTracer(new AlwaysSampler(), new Random(),
                new DefaultSpanNamer(), new Slf4jSpanLogger(""),new NoOpSpanReporter(),new TraceKeys()) {

        };

        JobExecutionSleuthContextListener listener= new JobExecutionSleuthContextListener(tracer);
        Span span= tracer.createSpan("MyTestSpan");
        SpanInfoHolder infoHolder = createSpanInfoHolder(span);
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        SerializableJobParameter<SpanInfoHolder> serializableJobParameter = (SerializableJobParameter<SpanInfoHolder>) parametersBuilder.toJobParameters().getParameters().get(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME);
        Assert.assertEquals(infoHolder,serializableJobParameter.getValue());

    }


    @Test
    public void testRestoreAndCleanContextForJob(){
        Tracer tracer = new DefaultTracer(new AlwaysSampler(), new Random(),
                new DefaultSpanNamer(), new Slf4jSpanLogger(""),new NoOpSpanReporter(),new TraceKeys()) {

        };
        Span defaultSpan= tracer.createSpan("DefaultSpan");

        JobExecutionSleuthContextListener listener= new JobExecutionSleuthContextListener(tracer);

        Span span= tracer.createSpan("MyTestSpan");
        SpanInfoHolder infoHolder = createSpanInfoHolder(span);


        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        parametersBuilder.addParameter(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME, new SerializableJobParameter(infoHolder));
        JobExecution jobExecution= new JobExecution(new JobInstance(1L,"Job"),parametersBuilder.toJobParameters(),"");
        listener.restoreContext(jobExecution);
        Assert.assertEquals(span, tracer.getCurrentSpan());
        listener.clearContext(jobExecution);
        Assert.assertEquals(defaultSpan.getTraceId(),tracer.getCurrentSpan().getTraceId());
        Assert.assertTrue(tracer.getCurrentSpan().getParents().contains(defaultSpan.getSpanId()));
    }


    SpanInfoHolder createSpanInfoHolder(Span span){
        SpanInfoHolder infoHolder = new SpanInfoHolder();
        infoHolder.setName(span.getName());
        infoHolder.setTraceIdHigh(span.getTraceIdHigh());
        infoHolder.setTraceId(span.getTraceId());
        infoHolder.setParents(span.getParents());
        infoHolder.setSpanId(span.getSpanId());
        infoHolder.setRemote(span.isRemote());
        infoHolder.setExportable(span.isExportable());
        infoHolder.setTags(span.tags());
        infoHolder.setProcessId(span.getProcessId());
        infoHolder.setBaggage(span.getBaggage());
        return infoHolder;
    }
}
