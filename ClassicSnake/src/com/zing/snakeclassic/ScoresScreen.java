package com.zing.snakeclassic;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.adapter.SpecialAdapter;
import com.android.database.DataHelper;
import com.android.model.ScoreModel;
import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class ScoresScreen extends Activity implements FlurryAdListener {

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
        
        setContentView(R.layout.score);
        
        lstScore = (ListView) findViewById(R.id.lstScore);
        dh  = new DataHelper(this);
        // the XML defined views which the data will be bound to
        
/*        // The desired columns to be bound
        String[] columns = new String[] {
          "playername",
          "gamemode",
          "score"
        };
        
        int[] to = new int[] {
          R.id.txtPlayerName,
          R.id.txtGameMode,
          R.id.txtScore
        };
        dataAdapter = new SimpleCursorAdapter(this, R.layout.customlist, dh.selectTOP10(), columns, to);
        lstScore.setAdapter(dataAdapter);*/
        
        scoreArrayList = new ArrayList<ScoreModel>();
        poppulateArraylist(dh.selectTOP10());
        SpecialAdapter adapter = new SpecialAdapter(this, scoreArrayList);
        lstScore.setAdapter(adapter);
        
		mBanner = (FrameLayout) findViewById(R.id.score_banner);

		FlurryAds.setAdListener(this);
        
	}

	private void poppulateArraylist(Cursor cursor) {
		// TODO Auto-generated method stub
		if(cursor!=null){
			if(cursor.getCount()!=0){
				int i=1;
				cursor.moveToFirst();
				do {
					ScoreModel tempScoreModel = new ScoreModel();
					tempScoreModel.setPlayerRank(i++);
					tempScoreModel.setGameMode(cursor.getString(cursor.getColumnIndex("gamemode")));
					tempScoreModel.setGameScore(cursor.getLong(cursor.getColumnIndex("score")));
					tempScoreModel.setPlayerName(" "+cursor.getString(cursor.getColumnIndex("playername")));
					scoreArrayList.add(tempScoreModel);					
					
				} while (cursor.moveToNext());
				
			}
		}
		
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
