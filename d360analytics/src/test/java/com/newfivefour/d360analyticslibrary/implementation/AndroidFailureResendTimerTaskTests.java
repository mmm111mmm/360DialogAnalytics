package com.newfivefour.d360analyticslibrary.implementation;

import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;
import com.newfivefour.d360analyticslibrary.interfaces.NetworkAvailability;
import com.newfivefour.d360analyticslibrary.interfaces.RemoveAndRetrieveFailedSends;
import com.newfivefour.d360analyticslibrary.interfaces.ServerSend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class AndroidFailureResendTimerTaskTests {

  AndroidFailureResendTimerTask timer;
  @Mock NetworkAvailability networkAvailability;
  @Mock RemoveAndRetrieveFailedSends removeAndRetrieveFailedSends;
  @Mock ServerSend serverSend;

  @Before
  public void before() {
    timer = new AndroidFailureResendTimerTask(
            networkAvailability, removeAndRetrieveFailedSends, serverSend
    );
  }

  @Test
  public void run_checkInit() throws Exception {
  }

  @Test
  public void run_checkNetworkAvailability() throws Exception {
    timer.run();
    verify(networkAvailability, Mockito.times(1)).isNetworkAvailable();
  }

  @Test
  public void run_noFailedResourcesCollectedOnNoNetwork() throws Exception {
    when(networkAvailability.isNetworkAvailable()).thenReturn(false);
    timer.run();
    verify(removeAndRetrieveFailedSends, never()).removeFailurePayloads();
  }

  @Test
  public void run_failedResourcesCollectedOnGoodNetwork() throws Exception {
    when(networkAvailability.isNetworkAvailable()).thenReturn(true);
    timer.run();
    verify(removeAndRetrieveFailedSends, Mockito.times(1)).removeFailurePayloads();
  }

  @Test
  public void run_sendToServerWhenMoreThanOnePayload() throws Exception {
    when(networkAvailability.isNetworkAvailable()).thenReturn(true);
    final D360AnalyticsPayload pl = new D360AnalyticsPayload("hi", null);
    when(removeAndRetrieveFailedSends.removeFailurePayloads()).thenReturn(
            new ArrayList<D360AnalyticsPayload>() {{
              add(pl);
            }});
    timer.run();
    verify(serverSend, Mockito.times(1)).send(pl);
  }

  @Test
  public void run_dontSendToServerWhenNullPayloads() throws Exception {
    when(networkAvailability.isNetworkAvailable()).thenReturn(true);
    final D360AnalyticsPayload pl = new D360AnalyticsPayload("hi", null);
    when(removeAndRetrieveFailedSends.removeFailurePayloads()).thenReturn(null);
    timer.run();
    verify(serverSend, Mockito.times(0)).send(pl);
  }

}