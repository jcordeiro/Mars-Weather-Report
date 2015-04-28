package com.cordeiro.marsweatherreport.prefs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cordeiro.marsweatherreport.R;


@SuppressLint("NewApi")
public class UserPrefFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Add the preferences from resource using
		   Fragment's implementation of addPreferencesFromResource() */
		addPreferencesFromResource(R.xml.user_pref);

	}
}
