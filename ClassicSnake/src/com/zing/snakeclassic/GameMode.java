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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class GameMode extends Activity implements FlurryAdListener {
	private SharedPreferences prefs;
	RelativeLayout gm_bgrd_theme_control;
	FrameLayout mBanner;
	ImageView imgViewClassic;
	ImageView imgViewExtreme;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gamemode);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		gm_bgrd_theme_control = (RelativeLayout) findViewById(R.id.gm_bgrd_theme_control);
		imgViewClassic = (ImageView) findViewById(R.id.imgViewClassic);
		imgViewExtreme = (ImageView) findViewById(R.id.imgViewExtreme);
		setTheme();
		mBanner = (FrameLayout) findViewById(R.id.gm_banner);

		FlurryAds.setAdListener(this);
	}

	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			gm_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			gm_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			gm_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			gm_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTheme();
	}

	public void playClassic(View v) {
		imgViewClassic.setImageResource(R.drawable.gamemode_classic_selected);
		Intent i = new Intent(getApplicationContext(), OptionDialog.class);
		ClassicSnakeConstants.GAME_PLAY_MODE = 1;
		startActivity(i);
		finish();
	}

	public void playExtreme(View v) {
		imgViewExtreme.setImageResource(R.drawable.gamemode_extreme_selected);
		Intent i = new Intent(getApplicationContext(), SpeedDialog.class);
		ClassicSnakeConstants.GAME_PLAY_MODE = 2;
		ClassicSnakeConstants.GAME_LEVEL = 1;
		ClassicSnakeConstants.GAME_MOVEDELAY = 75;
		startActivity(i);
		finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		imgViewExtreme.setImageResource(R.drawable.txt_extreme);
		imgViewClassic.setImageResource(R.drawable.txt_classic);
		FlurryAgent.onStartSession(this, ClassicSnakeConstants.flurryKey);
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
		return false;
	}

	@Override
	public void spaceDidFailToReceiveAd(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void spaceDidReceiveAd(String arg0) {
		// TODO Auto-generated method stub
		FlurryAds.displayAd(this, "AdSpaceName", mBanner);

	}

}
