package com.cordeiro.marsweatherreport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;


@SuppressLint("NewApi")
public class UserPrefFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Add the preferences from resource using
		// Fragment's implementation of addPreferencesFromResource()
		addPreferencesFromResource(R.xml.user_pref);
	
	}
}
