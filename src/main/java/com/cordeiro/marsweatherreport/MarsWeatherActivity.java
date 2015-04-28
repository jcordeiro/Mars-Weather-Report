package com.cordeiro.marsweatherreport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cordeiro.marsweatherreport.api.MarsWeatherService;
import com.cordeiro.marsweatherreport.model.MarsWeatherReport;
import com.cordeiro.marsweatherreport.model.MarsWeatherResponse;
import com.cordeiro.marsweatherreport.prefs.PreApi11UserPrefActivity;
import com.cordeiro.marsweatherreport.prefs.UserPrefActivity;
import com.cordeiro.marsweatherreport.util.AppRater;
import com.google.analytics.tracking.android.EasyTracker;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MarsWeatherActivity extends Activity implements OnSharedPreferenceChangeListener {

	/* Unicode character codes for temperature degree symbols */
	final String CELSIUS = "\u2103";
	final String FAHRENHEIT = "\u2109";
	final String KELVIN = "\u212A";

	/* Key for shared preferences */
	final String TEMP_UNITS = "tempUnits";

	/* Key for savedInstanceState bundle */
	final String SAVED_REPORT = "savedReport";

	String temperatureUnits;
	MarsWeatherReport report;
	SharedPreferences prefs;
	Button btnRetry;
	boolean weatherDataFetchedSuccesfully = false;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		/* We only saved the report in the bundle if the weather data was fetched successfully.
		   If the weather data wasn't fetched successfully then we'll leave the report in
		   the bundle null
		 */
		if (weatherDataFetchedSuccesfully) {
			/* Saved the weather report in the out state bundle */
			outState.putParcelable(SAVED_REPORT, report);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mars_weather);

		AppRater.app_launched(this);

		/* Set the default preferences if the user has not set them previously */
		PreferenceManager.setDefaultValues(this, R.xml.user_pref, false);

		/* Register the OnSharedPreferenceChangeListener */
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		/* Get the user's temperature unit setting. Default is Celsius */
		temperatureUnits = prefs.getString(TEMP_UNITS, CELSIUS);

		/* Get a reference to the retry button and
		  pass it in as the view to fetch a weather report */
		btnRetry = (Button)findViewById(R.id.btnRetry);

		/* Check if we're coming from an orientation change. If not, fetch the data over the network
		   otherwise we load the weather data from our saved bundle */
		 if (savedInstanceState != null) {
			/* Get the weather report from the bundle and update the UI */
			 report = savedInstanceState.getParcelable(SAVED_REPORT);

			 if (report != null) {
				 weatherDataFetchedSuccesfully = true;
				 loadInUI();
				 fillViewsWithWeatherData(report, temperatureUnits);
			 }
			 else {
				displayFailureUI();
			 }
		}
		else {
			 /* Make the request for the weather data */
			 MarsWeatherService.Implementation.get().getWeatherReport(fetchWeatherData);
		 }

		/* When the user clicks the "retry" button */
		btnRetry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/* Make the request for the weather data */
				MarsWeatherService.Implementation.get().getWeatherReport(fetchWeatherData);
			}
		});
	}

	/* Callback function called by Retrofit after making the request for the weather data */
	Callback fetchWeatherData = new Callback<MarsWeatherResponse>() {

		@Override
		public void success(MarsWeatherResponse marsWeatherResponse, Response response) {
			/* Get the weather report and update the UI */
			report = marsWeatherResponse.getReport();
			weatherDataFetchedSuccesfully = true;
			loadInUI();
			fillViewsWithWeatherData(report, temperatureUnits);
		}

		@Override
		public void failure(RetrofitError error) {

			displayFailureUI();
		}
	};

	/**
	 * Displays the retry button and tells the user that the request for weather data has failed
	 */
	 public void displayFailureUI() {
		 weatherDataFetchedSuccesfully = false;

		 /* Display an alert crouton */
		 Crouton.makeText(MarsWeatherActivity.this, R.string.error, Style.ALERT).show();

		 /* Hide the progressBar from the user */
		 findViewById(R.id.progressBar).setVisibility(View.GONE);

		 /* Display retry button to the user */
		 findViewById(R.id.btnRetry).setVisibility(View.VISIBLE);
	 }

	/**
	 * Makes the UI views visible and hides the progress bar
	 */
	public void loadInUI() {
		findViewById(R.id.txtLatestWeather).setVisibility(View.VISIBLE);
		findViewById(R.id.txtCurrentTemperature).setVisibility(View.VISIBLE);
		findViewById(R.id.txtEarthDate).setVisibility(View.VISIBLE);
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

		/* Hide the progress bar and retry button */
		findViewById(R.id.progressBar).setVisibility(View.GONE);
		findViewById(R.id.btnRetry).setVisibility(View.GONE);
	}

	/**
	 * Fills in the UI views with weather data from a MarsWeatherReport
	 *
	 * @param report The weather report that we're taking the data from
	 * @param units The temperature units to use
	 */
	public void fillViewsWithWeatherData (MarsWeatherReport report, String units) {

		/* Check that report isn't null */
		if (report != null) {

			try {
				TextView txtEarthDate = (TextView) findViewById(R.id.txtEarthDate);
				TextView txtCurrentTemperature = (TextView) findViewById(R.id.txtCurrentTemperature);
				TextView txtHighTempValue = (TextView) findViewById(R.id.txtHighTempValue);
				TextView txtLowTempValue = (TextView) findViewById(R.id.txtLowTempValue);
				TextView txtWeatherStatusValue = (TextView) findViewById(R.id.txtWeatherStatusValue);
				TextView txtSunriseValue = (TextView) findViewById(R.id.txtSunriseValue);
				TextView txtSunsetValue = (TextView) findViewById(R.id.txtSunsetValue);

				if (units.equals(CELSIUS)) {
					txtCurrentTemperature.setText(report.getAverageCelsiusTemp() + CELSIUS);
					txtHighTempValue.setText(report.getMaxCelsiusTemp() + CELSIUS);
					txtLowTempValue.setText(report.getMinCelsiusTemp() + CELSIUS);
				} else if (units.equals(FAHRENHEIT)) {
					txtCurrentTemperature.setText(report.getAverageFahrenheitTemp() + FAHRENHEIT);
					txtHighTempValue.setText(report.getMaxFahrenheitTemp() + FAHRENHEIT);
					txtLowTempValue.setText(report.getMinFahrenheitTemp() + FAHRENHEIT);
				} else { /* KELVIN */
					txtCurrentTemperature.setText(report.getAverageKelvinTemp() + KELVIN);
					txtHighTempValue.setText(report.getMaxKelvinTemp() + KELVIN);
					txtLowTempValue.setText(report.getMinKelvinTemp() + KELVIN);
				}

				txtEarthDate.setText(getResources().getString(R.string.earth_date) + " " + report.getEarthDate());
				txtWeatherStatusValue.setText(report.getWeatherStatus());
				txtSunriseValue.setText(report.getSunrise());
				txtSunsetValue.setText(report.getSunset());
			} catch (NullPointerException e) {
				/* One of the values returned by the report is null */
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* Inflate the menu */
		getMenuInflater().inflate(R.menu.mars_weather, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		/* If the user selects "Settings" in the menu */
		if (item.getItemId() == R.id.action_settings) {

			/* Check what Android version the user is running and
			   start the appropriate PreferenceActivity */
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		/* If the user has changed their preference for the temperature units
		   we need to update the view with weather data again */
		if (key.equals(TEMP_UNITS)) {
			/* Get the user's new temperature unit setting. Default is Celsius */
			temperatureUnits = prefs.getString(TEMP_UNITS, CELSIUS);
			fillViewsWithWeatherData(report, temperatureUnits);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		/* Google Analytics */
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		/* Google Analytics */
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onDestroy() {
		/* Need to call to allow Crouton queue to be displayed correctly
		   during orientation changes */
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}
}