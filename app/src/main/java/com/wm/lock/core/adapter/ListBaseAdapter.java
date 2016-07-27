package com.wm.lock.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class ListBaseAdapter<T, ViewHolder extends ViewHolderBase> extends BaseAdapter {

	protected Context mCtx;
	protected List<T> mList;
	protected final LayoutInflater mInflater;
	
	public ListBaseAdapter(Context ctx, List<T> list) {
		this.mCtx = ctx;
		this.mList = list;
		this.mInflater = LayoutInflater.from(ctx);
	}
	
	@Override
	public int getCount() {
		return null == mList ? 0 : mList.size();
	}

	@Override
	public T getItem(int position) {
		return null == mList ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(getViewLayoutId(position), null);
			holder = toViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		T item = mList.get(position);
		onBindViewHolder(convertView, holder, item, position);
		return convertView;
	}

	public abstract int getViewLayoutId(int position);
	
	public abstract void onBindViewHolder(View convertView, ViewHolder holder, T item, int position);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ViewHolder toViewHolder(View view) {
		try {
			// 代表构造方法的参数类型数组
			Class[] argtype = new Class[] { View.class };
			// 代表构造方法的参数值数组
			Object[] argparam = new Object[] { view };
			Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]; 
			Class classType = Class.forName(clazz.getName());
			// 获得构造方法，argtype是参数类型数组
			Constructor constructor = classType.getDeclaredConstructor(argtype);
			// 访问私有构造函数
			constructor.setAccessible(true);
			Object result = constructor.newInstance(argparam);
			return (ViewHolder) result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
}
