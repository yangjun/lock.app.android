package com.wm.lock.core.async;

public interface AsyncWork<T> {

	public void onPreExecute();
	
	public void onSuccess(T result);
	
	public void onFail(Exception e);
	
	public T onExecute() throws Exception;

}
