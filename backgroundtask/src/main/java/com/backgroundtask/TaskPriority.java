package com.backgroundtask;

import android.os.Process;

public enum TaskPriority {

	UNSPECIFIED(Integer.MAX_VALUE),
	BACKGROUND_LOWEST(Process.THREAD_PRIORITY_LOWEST),
	BACKGROUND_DEFAULT(Process.THREAD_PRIORITY_BACKGROUND),
	BACKGROUND_HIGH(Process.THREAD_PRIORITY_LESS_FAVORABLE),
	BACKGROUND_VERY_HIGH(Process.THREAD_PRIORITY_DEFAULT),
	BACKGROUND_URGENT(Process.THREAD_PRIORITY_MORE_FAVORABLE);

	private final int mPriority;

	private TaskPriority(final int priority) {
		mPriority = priority;
	}

	/*package*/ void apply() {
		if (mPriority == Integer.MAX_VALUE)
			return;
		Process.setThreadPriority(mPriority);
	}
}
