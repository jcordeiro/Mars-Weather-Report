package com.cordeiro.marsweatherreport.api;

import com.cordeiro.marsweatherreport.model.MarsWeatherResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

public interface MarsWeatherService {

    @GET("/latest")
    void getWeatherReport(Callback<MarsWeatherResponse> callback);

    class Implementation {
        public static MarsWeatherService get() {
            return getBuilder()
                    .build()
                    .create(MarsWeatherService.class);
        }

        static RestAdapter.Builder getBuilder() {
            return new RestAdapter.Builder()
                    .setEndpoint("http://marsweather.ingenology.com/v1")
                    .setLogLevel(RestAdapter.LogLevel.NONE);
        }
    }

}
