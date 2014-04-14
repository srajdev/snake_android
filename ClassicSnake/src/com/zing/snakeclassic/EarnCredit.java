package com.zing.snakeclassic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class EarnCredit extends Activity implements FlurryAdListener  {
	RelativeLayout ec_bgrd_theme_control;
	private SharedPreferences prefs;
	ImageView u_imgbtn_preview;
	ImageView u_imgbtn_refresh;
	JSONObject jsonObject;
	JSONArray json_recommend;
	JSONObject json_obj;
	String response_result;
	FrameLayout mBanner;
	int creditBal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.earncredit);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		ec_bgrd_theme_control = (RelativeLayout) findViewById(R.id.ec_bgrd_theme_control);
		setTheme();
		u_imgbtn_preview = (ImageView) findViewById(R.id.u_imgbtn_preview);
		u_imgbtn_preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW);
				try {
					i.setData(Uri.parse(json_obj.getString("@actionUrl")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(i);
			}
		});
		u_imgbtn_refresh = (ImageView) findViewById(R.id.u_imgbtn_refresh);
		u_imgbtn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				u_imgbtn_refresh.setImageResource(R.drawable.earncredits_refresh_selected);
				Intent intent = getIntent();
			    finish();
			    startActivity(intent);
			}
		});
		// Start Async task
		CreditTask creditTask = new CreditTask();
		creditTask.execute();
		
		creditBal = prefs.getInt("usercredits", 0);
		String user_credits = creditBal + " Cr";
		TextView textView = (TextView) this.findViewById(R.id.usercredits);
		textView.setText(String.valueOf(user_credits));
		
		mBanner = (FrameLayout) findViewById(R.id.ec_banner);

		FlurryAds.setAdListener(this);

	}

	private void setTheme() {
		// TODO Auto-generated method stub

		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			ec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			ec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			ec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			ec_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, ClassicSnakeConstants.flurryKey);
		//FlurryAds.enableTestAds(true);
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

	public String getAndroidIP() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.i("externalip", ex.toString());
		}
		return null;
	}

	class CreditTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog pd;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String android_id = Secure.getString(getContentResolver(),
					Secure.ANDROID_ID);

			String ipAddress = getAndroidIP();

			String url = "http://api.flurry.com/appCircle/v2/getRecommendations?apiAccessCode="
					+ ClassicSnakeConstants.flurryAccessCode
					+ "&"
					+ "apiKey="
					+ ClassicSnakeConstants.flurryKey
					+ "&"
					+ "androidId="
					+ android_id
					+ "&"
					+ "platform="
					+ "AND&"
					+ "ipAddress="
					+ ipAddress.replace("%","");
			response_result = Utility.connect(url);

			
			// Get credit balance
			
			url = "http://zingapps.co/get_balance_android.php?android_id=" + android_id; 
			String credit_balance = Utility.connect(url);
			
			//http://zingapps.co/get_balance_android.php?android_id=3a6ce377d396eacf
			 
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(EarnCredit.this);
			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			// Instantiate a JSON object from the request response
			try {
				jsonObject = new JSONObject(response_result);
				/*json_recommend = jsonObject
						.getJSONObject("recommendation");*/
				json_recommend = jsonObject.getJSONArray("recommendation");
				json_obj =json_recommend.getJSONObject(0);
				URL image_url = new URL(json_obj.getString("@appIconUrl"));
				Bitmap bmp = BitmapFactory.decodeStream(image_url
						.openConnection().getInputStream());
				u_imgbtn_preview.setImageBitmap(bmp);				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
