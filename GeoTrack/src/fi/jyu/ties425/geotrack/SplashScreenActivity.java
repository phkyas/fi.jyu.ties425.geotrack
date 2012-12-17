package fi.jyu.ties425.geotrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * class to show a splashscreen every time the app is started
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class SplashScreenActivity extends Activity {
	/*
	 * define the necessary constants and variables
	 */
	private static int SLEEP_TIME = 1200;
	private Thread splashThread;

	/*
	 * show the splashscreen within a new thread so that it is possible, that
	 * the user can cancel respectively remove the splashscreen
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);

		splashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (splashThread) {
						wait(SLEEP_TIME);
					}

				} catch (InterruptedException e) {
				}
				finish();
				Intent intent = new Intent(SplashScreenActivity.this,
						MainActivity.class);
				SplashScreenActivity.this.startActivity(intent);
			}
		};
		splashThread.start();
	}

	/*
	 * terminate the splashscreen activity at any ACTION_DOWN MotionEvent
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (splashThread) {
				splashThread.notifyAll();
			}
		}
		return true;
	}
}