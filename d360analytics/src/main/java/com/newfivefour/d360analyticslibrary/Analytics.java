package com.newfivefour.d360analyticslibrary;

import android.content.Context;
import android.util.Log;

import com.newfivefour.d360analyticslibrary.dbtables.FailureDatabaseHelper;
import com.newfivefour.d360analyticslibrary.endpoints.AnalyticsService;
import com.newfivefour.d360analyticslibrary.exceptions.AnalyticsInitFailureException;
import com.newfivefour.d360analyticslibrary.implementation.AnalyticsFailureDatabase;
import com.newfivefour.d360analyticslibrary.implementation.AnalyticsServerSender;
import com.newfivefour.d360analyticslibrary.implementation.AndroidConnectionType;
import com.newfivefour.d360analyticslibrary.implementation.AndroidFailureResendTimerTask;
import com.newfivefour.d360analyticslibrary.implementation.AndroidNetworkAvailability;
import com.newfivefour.d360analyticslibrary.implementation.SimpleLastEventNumber;
import com.newfivefour.d360analyticslibrary.interfaces.ConnectionType;
import com.newfivefour.d360analyticslibrary.interfaces.FailureDatabase;
import com.newfivefour.d360analyticslibrary.interfaces.LastEventNumber;
import com.newfivefour.d360analyticslibrary.interfaces.NetworkAvailability;
import com.newfivefour.d360analyticslibrary.interfaces.PostFailedCallback;
import com.newfivefour.d360analyticslibrary.interfaces.RemoveAndRetrieveFailedSends;
import com.newfivefour.d360analyticslibrary.interfaces.ServerSend;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Entry point for analytics
 */
public class Analytics {
  private static String TAG = Analytics.class.getSimpleName();
  private static Analytics instance;

  private Context mApplicationContext;
  // protected for testing purposes...
  protected ServerSend mAnalyticsServer;
  protected FailureDatabase mFailureDatabase;
  protected NetworkAvailability mNetworkAvailability;
  protected RemoveAndRetrieveFailedSends mRetrieveRemoveFailedSends;
  protected LastEventNumber mLastEventNumber;
  protected ConnectionType mConnectionType;
  private static Timer timer;
  private static TimerTask timertask;

  private Analytics() {} // Singleton

  /**
   * Initialise the Analytics.
   * Sets up the dependencies.
   * @param applicationContext
   * @param analyticsKey
   * @throws AnalyticsInitFailureException DId we pass in the a good key?
   * @return
   */
  public static Analytics init(Context applicationContext, String analyticsKey) throws AnalyticsInitFailureException {
      try {
        createNewInstance(applicationContext, analyticsKey);
        return instance;
      } catch(Exception e) {
        Log.d(TAG, "Error creating instance");
        throw new AnalyticsInitFailureException("Failure in init()", e);
      }
  }

  private static void createNewInstance(Context applicationContext, String analyticsKey) {
    instance = new Analytics();
    setupDependencies(applicationContext, analyticsKey);
    if(timertask!=null) timertask.cancel();
    timertask = new AndroidFailureResendTimerTask(instance.mNetworkAvailability,
            instance.mRetrieveRemoveFailedSends, instance.mAnalyticsServer);
    timer = new Timer();
    timer.purge();
    timer.schedule(timertask, 1000, 50000);
  }

  private static void setupDependencies(Context applicationContext, String analyticsKey) {
    instance.mApplicationContext = applicationContext;
    instance.mNetworkAvailability = new AndroidNetworkAvailability(instance.mApplicationContext);
    instance.mLastEventNumber = new SimpleLastEventNumber();
    AnalyticsFailureDatabase failureDatabase = new AnalyticsFailureDatabase(new FailureDatabaseHelper(applicationContext).getWritableDatabase());
    instance.mFailureDatabase = failureDatabase;
    instance.mRetrieveRemoveFailedSends = failureDatabase;
    instance.mConnectionType = new AndroidConnectionType(applicationContext);
    instance.mAnalyticsServer = new AnalyticsServerSender(
            new AnalyticsService().getService(),
            analyticsKey,
            new PostFailedCallback() {
              @Override public void onBadPost(int code, D360AnalyticsPayload payload) {
                instance.mFailureDatabase.add(code, payload);
              }
            });
  }

  private static void checkIfInitialised() throws AnalyticsInitFailureException {
    if(instance==null) throw new AnalyticsInitFailureException("You must initialise 360Dialog analytics with Analytics.init() first.");
  }

  public static Analytics instance() {
    checkIfInitialised();
    return instance;
  }

  public void send(String name, HashMap<String, String> optionalData) throws AnalyticsInitFailureException {
    checkIfInitialised();
    D360AnalyticsPayload ob = new D360AnalyticsPayload(name, optionalData);
    send(ob);
  }

  /**
   * Main entry point for sending events
   * We set the payload number of the payload and if it's on a WIFI or MOBILE connection
   * @param payload
   */
  public void send(D360AnalyticsPayload payload) throws AnalyticsInitFailureException {
    checkIfInitialised();
    payload.getMeta().setEventNo(mLastEventNumber.incrementEventNumber(1));
    String connectionType = mConnectionType.getConnectionType();
    if(connectionType ==ConnectionType.WIFI) {
      payload.getMeta().setFromWifi();
    } else if(connectionType==ConnectionType.MOBILE) {
      payload.getMeta().setFromMobile();
    } else {

    }
    instance.mAnalyticsServer.send(payload);
  }

}
