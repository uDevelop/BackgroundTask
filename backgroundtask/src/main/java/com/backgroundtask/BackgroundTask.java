package com.backgroundtask;

import android.annotation.TargetApi;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class BackgroundTask<T> extends AsyncTask<Void, Void, T> {

	private final WeakReference<TaskCompleteListener<T>> mWeakListener;
	private Throwable mThrowable;
	private int mRetryMaxCount = 0;
	private int mDelay = 0;


	private BackgroundTask() {
		throw new AssertionError();
	}

	public BackgroundTask(TaskCompleteListener<T> weakListener) {
		super();
		mWeakListener = new WeakReference<TaskCompleteListener<T>>(weakListener);
	}

	@Override
	protected final T doInBackground(Void[] params) {
		int retryCount = 0;
		for(;;) {
			mThrowable = null;
			try {
				return doInBackground();
			} catch (Throwable tr) {
				mThrowable = tr;
			}
			if (isCancelled() || retryCount++ == mRetryMaxCount) {
				break;
			}
			else if (mDelay > 0) {
				Utils.sleep(mDelay);
			}
		}
		return null;
	}

	public abstract T doInBackground() throws Throwable;

	@Override
	protected final void onPostExecute(T data) {
		super.onPostExecute(data);
		TaskCompleteListener<T> listener = mWeakListener.get();
		if (listener != null && !isCancelled()) {
			if (mThrowable == null) {
				listener.onSuccess(data);
			}
			else {
				listener.onError(mThrowable);
			}
		}
	}

	@TargetApi(11)
	public final void start() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else {
			execute();
		}
	}

	public BackgroundTask<T> retryOnFail(final int retryMaxCount, final int delay) {
		if (retryMaxCount < 0) {
			throw new IllegalArgumentException("retryMaxCount must be positive number");
		}
		if (delay < 0) {
			throw new IllegalArgumentException("delay must be positive number");
		}
		mRetryMaxCount = retryMaxCount;
		mDelay = delay;
		return this;
	}

	public BackgroundTask<T> retryOnFail(final int retryMaxCount) {
		return retryOnFail(retryMaxCount, 0);
	}
}
