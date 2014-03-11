package com.cordeirolabs.weatherofmars;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MarsWeatherClient {
	private final String API_BASE_URL= "http://marsweather.ingenology.com/v1/";
	private AsyncHttpClient client;
	
	public MarsWeatherClient() {
		
		Log.d("MARS!", "CLIENT ABOUT TO BE CREATED!");
		
		this.client = new AsyncHttpClient();
		
		Log.d("MARS!", "CLIENT JUST CREATED!");
	}
	
	 private String getApiUrl(String relativeUrl) {
	        return API_BASE_URL + relativeUrl;
	    }
	
	 // Gets the latest weather data from the MAAS API
	 // http://marsweather.ingenology.com/v1/latest/
	public void getLatestWeatherReport(JsonHttpResponseHandler handler) {
		String url = getApiUrl("latest");
		Log.d("MARS", "ABOUT TO GET!");
		client.get(url, handler);
		Log.d("MARS", "DID GET!");
	}

}
