package com.backgroundtask;

/*package*/ final class Utils {

	private Utils() {
		throw new AssertionError();
	}

	/*package*/ static void sleep(final long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			//do nothing
		}
	}


}
