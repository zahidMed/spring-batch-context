package com.digibooster.spring.batch.util;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.batch.core.JobParameter;

/**
 * Customized generic class that inherits {@link JobParameter}in order to bypass the restriction made
 * by spring batch for parameter types (String, Date, Double,Long).
 * This class was copied from StackOverflow
 * 
 * <a href="https://stackoverflow.com/questions/33761730/how-to-send-a-custom-object-as-job-parameter-in-spring-batch">Question link</a>
 *
 */
public class CustomJobParameter<T extends Serializable> extends JobParameter {

	private static final long serialVersionUID = -5137416276827316303L;
	
	private T value;
	
    public CustomJobParameter(T value){
        super(UUID.randomUUID().toString());
        this.value = value;
    }
    
    @Override
    public T getValue(){
        return value;
    }
}