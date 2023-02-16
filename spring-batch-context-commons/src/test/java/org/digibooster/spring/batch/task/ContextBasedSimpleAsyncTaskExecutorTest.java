package org.digibooster.spring.batch.task;

import org.awaitility.Awaitility;
import org.digibooster.spring.batch.listener.TaskExecutorListener;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ContextBasedSimpleAsyncTaskExecutorTest {

    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Test
    public void testDoExecute() throws InterruptedException {
        threadLocal.set("test-value");
        TestRunnable testRunnable= new TestRunnable();
        List<TaskExecutorListener> taskExecutorListeners = new ArrayList<>();
        TestListener testListener= new TestListener();
        taskExecutorListeners.add(testListener);
        ContextBasedSimpleAsyncTaskExecutor contextBasedSimpleAsyncTaskExecutor = new ContextBasedSimpleAsyncTaskExecutor(taskExecutorListeners);
        contextBasedSimpleAsyncTaskExecutor.doExecute(testRunnable);
        Awaitility
                .await()
                .atMost(30, TimeUnit.SECONDS)
                .until(()-> testListener.getValue()!=null);
        Assert.assertEquals("test-value",testListener.getValue());
    }


    public static class TestRunnable implements Runnable{

        @Override
        public void run() {

        }
    }
    public static class TestListener implements TaskExecutorListener {


        private static final long serialVersionUID = 7289943026018451121L;

        static final String PARAM_NAME="test-param";

        private String value=null;

        @Override
        public void copyContext(Map<String, Object> memory) {
            memory.put(PARAM_NAME,threadLocal.get());
        }

        @Override
        public void restoreContext(Map<String, Object> memory) {
            value = (String) memory.get(PARAM_NAME);
        }

        @Override
        public void cleanContext() {

        }

        public String getValue(){
            return value;
        }
    }
}
