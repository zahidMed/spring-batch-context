package org.digibooster.spring.batch.task;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Runnable proxy that restores the contexts before running the original task.
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public class RunnableTaskProxy implements Runnable{

    protected Runnable task;

    protected Map<String,Object> contexts;

    protected List<TaskExecutorListener> taskExecutorListeners;

    protected boolean hasListeners;

    public RunnableTaskProxy(Runnable task, Map<String,Object> contexts, List<TaskExecutorListener> taskExecutorListeners){
        this.task=task;
        this.contexts = contexts;
        this.taskExecutorListeners = taskExecutorListeners;
        this.hasListeners = !CollectionUtils.isEmpty(this.taskExecutorListeners);
    }

    @Override
    public void run() {
        if(hasListeners) {
            for (TaskExecutorListener listener : this.taskExecutorListeners) {
                listener.restoreContext(this.contexts);
            }
        }
        try {
            task.run();
        }
        finally {
            if(hasListeners) {
                for (TaskExecutorListener listener : this.taskExecutorListeners) {
                    listener.cleanContext();
                }
            }
        }
    }
}
