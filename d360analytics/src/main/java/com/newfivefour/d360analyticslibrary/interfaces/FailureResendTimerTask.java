package com.newfivefour.d360analyticslibrary.interfaces;

import java.util.TimerTask;

/**
 * The task to be run to resend all the failed payloads
 * It checks the network availability, retrieves the payloads, and then sends them.
 * All via its dependencies.
 */
public abstract class FailureResendTimerTask extends TimerTask {
  protected ServerSend mServerSend;
  protected NetworkAvailability mNetworkAvailability;
  protected RemoveAndRetrieveFailedSends mRetrieveRemoveFailures;

  public FailureResendTimerTask(NetworkAvailability networkAvailability,
                                RemoveAndRetrieveFailedSends retrieveRemoveFailures,
                                ServerSend serverSend) {
    mNetworkAvailability = networkAvailability;
    mRetrieveRemoveFailures = retrieveRemoveFailures;
    mServerSend = serverSend;
  }
}
