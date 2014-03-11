package com.cordeirolabs.weatherofmars;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MarsWeatherActivity extends Activity {
	private MarsWeatherClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars_weather);
		
		client = new MarsWeatherClient();
		
	}

	
	public void updateWeatherView(MarsWeatherReport report) {
		
		// Get references to the UI widgets
		
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mars_weather, menu);
		return true;
	}

}
