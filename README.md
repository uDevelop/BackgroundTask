# BackgroundTask [![Build Status](https://travis-ci.org/uDevelop/BackgroundTask.svg?branch=master)](https://travis-ci.org/uDevelop/BackgroundTask)

BackgroundTask is an easiest way to execute code in the background thread without memory leaks on Android.

###Usage###
BackgroundTask must be subclassed to be used. The subclass will override constructor and doInBackground() method.
```java
private static class DownloadTask extends BackgroundTask<String> {

		private final String mUrl;

		public DownloadTask(TaskCompleteListener<String> strongListener, String url) {
			super(strongListener);
			mUrl = url;
		}

		@Override
		public String doInBackground() throws Throwable {
			return FakeDownloader.download(mUrl);
		}
	}
```
Results handling:
```java
private TaskCompleteListener<String> mListener = new TaskCompleteListener<String>() { //strong reference!
		@Override
		public void onSuccess(String data) {
		  //do something 
		}

		@Override
		public void onError(Throwable error) {
		  //oops! You must handle this error.
		}
	};
```
Task execution:
```java
new DownloadTask(mListener, URL).start();
```



