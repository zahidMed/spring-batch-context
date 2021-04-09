package com.digibooster.spring.batch.util;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.batch.core.JobParameter;

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