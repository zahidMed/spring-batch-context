package org.digibooster.spring.batch.security.listener;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class SecurityTaskExecutorListenerTest {

    @Test
    public void testSecurityTaskExecutorListener(){
        Authentication authentication= new UsernamePasswordAuthenticationToken("username", "credential");
        SecurityContext securityContext= SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        SecurityTaskExecutorListener listener= new SecurityTaskExecutorListener();
        Map<String,Object> memory= new HashMap();
        listener.copyContext(memory);
        Assert.assertEquals(authentication,memory.get(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME));
        listener.cleanContext();
        Assert.assertNull(securityContext.getAuthentication());
        listener.restoreContext(memory);
        Assert.assertEquals(authentication,securityContext.getAuthentication());

    }
}
