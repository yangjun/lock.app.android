package com.wm.lock.core.async;

import android.os.AsyncTask;

import com.wm.lock.exception.BizException;

public final class AsyncExecutor {

	private volatile boolean isCancel = false;
	private AsyncTask mAsyncTask;

	@SuppressWarnings("rawtypes")
	AsyncWork mListener;

	public <T> void execute(AsyncWork<T> listener) {
		if (mListener != null) {
			throw new BizException("a async task is already running, please make a new instance to execute this one");
		}
		mListener = listener;
		isCancel = false;
		perform();
	}

	public void cancel() {
		isCancel = true;
		if (mAsyncTask != null) {
			mAsyncTask.cancel(true);
		}
	}

	public boolean isExecuting() {
		return mListener != null;
	}

	private void stop() {
		mListener = null;
	}

	private <T> void perform() {
		mAsyncTask = new AsyncPerformTask<T>();
		mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private class AsyncPerformTask<T> extends AsyncTask<Object, Void, T> {

		private Exception mEx;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (isCancel) {
				stop();
				return;
			}

			try {
				mListener.onPreExecute();
			} catch (Exception e) {
				mEx = e;
			}
		}

		@Override
		protected T doInBackground(Object... params) {
			if (isCancel || mEx != null) {
				return null;
			}

			try {
				return (T) mListener.onExecute();
			}
			catch (Exception e) {
				mEx = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(T t) {
			super.onPostExecute(t);

			if (isCancel) {
				stop();
				return;
			}

			try {
				if (mEx == null) {
					mListener.onSuccess(t);
				}
				else {
					mListener.onFail(mEx);
				}
			}
			catch (Exception e) {
				try {
					mListener.onFail(e);
				} catch (Exception e1) {
					throw e1;
				}
			}
			finally {
				stop();
			}
		}
	}

}
