package com.newfivefour.d360analyticslibrary.implementation;

import android.content.Context;
import android.net.ConnectivityManager;

import com.newfivefour.d360analyticslibrary.interfaces.ConnectionType;

/**
 * What's our connection type?
 */
public class AndroidConnectionType implements ConnectionType {
  private final Context mApplicationContext;

  public AndroidConnectionType(Context applicationContext){
    mApplicationContext = applicationContext;
  }

  @Override public String getConnectionType() {
    ConnectivityManager cm = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(cm==null || cm.getActiveNetworkInfo()==null) {
      return ConnectionType.UNKNOWN;
    } else {
      int type = cm.getActiveNetworkInfo().getType();
      if(type== ConnectivityManager.TYPE_WIFI) return WIFI;
      if(type== ConnectivityManager.TYPE_MOBILE) return MOBILE;
      else return UNKNOWN;
    }
  }
}
