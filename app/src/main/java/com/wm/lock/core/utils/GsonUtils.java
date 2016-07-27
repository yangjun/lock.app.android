package com.wm.lock.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonUtils {

	public static String toJson(Object input) throws Exception {
		if (input == null) {
			return null;
		}
		final Gson gson = new Gson();
		return gson.toJson(input);
	}
	
	public static String toJson(Object input, TypeToken<?> typeToken) throws Exception {
		if (input == null) {
			return null;
		}
		final Gson gson = new Gson();
		return gson.toJson(input, typeToken.getType());
	}
	
	public static <T> T fromJson(Object object, Class<T> clazz) throws Exception {
		final Gson gson = new Gson();

		if (object == null) {
			return null;
		}

		T result = null;
		if (object instanceof String) {
			result = gson.fromJson(object.toString(), clazz);
		}
		else if (object instanceof JsonElement) {
			result = gson.fromJson((JsonElement) object, clazz);
		}
		else if (object instanceof Reader) {
			result = gson.fromJson((Reader) object, clazz);
		}
		else {
			final JsonElement element = toJsonElement(object);
			result = gson.fromJson(element, clazz);
		}
		return filter(result);
	}
	
	public static <T> T fromJson(Object object, TypeToken<T> typeToken) throws Exception {
		final Gson gson = new Gson();
		final Type type = typeToken.getType();

		T result = null;
		if (object instanceof String) {
			result = gson.fromJson(object.toString(), type);
		}
		else if (object instanceof JsonElement) {
			result = gson.fromJson((JsonElement) object, type);
		}
		else if (object instanceof Reader) {
			result = gson.fromJson((Reader) object, type);
		}
		else if (object instanceof JsonReader) {
			result = gson.fromJson((JsonReader) object, type);
		}
		else {
			final JsonElement element = toJsonElement(object);
			result = gson.fromJson(element, type);
		}
		return filter(result);
	}

	public static JsonElement toJsonElement(Object obj) throws Exception {
		return new Gson().toJsonTree(obj);
	}

	private static <T> T filter(T input) throws Exception {
		if (input == null) {
			throwIllegal();
		}
		return input;
	}

	private static void throwIllegal() throws Exception {
		throw new Exception("illegal json String for convert");
	}
	
}
