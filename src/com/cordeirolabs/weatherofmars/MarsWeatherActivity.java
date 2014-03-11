package com.cordeirolabs.weatherofmars;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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


				MarsWeatherReport latest = MarsWeatherReport.fromJson(json);

				Log.d("MARS!", latest.toString());

				updateWeatherView(latest);

				Log.d("MARS!", "THIS RUNS THREE!");

			}

			@Override
			public void onFailure ( Throwable e, JSONObject errorResponse ) {
				String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
				new AlertDialog.Builder(MarsWeatherActivity.this).setMessage(msg).setCancelable(false).setTitle("1").setPositiveButton("OK", null).create().show();
			}

			@Override
			public void onFailure ( Throwable e, JSONArray errorResponse ) {
				String msg = "Array *" + e.toString() + "*" + errorResponse.toString();
				new AlertDialog.Builder(MarsWeatherActivity.this).setMessage(msg).setCancelable(false).setTitle("2").setPositiveButton("OK", null).create().show();
			}

			@Override
			public void onFailure ( Throwable e, String errorResponse ) {

				// Display an alert crouton
				Crouton.makeText(MarsWeatherActivity.this, R.string.error, Style.ALERT).show();

			}

		});



	}

	// Update the UI with values from a MarsWeatherReport
	public void updateWeatherView(MarsWeatherReport report) {

		Log.d("MARS!", "UPDATE!");

		// Get references to the UI widgets
		TextView txtCurrentTemperature = (TextView)findViewById(R.id.txtCurrentTemperature);
		TextView txtHighTempValue = (TextView)findViewById(R.id.txtHighTempValue);
		TextView txtLowTempValue = (TextView)findViewById(R.id.txtLowTempValue);
		TextView txtWeatherStatusValue = (TextView)findViewById(R.id.txtWeatherStatusValue);
		TextView txtSunriseValue = (TextView)findViewById(R.id.txtSunriseValue);
		TextView txtSunsetValue = (TextView)findViewById(R.id.txtSunsetValue);

		// Update the UI with the values from the MarsWeatherReport
		txtCurrentTemperature.setText(report.getMaxCelsiusTemp() + " C");
		txtHighTempValue.setText(report.getMaxCelsiusTemp() + " C");
		txtLowTempValue.setText(report.getMinCelsiusTemp() + " C");
		txtWeatherStatusValue.setText(report.getWeatherStatus());
		txtSunriseValue.setText(report.getSunrise());
		txtSunsetValue.setText(report.getSunset());

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mars_weather, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		
		Crouton.cancelAllCroutons();
		
		
	}

}
