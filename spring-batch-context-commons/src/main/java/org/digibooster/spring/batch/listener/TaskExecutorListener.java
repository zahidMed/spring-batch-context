package org.digibooster.spring.batch.listener;

import java.io.Serializable;
import java.util.Map;

/**
 * Task listener called by Spring batch task executor in order to copy and restore the context information
 * inside the threads that run the job steps.
 *
 * @author Mohammed ZAHID {@literal <}zahid.med@gmail.com{@literal >}
 *
 */
public interface TaskExecutorListener extends Serializable {

    /**
     * Copies context information from the current thread into the Map given as parameter
     * @param memory
     */
    void copyContext(Map<String,Object> memory);

    /**
     * Retrieves the context information from the Map given as parameter and restores it in the current Thread
     * @param memory
     */
    void restoreContext(Map<String,Object> memory);

    /**
     * Clear the current Thread context
     */
    void cleanContext();

}
