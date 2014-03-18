package com.cordeirolabs.weatherofmars;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPrefActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set content view to be xml layout with fragment element
		setContentView(R.layout.pref_activity_user_pref);
	}

}
