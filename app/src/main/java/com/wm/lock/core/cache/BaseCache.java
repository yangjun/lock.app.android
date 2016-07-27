package com.wm.lock.core.cache;

abstract class BaseCache {

	public abstract void putString(String key, String value);
	
	public abstract void putInt(String key, int value);
	
	public abstract void putLong(String key, long value);
	
	public abstract void putFloat(String key, float value);
	
	public abstract void putDouble(String key, double value);
	
	public abstract void putBoolean(String key, boolean value);
	
	public abstract String getString(String key, String defaultValue);
	
	public abstract int getInt(String key, int defaultValue);
	
	public abstract long getLong(String key, long defaultValue);
	
	public abstract float getFloat(String key, float defaultValue);
	
	public abstract double getDouble(String key, double defaultValue);
	
	public abstract boolean getBoolean(String key, boolean defaultValue);

	public abstract void delete(String key);
	
	public abstract void deleteAll();
	
	public abstract boolean isExist(String key);

	public String getString(String key) {
		return getString(key, "");
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public long getLong(String key) {
		return getLong(key, 0);
	}
	
	public float getFloat(String key) {
		return getFloat(key, 0);
	}
	
	public double getDouble(String key) {
		return getDouble(key, 0);
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

}
