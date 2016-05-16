package com.newfivefour.d360analyticslibrary.pojos;

import com.newfivefour.d360analyticslibrary.implementation.AndroidFailureResendTimerTask;
import com.newfivefour.d360analyticslibrary.interfaces.NetworkAvailability;
import com.newfivefour.d360analyticslibrary.interfaces.RemoveAndRetrieveFailedSends;
import com.newfivefour.d360analyticslibrary.interfaces.ServerSend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class D360AnalyticsPayloadTests {

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

  //TODO test
  @Test
  public void test_stub() {
  }
}