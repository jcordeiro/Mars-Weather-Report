package com.cordeiro.marsweatherreport;


import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreApi11UserPrefActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.user_pref);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Google Analytics
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Google Analytics
		EasyTracker.getInstance(this).activityStop(this);
	}
}
