package com.cordeiro.marsweatherreport;


import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPrefActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set content view to be xml layout with fragment element
		setContentView(R.layout.pref_activity_user_pref);
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
