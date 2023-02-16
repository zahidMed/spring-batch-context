package org.digibooster.spring.batch.mdc.listener;

import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.HashMap;
import java.util.Map;

public class JobExecutionMDCContextListenerTest {

    @Test
    public void testInsertContextInfo(){
        MDC.clear();
        JobExecutionMDCContextListener listener= new JobExecutionMDCContextListener();
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        Assert.assertNull(parametersBuilder.toJobParameters().getParameters().get(JobExecutionMDCContextListener.MDC_PARAM_NAME));
        Map contextMap= new HashMap();
        contextMap.put("key1","val1");
        contextMap.put("key2","val2");
        MDC.setContextMap(contextMap);
        parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        SerializableJobParameter<HashMap> serializableJobParameter = (SerializableJobParameter<HashMap>) parametersBuilder.toJobParameters().getParameters().get(JobExecutionMDCContextListener.MDC_PARAM_NAME);
        Assert.assertEquals(contextMap,serializableJobParameter.getValue());
        MDC.clear();
    }


    @Test
    public void testRestoreAndCleanContextForJob(){
        JobExecutionMDCContextListener listener= new JobExecutionMDCContextListener();
        HashMap defaultContextMap= new HashMap();
        defaultContextMap.put("key1","val1");
        defaultContextMap.put("key2","val2");
        MDC.setContextMap(defaultContextMap);

        HashMap contextMap= new HashMap();
        contextMap.put("key1","val1");
        contextMap.put("key2","val2");
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        parametersBuilder.addParameter(JobExecutionMDCContextListener.MDC_PARAM_NAME, new SerializableJobParameter(contextMap));
        JobExecution jobExecution= new JobExecution(new JobInstance(1L,"Job"),parametersBuilder.toJobParameters(),"");
        listener.restoreContext(jobExecution);
        Assert.assertEquals(contextMap, MDC.getCopyOfContextMap());
        listener.clearContext(jobExecution);
        Assert.assertEquals(defaultContextMap,MDC.getCopyOfContextMap());
    }

}
