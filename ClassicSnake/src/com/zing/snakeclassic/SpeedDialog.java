package com.zing.snakeclassic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.android.utils.ClassicSnakeConstants;

public class SpeedDialog extends Activity {

	ImageView sd_imgv_go;
	LinearLayout sd_dialog_bgrd;
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
		setContentView(R.layout.speeddialog);
		sd_imgv_go = (ImageView) findViewById(R.id.sd_imgv_go);
		sd_imgv_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sd_imgv_go.setImageResource(R.drawable.go_selected);
				Intent i = new Intent(SpeedDialog.this, GameScreen.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				ClassicSnakeConstants.GAME_MODE= 1;
				startActivity(i);
				SpeedDialog.this.finish();

			}
		});

		prefs = getSharedPreferences("Snake_color", Context.MODE_PRIVATE);
		sd_dialog_bgrd = (LinearLayout) findViewById(R.id.sd_dialog_bgrd);
		//setTheme();
		ClassicSnakeConstants.GAME_MOVEDELAY =75;
	}

/*	private void setTheme() {
		// TODO Auto-generated method stub
		String theme = prefs.getString("theme", "STANDARD");
		if (theme.equals("STANDARD")) {
			sd_dialog_bgrd.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.difficulty_classic));
		} else if (theme.equals("GARDEN")) {
			sd_dialog_bgrd.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.difficulty_garden));

		} else if (theme.equals("NIGHT")) {
			sd_dialog_bgrd.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.difficulty_night));

		} else if (theme.equals("BEACH")) {
			sd_dialog_bgrd.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.difficulty_beach));

		}

	}*/

	
	public void onRadioButtonClicked(View view) {
		
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        switch (view.getId()) {
		case R.id.sd_radiobtn1:
			if(checked){
				ClassicSnakeConstants.GAME_MOVEDELAY =75;
			}			
			break;
		case R.id.sd_radiobtn2:
			if(checked){
				ClassicSnakeConstants.GAME_MOVEDELAY =70;
			}		
			
			break;
		case R.id.sd_radiobtn3:
			if(checked){
				ClassicSnakeConstants.GAME_MOVEDELAY =60;
			}		
			
			break;
		case R.id.sd_radiobtn4:
			if(checked){
				ClassicSnakeConstants.GAME_MOVEDELAY =45;
			}		
			
			break;
		case R.id.sd_radiobtn5:
			if(checked){
				ClassicSnakeConstants.GAME_MOVEDELAY =30;
			}		
			break;

		default:
			break;
		}
		
	}
}
