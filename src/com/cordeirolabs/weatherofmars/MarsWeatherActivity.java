package com.cordeirolabs.weatherofmars;

import java.util.TimeZone;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

//TODO: Adjust layout to work on all screen sizes (multiple dimens.xml files?)
//TODO: Fix format for sunrise & sunset time (Convert to Earth time)
//TODO: Add refresh functionality & edit Crouton to alert user about refresh (by swiping down?)
//TODO: Add SettingsActivity (Celsius vs. Fahrenheit) (Don't forget ActionBar Up) (UIListView?)
//TODO: Add about dialog. Possibly about and link to site in settings?
//TODO: Possible way to check for updates on API? Push Notifications? (Later versions)
//TODO: TEST ON OTHER DEVICES!! LOW CONNECTIVITY (emulator?)
//TODO: Search for what next
//TODO: Finalize image sizes for backgrounds
//TODO: SHRINK IMAGES SIZES - PNGCRUSH!!!!!!
//TODO: Add analytics
//TODO: Remove debug logs
//TODO: Look for Android checklist before Google Play submission
//TODO: Done (Begin to explore the graphical view?) (Scrolls through Sols?)-118


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
				
				// Hide the progressBar from the user
				findViewById(R.id.progressBar).setVisibility(View.GONE);
				
				// Display all the UI widgets
				loadInUI();

			}

			// Runs when the JSON request for the weather report fails
			@Override
			public void onFailure ( Throwable e, String errorResponse ) {
				// Display an alert crouton
				Crouton.makeText(MarsWeatherActivity.this, R.string.error, Style.ALERT).show();
				
				// Hide the progressBar from the user
				findViewById(R.id.progressBar).setVisibility(View.GONE);
				
				// Display retry button to the user
				findViewById(R.id.btnRetry).setVisibility(View.VISIBLE);
				
			}
		});
	}

	// Update the UI with values from a MarsWeatherReport
	public void updateWeatherView(MarsWeatherReport report) {
		// Get references to the UI widgets
		TextView txtLastUpdatedText = (TextView)findViewById(R.id.txtLastUpdatedText);
//		TextView txtLastUpdatedValue = (TextView)findViewById(R.id.txtLastUpdatedValue);
		TextView txtCurrentTemperature = (TextView)findViewById(R.id.txtCurrentTemperature);
		TextView txtHighTempValue = (TextView)findViewById(R.id.txtHighTempValue);
		TextView txtLowTempValue = (TextView)findViewById(R.id.txtLowTempValue);
		TextView txtWeatherStatusValue = (TextView)findViewById(R.id.txtWeatherStatusValue);
		TextView txtSunriseValue = (TextView)findViewById(R.id.txtSunriseValue);
		TextView txtSunsetValue = (TextView)findViewById(R.id.txtSunsetValue);

		// Update the UI with the values from the MarsWeatherReport
		txtLastUpdatedText.append(" " + report.getEarthDate());
		txtCurrentTemperature.setText(report.getMaxCelsiusTemp() + CELSIUS);
		txtHighTempValue.setText(report.getMaxCelsiusTemp() + CELSIUS);
		txtLowTempValue.setText(report.getMinCelsiusTemp() + CELSIUS);
		txtWeatherStatusValue.setText(report.getWeatherStatus());
		txtSunriseValue.setText(report.getSunrise());
		txtSunsetValue.setText(report.getSunset());
		
		TimeZone tz = TimeZone.getDefault();
		Log.d("TIMEZONE", tz.getDisplayName(false, TimeZone.SHORT));
		Log.d("TIMEZONE", tz.getDisplayName(false, TimeZone.LONG));
		
	}
	
	// Convert the times in the MarsWeathReport to the user's local timezone
	public void convertMarsTimeToLocalTime() {

		TimeZone tz = TimeZone.getDefault();
		tz.getDisplayName(false, TimeZone.SHORT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mars_weather, menu);
		return true;
	}

	// Change the visibility of all the UI widget to visible from gone
	public void loadInUI() {

		findViewById(R.id.txtLatestWeather).setVisibility(View.VISIBLE);
		findViewById(R.id.txtCurrentTemperature).setVisibility(View.VISIBLE);
		findViewById(R.id.txtLastUpdatedText).setVisibility(View.VISIBLE);
//		findViewById(R.id.txtLastUpdatedValue).setVisibility(View.VISIBLE);
		findViewById(R.id.txtHighTempText).setVisibility(View.VISIBLE);
		findViewById(R.id.txtHighTempValue).setVisibility(View.VISIBLE);
		findViewById(R.id.txtLowTempText).setVisibility(View.VISIBLE);
		findViewById(R.id.txtLowTempValue).setVisibility(View.VISIBLE);
		findViewById(R.id.txtWeatherStatusText).setVisibility(View.VISIBLE);
		findViewById(R.id.txtWeatherStatusValue).setVisibility(View.VISIBLE);
		findViewById(R.id.txtSunriseText).setVisibility(View.VISIBLE);
		findViewById(R.id.txtSunriseValue).setVisibility(View.VISIBLE);
		findViewById(R.id.txtSunsetText).setVisibility(View.VISIBLE);
		findViewById(R.id.txtSunsetValue).setVisibility(View.VISIBLE);

	}

	@Override
	protected void onDestroy() {
		// Need to call to allow Crouton queue to be displayed correctly
		// during orientation changes
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}

}
