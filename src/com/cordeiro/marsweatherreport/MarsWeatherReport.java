package com.cordeiro.marsweatherreport;

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

			JSONObject json = jsonObject.getJSONObject("report");

			// Parse the json results into the MarsWeatherReport's fields
			report.earthDate = json.getString("terrestrial_date");
			report.minCelsiusTemp = json.getInt("min_temp");
			report.minFahrenheitTemp = json.getInt("min_temp_fahrenheit");
			report.maxCelsiusTemp = json.getInt("max_temp");
			report.maxFahrenheitTemp = json.getInt("max_temp_fahrenheit");
			report.weatherStatus = json.getString("atmo_opacity");
			report.sunrise = parseTime(json.getString("sunrise"));
			report.sunset = parseTime(json.getString("sunset"));

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new MarsWeatherReport
		return report;
	}

	// The json call for the mars weather data returns time in a format like this "2014-03-07T11:30:00Z"
	// This method splits the time String by the "T" and returns just the actual time
	// The trailing "z" and seconds are removed and "UTC" (Coordinated Universal Time) is appended
	// E.g 2014-03-07T11:30:00Z -> 11:30:00 UTC
	public static String parseTime(String time) {	
		String[] parts = time.split("T");
		return parts[1].substring(0, parts[1].length() - 4) + " UTC";
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
