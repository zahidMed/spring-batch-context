package org.digibooster.spring.batch.task;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inherits from ThreadPoolTaskExecutor that copy contexts inside a Map and encapsulate the created
 * tasks in {@link RunnableTaskProxy} to allow their restoration and cleaning during the task execution.
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class ContextBasedThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final long serialVersionUID = 695828030196225192L;

    protected List<TaskExecutorListener> taskExecutorListeners;

    public ContextBasedThreadPoolTaskExecutor(List<TaskExecutorListener> taskExecutorListeners) {
        this.taskExecutorListeners=taskExecutorListeners;
    }

    @Override
    public void execute(Runnable task) {
        Map<String,Object> contexts= new HashMap<>();
        if (!CollectionUtils.isEmpty(taskExecutorListeners)) {
            for(TaskExecutorListener listener: taskExecutorListeners){
                listener.copyContext(contexts);
            }
        }
        RunnableTaskProxy proxy= new RunnableTaskProxy(task,contexts, taskExecutorListeners);
        super.execute(proxy);
    }
}
