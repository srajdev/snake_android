package com.zing.snakeclassic;

import com.android.utils.ClassicSnakeConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OptionDialog extends Activity implements OnClickListener {

	ImageView od_imgv_openfield, od_imgv_box, od_imgv_hole, od_imgv_foursquare;
	Intent intent;
	LinearLayout od_backgrnd;
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.optiondialog);
		od_imgv_openfield = (ImageView) findViewById(R.id.od_imgv_openfield);
		od_imgv_openfield.setOnClickListener(this);
		od_imgv_box = (ImageView) findViewById(R.id.od_imgv_box);
		od_imgv_box.setOnClickListener(this);
		od_imgv_hole = (ImageView) findViewById(R.id.od_imgv_hole);
		od_imgv_hole.setOnClickListener(this);
		od_imgv_foursquare = (ImageView) findViewById(R.id.od_imgv_foursquare);
		od_imgv_foursquare.setOnClickListener(this);
		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		od_backgrnd = (LinearLayout) findViewById(R.id.od_backgrnd);
		//setTheme();
		boolean holeUnlocked = prefs.getBoolean("holeUnlocked", false);
		if (holeUnlocked == true) {
			od_imgv_hole.setImageResource(R.drawable.txt_hole);
		}
		else{
			od_imgv_hole.setImageResource(R.drawable.hole_locked);
		}
		boolean squareUnlocked = prefs.getBoolean("squareUnlocked", false);
		if (squareUnlocked == true) {
			od_imgv_foursquare.setImageResource(R.drawable.txt_foursquare);
		}
		else{
			od_imgv_foursquare.setImageResource(R.drawable.square_locked);
		}
	}

/*	private void setTheme() {
		// TODO Auto-generated method stub

		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			od_backgrnd.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.dilaog_bgrd));
		} else if (theme.equals("GARDEN")) {
			od_backgrnd.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.unlock_theme1));

		} else if (theme.equals("NIGHT")) {
			od_backgrnd.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.unlock_theme3));

		} else if (theme.equals("BEACH")) {
			od_backgrnd.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.unlock_theme2));

		}

	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		intent = new Intent(OptionDialog.this, SpeedDialog.class);
		switch (v.getId()) {
		case R.id.od_imgv_openfield:
			ClassicSnakeConstants.GAME_LEVEL = 1;
			od_imgv_openfield.setImageResource(R.drawable.open_field_pressed);
			startActivity(intent);
			finish();
			break;
		case R.id.od_imgv_box:
			ClassicSnakeConstants.GAME_LEVEL = 2;
			od_imgv_box.setImageResource(R.drawable.box_pressed);
			startActivity(intent);
			finish();
			break;
		case R.id.od_imgv_hole:
			boolean holeUnlocked = prefs.getBoolean("holeUnlocked", false);
			if(holeUnlocked){
				ClassicSnakeConstants.GAME_LEVEL = 3;
			od_imgv_hole.setImageResource(R.drawable.hole_pressed);
			startActivity(intent);
			finish();
			break;
			}
		case R.id.od_imgv_foursquare:
			boolean squareUnlocked = prefs.getBoolean("squareUnlocked", false);
			if(squareUnlocked){
			ClassicSnakeConstants.GAME_LEVEL = 4;
			od_imgv_foursquare.setImageResource(R.drawable.square_pressed);
			startActivity(intent);
			finish();
			break;
			}
		default:
			break;
		}
	}

}
