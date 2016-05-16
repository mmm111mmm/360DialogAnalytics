package com.newfivefour.d360analyticslibrary.implementation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.newfivefour.d360analyticslibrary.interfaces.NetworkAvailability;

/**
 * Uses the android libraries to check if the network is connected.
 */
public class AndroidNetworkAvailability implements NetworkAvailability {

  private final Context mApplicationContext;

  public AndroidNetworkAvailability(@NonNull Context appContext) {
    mApplicationContext = appContext;
  }

  @Override public boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnected();
  }
}
