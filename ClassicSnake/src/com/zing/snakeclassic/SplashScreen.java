package com.zing.snakeclassic;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAgent;

public class SplashScreen extends Activity implements OnClickListener{
	private int start_app = 1;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == start_app)
				startApplication();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		
		sendMessageToHandler();

	}

	private void sendMessageToHandler() {
		handler.sendEmptyMessageDelayed(start_app,
				ClassicSnakeConstants.DEFAULT_SPLASH_SCREEN_TIME);
	}

	private void startApplication() {
		/*
		 * If the user taps the splash screen, then we are starting activity.
		 * the below statement will prevent app from opening activity twice.
		 */
		handler.removeMessages(start_app);
		Intent intent = new Intent(this, MenuScreen.class);
		startActivity(intent);
		this.finish();
	}


	@Override
	public void onClick(View v) {
		startApplication();
	}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, ClassicSnakeConstants.flurryKey);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}