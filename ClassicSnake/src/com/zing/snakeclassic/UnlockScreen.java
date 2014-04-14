package com.zing.snakeclassic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zing.snakeclassic.EarnCredit.CreditTask;

public class UnlockScreen extends Activity implements OnClickListener, FlurryAdListener {

	ImageView u_imgbtn_name, u_imgbtn_preview, u_imgbtn_unlock;
	Intent intent;
	String unlock_keyword;
	RelativeLayout us_bgrd_theme_control;
	SharedPreferences prefs;
	Editor editor;
	FrameLayout mBanner;
	String credit_balance;
	JSONObject jsonObject;
	int creditBal;
	int beachUnlockedFlag = 0;
	int nightUnlockedFlag = 0;
	int gardenUnlockedFlag = 0;
	int holeUnlockedFlag = 0;
	int squareUnlockedFlag = 0;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.unlock);
				String android_id = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);		
		// Get credit balance
		//String url = "http://zingapps.co/get_balance_android.php?android_id=3a6ce377d396eacf";
		//String url = "http://zingapps.co/get_balance_android.php?android_id=" + android_id; 
		//String credit_balance = Utility.connect(url);
		//credit_balance = credit_balance.trim();
		//creditBal = Integer.parseInt(credit_balance);
		
		u_imgbtn_name = (ImageView) findViewById(R.id.u_imgbtn_name);
		u_imgbtn_preview = (ImageView) findViewById(R.id.u_imgbtn_preview);
		u_imgbtn_unlock = (ImageView) findViewById(R.id.u_imgbtn_unlock);
		u_imgbtn_unlock.setOnClickListener(this);
		intent = getIntent();
		unlock_keyword = intent.getExtras().getString("unlock");
		setUI(unlock_keyword);
		u_imgbtn_name.setOnClickListener(this);
		
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		us_bgrd_theme_control = (RelativeLayout) findViewById(R.id.us_bgrd_theme_control);
		setTheme();
		mBanner = (FrameLayout) findViewById(R.id.us_banner);

		FlurryAds.setAdListener(this);
		
		/*boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
		boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
		boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
		boolean holeUnlocked = prefs.getBoolean("holeUnlocked", false);
		boolean squareUnlocked = prefs.getBoolean("squareUnlocked", false);
		
		if(beachUnlocked){ beachUnlockedFlag = 1; }
		if(nightUnlocked){ nightUnlockedFlag = 1; }
		if(gardenUnlocked){ gardenUnlockedFlag = 1; }
		if(holeUnlocked){ holeUnlockedFlag = 1; }
		if(squareUnlocked){ squareUnlockedFlag = 1; }
		*/
		//creditBal = creditBal - ( (beachUnlockedFlag * 20) + (nightUnlockedFlag * 20) + (gardenUnlockedFlag * 20) + (holeUnlockedFlag *30) + (squareUnlockedFlag * 30));
		creditBal = prefs.getInt("usercredits", 0);
		String user_credits = creditBal + " Cr";
		TextView textView = (TextView) this.findViewById(R.id.usercredits);
		textView.setText(String.valueOf(user_credits));
		
	}
	
	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			us_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			us_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			us_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			us_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	private void setUI(String unlock_keyword2) {
		// TODO Auto-generated method stub
		if (unlock_keyword.equals("BEACH")) {
			u_imgbtn_name.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.txt_beach));
			u_imgbtn_preview.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.unlock_theme2));
		} else if (unlock_keyword.equals("GARDEN")) {
			u_imgbtn_name.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.txt_garden));
			u_imgbtn_preview.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.unlock_theme1));

		} else if (unlock_keyword.equals("NIGHT")) {
			u_imgbtn_name.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.txt_night));
			u_imgbtn_preview.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.unlock_theme3));

		} else if (unlock_keyword.equals("HOLE")) {
			u_imgbtn_name.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.txt_hole));
			u_imgbtn_preview.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.unlock_hole));

		} else if (unlock_keyword.equals("SQUARE")) {
			u_imgbtn_name.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.txt_foursquare));
			u_imgbtn_preview.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.unlock_square));

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.u_imgbtn_unlock:
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			u_imgbtn_unlock.setImageResource(R.drawable.main_unlock_selected);
			//intent = new Intent(UnlockScreen.this, UseCredit.class);
			//startActivity(intent);
				alertDialog = new AlertDialog.Builder(this).create();
				if(unlock_keyword.equals("BEACH")){
					if(creditBal >= 20){
					alertDialog.setTitle("Unlock Beach");
					alertDialog.setMessage("Are you sure?");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							editor = prefs.edit();
							editor.putBoolean("beachUnlocked", true);
							editor.commit();
							Intent intent = getIntent();
						    finish();
						    startActivity(intent);
						}
					});
					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
					}
					else{
						alertDialog.setTitle("Unlock Beach");
						alertDialog.setMessage("You need to earn credits first");
						alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								i = new Intent(getApplicationContext(), EarnCredit.class);
								startActivity(i);
							}
						});
						alertDialog.show();
					}
			}
				else if(unlock_keyword.equals("GARDEN")){
					if(creditBal >= 20){
						alertDialog.setTitle("Unlock garden");
						alertDialog.setMessage("Are you sure?");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								editor = prefs.edit();
								editor.putBoolean("gardenUnlocked", true);
								editor.commit();
								Intent intent = getIntent();
							    finish();
							    startActivity(intent);
							}
						});
						//alertDialog.setIcon(R.drawable.icon);
						alertDialog.show();
						}
						else{
							alertDialog.setTitle("Unlock Garden");
							alertDialog.setMessage("Need to earn credits first");
							alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									i = new Intent(getApplicationContext(), EarnCredit.class);
									startActivity(i);
								}
							});
							alertDialog.show();
						}
				}
				else if(unlock_keyword.equals("NIGHT")){
					if(creditBal >= 20){
						alertDialog.setTitle("Unlock NIGHT");
						alertDialog.setMessage("Are you sure?");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								editor = prefs.edit();
								editor.putBoolean("nightUnlocked", true);
								editor.commit();
								Intent intent = getIntent();
							    finish();
							    startActivity(intent);
							}
						});
						//alertDialog.setIcon(R.drawable.icon);
						alertDialog.show();
						}
						else{
							alertDialog.setTitle("Unlock Night");
							alertDialog.setMessage("Need to earn credits first");
							alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									i = new Intent(getApplicationContext(), EarnCredit.class);
									startActivity(i);
								}
							});
							alertDialog.show();
						}
				}
				else if(unlock_keyword.equals("HOLE")){
					if(creditBal >= 30){
					alertDialog.setTitle("Unlock Hole");
					alertDialog.setMessage("Are you sure?" + creditBal);
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							editor = prefs.edit();
							editor.putBoolean("holeUnlocked", true);
							editor.commit();
							Intent intent = getIntent();
						    finish();
						    startActivity(intent);
						}
					});
					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
					}
					else{
						alertDialog.setTitle("Unlock Hole");
						alertDialog.setMessage("Need to earn credits first");
						alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								i = new Intent(getApplicationContext(), EarnCredit.class);
								startActivity(i);
							}
						});
						alertDialog.show();
					}
			}
			else if(unlock_keyword.equals("SQUARE")){
				if(creditBal >= 30){
				alertDialog.setTitle("Unlock square");
				alertDialog.setMessage("Are you sure?" + credit_balance);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
					editor = prefs.edit();
					editor.putBoolean("squareUnlocked", true);
					editor.commit();
				
					}
				});
				//alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
				}
				else{
					alertDialog.setTitle("Unlock Square");
					alertDialog.setMessage("Need to earn credits first");
					alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							i = new Intent(getApplicationContext(), EarnCredit.class);
							startActivity(i);
						}
					});
					alertDialog.show();
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		u_imgbtn_unlock.setImageResource(R.drawable.snakeclassic_unlock);
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
