package com.zing.snakeclassic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class HelpMenu extends Activity implements FlurryAdListener {
	SharedPreferences prefs;
	RelativeLayout help_bgrd_theme_control;
	Intent i;
	FrameLayout mBanner;
	ImageView imgViewGameplay, imgViewFAQ, imgViewUnlock, imgViewBlog, imgViewFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.help);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		help_bgrd_theme_control = (RelativeLayout) findViewById(R.id.help_bgrd_theme_control);
		setTheme();
		imgViewGameplay = (ImageView) findViewById(R.id.imgViewGameplay);
		imgViewFAQ = (ImageView) findViewById(R.id.imgViewFAQ);
		imgViewUnlock = (ImageView) findViewById(R.id.imgViewUnlock);
		imgViewBlog = (ImageView) findViewById(R.id.imgViewBlog);
		imgViewFacebook = (ImageView) findViewById(R.id.imgViewFacebook);
		
		mBanner = (FrameLayout) findViewById(R.id.menu_banner);
		
		// allow us to get callbacks for ad events
		FlurryAds.setAdListener(this);
	//	imgViewResume = (ImageView) findViewById(R.id.imgViewResume);

	}

	@SuppressWarnings("deprecation")
	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			help_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			help_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			help_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			help_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

/*	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTheme();
		if(ClassicSnakeConstants.gamePause){
			imgViewResume.setEnabled(true);
		}else{
			imgViewResume.setEnabled(false);
		}
	}

	public void startNewGame(View v) {
		i = new Intent(getApplicationContext(), GameMode.class);
		startActivity(i);

	}

	public void resumeGame(View v) {
		//ClassicSnakeConstants.GAME_MODE = 2;
		ClassicSnakeConstants.gamePause=false;
		i = new Intent(getApplicationContext(), GameScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}
*/
	public void gameplayHelp(View v) {
		imgViewGameplay.setImageResource(R.drawable.help_gameplay_selected);
		i = new Intent(getApplicationContext(), GameplayHelp.class);
		startActivity(i);
	}

	public void faqHelp(View v) {
		imgViewFAQ.setImageResource(R.drawable.help_faq_selected);
		i = new Intent(getApplicationContext(), FAQHelp.class);
		startActivity(i);

	}

	public void unlockingHelp(View v) {
		imgViewUnlock.setImageResource(R.drawable.help_unlocking_selected);
		i = new Intent(getApplicationContext(), UnlockingHelp.class);
		startActivity(i);
	}
	
	public void blogHelp(View v) {
		//i = new Intent(getApplicationContext(), UnlockingHelp.class);
		//startActivity(i);
		imgViewBlog.setImageResource(R.drawable.help_blog_selected);
		Uri uri = Uri.parse("http://www.snakeclassic.blogspot.com");
		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		 startActivity(intent);
		
	}

	public void facebookPage(View v) {
		//i = new Intent(getApplicationContext(), UnlockingHelp.class);
		//startActivity(i);
		imgViewFacebook.setImageResource(R.drawable.help_facebook_selected);
		Uri uri = Uri.parse("http://www.facebook.com/snakeclassic");
		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		 startActivity(intent);
		
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
		imgViewGameplay.setImageResource(R.drawable.gameplay_help);
		imgViewFAQ.setImageResource(R.drawable.faq_help);
		imgViewUnlock.setImageResource(R.drawable.unlock_items_help);
		imgViewBlog.setImageResource(R.drawable.blog_help);
		imgViewFacebook.setImageResource(R.drawable.facebook_page_help);
		
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
		return false;
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
	
/*	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

	}
*/
}
