package com.cordeirolabs.weatherofmars;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreApi11UserPrefActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.user_pref);
	}
}
