package com.zing.snakeclassic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.ClassicSnakeConstants;
import com.android.utils.Utility;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class UseCredit extends Activity implements OnClickListener,FlurryAdListener {

	ImageView btn_view_beach, btn_view_garden, btn_view_night;
	ImageView btn_view_hole, btn_view_square;
	Intent intent;
	TextView textView;
	SharedPreferences prefs;
	int creditBal = 0;
	int beachUnlockedFlag = 0;
	int nightUnlockedFlag = 0;
	int gardenUnlockedFlag = 0;
	int holeUnlockedFlag = 0;
	int squareUnlockedFlag = 0;
	String credit_balance = null;
	RelativeLayout usec_bgrd_theme_control;
	FrameLayout mBanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.usecredit);
		btn_view_beach = (ImageView) findViewById(R.id.btn_view_beach);
		btn_view_beach.setOnClickListener(this);
		btn_view_garden = (ImageView) findViewById(R.id.btn_view_garden);
		btn_view_garden.setOnClickListener(this);
		btn_view_night = (ImageView) findViewById(R.id.btn_view_night);
		btn_view_night.setOnClickListener(this);
		btn_view_hole = (ImageView) findViewById(R.id.btn_view_hole);
		btn_view_hole.setOnClickListener(this);
		btn_view_square = (ImageView) findViewById(R.id.btn_view_foursquare);
		btn_view_square.setOnClickListener(this);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		usec_bgrd_theme_control = (RelativeLayout) findViewById(R.id.usec_bgrd_theme_control);
		setTheme();
		String android_id = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);		
		String url = "http://zingapps.co/get_balance_android.php?android_id=3a6ce377d396eacf";
		
		//String url = "http://zingapps.co/get_balance_android.php?android_id=" + android_id; 
		//new GetBalance().execute(url);
		//credit_balance = Utility.connect("http://zingapps.co/get_balance_android.php?android_id=3a6ce377d396eacf");
	    //String credit_balance = phpScript(url);
		//if(credit_balance != ""){
		//credit_balance = credit_balance.trim();
		//creditBal = Integer.parseInt(credit_balance);
		//}
		
	/*	boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
		boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
		boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
		boolean holeUnlocked = prefs.getBoolean("holeUnlocked", false);
		boolean squareUnlocked = prefs.getBoolean("squareUnlocked", false);
		
		if(beachUnlocked){ beachUnlockedFlag = 1; }
		if(nightUnlocked){ nightUnlockedFlag = 1; }
		if(gardenUnlocked){ gardenUnlockedFlag = 1; }
		if(holeUnlocked){ holeUnlockedFlag = 1; }
		if(squareUnlocked){ squareUnlockedFlag = 1; }
		
		creditBal = creditBal - ( (beachUnlockedFlag * 20) + (nightUnlockedFlag * 20) + (gardenUnlockedFlag * 20) + (holeUnlockedFlag *30) + (squareUnlockedFlag * 30));
		//String user_credits = creditBal + "Cr";
		//TextView textView = (TextView) this.findViewById(R.id.usercredits);
		//textView.setText(String.valueOf(user_credits));
		if(creditBal < 0){
			creditBal = 0;
		}
		*/
		creditBal = prefs.getInt("usercredits", 0);
		String user_credits = creditBal + " Cr";
		textView = (TextView) this.findViewById(R.id.usercredits);
		textView.setText(String.valueOf(user_credits));
		
		textView = (TextView) this.findViewById(R.id.usercredits);
		mBanner = (FrameLayout) findViewById(R.id.uc_banner);

		FlurryAds.setAdListener(this);

	}

	
	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			usec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			usec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			usec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			usec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		intent = new Intent(UseCredit.this, UnlockScreen.class);
		switch (v.getId()) {
		case R.id.btn_view_beach:
			intent.putExtra("unlock", "BEACH");
			break;
		case R.id.btn_view_garden:
			intent.putExtra("unlock", "GARDEN");
			break;
		case R.id.btn_view_night:
			intent.putExtra("unlock", "NIGHT");
			break;
		case R.id.btn_view_hole:
			intent.putExtra("unlock", "HOLE");
			break;
		case R.id.btn_view_foursquare:
			intent.putExtra("unlock", "SQUARE");
			break;

		default:
			break;
		}

		startActivity(intent);

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
