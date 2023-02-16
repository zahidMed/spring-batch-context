package org.digibooster.spring.batch.mdc.listener;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MDCTaskExecutorListenerTest {

    @Test
    public void testMDCTaskExecutorListener(){
        Map contextMap= new HashMap();
        contextMap.put("key1","val1");
        contextMap.put("key2","val2");
        MDC.setContextMap(contextMap);
        MDCTaskExecutorListener listener= new MDCTaskExecutorListener();
        Map<String,Object> memory= new HashMap();
        listener.copyContext(memory);
        Assert.assertEquals(contextMap,memory.get(JobExecutionMDCContextListener.MDC_PARAM_NAME));
        listener.cleanContext();
        Assert.assertTrue(CollectionUtils.isEmpty(MDC.getCopyOfContextMap()));
        listener.restoreContext(memory);
        Assert.assertEquals(contextMap,MDC.getCopyOfContextMap());

    }
}
