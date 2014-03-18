package com.cordeirolabs.weatherofmars;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//TODO: Add SettingsActivity (Celsius vs. Fahrenheit)

//TODO: Finalize image sizes for backgrounds
//TODO: SHRINK IMAGES SIZES - PNGCRUSH!!!!!!
//TODO: Finalize ic_launcher sizes
//TODO: Add analytics **********************************
//TODO: Remove debug logs


//TODO: Ask user to rate app?

//TODO: Change app name to Mars Weather Report
//TODO: Obfuscate code?

//TODO: Look for Android checklist before Google Play submission
//TODO: Get assets for Goole Play Store

//TODO: Done (Begin to explore the graphical view?) (Scrolls through Sols?)-
//TODO: Add about dialog. Possibly about and link to site in settings?
//TODO: Possible way to check for updates on API? Push Notifications? (Later versions)
//TODO: Search for what next


public class MarsWeatherActivity extends Activity {
	private MarsWeatherClient client;

	final String CELSIUS = "\u2103";
	final String FAHRENHEIT = "\u2109";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars_weather);

		// Set the default prefences if the user has not set them previously
		PreferenceManager.setDefaultValues(this, R.xml.user_pref, false);

		// Register the OnSharedPreferenceChangeListener
		SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//		userPrefs.registerOnSharedPreferenceChangeListener(this);
		
		// Get a reference to the retry button and
		// pass it in as the view to fetch a weather report
		Button btnRetry = (Button)findViewById(R.id.btnRetry);
		fetchWeatherReport(btnRetry);

		Intent emailIntent = getIntent();

		startActivity(Intent.createChooser(emailIntent,
				"Send Email Using: "));
	}

	// Attempts to get the latest mars weather reading
	// On success the UI is loaded in with the weather info displayed to the user
	// On failure a Crouton is displayed telling the user there was an error
	// and a retry button is displayed. This function is also run onClick of btnRetry
	public void fetchWeatherReport(View view) {

		client = new MarsWeatherClient();

		client.getLatestWeatherReport(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int code, JSONObject json) {
				MarsWeatherReport latest = MarsWeatherReport.fromJson(json);
				updateWeatherView(latest);

				// Hide the progressBar from the user
				findViewById(R.id.progressBar).setVisibility(View.GONE);

				//Hide the retry button from the user
				findViewById(R.id.btnRetry).setVisibility(View.GONE);

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
		TextView txtLastUpdatedText = (TextView)findViewById(R.id.txtLastUpdated);
		TextView txtCurrentTemperature = (TextView)findViewById(R.id.txtCurrentTemperature);
		TextView txtHighTempValue = (TextView)findViewById(R.id.txtHighTempValue);
		TextView txtLowTempValue = (TextView)findViewById(R.id.txtLowTempValue);
		TextView txtWeatherStatusValue = (TextView)findViewById(R.id.txtWeatherStatusValue);
		TextView txtSunriseValue = (TextView)findViewById(R.id.txtSunriseValue);
		TextView txtSunsetValue = (TextView)findViewById(R.id.txtSunsetValue);

		//TODO: Check for temperature unit setting

		// Calculate the average temperate from the weather reading
		// The middle point between the day's low and high temperatures
		int averageTemp = (report.getMaxCelsiusTemp() + report.getMinCelsiusTemp()) / 2;

		// Update the UI with the values from the MarsWeatherReport
		txtLastUpdatedText.append(" " + report.getEarthDate());
		txtCurrentTemperature.setText(averageTemp + CELSIUS);
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

	// Change the visibility of all the UI widget to visible from gone
	public void loadInUI() {
		findViewById(R.id.txtLatestWeather).setVisibility(View.VISIBLE);
		findViewById(R.id.txtCurrentTemperature).setVisibility(View.VISIBLE);
		findViewById(R.id.txtLastUpdated).setVisibility(View.VISIBLE);
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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {


		// If the user selects "Settings" in the menu
		if (item.getItemId() == R.id.action_settings) {

			//			if (Build.VERSION.SDK_INT < 11) {
			//				startActivity(new Intent(this, PreferencesActivity.class);
			//			} else {
			//				startActivity(new Intent(this, OtherPreferencesActivity.class);
			//			}

			// Check what Android version the user is running and 
			// start the appropriate PreferenceActivity
			if (Build.VERSION.SDK_INT < 11) {			
				Intent intent = new Intent(MarsWeatherActivity.this, PreApi11UserPrefActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MarsWeatherActivity.this, UserPrefActivity.class);
				startActivity(intent);
			}


		}


		return super.onMenuItemSelected(featureId, item);







	}

	//	@Override
	//	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	//			String key) {
	//		
	//
	//		if (key.equals("about")) {
	//			
	//			Intent i = new Intent(Intent.ACTION_SEND);  
	//			//i.setType("text/plain"); //use this line for testing in the emulator  
	//			i.setType("message/rfc822") ; // use from live device
	//			i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@joncordeiro.com"});  
	//			i.putExtra(Intent.EXTRA_SUBJECT,"MarsWeatherReport");  
	////			i.putExtra(Intent.EXTRA_TEXT,"body goes here");  
	//			startActivity(Intent.createChooser(i, "Select email application."));
	//			
	//			
	//		}
	//		
	//	}

//	@Override
//	public boolean onPreferenceClick(Preference preference) {
//
//		
//		Toast.makeText(this, preference.getKey(), Toast.LENGTH_SHORT).show();
//		
//		if(preference.getKey().equals("about")) {
//
//			Intent i = new Intent(Intent.ACTION_SEND);  
//			//i.setType("text/plain"); //use this line for testing in the emulator  
//			i.setType("message/rfc822") ; // use from live device
//			i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@joncordeiro.com"});  
//			i.putExtra(Intent.EXTRA_SUBJECT,"MarsWeatherReport");  
//			//			i.putExtra(Intent.EXTRA_TEXT,"body goes here");  
//			startActivity(Intent.createChooser(i, "Select email application."));
//
//		}
//
//		return false;
//	}

}
