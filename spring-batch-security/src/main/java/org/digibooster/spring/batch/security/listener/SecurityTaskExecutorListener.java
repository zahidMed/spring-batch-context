package org.digibooster.spring.batch.security.listener;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * Implementation for Security context propagation
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class SecurityTaskExecutorListener implements TaskExecutorListener {

    private static final long serialVersionUID = 8872556462437512642L;

    @Override
    public void copyContext(Map<String, Object> memory) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
            memory.put(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME, authentication);
        }
    }

    @Override
    public void restoreContext(Map<String, Object> memory) {
        if(memory.containsKey(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME)){
            SecurityContextHolder.getContext().setAuthentication((Authentication)memory.get(JobExecutionSecurityContextListener.SECURITY_PARAM_NAME));
        }
    }

    @Override
    public void cleanContext() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
