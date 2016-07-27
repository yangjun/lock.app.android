package com.wm.lock.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RuntimeCache extends BaseCache {

	private static Map<String, Object> map;

	public RuntimeCache() {
		map = new ConcurrentHashMap<String, Object>();
	}

	@Override
	public void putString(String key, String value) {
		map.put(key, value);
	}

	@Override
	public void putInt(String key, int value) {
		map.put(key, value);
	}

	@Override
	public void putLong(String key, long value) {
		map.put(key, value);
	}

	@Override
	public void putFloat(String key, float value) {
		map.put(key, value);
	}

	@Override
	public void putDouble(String key, double value) {
		map.put(key, value);
	}

	@Override
	public void putBoolean(String key, boolean value) {
		map.put(key, value);
	}

	@Override
	public void delete(String key) {
		if (isExist(key)) {
			map.remove(key);
		}
	}

	@Override
	public void deleteAll() {
		map.clear();
	}

	@Override
	public boolean isExist(String key) {
		return map.containsKey(key);
	}

    @Override
	public String getString(String key, String defaultValue) {
		return isExist(key) ? map.get(key).toString() : defaultValue;
	}

	@Override
	public int getInt(String key, int defaultValue) {
		if (isExist(key)) {
			try {
				return Integer.valueOf(map.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	@Override
	public long getLong(String key, long defaultValue) {
		if (isExist(key)) {
			try {
				return Long.valueOf(map.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		if (isExist(key)) {
			try {
				return Float.valueOf(map.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		if (isExist(key)) {
			try {
				return Double.valueOf(map.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		if (isExist(key)) {
			try {
				return Boolean.valueOf(map.get(key).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	public Object get(String key) {
		return isExist(key) ? map.get(key) : null;
	}
	
}
