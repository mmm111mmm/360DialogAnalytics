package com.newfivefour.d360analyticslibrary;

import android.content.Context;
import android.database.Cursor;

import com.newfivefour.d360analyticslibrary.exceptions.AnalyticsInitFailureException;
import com.newfivefour.d360analyticslibrary.interfaces.ConnectionType;
import com.newfivefour.d360analyticslibrary.interfaces.LastEventNumber;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsTests {

  Analytics analytics;
  @Mock Context context;
  @Mock Cursor cursor;
  @Mock LastEventNumber lastEventNumber;
  @Mock ConnectionType connectionType;

  @Before
  public void before() {
    analytics = Analytics.init(context, "hiya");
    analytics.mLastEventNumber = lastEventNumber;
    analytics.mConnectionType = connectionType;
  }

  @Test
  public void test_initException() {
    try {
      Analytics.init(context, "");
    } catch (AnalyticsInitFailureException e) {
      assertTrue("Found exception", true);
      return;
    }
    assertTrue("Wanted exception", false);
  }

  @Test
  public void test_noInitException() {
    try {
      Analytics.init(context, "hiya");
    } catch (AnalyticsInitFailureException e) {
      assertTrue("Found exception", false);
      return;
    }
    assertTrue("No exception", true);
  }

  @Test
  public void test_setConnectionWifi() {
    Mockito.when(connectionType.getConnectionType()).thenReturn(ConnectionType.WIFI);
    D360AnalyticsPayload payload = new D360AnalyticsPayload("hi", null);
    Analytics.instance().send(payload);
    assertThat(payload.getMeta().getConnectionInfo(), is(ConnectionType.WIFI));
  }

  @Test
  public void test_setConnectionMobile() {
    Mockito.when(connectionType.getConnectionType()).thenReturn(ConnectionType.MOBILE);
    D360AnalyticsPayload payload = new D360AnalyticsPayload("hi", null);
    Analytics.instance().send(payload);
    assertThat(payload.getMeta().getConnectionInfo(), is(ConnectionType.MOBILE));
  }

  @Test
  public void test_setEventNumber() {
    Mockito.when(lastEventNumber.incrementEventNumber(1)).thenReturn(17);
    D360AnalyticsPayload payload = new D360AnalyticsPayload("hi", null);
    Analytics.instance().send(payload);
    assertThat(payload.getMeta().getEventNo(), is(17L));
  }

}
