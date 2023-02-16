package org.digibooster.spring.batch.sleuth.listener;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.sleuth.*;
import org.springframework.cloud.sleuth.log.Slf4jSpanLogger;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.sleuth.trace.DefaultTracer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SleuthTaskExecutorListenerTest {

    @Test
    public void testSleuthTaskExecutorListener(){
        Tracer tracer = new DefaultTracer(new AlwaysSampler(), new Random(),
                new DefaultSpanNamer(), new Slf4jSpanLogger(""),new NoOpSpanReporter(),new TraceKeys()) {

        };
        Span span= tracer.createSpan("MyTestSpan");
        SleuthTaskExecutorListener listener= new SleuthTaskExecutorListener(tracer);
        Map<String,Object> memory= new HashMap();
        listener.copyContext(memory);
        Assert.assertEquals(span,memory.get(JobExecutionSleuthContextListener.SLEUTH_PARAM_NAME));
        listener.cleanContext();
        Assert.assertNotEquals(tracer.getCurrentSpan().getSpanId(),span.getSpanId());
        listener.restoreContext(memory);
        Assert.assertEquals(span.getTraceId(),tracer.getCurrentSpan().getTraceId());
        Assert.assertTrue(tracer.getCurrentSpan().getParents().contains(span.getSpanId()));
    }
}
