package com.zing.snakeclassic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.database.DataHelper;
import com.android.socialnetwork.facebook.AndroidFacebook;
import com.android.socialnetwork.facebook.FacebookSessionStore;
import com.android.utils.ClassicSnakeConstants;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class GameOver extends Activity implements FlurryAdListener {
	private SharedPreferences prefs;
	RelativeLayout go_bgrd_theme_control;

	TextView txtFinalScore;
	ImageView imgview_menu, imgBtnSharefb,imgBtnShareTwitter,imgview_playagain;
	TextView txthighScore;

	DataHelper dh;
	long gameScore;
	String gameMode;
	EditText editPlayerName;
	AndroidFacebook androidFacebook;
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
		setContentView(R.layout.gameover);
		dh = new DataHelper(this);
		gameScore = SnakeView.mScore;		
		if (ClassicSnakeConstants.GAME_PLAY_MODE == 1){
			gameMode = "classic";
		}
		else{
			gameMode = "extreme";
		}

		editPlayerName = (EditText) findViewById(R.id.editPlayerName);
		txtFinalScore = (TextView) findViewById(R.id.txtfinalScore);
		txtFinalScore.setText(String.valueOf(SnakeView.mScore));
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		String playerName = prefs.getString("player_name", "Player1");
		editPlayerName.setText(playerName);
		go_bgrd_theme_control = (RelativeLayout) findViewById(R.id.go_bgrd_theme_control);
		setTheme();
		imgview_menu = (ImageView) findViewById(R.id.imgview_menu);
		imgview_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgview_menu.setImageResource(R.drawable.menu_selected);
				if (gameScore != 0 && dh.checkValue(gameScore)) {
					dh.Insert(editPlayerName.getText().toString(), gameMode,
							gameScore);
				}
				Intent i = new Intent(GameOver.this, MenuScreen.class);
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				GameOver.this.finish();
			}
		});

		if (androidFacebook == null)
			androidFacebook = new AndroidFacebook();
		
		imgview_playagain = (ImageView) findViewById(R.id.imgview_playagain);
		imgview_playagain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgview_playagain.setImageResource(R.drawable.playagain_selected);
				if (gameScore != 0 && dh.checkValue(gameScore)) {
					dh.Insert(editPlayerName.getText().toString(), gameMode,
							gameScore);
				}
				if (ClassicSnakeConstants.GAME_PLAY_MODE == 2){
					ClassicSnakeConstants.GAME_LEVEL = 1;
				}
				Intent i = new Intent(GameOver.this, GameScreen.class);
				startActivity(i);
			}
		});

		imgBtnSharefb = (ImageView) findViewById(R.id.imgBtnSharefb);
		imgBtnSharefb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				postStatusUpdate();
			}

		});

		imgBtnShareTwitter = (ImageView) findViewById(R.id.imgBtnShareTwitter);
		imgBtnShareTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Twitter twitter = TwitterFactory.getSingleton();
			    Status status;
				try {
					status = twitter.updateStatus("my high score");
					System.out.println("Successfully updated the status to [" + status.getText() + "].");
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    */
				
				postTwitterStatusUpdate();
				
			}

		});

		txthighScore = (TextView) findViewById(R.id.txthighScore);
		txthighScore.setText(String.valueOf(dh.selectMax()));

		mBanner = (FrameLayout) findViewById(R.id.go_banner);

		FlurryAds.setAdListener(this);
	}

	private void postStatusUpdate() {

		FacebookSessionStore.restore(androidFacebook, this);
		if (androidFacebook.isSessionValid())
			performPublish();
		else
			// send -1 as an activity code value so it will open dialog
			androidFacebook.authorize(this, -1, new DialogListener() {

				@Override
				public void onFacebookError(FacebookError e) {
					if (e != null && e.getMessage() != null)
						Toast.makeText(GameOver.this,
								getString(R.string.fb_post_error),
								Toast.LENGTH_LONG).show();

				}

				@Override
				public void onError(DialogError e) {
					if (e != null && e.getMessage() != null)
						Toast.makeText(GameOver.this,
								getString(R.string.fb_post_error),
								Toast.LENGTH_LONG).show();
				}

				@Override
				public void onComplete(Bundle values) {
					FacebookSessionStore.save(androidFacebook, GameOver.this);
					performPublish();
				}

				@Override
				public void onCancel() {
					System.out.println("cancelled");
				}
			});

	}

	private void performPublish() {
		// TODO Auto-generated method stub
		String shareText, shareLink;
		shareText = "I just scored " + gameScore + " in Snake Classic!";
		shareLink = "http://itunes.apple.com";

		Bundle mShareBundle = new Bundle();
		mShareBundle.putString(getString(R.string.facebookShareBundleKeyName),
				shareText);
		String linkUrl = shareLink;
		if (StringUtils.isNotEmpty(linkUrl)) {
			mShareBundle.putString(
					getString(R.string.facebookShareBundleKeyLink), linkUrl);
		}
		mShareBundle
				.putString(getString(R.string.facebookShareBundleKeyCaption),
						"BEAT THAT!");
		mShareBundle.putString(
				getString(R.string.facebookShareBundleKeyDescription),
				"More Info: Snake Classic for Android");
		mShareBundle.putString(
				getString(R.string.facebookShareBundleKeyPicture), "");

		androidFacebook.dialog(this, "feed", mShareBundle,
				new DialogListener() {

					@Override
					public void onFacebookError(FacebookError e) {
						if (e != null && e.getMessage() != null)
							Toast.makeText(GameOver.this,
									getString(R.string.fb_post_error),
									Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(DialogError e) {
						if (e != null && e.getMessage() != null)
							Toast.makeText(GameOver.this,
									getString(R.string.fb_post_error),
									Toast.LENGTH_LONG).show();
					}

					@Override
					public void onComplete(Bundle values) {
						if (values.containsKey("post_id"))
							Toast.makeText(GameOver.this,
									getString(R.string.fb_post_posted),
									Toast.LENGTH_LONG).show();

					}

					@Override
					public void onCancel() {

					}
				});

	}
	private void postTwitterStatusUpdate(){
		String tweetUrl = "https://twitter.com/intent/tweet?text=I Just Scored " +gameScore+" in Snake Classic! &url="
            + "http://bit.ly/c26ZIw";
			Uri uri = Uri.parse(tweetUrl);	
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	/*private void postTwitterStatusUpdate() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("qbOcFT2SmkVJ5JKNLpq1jg")
		  .setOAuthConsumerSecret("W3zanQvRhXx1rT9rsOJqlpIrwu1slvUbVabB4c2wDc")
		  .setOAuthAccessToken("43354656-CXLtCNt2XDqHRjJWOE5xGLZFAiyqWVQh1knclqkw")
		  .setOAuthAccessTokenSecret("mvqyuNTBFPMSavDESdhd5XQIMOnQwsoY5Dd5epIpYY");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		try {
         //   Twitter twitter = new TwitterFactory().getInstance();
            try {
                // get request token.
                // this will throw IllegalStateException if access token is already available
                RequestToken requestToken = twitter.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    System.exit(-1);
                }
            }
            
            Status status = twitter.updateStatus("I scored 100");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            //System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
		
	}
	*/
	
	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			go_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
		} else if (theme.equals("GARDEN")) {
			go_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));

		} else if (theme.equals("NIGHT")) {
			go_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));

		} else if (theme.equals("BEACH")) {
			go_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTheme();
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
