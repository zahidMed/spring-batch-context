package org.digibooster.spring.batch.task;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

/**
 * Inherits from SimpleAsyncTaskExecutor that copy contexts inside a Map and encapsulate the created
 * tasks in {@link RunnableTaskProxy} to allow their restoration inside the new created Threads.
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class ContextBasedSimpleAsyncTaskExecutor extends SimpleAsyncTaskExecutor {

    private static final long serialVersionUID = 8591546255064750901L;
    protected List<TaskExecutorListener> taskExecutorListeners;

    public ContextBasedSimpleAsyncTaskExecutor(List<TaskExecutorListener> taskExecutorListeners) {
        this.taskExecutorListeners=taskExecutorListeners;
    }

    public ContextBasedSimpleAsyncTaskExecutor(String threadNamePrefix, List<TaskExecutorListener> taskExecutorListeners) {
        super(threadNamePrefix);
        this.taskExecutorListeners=taskExecutorListeners;
    }

    public ContextBasedSimpleAsyncTaskExecutor(ThreadFactory threadFactory, List<TaskExecutorListener> taskExecutorListeners) {
        super(threadFactory);
        this.taskExecutorListeners=taskExecutorListeners;
    }

    @Override
    protected void doExecute(Runnable task) {
        Map<String,Object> contexts= new HashMap();
        if (!CollectionUtils.isEmpty(taskExecutorListeners)) {
            for(TaskExecutorListener listener: taskExecutorListeners){
                listener.copyContext(contexts);
            }
        }
        RunnableTaskProxy proxy= new RunnableTaskProxy(task,contexts, taskExecutorListeners);
        super.doExecute(proxy);
    }
}
