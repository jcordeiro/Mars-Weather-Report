package com.cordeirolabs.weatherofmars;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarsWeatherReport {
	private String earthDate;
	private int minCelsiusTemp;
	private int maxCelsiusTemp;
	private int minFahrenheitTemp;
	private int maxFahrenheitTemp;
	private String weatherStatus;
	private String sunrise;
	private String sunset;


	public String getEarthDate() {
		return earthDate;
	}
	public int getMinCelsiusTemp() {
		return minCelsiusTemp;
	}
	public int getMaxCelsiusTemp() {
		return maxCelsiusTemp;
	}
	public int getMinFahrenheitTemp() {
		return minFahrenheitTemp;
	}
	public int getMaxFahrenheitTemp() {
		return maxFahrenheitTemp;
	}
	public String getWeatherStatus() {
		return weatherStatus;
	}
	public String getSunrise() {
		return sunrise;
	}
	public String getSunset() {
		return sunset;
	}

	// Builds a MarsWeatherReport given the expected JSON
	public static MarsWeatherReport fromJson(JSONObject jsonObject) {
		MarsWeatherReport report = new MarsWeatherReport();
		try {

			// Parse the json results into the MarsWeatherReport's fields
			report.earthDate = jsonObject.getString("terrestrial_date");
			report.minCelsiusTemp = jsonObject.getInt("min_temp");
			report.minFahrenheitTemp = jsonObject.getInt("min_temp_fahrenheit");
			report.maxCelsiusTemp = jsonObject.getInt("max_temp");
			report.maxFahrenheitTemp = jsonObject.getInt("max_temp_fahrenheit");
			report.weatherStatus = jsonObject.getString("atmo_opacity");
			report.sunrise = jsonObject.getString("sunrise");
			report.sunset = jsonObject.getString("sunset");

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new MarsWeatherReport
		return report;
	}

	// Parses through an array of json weather reports and builds new MarsWeatherReport objects
	// Stored in an ArrayList. These reports will be used when building the graphical view
	// of the weather data
	public static ArrayList<MarsWeatherReport> fromJson(JSONArray jsonArray) {
		ArrayList<MarsWeatherReport> weatherReports = new ArrayList<MarsWeatherReport>(jsonArray.length());

		// Parse through each result in the json array and build a new MarsWeatherReport
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject reportJson = null;
			try {
				reportJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			MarsWeatherReport report = MarsWeatherReport.fromJson(reportJson);
			if (report != null) {
				weatherReports.add(report);
			}
		}

		return weatherReports;
	}

}
