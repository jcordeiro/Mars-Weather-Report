package com.cordeiro.marsweatherreport.util;

/* Taken from: http://blog.zeezonline.com/2012/07/android-rate-my-app-dialog */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {

	private final static String APP_TITLE = "Mars Weather Report";     
	private final static String APP_PACKAGE_NAME = "com.cordeiro.marsweatherreport";

	private final static int DAYS_UNTIL_PROMPT = 5;
	private final static int LAUNCH_UNTIL_PROMPT = 7;

	public static void app_launched(Context mContext){
		SharedPreferences prefs = mContext.getSharedPreferences("rate_app", 0);
		if (prefs.getBoolean("dontshowagain", false)){return;}

		SharedPreferences.Editor editor = prefs.edit();

		/* Add to launch Counter */
		long launch_count = prefs.getLong("launch_count", 0) +1;
		editor.putLong("launch_count", launch_count);

		/* Get Date of first launch */
		Long date_firstLaunch = prefs.getLong("date_first_launch",0);
		if (date_firstLaunch == 0){
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_first_launch", date_firstLaunch);
		}

		/* Wait at least X days to launch */
		if (launch_count >= LAUNCH_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)){
				showRateDialog(mContext, editor);
			}
		}

		editor.commit();

	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		Dialog dialog = new Dialog(mContext);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		String message = "If you enjoy using "
				+ APP_TITLE
				+ ", please take a moment to rate the app. Thank you for your support!";
		builder.setMessage(message)
		.setTitle("Rate " + APP_TITLE)
		.setIcon(mContext.getApplicationInfo().icon)
		.setCancelable(false)
		.setPositiveButton("Rate Now",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				editor.putBoolean("dontshowagain", true);
				editor.commit();
				mContext.startActivity(new Intent(
						Intent.ACTION_VIEW, Uri
						.parse("market://details?id="
								+ APP_PACKAGE_NAME)));
				dialog.dismiss();
			}
		})
		.setNeutralButton("Later",        
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();

			}
		})
		.setNegativeButton("No, Thanks",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();

			}
		});
		dialog = builder.create();

		dialog.show();
	}
}