package com.backgroundtask.backgroundtaskdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.backgroundtask.BackgroundTask;
import com.backgroundtask.TaskCompleteListener;

import java.util.Random;
import java.util.concurrent.TimeoutException;


public class MainActivity extends ActionBarActivity {

	private final static String URL = "https://www.google.com";

	private TaskCompleteListener<String> mListener = new TaskCompleteListener<String>() {
		@Override
		public void onSuccess(String data) {
			Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(Throwable error) {
			Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new DownloadTask(mListener, URL).retryOnFail(2, 2000).start();
	}

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

	private static class FakeDownloader {

		static String download(String url) throws InterruptedException, TimeoutException {
			Thread.sleep(2000);
			if (new Random().nextBoolean()) {
				return "Response";
			}
			else {
				throw new TimeoutException();
			}
		}
	}
}
