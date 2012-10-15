package fi.jyu.ties425.geotrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {

	private static int SLEEP_TIME = 1200;
	private Thread splashThread;

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