package com.zing.snakeclassic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.android.utils.ClassicSnakeConstants;
import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAppCloud;

public class OptionsMenu extends Activity implements OnClickListener,FlurryAdListener {

	// ImageView imgViewGreen, imgViewBlue, imgViewYellow, imgViewOrange;
	ImageView imgViewStandard, imgViewGarden, imgViewBeach, imgViewNight;
	SharedPreferences prefs;
	Editor editor;
	RelativeLayout om_bgrd_theme_control;
	EditText editview_playername;
	RadioButton om_color_radiobtn1, om_color_radiobtn2, om_color_radiobtn3,
			om_color_radiobtn4;
	RadioButton om_vibrate_radiobtn1, om_vibrate_radiobtn2;
	RadioButton om_sound_radiobtn1, om_sound_radiobtn2;
	FrameLayout mBanner;
	//Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.optionsmenu);

		/*
		 * imgViewBlue = (ImageView) findViewById(R.id.imgViewBlue);
		 * imgViewBlue.setOnClickListener(this); imgViewGreen = (ImageView)
		 * findViewById(R.id.imgViewGreen);
		 * imgViewGreen.setOnClickListener(this); imgViewOrange = (ImageView)
		 * findViewById(R.id.imgViewOrange);
		 * imgViewOrange.setOnClickListener(this); imgViewYellow = (ImageView)
		 * findViewById(R.id.imgViewYellow);
		 * imgViewYellow.setOnClickListener(this);
		 */
		imgViewStandard = (ImageView) findViewById(R.id.imgViewStandard);
		imgViewStandard.setOnClickListener(this);
		imgViewBeach = (ImageView) findViewById(R.id.imgViewBeach);
		imgViewBeach.setOnClickListener(this);
		imgViewGarden = (ImageView) findViewById(R.id.imgViewGarden);
		imgViewGarden.setOnClickListener(this);
		imgViewNight = (ImageView) findViewById(R.id.imgViewNight);
		imgViewNight.setOnClickListener(this);
		om_color_radiobtn1 = (RadioButton) findViewById(R.id.om_color_radiobtn1);
		om_color_radiobtn2 = (RadioButton) findViewById(R.id.om_color_radiobtn2);
		om_color_radiobtn3 = (RadioButton) findViewById(R.id.om_color_radiobtn3);
		om_color_radiobtn4 = (RadioButton) findViewById(R.id.om_color_radiobtn4);

		om_vibrate_radiobtn1 = (RadioButton) findViewById(R.id.om_vibrate_radiobtn1);
		om_vibrate_radiobtn2 = (RadioButton) findViewById(R.id.om_vibrate_radiobtn2);
		
		om_sound_radiobtn1 = (RadioButton) findViewById(R.id.om_sound_radiobtn1);
		om_sound_radiobtn2 = (RadioButton) findViewById(R.id.om_sound_radiobtn2);

		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		om_bgrd_theme_control = (RelativeLayout) findViewById(R.id.om_bgrd_theme_control);
		editview_playername = (EditText) findViewById(R.id.editview_playername);
		applyPrevSettings();
		
		mBanner = (FrameLayout) findViewById(R.id.om_banner);

		FlurryAds.setAdListener(this);

	}

	private void applyPrevSettings() {
		// TODO Auto-generated method stub
		// Set Player Name
		editview_playername.setText(prefs.getString("player_name", "Player1"));
		// Set theme
		setTheme();
		// Set color
		String selected_color = prefs.getString("snake_color", "GREEN");
		if (selected_color.equals("GREEN")) {
			om_color_radiobtn1.setChecked(true);
		} else if (selected_color.equals("ORANGE")) {
			om_color_radiobtn2.setChecked(true);

		} else if (selected_color.equals("YELLOW")) {
			om_color_radiobtn3.setChecked(true);

		} else if (selected_color.equals("BLUE")) {
			om_color_radiobtn4.setChecked(true);
		}
		// Set vibrate settings
		boolean vibrateValue = prefs.getBoolean("snake_vibrate", false);
		if (vibrateValue)
			om_vibrate_radiobtn1.setChecked(true);
		else
			om_vibrate_radiobtn2.setChecked(true);
		//Set sound settings
		boolean soundValue = prefs.getBoolean("snake_sound", false);
		if (soundValue)
			om_sound_radiobtn1.setChecked(true);
		else
			om_sound_radiobtn2.setChecked(true);
		
		//imgViewBeach = (ImageView) findViewById(R.id.imgViewBeach);
		/*boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
		if (beachUnlocked == true) {
			imgViewBeach.setImageResource(R.drawable.txt_beach);
		}
		else{
			imgViewBeach.setImageResource(R.drawable.lock);
		}
		boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
		if (gardenUnlocked == true) {
			imgViewGarden.setImageResource(R.drawable.txt_garden);
		}
		else{
			imgViewGarden.setImageResource(R.drawable.lock);
		}
		boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
		if (nightUnlocked == true) {
			imgViewNight.setImageResource(R.drawable.txt_night);
		}
		else{
			imgViewNight.setImageResource(R.drawable.lock);
		}
		*/
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		editor = prefs.edit();
		editor.putString("player_name", editview_playername.getText()
				.toString());
		editor.commit();
	}

	public void onRadioButtonClicked(View view) {

		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.om_sound_radiobtn1:
			editor = prefs.edit();
			editor.putBoolean("snake_sound", true);
			editor.commit();
			break;
		case R.id.om_sound_radiobtn2:
			editor = prefs.edit();
			editor.putBoolean("snake_sound", false);
			editor.commit();
			break;
		case R.id.om_vibrate_radiobtn1:
			editor = prefs.edit();
			editor.putBoolean("snake_vibrate", true);
			editor.commit();
			break;
		case R.id.om_vibrate_radiobtn2:
			editor = prefs.edit();
			editor.putBoolean("snake_vibrate", false);
			editor.commit();
			break;
		case R.id.om_color_radiobtn1:
			if (checked) {
				editor = prefs.edit();
				editor.putString("snake_color", "GREEN");
				editor.commit();
			}
			break;
		case R.id.om_color_radiobtn2:
			if (checked) {
				editor = prefs.edit();
				editor.putString("snake_color", "ORANGE");
				editor.commit();
			}
			break;
		case R.id.om_color_radiobtn3:
			if (checked) {
				editor = prefs.edit();
				editor.putString("snake_color", "YELLOW");
				editor.commit();
			}

			break;
		case R.id.om_color_radiobtn4:
			if (checked) {
				editor = prefs.edit();
				editor.putString("snake_color", "BLUE");
				editor.commit();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*
		 * case R.id.imgViewBlue: editor = prefs.edit();
		 * editor.putString("snake_color", "BLUE"); editor.commit(); break; case
		 * R.id.imgViewGreen: editor = prefs.edit();
		 * editor.putString("snake_color", "GREEN"); editor.commit(); break;
		 * case R.id.imgViewOrange: editor = prefs.edit();
		 * editor.putString("snake_color", "ORANGE"); editor.commit(); break;
		 * case R.id.imgViewYellow: editor = prefs.edit();
		 * editor.putString("snake_color", "YELLOW"); editor.commit(); break;
		 */
		case R.id.imgViewBeach:
			boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
			if(beachUnlocked){
			//imgViewBeach.setBackgroundResource(R.drawable.theme_beach_selected);
			editor = prefs.edit();
			editor.putString("theme", "BEACH");
			editor.commit();
			setTheme();
			}
			/*else{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Alert");
				alertDialog.setMessage("You need to unlock the theme first");
				alertDialog.setButton("Earn Credits", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					intent = new Intent(OptionsMenu.this, EarnCredit.class);
					startActivity(intent);
				}
				});
				//alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
				
			}
			*/
			break;
		case R.id.imgViewStandard:
			editor = prefs.edit();
			editor.putString("theme", "STANDARD");
			editor.commit();
			setTheme();
			break;
		case R.id.imgViewNight:
			boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
			if(nightUnlocked){
			editor = prefs.edit();
			editor.putString("theme", "NIGHT");
			editor.commit();
			setTheme();
			}
			break;
		case R.id.imgViewGarden:
			boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
			if(gardenUnlocked){
			editor = prefs.edit();
			editor.putString("theme", "GARDEN");
			editor.commit();
			setTheme();
			}
			break;
		default:
			break;
		}

	}

	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			om_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.classic));
			imgViewStandard.setImageResource(R.drawable.theme_standard_selected);
			//imgViewBeach.setImageResource(R.drawable.txt_beach);
			//imgViewNight.setImageResource(R.drawable.txt_night);
			//imgViewGarden.setImageResource(R.drawable.txt_garden);
			boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
			if (beachUnlocked == true) {
				imgViewBeach.setImageResource(R.drawable.txt_beach);
			}
			else{
				imgViewBeach.setImageResource(R.drawable.beach_locked);
			}
			boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
			if (gardenUnlocked == true) {
				imgViewGarden.setImageResource(R.drawable.txt_garden);
			}
			else{
				imgViewGarden.setImageResource(R.drawable.garden_locked);
			}
			boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
			if (nightUnlocked == true) {
				imgViewNight.setImageResource(R.drawable.txt_night);
			}
			else{
				imgViewNight.setImageResource(R.drawable.night_locked);
			}

		} else if (theme.equals("GARDEN")) {
			om_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_garden));
			imgViewGarden.setImageResource(R.drawable.theme_garden_selected);
			//imgViewBeach.setImageResource(R.drawable.txt_beach);
			//imgViewNight.setImageResource(R.drawable.txt_night);
			//imgViewStandard.setImageResource(R.drawable.theme_standard);
			imgViewStandard.setImageResource(R.drawable.theme_standard);
			boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
			if (beachUnlocked == true) {
				imgViewBeach.setImageResource(R.drawable.txt_beach);
			}
			else{
				imgViewBeach.setImageResource(R.drawable.beach_locked);
			}
			boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
			if (nightUnlocked == true) {
				imgViewNight.setImageResource(R.drawable.txt_night);
			}
			else{
				imgViewNight.setImageResource(R.drawable.night_locked);
			}

		} else if (theme.equals("NIGHT")) {
			om_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_night));
			imgViewNight.setImageResource(R.drawable.theme_night_selected);
			imgViewStandard.setImageResource(R.drawable.theme_standard);
			//imgViewBeach.setImageResource(R.drawable.txt_beach);
			//imgViewGarden.setImageResource(R.drawable.txt_garden);
			//imgViewStandard.setImageResource(R.drawable.theme_standard);
			boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
			if (beachUnlocked == true) {
				imgViewBeach.setImageResource(R.drawable.txt_beach);
			}
			else{
				imgViewBeach.setImageResource(R.drawable.beach_locked);
			}
			boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
			if (gardenUnlocked == true) {
				imgViewGarden.setImageResource(R.drawable.txt_garden);
			}
			else{
				imgViewGarden.setImageResource(R.drawable.garden_locked);
			}
		} else if (theme.equals("BEACH")) {
			om_bgrd_theme_control.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.bgrd_beach));
			imgViewBeach.setImageResource(R.drawable.theme_beach_selected);
			//imgViewNight.setImageResource(R.drawable.txt_night);
			//imgViewGarden.setImageResource(R.drawable.txt_garden);
			//imgViewStandard.setImageResource(R.drawable.theme_standard);
			/*boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
			if (beachUnlocked == true) {
				imgViewBeach.setImageResource(R.drawable.txt_beach);
			}
			else{
				imgViewBeach.setImageResource(R.drawable.lock);
			}
			*/
			imgViewStandard.setImageResource(R.drawable.theme_standard);
			boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
			if (gardenUnlocked == true) {
				imgViewGarden.setImageResource(R.drawable.txt_garden);
			}
			else{
				imgViewGarden.setImageResource(R.drawable.garden_locked);
			}
			boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
			if (nightUnlocked == true) {
				imgViewNight.setImageResource(R.drawable.txt_night);
			}
			else{
				imgViewNight.setImageResource(R.drawable.night_locked);
			}

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
		boolean beachUnlocked = prefs.getBoolean("beachUnlocked", false);
		if (beachUnlocked == true) {
			imgViewBeach.setImageResource(R.drawable.txt_beach);
		}
		else{
			imgViewBeach.setImageResource(R.drawable.beach_locked);
		}
		boolean gardenUnlocked = prefs.getBoolean("gardenUnlocked", false);
		if (gardenUnlocked == true) {
			imgViewGarden.setImageResource(R.drawable.txt_garden);
		}
		else{
			imgViewGarden.setImageResource(R.drawable.garden_locked);
		}
		boolean nightUnlocked = prefs.getBoolean("nightUnlocked", false);
		if (nightUnlocked == true) {
			imgViewNight.setImageResource(R.drawable.txt_night);
		}
		else{
			imgViewNight.setImageResource(R.drawable.night_locked);
		}

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
