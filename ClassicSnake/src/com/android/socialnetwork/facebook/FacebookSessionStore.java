package com.android.socialnetwork.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.facebook.android.Facebook;

/**
 * 
 * * FacebookSessionStore : Store facebook session, user can restore it by using
 * restore. clear fb session using Clear.
 * @author abhijeet.bhosale
 */
public class FacebookSessionStore {

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-session";

	/**
	 * 
	 * @param session
	 * @param context
	 * @return
	 * Save FB session in SharedPreferences.
	 */
	public static boolean save(Facebook session, Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, session.getAccessToken());
		editor.putLong(EXPIRES, session.getAccessExpires());
		return editor.commit();
	}
	/**
	 * 
	 * @param session
	 * @param context
	 * @return true if FB session is valid otherwise false.
	 */

	public static boolean restore(Facebook session, Context context) {
		Log.e("Context", "" + context);

		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		session.setAccessToken(savedSession.getString(TOKEN, null));
		session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
		return session.isSessionValid();
	}
/**
 * 
 * @param context
 * 
 * Clear FB session.
 */
	
	public static void clear(Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
		
		
		
	}
}
