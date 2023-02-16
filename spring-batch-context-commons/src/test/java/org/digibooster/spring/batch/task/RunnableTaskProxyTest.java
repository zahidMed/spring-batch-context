package org.digibooster.spring.batch.task;

import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class RunnableTaskProxyTest {

    static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    @Test
    public void testRun(){
        Map<String, Object> memory = new HashMap<>();
        memory.put(TestListener.PARAM_NAME,"test-value");
        TestRunnable testRunnable= new TestRunnable();
        List<TaskExecutorListener> taskExecutorListeners = new ArrayList<>();
        taskExecutorListeners.add(new TestListener());
        RunnableTaskProxy runnableTaskProxy= new RunnableTaskProxy(testRunnable,memory, taskExecutorListeners);
        runnableTaskProxy.run();
        Assert.assertEquals("test-value", threadLocal.get());

    }

    public static class TestRunnable implements Runnable{

        @Override
        public void run() {

        }
    }
    public static class TestListener implements TaskExecutorListener{


        private static final long serialVersionUID = 7289943026018451121L;

        static final String PARAM_NAME="test-param";

        @Override
        public void copyContext(Map<String, Object> memory) {

        }

        @Override
        public void restoreContext(Map<String, Object> memory) {
            threadLocal.set((String) memory.get(PARAM_NAME));
        }

        @Override
        public void cleanContext() {

        }
    }
}
