package com.cordeirolabs.weatherofmars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MarsWeatherActivity extends Activity {
	private MarsWeatherClient client;
//	private static MarsWeatherReport latest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars_weather);

		client = new MarsWeatherClient();

		Log.d("MARS!", "THIS RUNS!");

		client.getLatestWeatherReport(new JsonHttpResponseHandler() {



			@Override
			public void onSuccess(int code, JSONObject json) {
				
				System.out.println(json.toString());

				Log.d("MARS!", "THIS RUNS TOO!");

				//				try {

				MarsWeatherReport latest = MarsWeatherReport.fromJson(json);
				
				Log.d("MARS!", latest.toString());

				updateWeatherView(latest);

				Log.d("MARS!", "THIS RUNS THREE!");

				//				} catch (JSONException e) {
				//					e.printStackTrace();
				//				}

			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				
				Log.d("MARS!", "WE FAILED!");
				
				arg0.printStackTrace();
				
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}
		});

	

	}


	public void updateWeatherView(MarsWeatherReport report) {
		
		Log.d("MARS!", "UPDATE!");

		// Get references to the UI widgets
		TextView tv = (TextView)findViewById(R.id.txtCurrentTemperature);
		tv.setText(report.getMaxCelsiusTemp() + " C");

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mars_weather, menu);
		return true;
	}

}
