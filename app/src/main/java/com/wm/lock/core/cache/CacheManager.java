package com.wm.lock.core.cache;

import android.content.Context;

/**
 * 缓存控制器。缓存处理的唯一入口
 */
public final class CacheManager {

	/** 内存渠道 */
	public static final int CHANNEL_RUNTIME = 1;
	/** SharedPreference渠道 */
	public static final int CHANNEL_PREFERENCE = 2;

	private RuntimeCache runtimeInstance;
	private PreferencesCache preferencesInstance;

	private Context mCtx;

	private CacheManager() {

	}

	private static class SingletonInstanceHolder {
		static CacheManager instance = new CacheManager();
	}

	public void init(Context ctx) {
		mCtx = ctx;
	}

	public void destory() {
//		mCtx = null;
		runtimeInstance = null;
		preferencesInstance = null;
	}

	/**
	 * 获取操作对象
	 */
	public static CacheManager getInstance() {
		return SingletonInstanceHolder.instance;
	}

	/**
	 * 缓存String类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putString(String key, String value, int channel) {
		getCacheInstance(channel).putString(key, value);
	}

	/**
	 * 缓存int类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putInt(String key, int value, int channel) {
		getCacheInstance(channel).putInt(key, value);
	}

	/**
	 * 缓存long类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putLong(String key, long value, int channel) {
		getCacheInstance(channel).putLong(key, value);
	}

	/**
	 * 缓存float类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putFloat(String key, float value, int channel) {
		getCacheInstance(channel).putFloat(key, value);
	}

	/**
	 * 缓存double类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putDouble(String key, double value, int channel) {
		getCacheInstance(channel).putDouble(key, value);
	}

	/**
	 * 缓存boolean类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void putBoolean(String key, boolean value, int channel) {
		getCacheInstance(channel).putBoolean(key, value);
	}

	/**
	 * 获取String类型的数据到指定渠道
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public String getString(String key, String defaultValue, int channel) {
		return getCacheInstance(channel).getString(key, defaultValue);
	}

	/**
	 * 从指定渠道获取int类型的数据
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public int getInt(String key, int defaultValue, int channel) {
		return getCacheInstance(channel).getInt(key, defaultValue);
	}

	/**
	 * 从指定渠道获取long类型的数据
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public long getLong(String key, long defaultValue, int channel) {
		return getCacheInstance(channel).getLong(key, defaultValue);
	}

	/**
	 * 从指定渠道获取float类型的数据
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public float getFloat(String key, float defaultValue, int channel) {
		return getCacheInstance(channel).getFloat(key, defaultValue);
	}

	/**
	 * 从指定渠道获取double类型的数据
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public double getDouble(String key, double defaultValue, int channel) {
		return getCacheInstance(channel).getDouble(key, defaultValue);
	}

	/**
	 * 从指定渠道获取boolean类型的数据
	 *
	 * @param key
	 *            键
	 * @param defaultValue
	 *            如果获取不到数据，返回的这个值
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public boolean getBoolean(String key, boolean defaultValue, int channel) {
		return getCacheInstance(channel).getBoolean(key, defaultValue);
	}

	/**
	 * 从指定渠道获取String类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public String getString(String key, int channel) {
		return getCacheInstance(channel).getString(key);
	}

	/**
	 * 从指定渠道获取int类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public int getInt(String key, int channel) {
		return getCacheInstance(channel).getInt(key);
	}

	/**
	 * 从指定渠道获取long类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public long getLong(String key, int channel) {
		return getCacheInstance(channel).getLong(key);
	}

	/**
	 * 从指定渠道获取float类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public float getFloat(String key, int channel) {
		return getCacheInstance(channel).getFloat(key);
	}

	/**
	 * 从指定渠道获取double类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public double getDouble(String key, int channel) {
		return getCacheInstance(channel).getDouble(key);
	}

	/**
	 * 从指定渠道获取booelan类型的数据
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public boolean getBoolean(String key, int channel) {
		return getCacheInstance(channel).getBoolean(key);
	}

	/**
	 * 判断指定渠道对应的缓存是否存在
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public boolean isExist(String key, int channel) {
		return getCacheInstance(channel).isExist(key);
	}

	/**
	 * 删除指定渠道对应的缓存
	 *
	 * @param key
	 *            键
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void delete(String key, int channel) {
		getCacheInstance(channel).delete(key);
	}

	/**
	 * 清空指定渠道的所有缓存
	 *
	 * @param channel
	 *            渠道标识。可选值：{@link #CHANNEL_RUNTIME}，{@link #CHANNEL_PREFERENCE}
	 */
	public void deleteAll(int channel) {
		getCacheInstance(channel).deleteAll();
	}

	/**
	 * 清空所有缓存。包括所有渠道
	 */
	public void deleteAll() {
		runtimeInstance.deleteAll();
		preferencesInstance.deleteAll();
	}

	/**
	 * 存放Object类型的数据到缓存。暂时仅支持缓存到内存
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public void putObject(String key, Object value) {
		getRuntimeInstance().put(key, value);
	}

	/**
	 * 获取Object类型的缓存。暂时仅支持缓存到内存
	 *
	 * @param key
	 *            键
	 */
	public Object getObject(String key) {
		return getRuntimeInstance().get(key);
	}

	public void deleteObject(String key) {
		getRuntimeInstance().delete(key);
	}

	/**
	 * 获取实际的缓存控制类。延迟初始化
	 */
	private BaseCache getCacheInstance(int channel) {
		BaseCache instance = null;
		switch (channel) {
		case CHANNEL_PREFERENCE:
			instance = getPreferenceInstance();
			break;
		default:
			instance = getRuntimeInstance();
			break;
		}
		return instance;
	}

	private PreferencesCache getPreferenceInstance() {
		if (preferencesInstance == null) {
			preferencesInstance = new PreferencesCache(mCtx);
		}
		return preferencesInstance;
	}

	private RuntimeCache getRuntimeInstance() {
		if (runtimeInstance == null) {
			runtimeInstance = new RuntimeCache();
		}
		return runtimeInstance;
	}

}
