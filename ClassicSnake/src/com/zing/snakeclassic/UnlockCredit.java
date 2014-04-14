package com.zing.snakeclassic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

import com.android.utils.ClassicSnakeConstants;
import com.android.utils.Utility;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class UnlockCredit extends Activity implements OnClickListener,FlurryAdListener {

	ImageView cm_earn_credits, cm_use_credits;
	Intent intent;
	private SharedPreferences prefs;
	RelativeLayout cm_bgrd_theme_control;
	FrameLayout mBanner;
	String credit_balance;
	Editor editor;
	
	String user_credit_balance;
	int creditBal;
	int beachUnlockedFlag = 0;
	int nightUnlockedFlag = 0;
	int gardenUnlockedFlag = 0;
	int holeUnlockedFlag = 0;
	int squareUnlockedFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.credit_mode);
		cm_earn_credits = (ImageView) findViewById(R.id.cm_earn_credits);
		cm_earn_credits.setOnClickListener(this);
		cm_use_credits = (ImageView) findViewById(R.id.cm_use_credits);
		cm_use_credits.setOnClickListener(this);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		cm_bgrd_theme_control = (RelativeLayout) findViewById(R.id.cm_bgrd_theme_control);
		setTheme();
		String android_id = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);		
		String url = "http://zingapps.co/get_balance_android.php?android_id=3a6ce377d396eacf";
		//String url = "http://zingapps.co/get_balance_android.php?android_id=" + android_id; 
		//String credit_balance = Utility.connect(url);
		new GetBalance().execute(url);
		/*if(credit_balance != null){
			credit_balance = credit_balance.trim();
		creditBal = Integer.parseInt(credit_balance);
		}
		else{
			creditBal = 0;
		}
		boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
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
		
		editor = prefs.edit();
		editor.putInt("usercredits", creditBal);
		editor.commit();
		*/
		
		mBanner = (FrameLayout) findViewById(R.id.uc_banner);

		FlurryAds.setAdListener(this);
	}

	private void setTheme() {
		// TODO Auto-generated method stub
		
		String theme = prefs.getString("theme", "STANDARD");
		if(theme.equals("STANDARD")){
			cm_bgrd_theme_control.setBackgroundDrawable(getResources().getDrawable(R.drawable.classic));
		}else if(theme.equals("GARDEN")){
			cm_bgrd_theme_control.setBackgroundDrawable(getResources().getDrawable(R.drawable.bgrd_garden));
			
		}else if(theme.equals("NIGHT")){
			cm_bgrd_theme_control.setBackgroundDrawable(getResources().getDrawable(R.drawable.bgrd_night));
			
		}else if(theme.equals("BEACH")){
			cm_bgrd_theme_control.setBackgroundDrawable(getResources().getDrawable(R.drawable.bgrd_beach));
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cm_use_credits:
			cm_use_credits.setImageResource(R.drawable.store_usecredits_selected);
			intent = new Intent(UnlockCredit.this, UseCredit.class);
			startActivity(intent);

			break;
		case R.id.cm_earn_credits:
			cm_earn_credits.setImageResource(R.drawable.store_earncredits_selected);
			intent = new Intent(UnlockCredit.this, EarnCredit.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		cm_use_credits.setImageResource(R.drawable.use_credits);
		cm_earn_credits.setImageResource(R.drawable.earn_credits);
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
	
	private class GetBalance extends AsyncTask<String,String,String> {

		@Override
		protected String doInBackground(String... urls) {
			
		    HttpClient httpclient = new DefaultHttpClient();

		    String result=null;
	/*	    String encodedurl=null;
		    
		    try {
				encodedurl = URLEncoder.encode(url,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		    // Prepare a request object
		    HttpGet httpget = new HttpGet(urls[0]); 
		    
		    //Set Accept header
		    httpget.setHeader("Accept",  "application/json");

		    // Execute the request
		    HttpResponse response;
		    try {
		        response = httpclient.execute(httpget);
		        // Examine the response status
		        Log.i("Praeda",response.getStatusLine().toString());

		        // Get hold of the response entity
		        HttpEntity entity = response.getEntity();
		        // If the response does not enclose an entity, there is no need
		        // to worry about connection release

		        if (entity != null) {

		            // A Simple JSON Response Read
		            InputStream instream = entity.getContent();
		            result= convertStreamToString(instream);
		            // now you have the string representation of the HTML request
		            instream.close();
		        }


		    } catch (Exception e) {
		    	  Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		    
		    return result;
		}
		
		@Override
	    protected void onPostExecute(String result) {
			
			credit_balance = result;
			try{
			credit_balance = credit_balance.trim();
			creditBal = Integer.parseInt(credit_balance);
			boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
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
			if(creditBal < 0){
				creditBal = 0;
			}
			//creditBal = prefs.getInt("usercredits", 0);
			//String user_credits = creditBal + " Cr";
			//textView.setText(user_credits);
			editor = prefs.edit();
			editor.putInt("usercredits", creditBal);
			editor.commit();

			}
			catch(Exception e){
				 Log.e("log_tag", "Error in post request"+e.toString());
			}
	   }
		
		 private String convertStreamToString(InputStream is) {
			    /*
			     * To convert the InputStream to String we use the BufferedReader.readLine()
			     * method. We iterate until the BufferedReader return null which means
			     * there's no more data to read. Each line will appended to a StringBuilder
			     * and returned as String.
			     */
			    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			    StringBuilder sb = new StringBuilder();

			    String line = null;
			    try {
			        while ((line = reader.readLine()) != null) {
			            sb.append(line + "\n");
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    } finally {
			        try {
			            is.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			    return sb.toString();
			}

	}


}
