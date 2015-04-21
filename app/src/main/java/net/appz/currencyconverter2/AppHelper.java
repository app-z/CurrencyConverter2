package net.appz.currencyconverter2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppHelper {

	public static final String CURRENCY_PREF_NAME = "currencyconverter_pref_xyz";

	private static final String COUNTRY_FROM_IDX = "from_idx";
	private static final String COUNTRY_TO_IDX = "to_idx";

	public static int getCountryFromIdx(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				CURRENCY_PREF_NAME, 0);
		return pref.getInt(COUNTRY_FROM_IDX, 0);
	}

	public static void setCountryFromIdx(Context context, int sortidx) {
		SharedPreferences pref = context.getSharedPreferences(
				CURRENCY_PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(COUNTRY_FROM_IDX, sortidx);
		editor.commit();
	}

	
	public static int getCountryToIdx(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				CURRENCY_PREF_NAME, 0);
		return pref.getInt(COUNTRY_TO_IDX, 0);
	}

	public static void setCountryToIdx(Context context, int sortidx) {
		SharedPreferences pref = context.getSharedPreferences(
				CURRENCY_PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(COUNTRY_TO_IDX, sortidx);
		editor.commit();
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null, otherwise check
		// if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean serviceErrorMessage(final Context context, final String message, final boolean finish) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage(message);
		// alertDialog.setIcon(R.drawable.tick);
		alertDialog.setButton(
				context.getResources().getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						if(finish)
							((Activity)context).finish();
					}
				});
		alertDialog.show();
		return false;
	}



	
}
