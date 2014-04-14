package com.android.socialnetwork.facebook;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.utils.ClassicSnakeConstants;
import com.facebook.android.Facebook;

/**
 * 
 * 
 * Intermediate class. Extends Facebook class of Facebook Android LIB.
 * interacting with the Facebook developer API Provides methods to log in and
 * log out a user, make requests using the REST and Graph APIs, and start user
 * interface interactions with the API (such as pop-ups promoting for
 * credentials, permissions, stream posts, etc.)
 * 
 * @author abhijeet.bhosale
 */

public class AndroidFacebook extends Facebook {

	/**
	 * list of Facebook permission. needed for application.
	 */
	private String[] PERMISSIONS = new String[] { "offline_access",
			"publish_stream", "read_stream" ,"email"};

	public AndroidFacebook() {
		super(ClassicSnakeConstants.FACEBOOK_APP_ID);
		
	}

	@Override
	public void authorize(Activity activity, DialogListener listener) {
		super.authorize(activity, PERMISSIONS, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Facebook#authorize(Activity, String[], int, Facebook.DialogListener)
	 */
	public void authorize(Activity activity, int activityCode,
			DialogListener listener) {
		super.authorize(activity, PERMISSIONS, activityCode, listener);
	}

	/**
	 * This method only clear the Facebook token locally but does not invalidate
	 * the Facebook token on the website
	 * 
	 * @param context
	 * @throws MalformedURLException
	 * @throws IOException
	 * 
	 */
	public void logoutLocally(Context context) throws MalformedURLException,
			IOException {
		setAccessToken(null);
		setAccessExpires(0);
	}

	/**
	 * Post message using fb dialog.
	 * 
	 * @param context
	 *            the context
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postMessageUsingFBDialog(Context context, Bundle message,
			DialogListener listener) {
		super.dialog(context, "/me/feed", message, listener);
	}

}
