package com.backgroundtask;

public interface TaskCompleteListener<T> {

	public void onSuccess(T data);
	public void onError(Throwable error);
}
