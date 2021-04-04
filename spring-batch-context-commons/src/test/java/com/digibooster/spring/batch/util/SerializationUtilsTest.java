package com.digibooster.spring.batch.util;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;


public class SerializationUtilsTest {
	
	
	@Test
	public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException{
		
		TestContextValue original=new TestContextValue("test value", 110);
		
		TestContextValue restored= SerializationUtils.deserialize(SerializationUtils.serialize(original), TestContextValue.class);
		
		Assert.assertEquals(original, restored);
		
	}

	
	
	public static class TestContextValue implements Serializable {

		private static final long serialVersionUID = -837861174494969475L;

		private String stringField;

		private Integer intField;
		
		public TestContextValue(){}
		
		public TestContextValue(String s, Integer i){
			this.stringField=s;
			this.intField=i;
		}

		public String getStringField() {
			return stringField;
		}

		public void setStringField(String stringField) {
			this.stringField = stringField;
		}

		public Integer getIntField() {
			return intField;
		}

		public void setIntField(Integer intField) {
			this.intField = intField;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof TestContextValue))
				return false;
			TestContextValue val = (TestContextValue) obj;
			if (getStringField() == null && val.getStringField() != null)
				return false;
			if (getIntField() == null && val.getIntField() != null)
				return false;
			return getStringField().equals(val.getStringField()) && getIntField().equals(val.getIntField());
		}

	}

}
