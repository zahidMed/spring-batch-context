package com.digibooster.spring.batch.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.digibooster.spring.batch.config.TestContextValue;

public class SerializationUtilsTest {
	
	
	@Test
	public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException{
		
		TestContextValue original=new TestContextValue("test value", 110);
		
		TestContextValue restored= SerializationUtils.deserialize(SerializationUtils.serialize(original), TestContextValue.class);
		
		Assert.assertEquals(original, restored);
		
	}

}
