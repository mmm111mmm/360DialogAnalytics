package com.newfivefour.d360analyticslibrary.implementation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AndroidNetworkAvailabilityTests {

  AndroidNetworkAvailability networkAvailability;
  @Mock Context context;
  @Mock ConnectivityManager connectivityManager;
  @Mock NetworkInfo netInfo;

  @Before
  public void before() {
    networkAvailability = new AndroidNetworkAvailability(context);
  }

  @Test
  public void run_networkAvailable() throws Exception {
    when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
    when(connectivityManager.getActiveNetworkInfo()).thenReturn(netInfo);
    when(netInfo.isConnected()).thenReturn(true);
    boolean result = networkAvailability.isNetworkAvailable();
    assertThat(result, is(true));
  }

  @Test
  public void run_NoNetworkAvailable() throws Exception {
    when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
    when(connectivityManager.getActiveNetworkInfo()).thenReturn(netInfo);
    when(netInfo.isConnected()).thenReturn(false);
    boolean result = networkAvailability.isNetworkAvailable();
    assertThat(result, is(false));
  }


}