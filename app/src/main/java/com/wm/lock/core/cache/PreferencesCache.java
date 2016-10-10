package com.wm.lock.core.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.math.BigDecimal;

class PreferencesCache extends BaseCache {

	private static final String PREFERENCES_FILE = "config";

    private SharedPreferences mPreferences;
    private Editor mEditor;

	public PreferencesCache(Context ctx) {
		mPreferences = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	@Override
	public void putString(String key, String value) {
		mEditor.putString(key, value).commit();
	}

	@Override
	public void putInt(String key, int value) {
		mEditor.putInt(key, value).commit();
	}

	@Override
	public void putLong(String key, long value) {
		mEditor.putLong(key, value).commit();
	}

	@Override
	public void putFloat(String key, float value) {
		mEditor.putFloat(key, value).commit();
	}

	@Override
	public void putDouble(String key, double value) {
		putFloat(key, (float) value);
	}

	@Override
	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value).commit();
	}

	@Override
	public void delete(String key) {
		if (isExist(key)) {
			mEditor.remove(key).commit();
		}
	}

	@Override
	public void deleteAll() {
		mEditor.clear().commit();
	}

	@Override
	public boolean isExist(String key) {
		return mPreferences.contains(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return mPreferences.getString(key, defaultValue);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return mPreferences.getInt(key, defaultValue);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return mPreferences.getLong(key, defaultValue);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return mPreferences.getFloat(key, defaultValue);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return new BigDecimal(getFloat(key)).doubleValue();
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return mPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * 获取编辑器
	 */
	public Editor getEditor() {
		return mEditor;
	}
	
}
