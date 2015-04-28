package com.cordeiro.marsweatherreport.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MarsWeatherReport implements Parcelable {

	/* Used to convert temperate from Celsius to Kelvin */
	final double KELVIN_CONVERSION = 273.15;

	@SerializedName("terrestrial_date") private String earthDate;
	@SerializedName("sol") private int sol;
	@SerializedName("ls") private int ls;
	@SerializedName("min_temp") private double minCelsiusTemp;
	@SerializedName("min_temp_fahrenheit") private double minFahrenheitTemp;
	@SerializedName("max_temp") private double maxCelsiusTemp;
	@SerializedName("max_temp_fahrenheit") private double maxFahrenheitTemp;
	@SerializedName("pressure") private double pressure;
	@SerializedName("pressure_string") private String pressureString;
	@SerializedName("abs_humidity") private double humidity;
	@SerializedName("wind_speed") private double windSpeed;
	@SerializedName("wind_direction") private String windDirection;
	@SerializedName("atmo_opacity") private String weatherStatus;
	@SerializedName("season") private String season;
	@SerializedName("sunrise") private String sunrise;
	@SerializedName("sunset") private String sunset;
	private double averageCelsiusTemp;
	private double averageFahrenheitTemp;
	private double minKelvinTemp;
	private double maxKelvinTemp;
	private double averageKelvinTemp;

	/* Getter methods */
	public String getEarthDate() {
		return earthDate;
	}

	public int getSol() {
		return sol;
	}

	public int getLs() {
		return ls;
	}

	public double getMinCelsiusTemp() {
		return minCelsiusTemp;
	}

	public double getMinFahrenheitTemp() {
		return minFahrenheitTemp;
	}

	public double getMaxCelsiusTemp() {
		return maxCelsiusTemp;
	}

	public double getMaxFahrenheitTemp() {
		return maxFahrenheitTemp;
	}

	public double getPressure() {
		return pressure;
	}

	public String getPressureString() {
		return pressureString;
	}

	public double getHumidity() {
		return humidity;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public String getWeatherStatus() {
		return weatherStatus;
	}

	public String getSeason() {
		return season;
	}

	public String getSunrise() {
		return parseTime(sunrise);
	}

	public String getSunset() {
		return parseTime(sunset);
	}

	public double getAverageCelsiusTemp() {
		return (minCelsiusTemp + maxCelsiusTemp) / 2;
	}

	public double getAverageFahrenheitTemp() {
		return (minFahrenheitTemp + maxFahrenheitTemp) / 2;
	}

	public double getMinKelvinTemp() {
		return convertCelsiusToKelvin(minCelsiusTemp);
	}

	public double getMaxKelvinTemp() {
			return convertCelsiusToKelvin(maxCelsiusTemp);
	}

	public double getAverageKelvinTemp() {
		double temp = (getMinKelvinTemp() + getMaxKelvinTemp()) / 2;
		return (double)Math.round(temp * 100) / 100;
	}

	/**
	 * Converts a Celsius temperature to Kelvin and rounds the answer to 2 decimal places
	 *
	 * @param temp The Celsius temperature to be converted
	 * @return The converted Kelvin temperature round to 2 decimal places
	 */
		public double convertCelsiusToKelvin(double temp) {
			return (double)Math.round((temp+KELVIN_CONVERSION) * 100) / 100;
		}

	/**
	 * The request for the Mars weather data returns the time in a format like this "2014-03-07T11:30:00Z"
	 * This method splits the time String by the "T" and returns just the actual time
	 * The trailing "z" and seconds are removed and "UTC" (Coordinated Universal Time) is appended
	 * e.g 2014-03-07T11:30:00Z -> 11:30:00 UTC
	 *
	 * @param time The time String to be parsed
	 * @return The parsed time
	 */
	// E.g 2014-03-07T11:30:00Z -> 11:30:00 UTC
	public static String parseTime(String time) {
		String[] parts = time.split("T");
		return parts[1].substring(0, parts[1].length() - 4) + " UTC";
	}

	/* Methods required for parcelable interface */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.earthDate);
		dest.writeInt(this.sol);
		dest.writeInt(this.ls);
		dest.writeDouble(this.minCelsiusTemp);
		dest.writeDouble(this.minFahrenheitTemp);
		dest.writeDouble(this.maxCelsiusTemp);
		dest.writeDouble(this.maxFahrenheitTemp);
		dest.writeDouble(this.pressure);
		dest.writeString(this.pressureString);
		dest.writeDouble(this.humidity);
		dest.writeDouble(this.windSpeed);
		dest.writeString(this.windDirection);
		dest.writeString(this.weatherStatus);
		dest.writeString(this.season);
		dest.writeString(getSunrise());
		dest.writeString(getSunset());
		dest.writeDouble(getAverageCelsiusTemp());
		dest.writeDouble(getAverageFahrenheitTemp());
		dest.writeDouble(getMinKelvinTemp());
		dest.writeDouble(getMaxKelvinTemp());
		dest.writeDouble(getAverageKelvinTemp());
	}

	public MarsWeatherReport(Parcel in) {
		this.earthDate = in.readString();
		this.sol = in.readInt();
		this.ls = in.readInt();
		this.minCelsiusTemp = in.readDouble();
		this.minFahrenheitTemp = in.readDouble();
		this.maxCelsiusTemp = in.readDouble();
		this.maxFahrenheitTemp = in.readDouble();
		this.pressure = in.readDouble();
		this.pressureString = in.readString();
		this.humidity = in.readDouble();
		this.windSpeed = in.readDouble();
		this.windDirection = in.readString();
		this.weatherStatus = in.readString();
		this.season = in.readString();
		this.sunrise = in.readString();
		this.sunset = in.readString();
		this.averageCelsiusTemp = in.readDouble();
		this.averageFahrenheitTemp = in.readDouble();
		this.minKelvinTemp = in.readDouble();
		this.maxKelvinTemp = in.readDouble();
		this.averageKelvinTemp = in.readDouble();
	}

	public static final Parcelable.Creator<MarsWeatherReport> CREATOR = new Parcelable.Creator<MarsWeatherReport>() {
		public MarsWeatherReport createFromParcel(Parcel in) {
			return new MarsWeatherReport(in);
		}

		public MarsWeatherReport[] newArray(int size) {
			return new MarsWeatherReport[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
