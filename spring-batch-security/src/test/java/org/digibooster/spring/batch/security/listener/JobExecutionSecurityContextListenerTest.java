package org.digibooster.spring.batch.security.listener;

import org.digibooster.spring.batch.util.SerializableJobParameter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class JobExecutionSecurityContextListenerTest {

    @Test
    public void testInsertContextInfo(){
        SecurityContextHolder.clearContext();
        JobExecutionSecurityContextListener listener= new JobExecutionSecurityContextListener();
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        Assert.assertNull(parametersBuilder.toJobParameters().getParameters().get(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME));
        Authentication authentication= new UsernamePasswordAuthenticationToken("username", "credential");
        SecurityContext securityContext= SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        parametersBuilder= new JobParametersBuilder();
        listener.insertContextInfo(parametersBuilder);
        SerializableJobParameter<Authentication> serializableJobParameter = (SerializableJobParameter<Authentication>) parametersBuilder.toJobParameters().getParameters().get(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME);
        Assert.assertEquals(authentication,serializableJobParameter.getValue());
        SecurityContextHolder.clearContext();
    }


    @Test
    public void testRestoreAndCleanContextForJob(){
        JobExecutionSecurityContextListener listener= new JobExecutionSecurityContextListener();
        Authentication defaultAuth= new UsernamePasswordAuthenticationToken("username", "credential");
        SecurityContext securityContext= SecurityContextHolder.getContext();
        securityContext.setAuthentication(defaultAuth);

        Authentication authentication= new UsernamePasswordAuthenticationToken("username", "credential");
        JobParametersBuilder parametersBuilder= new JobParametersBuilder();
        parametersBuilder.addParameter(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME, new SerializableJobParameter(authentication));
        JobExecution jobExecution= new JobExecution(new JobInstance(1L,"Job"),parametersBuilder.toJobParameters(),"");
        listener.restoreContext(jobExecution);
        Assert.assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
        listener.clearContext(jobExecution);
        Assert.assertEquals(defaultAuth,SecurityContextHolder.getContext().getAuthentication());
    }

}
