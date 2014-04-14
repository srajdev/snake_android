package com.android.utils;


public class ClassicSnakeConstants {

	public static final long DEFAULT_SPLASH_SCREEN_TIME = 2000;
	
	public static final String flurryKey= "8XJQVC3K5DWHWFHJSXQC";
	public static final String flurryAccessCode = "38VUATYSLU5JZ8JP6WGY";
	
	public static final String FACEBOOK_APP_ID ="529174267151990" ;//"309129395850049";// "158392174179755";
	
	
	public static int GAME_LEVEL=1;
	public static int GAME_MODE=1;	
	public static long GAME_MOVEDELAY=75;
	public static int GAME_PLAY_MODE=1; //1= Classic , 2= Extreme 
	
	public static boolean gamePause= false;


	public static int head_r;
	public static int head_g;
	public static int head_b;

	public static int body_r;
	public static int body_g;
	public static int body_b;

	public static void setRGBValue(String snake_color) {

		if (snake_color.equals("GREEN")) {
			head_r = 33;
			head_g = 100;
			head_b = 3;
			body_r = 56;
			body_g = 167;
			body_b = 0;
		} else if (snake_color.equals("BLUE")) {
			head_r = 3;
			head_g = 106;
			head_b = 105;
			body_r = 0;
			body_g = 174;
			body_b = 171;

		} else if (snake_color.equals("ORANGE")) {
			head_r = 156;
			head_g = 90;
			head_b = 3;
			body_r = 255;
			body_g = 145;
			body_b = 1;

		} else if (snake_color.equals("YELLOW")) {
			head_r = 156;
			head_g = 132;
			head_b = 3;
			body_r = 255;
			body_g = 210;
			body_b = 0;

		}

	}	


}
