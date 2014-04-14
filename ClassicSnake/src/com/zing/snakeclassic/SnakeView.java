package com.zing.snakeclassic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.utils.ClassicSnakeConstants;

/**
 * SnakeView: implementation of a simple game of Snake
 * 
 * 
 */
public class SnakeView extends View {
	
	private SharedPreferences prefs;

	private static final String TAG = "SnakeView";
	//Thread timer;
	
	int mXMax_point1, mXMax_point2, mYMax_point1, mYMax_point2;
	static int extremeModeTimer;

	protected static int mXPixelCount; // Max area available
	protected static int mYPixelCount; // Max area available
	protected static int mXMax; // Max playing area available
	protected static int mYMax; // Max playing area available
	protected static int mXMin = 5; // Min playing area available
	protected static int mYMin = 5; // Min playing area available
	protected static int mSnakeUnitSize = 10;
	protected static int mSoftboardSize;// = 180;
	protected static int mRedAppleCount = 0;
	protected static int mGreenAppleCount = 0;
	protected static int mScoreFactorRed;
	protected static int mScoreFactorGreen;
	protected static boolean mShowGreenFlag = false;

	/**
	 * Current mode of application: READY to run, RUNNING, or you have already
	 * lost. static final ints are used instead of an enum for performance
	 * reasons.
	 */
	// public int mMode = READY;
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;
	public static final int BACK = 4;

	/**
	 * Current direction the snake is headed.
	 */
	public int mDirection = EAST;
	public int mNextDirection = EAST;
	public int mBoundaryDirection;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;

	// public int LEVEL;
	public static final int LEVEL1 = 1;
	public static final int LEVEL2 = 2;
	public static final int LEVEL3 = 3;
	public static final int LEVEL4 = 4;

	/**
	 * mScore: used to track the number of apples captured mMoveDelay: number of
	 * milliseconds between snake movements. This will decrease as apples are
	 * captured.
	 */
	public static long mScore = 0;
	// private long mMoveDelay = 100;
	/**
	 * mLastMove: tracks the absolute time when the snake last moved, and is
	 * used to determine if a move should be made based on mMoveDelay.
	 */
	private long mLastMove;

	/**
	 * mStatusText: text shows to the user in some run states
	 */
	public TextView mScoreView;

	public TextView mExtremeView;

	/**
	 * mSnakeTrail: a list of Coordinates that make up the snake's body
	 * mAppleList: the secret location of the juicy apples the snake craves.
	 */
	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();
	private Coordinate mGreenAppleCoord = new Coordinate(1, 1);
	Coordinate head = new Coordinate(1, 1);

	/**
	 * Everyone needs a little randomness in their life
	 */
	private static final Random RNG = new Random();

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 */
	public RefreshHandler mRedrawHandler = new RefreshHandler();

	//public ExtremeHandler mExtremeHandler = new ExtremeHandler();

	private Context context;
	private Activity current_activity;

	Calendar startTime, endTime;

	float circle_radius;
	float snake_width;
	float circle_6, circle_8, circle_1_2;
	int apple_height_width;
	/*class ExtremeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (ClassicSnakeConstants.GAME_PLAY_MODE == 2) {
				// We are in extreme mode
				if (extremeModeTimer <= 0
						&& ClassicSnakeConstants.GAME_LEVEL == 4) {
					setMode(LOSE);
					mExtremeHandler.removeMessages(1);
					//timer.stop();
					return;

				} else if (extremeModeTimer <= 0) {
					ClassicSnakeConstants.GAME_LEVEL++;
					extremeModeTimer = 45;
				}

				endTime = Calendar.getInstance();

				if (endTime.getTimeInMillis() - startTime.getTimeInMillis() > 1000) {
					--extremeModeTimer;
					mExtremeView.setText(String.valueOf(extremeModeTimer)
							+ " diff - "
							+ (endTime.getTimeInMillis() - startTime
									.getTimeInMillis()));

					startTime = Calendar.getInstance();
					endTime = Calendar.getInstance();
				} else {
					mExtremeView.setText(String.valueOf(extremeModeTimer)
							+ " diff - "
							+ (endTime.getTimeInMillis() - startTime
									.getTimeInMillis()));
				}

				mExtremeView.setText(String.valueOf(extremeModeTimer));
				mExtremeHandler.removeMessages(1);

				mExtremeHandler.sleep(1000);

			}
		}

		private void sleep(long delayMillis) {
			// TODO Auto-generated method stub
			this.removeMessages(1);
			sendMessageDelayed(obtainMessage(1), delayMillis);

		}

	}
*/
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SnakeView.this.update();
			SnakeView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	/*
	 * class RefreshHandler extends Handler { Handler h = new Handler(); Run run
	 * = new Run();
	 * 
	 * class Run implements Runnable {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * SnakeView.this.update(); SnakeView.this.invalidate(); } }
	 * 
	 * public void sleep(long delayMillis) { h.postDelayed(run, delayMillis); }
	 * 
	 * public void sleepCanceled() { h.removeCallbacks(run); } };
	 */

	/**
	 * Constructs a SnakeView based on inflation from XML
	 * 
	 * @param context
	 * @param attrs
	 */
	public SnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		current_activity = (Activity) context;
		initSnakeView();
	}

	public SnakeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		current_activity = (Activity) context;
		initSnakeView();
	}

	public void initSnakeView() {
		setFocusable(true);
		mRedrawHandler.sleep(ClassicSnakeConstants.GAME_MOVEDELAY);
	}

	private void initNewGame() {
		
		prefs = context.getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		initialposition();

		// mMoveDelay = 100;
		mScore = 0;
		mGreenAppleCount = 0;
		mRedAppleCount = 0;

		// Timer for Green apples
		/*MyTimerTask myTask = new MyTimerTask();
		new Timer().scheduleAtFixedRate(myTask, 13000, 13000);*/

		// Extreme mode timer
		/*
		 * ExtremeTimerTask extremeTask = new ExtremeTimerTask(); new
		 * Timer().scheduleAtFixedRate(extremeTask, 1000, 1000);
		 */

	/*	extremeModeTimer = 45;
		startTime = Calendar.getInstance();
		 mExtremeHandler.sleep(1000); 
		mExtremeView.setText(String.valueOf(extremeModeTimer));*/

		/*timer = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (;;) {
					if (!mExtremeHandler.hasMessages(0)) {
						endTime = Calendar.getInstance();
						if (endTime.getTimeInMillis() - startTime.getTimeInMillis() > 1000) {
							--extremeModeTimer;
							mExtremeHandler.sendEmptyMessage(0);

							startTime = Calendar.getInstance();
							endTime = Calendar.getInstance();
						}

					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // sleep for 3 seconds
				}
			}
		};
		timer.start();*/
	}

	
	void initialposition(){
		mSnakeTrail.clear();
		mAppleList.clear();

		// For now we're just going to load up a short default eastbound snake
		// that's just turned north

		// mSnakeTrail.add(new Coordinate(90, 120));
		// mSnakeTrail.add(new Coordinate(80, 120));
		// mSnakeTrail.add(new Coordinate(70, 120));
		// mSnakeTrail.add(new Coordinate(70, 130));
		// mSnakeTrail.add(new Coordinate(70, 140));
		// mSnakeTrail.add(new Coordinate(70, 160));
		// mNextDirection = NORTH;
		mSnakeTrail.add(new Coordinate(95, 125));
		mSnakeTrail.add(new Coordinate(85, 125));
		mSnakeTrail.add(new Coordinate(75, 125));
		mSnakeTrail.add(new Coordinate(65, 125));
		mSnakeTrail.add(new Coordinate(55, 125));
		mSnakeTrail.add(new Coordinate(45, 125));
		mDirection = EAST;
		mNextDirection = EAST;

		// Two apples to start with
		addRandomApple("RED");
		//addRandomApple("RED");
		
	}
	
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mShowGreenFlag = true;
			//addRandomApple("GREEN");
			NewTimerTask newTask = new NewTimerTask();
			new Timer().schedule(newTask, 5000);

		}

	}

	class NewTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mShowGreenFlag = false;
			mGreenAppleCoord.x = -1;
			mGreenAppleCoord.y = -1;
		}

	}

	class ExtremeTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			--extremeModeTimer;
		}

	}

	/**
	 * Given a ArrayList of coordinates, we need to flatten them into an array
	 * of ints before we can stuff them into a map for flattening and storage.
	 * 
	 * @param cvec
	 *            : a ArrayList of Coordinate objects
	 * @return : a simple array containing the x/y values of the coordinates as
	 *         [x1,y1,x2,y2,x3,y3...]
	 */
	private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 2];
		for (int index = 0; index < count; index++) {
			Coordinate c = cvec.get(index);
			rawArray[2 * index] = c.x;
			rawArray[2 * index + 1] = c.y;
		}
		return rawArray;
	}

	/**
	 * Save game state so that the user does not lose anything if the game
	 * process is killed while we are in the background.
	 * 
	 * @return a Bundle with this view's state
	 */
	public Bundle saveState() {
		Bundle map = new Bundle();

		map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		map.putInt("mDirection", Integer.valueOf(mDirection));
		map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
		map.putLong("mMoveDelay",
				Long.valueOf(ClassicSnakeConstants.GAME_MOVEDELAY));
		map.putLong("mScore", Long.valueOf(mScore));
		map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

		return map;
	}

	/**
	 * Given a flattened array of ordinate pairs, we reconstitute them into a
	 * ArrayList of Coordinate objects
	 * 
	 * @param rawArray
	 *            : [x1,y1,x2,y2,...]
	 * @return a ArrayList of Coordinates
	 */
	private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}

	/**
	 * Restore game state if our process is being relaunched
	 * 
	 * @param icicle
	 *            a Bundle containing the game state
	 */
	public void restoreState(Bundle icicle) {
		setMode(PAUSE);

		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		ClassicSnakeConstants.GAME_MOVEDELAY = icicle.getLong("mMoveDelay");
		mScore = icicle.getLong("mScore");
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
		
	}

	/*
	 * handles key events in the game. Update the direction our snake is
	 * traveling based on the DPAD. Ignore events that would cause the snake to
	 * immediately turn back on itself.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			return (true);
		}

		return super.onKeyDown(keyCode, msg);
	}

	/**
	 * Sets the TextView that will be used to give information (such as "Game
	 * Over" to the user.
	 * 
	 * @param newView
	 */
	public void setTextView(TextView newView) {
		mScoreView = newView;
	}

	public void setExtremeView(TextView newView) {
		mExtremeView = newView;
	}

	/**
	 * Updates the current mode of the application (RUNNING or PAUSED or the
	 * like) as well as sets the visibility of textview for notification
	 * 
	 * @param newMode
	 */
	public void setMode(int newMode) {
		int oldMode = ClassicSnakeConstants.GAME_MODE;
		ClassicSnakeConstants.GAME_MODE = newMode;

		if (newMode == RUNNING & oldMode != RUNNING) {
			// mStatusText.setVisibility(View.INVISIBLE);

			update();
			return;
		}

		//Resources res = getContext().getResources();
		//CharSequence str = "";
		if (newMode == PAUSE) {
			// str = res.getText(R.string.mode_pause);
		}
		if (newMode == READY) {
			// str = res.getText(R.string.mode_ready);
		}
		if (newMode == LOSE) {		
						
			boolean vibrateValue= prefs.getBoolean("snake_vibrate", false);
			if(vibrateValue){
				((Vibrator)context.getSystemService(context.VIBRATOR_SERVICE)).vibrate(400);
			}			
			boolean soundValue= prefs.getBoolean("snake_sound", false);
			if(soundValue){
				playSound("OVER");					
			}
			// str = res.getString(R.string.mode_lose_prefix) + mScore
			// + res.getString(R.string.mode_lose_suffix);
			mRedrawHandler.removeMessages(0);
			GameScreen.pqrHandler.removeMessages(3);
			GameScreen.greenAppleHandler.removeMessages(4);
			// Context context =getContext();
			Intent intent = new Intent(context, GameOver.class);
			context.startActivity(intent);
			current_activity.finish();
		}

		// mStatusText.setText(str);
		// mStatusText.setVisibility(View.VISIBLE);
	}

	/**
	 * Selects a random location within the garden that is not currently covered
	 * by the snake. Currently _could_ go into an infinite loop if the snake
	 * currently fills the garden, but we'll leave discovery of this prize to a
	 * truly excellent snake-player.
	 * 
	 * @param apple_color
	 * 
	 */
	void addRandomApple(String apple_color) {
		Coordinate newCoord = null;
		boolean found = false;
		while (!found) {
			// Choose a new location for our apple
			int newX;
			// newX= RNG.nextInt(mXPixelCount);
			newX = RNG.nextInt(mXMax);
			if (newX < 15)
				newX = 15;
			else {
				newX = newX - (newX % 5);
				if (newX % 10 == 0)
					newX = newX - 5;
			}
			int newY;
			// newY= RNG.nextInt(mYPixelCount);
			newY = RNG.nextInt(mYMax);
			if (newY < 15)
				newY = 15;
			else {
				newY = newY - (newY % 5);
				if (newY % 10 == 0)
					newY = newY - 5;
			}
			if (ClassicSnakeConstants.GAME_LEVEL == LEVEL4) {
				if ((newX == mXMax_point1)
						&& (newY < mYMax_point1 || newY > mYMax_point2)) {
					newX = mXMax_point1 - 20;
				}
				if ((newX == mXMax_point2)
						&& (newY < mYMax_point1 || newY > mYMax_point2)) {
					newX = mXMax_point2 + 20;
				}
			}
			newCoord = new Coordinate(newX, newY);
			// Make sure it's not already under the snake
			boolean collision = false;
			int snakelength = mSnakeTrail.size();
			for (int index = 0; index < snakelength; index++) {
				if (mSnakeTrail.get(index).equals(newCoord)) {
					collision = true;
				}
			}
			// if we're here and there's been no collision, then we have
			// a good location for an apple. Otherwise, we'll circle back
			// and try again
			found = !collision;
		}
		if (newCoord == null) {
			Log.e(TAG, "Somehow ended up with a null newCoord!");
		}

		if (apple_color.equals("RED"))
			mAppleList.add(newCoord);
		else
			mGreenAppleCoord = newCoord;
	}

	/**
	 * Handles the basic update loop, checking to see if we are in the running
	 * state, determining if a move should be made, updating the snake's
	 * location.
	 */
	public void update() {
		if (ClassicSnakeConstants.GAME_MODE == READY)
				//| ClassicSnakeConstants.GAME_MODE == LOSE) 
		{
			/*
			 * At the beginning of the game, or the end of a previous one, we
			 * should start a new game.
			 */
			initNewGame();
			setMode(PAUSE);            
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                //Do something after 1000ms    
                  setMode(RUNNING);                 
              }
            }, 1000);            
		
			// update();
		}
		else if (ClassicSnakeConstants.GAME_MODE == PAUSE) {
			/*
			 * If the game is merely paused, we should just continue where we
			 * left off.
			 */        
			setMode(PAUSE);
			final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                //Do something after 1000ms    
                  setMode(RUNNING);                 
              }
            }, 1000);            
			//setMode(RUNNING);
			// update();
			  
			  
			 
		}

		else if (ClassicSnakeConstants.GAME_MODE == RUNNING) {
			long now = System.currentTimeMillis();

			if (now - mLastMove > ClassicSnakeConstants.GAME_MOVEDELAY) {
				//updateWalls();
				updateSnake();
				if (ClassicSnakeConstants.GAME_MODE == LOSE)
					return; // vins
				updateApples();
				mLastMove = now;
			}
			mRedrawHandler.sleep(ClassicSnakeConstants.GAME_MOVEDELAY);
		}

	}

	/**
	 * Draws some walls.
	 * 
	 */
	private void updateWalls() {
	}

	/**
	 * Draws some apples.
	 * 
	 */
	private void updateApples() {

	}

	/**
	 * Figure out which way the snake is going, see if he's run into anything
	 * (the walls, himself, or an apple). If he's not going to die, we then add
	 * to the front and subtract from the rear in order to simulate motion. If
	 * we want to grow him, we don't subtract from the rear.
	 * 
	 */
	private void updateSnake() {
		boolean growSnake = false;
		mScoreView.setText(String.valueOf(mScore));

		// grab the snake by the head
		Coordinate head = mSnakeTrail.get(0);
		Coordinate newHead = new Coordinate(1, 1);

		mDirection = mNextDirection;

		switch (mDirection) {
		case EAST: {
			newHead = new Coordinate(head.x + mSnakeUnitSize, head.y);
			break;
		}
		case WEST: {
			newHead = new Coordinate(head.x - mSnakeUnitSize, head.y);
			break;
		}
		case NORTH: {
			newHead = new Coordinate(head.x, head.y - mSnakeUnitSize);
			break;
		}
		case SOUTH: {
			newHead = new Coordinate(head.x, head.y + mSnakeUnitSize);
			break;
		}
		}

		if (ClassicSnakeConstants.GAME_LEVEL == LEVEL2) {
			// Collision detection
			// For now we have a 1-square wall around the entire arena
			if ((newHead.x <= mXMin) || (newHead.y <= mYMin)
					|| (newHead.x >= mXMax) || (newHead.y >= mYMax)) {
				setMode(LOSE);
				return;
			}
			mScoreFactorRed = 15;
			mScoreFactorGreen = 15;

		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL1) {

			if (newHead.x > mXMax)
				newHead.x = mXMin;
			else if (newHead.x < mXMin)
				newHead.x = mXMax;

			if (newHead.y > mYMax)
				newHead.y = mYMin;
			else if (newHead.y < mYMin)
				newHead.y = mYMax;
			mScoreFactorRed = 10;
			mScoreFactorGreen = 10;

		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL3) {
			mScoreFactorRed = 20;
			mScoreFactorGreen = 15;

			if ((newHead.x > mXMax)
					&& (newHead.y >= mYMax_point1 && newHead.y <= mYMax_point2)) {
				newHead.x = mXMin;
			} else if ((newHead.x == mXMax)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}
			if ((newHead.x < mXMin)
					&& (newHead.y >= mYMax_point1 && newHead.y <= mYMax_point2)) {
				newHead.x = mXMax;
			} else if ((newHead.x == mXMin)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}

			if ((newHead.y > mYMax)
					&& (newHead.x >= mXMax_point1 && newHead.x <= mXMax_point2)) {
				newHead.y = mYMin;
			} else if ((newHead.y == mYMax)
					&& (newHead.x < mXMax_point1 || newHead.x > mXMax_point2)) {
				setMode(LOSE);
				return;
			}

			if ((newHead.y < mYMin)
					&& (newHead.x >= mXMax_point1 && newHead.x <= mXMax_point2)) {
				newHead.y = mYMax;
			} else if ((newHead.y == mYMin)
					&& (newHead.x < mXMax_point1 || newHead.x > mXMax_point2)) {
				setMode(LOSE);
				return;
			}

		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL4) {

			mScoreFactorRed = 25;
			mScoreFactorGreen = 15;

			if ((newHead.x == mXMax_point1)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}
			if ((newHead.x == mXMax_point2)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}

			if ((newHead.x > mXMax)
					&& (newHead.y >= mYMax_point1 && newHead.y <= mYMax_point2)) {
				newHead.x = mXMin;
			} else if ((newHead.x == mXMax)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}
			if ((newHead.x < mXMin)
					&& (newHead.y >= mYMax_point1 && newHead.y <= mYMax_point2)) {
				newHead.x = mXMax;
			} else if ((newHead.x == mXMin)
					&& (newHead.y < mYMax_point1 || newHead.y > mYMax_point2)) {
				setMode(LOSE);
				return;
			}

			if ((newHead.y > mYMax)
					&& (newHead.x >= mXMax_point1 && newHead.x <= mXMax_point2)) {
				newHead.y = mYMin;
			} else if ((newHead.y == mYMax)
					&& (newHead.x < mXMax_point1 || newHead.x > mXMax_point2)) {
				setMode(LOSE);
				return;
			}

			if ((newHead.y < mYMin)
					&& (newHead.x >= mXMax_point1 && newHead.x <= mXMax_point2)) {
				newHead.y = mYMax;
			} else if ((newHead.y == mYMin)
					&& (newHead.x < mXMax_point1 || newHead.x > mXMax_point2)) {
				setMode(LOSE);
				return;
			}

		}

		// Look for collisions with itself
		int snakelength = mSnakeTrail.size();
		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newHead)) {
				setMode(LOSE);
				return;
			}
		}

		// Look for red apples
		int applecount = mAppleList.size();
		for (int appleindex = 0; appleindex < applecount; appleindex++) {
			Coordinate c = mAppleList.get(appleindex);
			if (c.equals(newHead)) {				
				boolean soundValue= prefs.getBoolean("snake_sound", false);
				if(soundValue){
					playSound("APPLE");					
				}	
				
				mAppleList.remove(c);
				addRandomApple("RED");

				++mRedAppleCount;
				mScore += 100 + ((mGreenAppleCount + mRedAppleCount) * mScoreFactorRed);
				mScoreView.setText(String.valueOf(mScore));
				// mMoveDelay *= 0.9;

				growSnake = true;

			}
		}

		// Look for green apples

		if (mShowGreenFlag && mGreenAppleCoord.equals(newHead)) {
			boolean soundValue= prefs.getBoolean("snake_sound", false);
			if(soundValue){
				playSound("APPLE");					
			}	
			mGreenAppleCoord.x = -1;
			mGreenAppleCoord.y = -1;
			++mGreenAppleCount;
			mScore += 2 * (100 + ((mGreenAppleCount + mRedAppleCount) * mScoreFactorGreen));
			mScoreView.setText(String.valueOf(mScore));
			// mMoveDelay *= 0.9;

			growSnake = true;

		}

		// push a new head onto the ArrayList and pull off the tail
		mSnakeTrail.add(0, newHead);
		// except if we want the snake to grow
		if (!growSnake) {
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}
	}

	private void playSound(String soundSelect) {
		// TODO Auto-generated method stub
		MediaPlayer mp;
		if(soundSelect.equals("OVER")){
			mp= MediaPlayer.create(getContext(), R.raw.gameover);
		}else{
			mp= MediaPlayer.create(getContext(), R.raw.food);
		}
        mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }

        });   
        mp.start();
		
	}

	/**
	 * Simple class containing two integer values and a comparison function.
	 * There's probably something I should use instead, but this was quick and
	 * easy to build.
	 * 
	 */
	private class Coordinate {
		public int x;
		public int y;

		public Coordinate(int newX, int newY) {
			x = newX;
			y = newY;
		}

		public boolean equals(Coordinate other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return "Coordinate: [" + x + "," + y + "]";
		}
	}

	

	Paint mPaint = new Paint();
	Path path = new Path();
	 //Coordinate head= new Coordinate(1, 1);
		Coordinate from = new Coordinate(1, 1);
		Coordinate to = new Coordinate(1, 1);
		PathEffect patheffect = new CornerPathEffect(1);
		Matrix matrix = new Matrix();
		//Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
		//		R.drawable.apple_red);

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//Paint mPaint = new Paint();

		// Draw circle at the tail
		if (mSnakeTrail.size() > 0) {
			mPaint.reset();
			Coordinate tail = mSnakeTrail.get(mSnakeTrail.size() - 1);
			mPaint.setColor(Color.rgb(ClassicSnakeConstants.head_r,
			ClassicSnakeConstants.head_g, ClassicSnakeConstants.head_b));
			
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE); // set to STOKE
			mPaint.setAntiAlias(true);
			canvas.drawCircle(tail.x, tail.y, circle_radius, mPaint);
		}
		// Draw path
		//Path path = new Path();
		path.reset();
		mPaint.setStrokeWidth(snake_width);
		mPaint.setColor(Color.rgb(ClassicSnakeConstants.body_r,
				ClassicSnakeConstants.body_g, ClassicSnakeConstants.body_b));
		
		mPaint.setStyle(Paint.Style.STROKE); // set to STOKE
		mPaint.setStrokeJoin(Paint.Join.ROUND); // set the join to round you
												// want
		mPaint.setPathEffect(patheffect); // set the path effect
														// when
		
		mPaint.setAntiAlias(true);
		int index = 0;
		 //Coordinate head= new Coordinate(1, 1);
		//Coordinate from = new Coordinate(1, 1);
		//Coordinate to = new Coordinate(1, 1);
		if (ClassicSnakeConstants.GAME_LEVEL == LEVEL2) {
			for (Coordinate c : mSnakeTrail) {
				if (index == 0) {
					path.moveTo(c.x, c.y);
					head = c;
					// Log.i("head","x="+ c.x +" y="+c.y);
				} else {
					path.lineTo(c.x, c.y);
					// Log.i("trail","x="+ c.x +" y="+c.y);
				}
				index++;
			}
			canvas.drawPath(path, mPaint);
		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL1) {
			for (Coordinate c : mSnakeTrail) {
				if (index == 0) {
					head = c;
					from = c;
					path.moveTo(from.x, from.y);
				} else {
					to = c;
					if ((boundaryCondition(from, to))) {
						canvas.drawPath(path, mPaint);
						path.moveTo(to.x, to.y);
					} else {

						path.lineTo(to.x, to.y);

						// canvas.drawLine(from.x, from.y, to.x, to.y, mPaint);
					}

					from = to;
				}
				index++;
			}
			canvas.drawPath(path, mPaint);
			 
		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL3
				|| ClassicSnakeConstants.GAME_LEVEL == LEVEL4) {
			for (Coordinate c : mSnakeTrail) {
				if (index == 0) {
					head = c;
					from = c;
					path.moveTo(from.x, from.y);
				} else {
					to = c;
					if ((boundaryCondition_level3(from, to))) {
						canvas.drawPath(path, mPaint);
						path.moveTo(to.x, to.y);
					} else {

						path.lineTo(to.x, to.y);

						// canvas.drawLine(from.x, from.y, to.x, to.y, mPaint);
					}

					from = to;
				}
				index++;
			}
			canvas.drawPath(path, mPaint);

		}

		// Draw Snake Head
		mPaint.reset();
		mPaint.setColor(Color.rgb(ClassicSnakeConstants.head_r,
				ClassicSnakeConstants.head_g, ClassicSnakeConstants.head_b));
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE); // set to STOKE
		canvas.drawCircle(head.x, head.y,6, mPaint);
		mPaint.setAntiAlias(true);
		if (mDirection == NORTH || mDirection == SOUTH)
			canvas.drawOval(new RectF(head.x - circle_6, head.y + circle_6, head.x + circle_6,
					head.y - circle_8), mPaint);
		else
			canvas.drawOval(new RectF(head.x - circle_8, head.y + circle_6, head.x + circle_6,
					head.y - circle_6), mPaint);
		// Draw Snake Eyes
		mPaint.setStyle(Paint.Style.STROKE); // set to STOKE
		mPaint.setColor(Color.WHITE);
		if (mDirection == NORTH || mDirection == SOUTH) {
			canvas.drawCircle((float) (head.x - 2.5), head.y, circle_1_2,
					mPaint);
			canvas.drawCircle((float) (head.x + 2.5), head.y, circle_1_2,
					mPaint);
		} else {
			canvas.drawCircle(head.x, (float) (head.y - 2.5),circle_1_2,
					mPaint);
			canvas.drawCircle(head.x, (float) (head.y + 2.5), circle_1_2,
					mPaint);
		}
		// Draw Apples
		for (Coordinate c : mAppleList) {
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeWidth(5);
			mPaint.setColor(Color.rgb(196, 93, 88));
			// canvas.drawCircle(c.x, c.y, 2, mPaint);
			/*
			 * Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
			 * R.drawable.apple_red); canvas.drawBitmap(bitmap, c.x, c.y,
			 * mPaint);
			 */

			// load the origial BitMap (500 x 500 px)
			Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
					R.drawable.apple_red);

			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();
			int newWidth = apple_height_width;
			int newHeight = apple_height_width;

			// calculate the scale - in this case = 0.4f
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			// create a matrix for the manipulation
			//Matrix matrix = new Matrix();
			// resize the bit map
			matrix.reset();
			matrix.postScale(scaleWidth, scaleHeight);
			// rotate the Bitmap
			// matrix.postRotate(45);

			// recreate the new Bitmap
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
					height, matrix, true);
			canvas.drawBitmap(resizedBitmap, c.x - 5, c.y - 5, mPaint);
		}

		// Draw green apple
		if (mShowGreenFlag) {
			Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
					R.drawable.apple_green);

			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();
			int newWidth = apple_height_width;
			int newHeight = apple_height_width;

			// calculate the scale - in this case = 0.4f
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			// create a matrix for the manipulation
			//Matrix matrix = new Matrix();
			// resize the bit map
			matrix.reset();
			matrix.postScale(scaleWidth, scaleHeight);
			// rotate the Bitmap
			// matrix.postRotate(45);

			// recreate the new Bitmap
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
					height, matrix, true);
			canvas.drawBitmap(resizedBitmap, mGreenAppleCoord.x - 5,
					mGreenAppleCoord.y - 5, mPaint);

		}

		// Draw wall
		if (ClassicSnakeConstants.GAME_LEVEL == LEVEL2) {
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mSnakeUnitSize);
			mPaint.setStrokeWidth(10);
			mPaint.setColor(Color.DKGRAY);
			// canvas.drawRect(4, 4, mXPixelCount-4, mYPixelCount, mPaint);
			canvas.drawRect(mXMin, mYMin, mXMax, mYMax, mPaint);
		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL3) {
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mSnakeUnitSize);
			mPaint.setStrokeWidth(10);
			mPaint.setColor(Color.DKGRAY);
			mXMax_point1 = (mXMax - mXMin) / 2 - 20;
			mXMax_point2 = (mXMax - mXMin) / 2 + 20;
			mYMax_point1 = (mYMax - mYMin) / 2 - 20;
			mYMax_point2 = (mYMax - mYMin) / 2 + 20;

			canvas.drawLine(mXMin, 0, mXMin, mYMax_point1 - 5, mPaint);
			canvas.drawLine(mXMin, mYMin, mXMax_point1 - 5, mYMin, mPaint);
			canvas.drawLine(mXMax_point2 + 5, mYMin, mXMax, mYMin, mPaint);
			canvas.drawLine(mXMax, 0, mXMax, mYMax_point1 - 5, mPaint);
			canvas.drawLine(mXMax, mYMax_point2 + 5, mXMax, mYMax + 5, mPaint);
			canvas.drawLine(mXMax_point2 + 5, mYMax, mXMax, mYMax, mPaint);
			canvas.drawLine(mXMin, mYMax, mXMax_point1 - 5, mYMax, mPaint);
			canvas.drawLine(mXMin, mYMax_point2 + 5, mXMin, mYMax + 5, mPaint);

		} else if (ClassicSnakeConstants.GAME_LEVEL == LEVEL4) {
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mSnakeUnitSize);
			mPaint.setStrokeWidth(10);
			mPaint.setColor(Color.DKGRAY);
			mXMax_point1 = (mXMax - mXMin) / 2 - 20;
			mXMax_point2 = (mXMax - mXMin) / 2 + 20;
			mYMax_point1 = (mYMax - mYMin) / 2 - 20;
			mYMax_point2 = (mYMax - mYMin) / 2 + 20;

			canvas.drawLine(mXMin, 0, mXMin, mYMax_point1 - 5, mPaint);
			canvas.drawLine(mXMin, mYMin, mXMax_point1 - 5, mYMin, mPaint);
			canvas.drawLine(mXMax_point2 + 5, mYMin, mXMax, mYMin, mPaint);
			canvas.drawLine(mXMax, 0, mXMax, mYMax_point1 - 5, mPaint);
			canvas.drawLine(mXMax, mYMax_point2 + 5, mXMax, mYMax + 5, mPaint);
			canvas.drawLine(mXMax_point2 + 5, mYMax, mXMax, mYMax, mPaint);
			canvas.drawLine(mXMin, mYMax, mXMax_point1 - 5, mYMax, mPaint);
			canvas.drawLine(mXMin, mYMax_point2 + 5, mXMin, mYMax + 5, mPaint);
			mPaint.setStrokeWidth(10);
			canvas.drawLine(mXMax_point1, 0, mXMax_point1, mYMax_point1 - 5,
					mPaint); // 1
			canvas.drawLine(mXMax_point2, 0, mXMax_point2, mYMax_point1 - 5,
					mPaint); // 2
			canvas.drawLine(mXMax_point2, mYMax_point2 + 5, mXMax_point2,
					mYMax + 5, mPaint); // 3
			canvas.drawLine(mXMax_point1, mYMax_point2 + 5, mXMax_point1,
					mYMax + 5, mPaint); // 4

		}
	}

	private boolean boundaryCondition_level3(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub
		if (from.y == to.y
				&& ((from.x == mXMin && to.x == mXMax) || (from.x == mXMax && to.x == mXMin))) {
			return true;
		}
		if (from.x == to.x
				&& ((from.y == mYMin && to.y == mYMax) || (from.y == mYMax && to.y == mYMin))) {
			return true;
		}

		return false;
	}

	private boolean boundaryCondition(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub

		if (from.y == to.y
				&& ((from.x == mXMin && to.x == mXMax) || (from.x == mXMax && to.x == mXMin))) {
			return true;
		}
		if (from.x == to.x
				&& ((from.y == mYMin && to.y == mYMax) || (from.y == mYMax && to.y == mYMin))) {
			return true;
		}

		return false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		float ratio;
		if (h <= 480){
			ratio =(float) 1.5;
			mSoftboardSize = 180;
			//Snake tail
			circle_radius=(float) 5.8/ratio;
			//Snake body
			snake_width=11/ratio;
			//Snake head and eyes
			circle_1_2 =(float) 1;
			circle_6= (float) 4.5;
			circle_8=7;
			//Apple height weight
			apple_height_width=8;
		}
		else if (h <= 800){
			mSoftboardSize = 270;
			circle_radius=(float) 5.8;
			snake_width=11;
			circle_1_2 =(float) 1.2;
			circle_6= 6;
			circle_8=8;
			apple_height_width=10;
		}
		else if(h<= 1280){
			mSoftboardSize = 360;
			ratio =(float) 1.5;
			//Snake tail
			circle_radius=(float) 5.8*ratio;
			//Snake body
			snake_width=14;
			//Snake head and eyes
			circle_1_2 =(float) 2.4;
			circle_6= (float) 9;
			circle_8=12;
			//Apple height weight
			apple_height_width=13;
		}
		super.onSizeChanged(w, h, oldw, oldh);
		mXPixelCount = (int) Math.floor(w);
		mYPixelCount = (int) Math.floor(h);
		mYPixelCount = mYPixelCount - mSoftboardSize;
		mXMax = mXPixelCount - (mXPixelCount % mSnakeUnitSize) - 5;
		mYMax = mYPixelCount - (mYPixelCount % mSnakeUnitSize) - 5;
		//Log.i("SCREEN", "SIZES: mXPixelCount " + mXPixelCount
		//		+ " mYPixelCount " + mYPixelCount + " mXMax " + mXMax
		//		+ " mYMax " + mYMax);
	}

}
