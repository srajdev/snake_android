package com.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class Utility extends AsyncTask<String,String,String> {

	@Override
	protected String doInBackground(String... urls) {
		
	    HttpClient httpclient = new DefaultHttpClient();

	    String result=null;
/*	    String encodedurl=null;
	    
	    try {
			encodedurl = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	    // Prepare a request object
	    HttpGet httpget = new HttpGet(urls[0]); 
	    
	    //Set Accept header
	    httpget.setHeader("Accept",  "application/json");

	    // Execute the request
	    HttpResponse response;
	    try {
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i("Praeda",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close();
	        }


	    } catch (Exception e) {
	    	  Log.e("log_tag", "Error in http connection"+e.toString());
	    }
	    
	    return result;
	}
	
	//@Override
    //protected void onPostExecute(String result) {
    //    result;
   //}

	public static String connect(String url)
	{

	    HttpClient httpclient = new DefaultHttpClient();

	    String result=null;
/*	    String encodedurl=null;
	    
	    try {
			encodedurl = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	    // Prepare a request object
	    HttpGet httpget = new HttpGet(url); 
	    
	    //Set Accept header
	    httpget.setHeader("Accept",  "application/json");

	    // Execute the request
	    HttpResponse response;
	    try {
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i("Praeda",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close();
	        }


	    } catch (Exception e) {
	    	  Log.e("log_tag", "Error in http connection"+e.toString());
	    }
	    
	    return result;
		
	}	

	    private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

		

}
