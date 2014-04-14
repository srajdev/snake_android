package com.zing.snakeclassic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class MenuScreen extends Activity implements FlurryAdListener {
	SharedPreferences prefs;
	RelativeLayout menu_bgrd_theme_control;
	Intent i;
	FrameLayout mBanner;
	ImageView imgViewResume;
	ImageView imgViewNew;
	ImageView imgViewUnlock;
	ImageView imgViewScores;
	ImageView imgViewOptions, imgViewHelp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		menu_bgrd_theme_control = (RelativeLayout) findViewById(R.id.menu_bgrd_theme_control);
		setTheme();
		//mBanner = new FrameLayout(this);
		mBanner = (FrameLayout) findViewById(R.id.menu_banner);
		// allow us to get callbacks for ad events
		FlurryAds.setAdListener(this);
		imgViewResume = (ImageView) findViewById(R.id.imgViewResume);
		imgViewNew = (ImageView) findViewById(R.id.imgViewNew);
		imgViewUnlock = (ImageView) findViewById(R.id.imgViewUnlock);
		imgViewScores = (ImageView) findViewById(R.id.imgViewScores);
		imgViewOptions = (ImageView) findViewById(R.id.imgViewOptions);
		imgViewHelp = (ImageView) findViewById(R.id.imgViewHelp);
	}

	@SuppressWarnings("deprecation")
	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			menu_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			menu_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			menu_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			menu_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTheme();
		if(ClassicSnakeConstants.gamePause){
			imgViewResume.setImageResource(R.drawable.snakeclassic_resume);
			imgViewResume.setEnabled(true);
		}else{
			imgViewResume.setImageResource(R.drawable.resume_transparent);
			imgViewResume.setEnabled(false);
		}
	}

	public void startNewGame(View v) {
		imgViewNew.setImageResource(R.drawable.main_new_selected);
		i = new Intent(getApplicationContext(), GameMode.class);
		startActivity(i);

	}

	public void resumeGame(View v) {
		//ClassicSnakeConstants.GAME_MODE = 2;
		ClassicSnakeConstants.gamePause=false;
		imgViewResume.setImageResource(R.drawable.main_resume_selected);
		i = new Intent(getApplicationContext(), GameScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	public void changeOptions(View v) {
		imgViewOptions.setImageResource(R.drawable.main_option_selected);
		i = new Intent(getApplicationContext(), OptionsMenu.class);
		startActivity(i);
	}

	public void viewScore(View v) {
		imgViewScores.setImageResource(R.drawable.main_score_selected);
		i = new Intent(getApplicationContext(), ScoresScreen.class);
		startActivity(i);

	}

	public void unlockField(View v) {
		imgViewUnlock.setImageResource(R.drawable.main_unlock_selected);
		i = new Intent(getApplicationContext(), UnlockCredit.class);
		startActivity(i);
	}

	public void helpMenu(View v) {
		imgViewHelp.setImageResource(R.drawable.help_selected);
		i = new Intent(getApplicationContext(), HelpMenu.class);
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		imgViewUnlock.setImageResource(R.drawable.snakeclassic_unlock);
		imgViewNew.setImageResource(R.drawable.snakeclassic_newgame);
		imgViewOptions.setImageResource(R.drawable.snakeclassic_options);
		imgViewResume.setImageResource(R.drawable.snakeclassic_resume);
		imgViewScores.setImageResource(R.drawable.snakeclassic_scores);
		imgViewHelp.setImageResource(R.drawable.help);
		
		FlurryAgent.onStartSession(this, ClassicSnakeConstants.flurryKey);
		FlurryAds.enableTestAds(true);

		FlurryAppCloud.initAppCloudModule(this);
		FlurryAds.initializeAds(this);
		FlurryAds.fetchAd(this, "AdSpaceName", mBanner,
				FlurryAdSize.BANNER_BOTTOM);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onAdClicked(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdClosed(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdOpened(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onApplicationExit(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRenderFailed(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoCompleted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldDisplayAd(String arg0, FlurryAdType arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void spaceDidFailToReceiveAd(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void spaceDidReceiveAd(String arg0) {
		// TODO Auto-generated method stub
		// called when the ad has been prepared, ad can be displayed:
		FlurryAds.displayAd(this, "AdSpaceName", mBanner);

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

	}

}
