package com.cordeirolabs.weatherofmars;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

//TODO: Properly resize background image
//TODO: Adjust layout to work on all screen sizes (widen vertical space between TextViews)
//TODO: Add backwards compatible action bar support
//TODO: Get rid of default text in TextViews
//TODO: Add loading spinner animations
//TODO: Fix format for sunrise & sunset time (Convert to Earth time)
//TODO: Add refresh functionality & edit Crouton to alert user about refresh (by swiping down?)
//TODO: Add SettingsActivity (Celsius vs. Fahrenheit) (Don't forget ActionBar Up) (UIListView?)
//TODO: Add about dialog. Possibly about and link to site in settings?
//TODO: Possible way to check for updates on API? Push Notifications? (Later versions)
//TODO: TEST ON OTHER DEVICES!!
//TODO: Search for what next
//TODO: Done? Begin to explore the graphical view


public class MarsWeatherActivity extends Activity {
	private MarsWeatherClient client;

	final String DEGREE  = "\u00b0";
	final String CELSIUS = "\u2103";
	final String FAHRENHEIT = "\u2109";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars_weather);

		client = new MarsWeatherClient();

		client.getLatestWeatherReport(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int code, JSONObject json) {
				MarsWeatherReport latest = MarsWeatherReport.fromJson(json);
				updateWeatherView(latest);
			}

			// Runs when the JSON request for the weather report fails
			@Override
			public void onFailure ( Throwable e, String errorResponse ) {
				// Display an alert crouton
				Crouton.makeText(MarsWeatherActivity.this, R.string.error, Style.ALERT).show();
			}
		});
	}

	// Update the UI with values from a MarsWeatherReport
	public void updateWeatherView(MarsWeatherReport report) {
		// Get references to the UI widgets
		TextView txtCurrentTemperature = (TextView)findViewById(R.id.txtCurrentTemperature);
		TextView txtHighTempValue = (TextView)findViewById(R.id.txtHighTempValue);
		TextView txtLowTempValue = (TextView)findViewById(R.id.txtLowTempValue);
		TextView txtWeatherStatusValue = (TextView)findViewById(R.id.txtWeatherStatusValue);
		TextView txtSunriseValue = (TextView)findViewById(R.id.txtSunriseValue);
		TextView txtSunsetValue = (TextView)findViewById(R.id.txtSunsetValue);

		// Update the UI with the values from the MarsWeatherReport
		txtCurrentTemperature.setText(report.getMaxCelsiusTemp() + CELSIUS);
		txtHighTempValue.setText(report.getMaxCelsiusTemp() + CELSIUS);
		txtLowTempValue.setText(report.getMinCelsiusTemp() + CELSIUS);
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
		// Need to call to allow Crouton queue to be displayed correctly
		// during orientation changes
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}

}
