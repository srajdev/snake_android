package com.zing.snakeclassic;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAgent;
import com.zing.snakeclassic.SnakeView.NewTimerTask;

/**
 * Snake: a simple game that everyone can enjoy.
 * 
 * This is an implementation of the classic Game "Snake", in which you control a
 * serpent roaming around the garden looking for apples. Be careful, though,
 * because when you catch one, not only will you become longer, but you'll move
 * faster. Running into yourself or the walls will end the game.
 * 
 */
public class GameScreen extends Activity implements OnClickListener,
		OnTouchListener {

	public SnakeView mSnakeView;
	private SharedPreferences prefs;
	ImageView imgBtnPause;

	private static String ICICLE_KEY = "snake-view";
	Intent intent;

	RelativeLayout bgrd_theme_control;

	TextView txttest;
	public static PqrHandler pqrHandler;
	int counter2 = 45;
	ImageView imgBtnUp, imgBtnDown, imgBtnLeft, imgBtnRight;
	
	public static GreenAppleHandler greenAppleHandler ;

	/*
	 * public ExtremeHandler mExtremeHandler = new ExtremeHandler();
	 * 
	 * class ExtremeHandler extends Handler {
	 * 
	 * 
	 * @Override public void handleMessage(Message msg) { // TODO Auto-generated
	 * method stub super.handleMessage(msg); }
	 * 
	 * private void sleep(long delay) { // TODO Auto-generated method stub
	 * this.removeMessages(1);
	 * 
	 * 
	 * } }
	 */

	/**
	 * Called when Activity is first created. Turns off the title bar, sets up
	 * the content views, and fires up the SnakeView.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game);

		// intent = getIntent();

		mSnakeView = (SnakeView) findViewById(R.id.snake);

		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		ClassicSnakeConstants.setRGBValue(prefs.getString("snake_color",
				"GREEN"));

		bgrd_theme_control = (RelativeLayout) findViewById(R.id.bgrd_theme_control);
		setTheme();

		// mSnakeView.LEVEL = intent.getIntExtra("LEVEL", 1);
		mSnakeView.setTextView((TextView) findViewById(R.id.txtScore));

		//mSnakeView.setExtremeView((TextView) findViewById(R.id.txtExtreme));
		// mExtremeView = (TextView) findViewById(R.id.txtExtreme);

		if (savedInstanceState == null) {
			// We were just launched -- set up a new game
			mSnakeView.setMode(SnakeView.READY);
		} else {
			// We are being restored
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (map != null) {
				mSnakeView.restoreState(map);
			} else {
				mSnakeView.setMode(SnakeView.PAUSE);
			}
		}

		imgBtnPause = (ImageView) findViewById(R.id.imgBtnPause);
		imgBtnPause.setOnClickListener(this);
		txttest = (TextView) findViewById(R.id.txtExtreme);
		pqrHandler = new PqrHandler();
		if (ClassicSnakeConstants.GAME_PLAY_MODE == 2) {
			txttest.setText(String.valueOf(counter2));
			pqrHandler.sleep(1000);
		}
		
		
		greenAppleHandler = new GreenAppleHandler();
		greenAppleHandler.sleep(14000);

		imgBtnDown = (ImageView) findViewById(R.id.imgBtnDown);
		imgBtnDown.setOnTouchListener(this);
		imgBtnLeft = (ImageView) findViewById(R.id.imgBtnLeft);
		imgBtnLeft.setOnTouchListener(this);
		imgBtnRight = (ImageView) findViewById(R.id.imgBtnRight);
		imgBtnRight.setOnTouchListener(this);
		imgBtnUp = (ImageView) findViewById(R.id.imgBtnUp);
		imgBtnUp.setOnTouchListener(this);
		
		counter2 = 45;

	}

	class PqrHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			/*
			 * super.handleMessage(msg); counter2++;
			 * txttest.setText(String.valueOf(counter2));
			 * pqrHandler.sleep(1000);
			 */

			super.handleMessage(msg);
			if (ClassicSnakeConstants.GAME_PLAY_MODE == 2) {
				// We are in extreme mode
				if (counter2 <= 0 && ClassicSnakeConstants.GAME_LEVEL == 4) {
					mSnakeView.setMode(SnakeView.LOSE);
					this.removeMessages(3);
					counter2 = 0;
					return;
				} else if (counter2 <= 0) {
                    ClassicSnakeConstants.GAME_LEVEL++;
                    mSnakeView.initialposition();
 
                    counter2 = 45;
                    
                    mSnakeView.setMode(SnakeView.PAUSE);
                    // mRedrawHandler.removeMessages(0);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 100ms
                            --counter2;
                            mSnakeView.setMode(SnakeView.RUNNING);
 
                            txttest.setText(String.valueOf(counter2));
 
                            if (!hasMessages(3))
                                sleep(1000);
                            
                        }
                    }, 1000);   
                    
                }else{
                    mSnakeView.setMode(SnakeView.RUNNING);
 
                    --counter2;
 
                    txttest.setText(String.valueOf(counter2));
 
                    if (!this.hasMessages(3))
                        this.sleep(1000);
                    
                }
                
          }
		}
		
		public void sleep(long delayMillis) {
			this.removeMessages(3);
			sendMessageDelayed(obtainMessage(3), delayMillis);
		}
	}
	
	class GreenAppleHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//Display apple
			mSnakeView.mShowGreenFlag = true;
			mSnakeView.addRandomApple("GREEN");
			NewTimerTask newTask = mSnakeView.new NewTimerTask();
			new Timer().schedule(newTask, 5000);	
			sleep(12000);
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(4);
			sendMessageDelayed(obtainMessage(4), delayMillis);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// mSnakeView.mSoftboardSize= bgrd_theme_control.getHeight();
	}

	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_classic_control));
		} else if (theme.equals("GARDEN")) {
			bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.theme_garden_control));

		} else if (theme.equals("NIGHT")) {
			bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.theme_night_control));

		} else if (theme.equals("BEACH")) {
			bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.theme_beach_control));

		}

	}

	@Override
	protected void onPause() {
		imgBtnPause.setImageResource(R.drawable.pause_selected);
		super.onPause();
		//mSnakeView.setMode(SnakeView.PAUSE);
		// Pause the game along with the activity
		if ((ClassicSnakeConstants.GAME_MODE == SnakeView.RUNNING))
			mSnakeView.setMode(SnakeView.BACK);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		imgBtnPause.setImageResource(R.drawable.txt_pause);
		super.onResume();
		if ((ClassicSnakeConstants.GAME_MODE == SnakeView.BACK)){
			mSnakeView.setMode(SnakeView.PAUSE);
            // mRedrawHandler.removeMessages(0);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 100ms
			mSnakeView.setMode(SnakeView.RUNNING);
			greenAppleHandler.sleep(14000);
		
		if (ClassicSnakeConstants.GAME_PLAY_MODE == 2) {
		//	greenAppleHandler.sleep(14000);
			pqrHandler.sleep(1000);
		}
        }
		},1000);
		
	}
	
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store the game state
		outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
	}

	/*
	 * public void clickRight(View v) { //
	 * Toast.makeText(getApplicationContext(), "right", //
	 * Toast.LENGTH_SHORT).show(); if (mSnakeView.mDirection != mSnakeView.WEST)
	 * { mSnakeView.mNextDirection = mSnakeView.EAST; //
	 * mSnakeView.invalidate(); } }
	 * 
	 * public void clickUp(View v) { // Toast.makeText(getApplicationContext(),
	 * "up", // Toast.LENGTH_SHORT).show(); if (mSnakeView.mDirection !=
	 * mSnakeView.SOUTH) { mSnakeView.mNextDirection = mSnakeView.NORTH; //
	 * mSnakeView.invalidate(); } }
	 * 
	 * public void clickLeft(View v) { //
	 * Toast.makeText(getApplicationContext(), "left", //
	 * Toast.LENGTH_SHORT).show(); if (mSnakeView.mDirection != mSnakeView.EAST)
	 * { mSnakeView.mNextDirection = mSnakeView.WEST; //
	 * mSnakeView.invalidate(); } }
	 * 
	 * public void clickDown(View v) { //
	 * Toast.makeText(getApplicationContext(), "down", //
	 * Toast.LENGTH_SHORT).show(); if (mSnakeView.mDirection !=
	 * mSnakeView.NORTH) { mSnakeView.mNextDirection = mSnakeView.SOUTH; //
	 * mSnakeView.invalidate(); } }
	 */

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// mSnakeView.mRedrawHandler.removeMessages(0);
		// mSnakeView.mRedrawHandler.getLooper().quit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgBtnPause:
			ClassicSnakeConstants.gamePause=true;
			//mSnakeView.setMode(SnakeView.PAUSE);
			pqrHandler.removeMessages(3);
			greenAppleHandler.removeMessages(4);
			if (ClassicSnakeConstants.GAME_MODE == SnakeView.RUNNING)
				mSnakeView.setMode(SnakeView.BACK);
			intent = new Intent(GameScreen.this, MenuScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			/*
			 * else mSnakeView.setMode(SnakeView.RUNNING);
			 */

			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			finish();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//FlurryAgent.onStartSession(this, ClassicSnakeConstants.flurryKey);
		mSnakeView.initSnakeView();		
		setTheme();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//FlurryAgent.onEndSession(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		imgBtnDown.setImageResource(R.drawable.snakeclassic_downarrow);
		imgBtnUp.setImageResource(R.drawable.snakeclassic_uparrow);
		imgBtnRight.setImageResource(R.drawable.snakeclassic_rightarrow);
		imgBtnLeft.setImageResource(R.drawable.snakeclassic_leftarrow);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.imgBtnDown:
				imgBtnDown.setImageResource(R.drawable.down_selected);
				if (mSnakeView.mDirection != mSnakeView.NORTH) {
					mSnakeView.mNextDirection = mSnakeView.SOUTH;
					// mSnakeView.invalidate();
				}

				break;
			case R.id.imgBtnLeft:
				imgBtnLeft.setImageResource(R.drawable.left_selected);
				if (mSnakeView.mDirection != mSnakeView.EAST) {
					mSnakeView.mNextDirection = mSnakeView.WEST;
					// mSnakeView.invalidate();
				}
				break;
			case R.id.imgBtnRight:
				imgBtnRight.setImageResource(R.drawable.right_selected);
				if (mSnakeView.mDirection != mSnakeView.WEST) {
					mSnakeView.mNextDirection = mSnakeView.EAST;
					// mSnakeView.invalidate();
				}
				break;
			case R.id.imgBtnUp:
				imgBtnUp.setImageResource(R.drawable.up_selected);
				if (mSnakeView.mDirection != mSnakeView.SOUTH) {
					mSnakeView.mNextDirection = mSnakeView.NORTH;
					// mSnakeView.invalidate();
				}
				break;

			default:
				break;
			}
		}
		return true;
	}
}
