package com.zing.snakeclassic;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.database.DataHelper;
import com.android.model.ScoreModel;
import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class UnlockingHelp extends Activity implements FlurryAdListener {

	DataHelper dh;
	SimpleCursorAdapter dataAdapter;
	ListView lstScore;
	ArrayList<ScoreModel> scoreArrayList;
	FrameLayout mBanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	
        
        setContentView(R.layout.unlock_help);
        
        TextView tv=(TextView)findViewById(R.id.lstUnlock);
        tv.setMovementMethod(new ScrollingMovementMethod());
        
        dh  = new DataHelper(this);
                
		mBanner = (FrameLayout) findViewById(R.id.score_banner);

		FlurryAds.setAdListener(this);
        
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
