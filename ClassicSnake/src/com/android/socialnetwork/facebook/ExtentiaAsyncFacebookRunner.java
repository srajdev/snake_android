package com.android.socialnetwork.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * 
 * 
 * Intermediate class, handle Async FB request and pass calls to
 * AsyncFacebookRunner fb class. This sample implementation simply spawns a new
 * thread for each request, and makes the API call immediately. This may work in
 * many applications, but more sophisticated users may re-implement this
 * behavior using a thread pool, a network thread, a request queue, or other
 * mechanism. Advanced functionality could be built, such as rate-limiting of
 * requests, as per a specific application's needs.
 * 
 * @author abhijeet.bhosale
 */
public class ExtentiaAsyncFacebookRunner extends AsyncFacebookRunner {

	/** The Constant SUCCESS. */
	private static final int SUCCESS = 1;

	/** The Constant NETWORK_ERROR. */
	private static final int NETWORK_ERROR = 2;

	/** The Constant FACEBOOK_ERROR. */
	private static final int FACEBOOK_ERROR = 3;

	/** The Constant FACEBOOK_ERROR. */
	private static final int FACEBOOK_CANCEL = 4;

	/** The listener. */
	FacebookPostListener listener;

	/**
	 * Instantiates a new extentiaasyncfacebook runner.
	 * 
	 * @param fb
	 *            the fb
	 */
	public ExtentiaAsyncFacebookRunner(Facebook fb) {
		super(fb);
	}

	/**
	 * Post message.
	 * 
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postMessage(String message, FacebookPostListener listener,
			Object object) {
		this.listener = listener;

		Bundle params = new Bundle();
		params.putString("message", message);

		super.request("/me/feed", params, "POST", requestListener, object);
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

	/**
	 * Post message.
	 * 
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postMessage(Bundle message, FacebookPostListener listener,
			Object object) {
		this.listener = listener;
		super.request("/me/feed", message, "POST", requestListener, object);
	}

	/**
	 * Post image to wall.
	 * 
	 * @param data
	 *            the data
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postImageToWall(byte[] data, String message,
			FacebookPostListener listener, Object object) {
		this.listener = listener;

		Bundle params = new Bundle();
		params.putString("message", message);
		params.putByteArray("facebookPictureData", data);

		super.request("/me/photos", params, "POST", requestListener, object);
	}

	/**
	 * Check in add.
	 * 
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void checkInAdd(Bundle message, FacebookPostListener listener,
			Object object) {
		this.listener = listener;
		super.request("me/checkins", message, "POST", requestListener, object);
	}

	/**
	 * Post message to friend wall.
	 * 
	 * @param friendUid
	 *            the friend uid
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postMessageToFriendWall(String friendUid, String message,
			FacebookPostListener listener, Object object) {
		this.listener = listener;

		Bundle params = new Bundle();
		params.putString("message", message);

		super.request(((friendUid == null) ? "me" : friendUid) + "/feed",
				params, "POST", requestListener, object);
	}

	/**
	 * Post message to friend wall.
	 * 
	 * @param friendUid
	 *            the friend uid
	 * @param message
	 *            the message
	 * @param listener
	 *            the listener
	 * @param object
	 *            the object
	 */
	public void postMessageToFriendWall(String friendUid, Bundle message,
			FacebookPostListener listener, Object object) {
		this.listener = listener;

		super.request(((friendUid == null) ? "me" : friendUid) + "/feed",
				message, "POST", requestListener, object);
	}

	/** The request listener. */
	private RequestListener requestListener = new RequestListener() {

		@Override
		public void onComplete(String response, Object state) {
			String error = null;
			String message = "Facebook error";
			Message msg = new Message();
			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject.has("error")) {
					error = jsonObject.getString("error");
					JSONObject jsonObject1 = new JSONObject(error);
					if (jsonObject1.has("message"))
						message = jsonObject1.getString("message");
					Log.d("JSON Error", error);
					msg.what = FACEBOOK_ERROR;
					msg.obj = message;
					handler.sendMessage(msg);
				} else {
					msg.what = SUCCESS;
					msg.obj = state;
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Message msg = new Message();
			msg.what = FACEBOOK_ERROR;
			msg.obj = state;
			Log.d("JSon Error msg", msg.what + msg.obj.toString() + "");
			handler.sendMessage(msg);
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Message msg = new Message();
			msg.what = NETWORK_ERROR;
			msg.obj = state;
			handler.sendMessage(msg);
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Message msg = new Message();
			msg.what = NETWORK_ERROR;
			msg.obj = state;
			handler.sendMessage(msg);
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Message msg = new Message();
			msg.what = NETWORK_ERROR;
			msg.obj = state;
			handler.sendMessage(msg);
		}

	};


	/** The handler. */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Object state = msg.obj;

			switch (msg.what) {
			case SUCCESS:
				listener.onSuccess(state);
				break;
			case NETWORK_ERROR:
				listener.onNetworkError(state);
				break;
			case FACEBOOK_ERROR:
				listener.onFacebookError(state);
				break;
			case FACEBOOK_CANCEL:
				listener.onFacebookError(state);
				break;
			}
		}
	};

	/**
	 * The listener interface for receiving facebookPost events. The class that
	 * is interested in processing a facebookPost event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addFacebookPostListener<code> method. When
	 * the facebookPost event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @author abhijeet.bhosale FacebookPostListener Listen response after
	 *         posting message on FB.
	 */
	public static interface FacebookPostListener {

		/**
		 * On success.
		 * 
		 * @param state
		 *            the state
		 */
		public void onSuccess(Object state);

		/**
		 * On network error.
		 * 
		 * @param state
		 *            the state
		 */
		public void onNetworkError(Object state);

		/**
		 * On facebook error.
		 * 
		 * @param state
		 *            the state
		 */
		public void onFacebookError(Object state);
	}
}
