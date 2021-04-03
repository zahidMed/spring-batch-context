package com.digibooster.spring.batch.util;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.util.Base64Utils;

/**
 * Utility class for the serialization and deserialization of objects
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class SerializationUtils {
	
	
	private SerializationUtils(){}

	/**
	 * Serializes the object given as parameter and returns it value in String format 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String serialize(Serializable object) {
		return Base64Utils.encodeToString(org.springframework.util.SerializationUtils.serialize(object));
	}
	
	/**
	 * Deserializes the object value given as parameter to 
	 * @param serializedObject
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T extends Serializable> T deserialize(String serializedObject, Class<T> type) {
		 return (T) org.springframework.util.SerializationUtils.deserialize(Base64Utils.decodeFromString(serializedObject));
	}
}
