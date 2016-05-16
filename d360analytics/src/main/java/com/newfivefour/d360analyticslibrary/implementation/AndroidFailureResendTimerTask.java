package com.newfivefour.d360analyticslibrary.implementation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.newfivefour.d360analyticslibrary.interfaces.FailureResendTimerTask;
import com.newfivefour.d360analyticslibrary.interfaces.NetworkAvailability;
import com.newfivefour.d360analyticslibrary.interfaces.RemoveAndRetrieveFailedSends;
import com.newfivefour.d360analyticslibrary.interfaces.ServerSend;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import java.util.List;

/**
 * Timer task that, on a good network, retrieves all the failed payloads from the database and
 * tries to send them again.
 */
public class AndroidFailureResendTimerTask extends FailureResendTimerTask {
  private static String TAG = AndroidFailureResendTimerTask.class.getSimpleName();
  public AndroidFailureResendTimerTask(@NonNull NetworkAvailability networkAvailability,
                                       @NonNull RemoveAndRetrieveFailedSends removeAndRetrieveFailedSends,
                                       @NonNull ServerSend serverSend) {
    super(networkAvailability, removeAndRetrieveFailedSends, serverSend);
  }

  @Override
  public void run() {
    Log.d(TAG, "Running resender timer task");
    if (!mNetworkAvailability.isNetworkAvailable()) return;
    List<D360AnalyticsPayload> failures = mRetrieveRemoveFailures.removeFailurePayloads();
    if(failures!=null) {
      for (D360AnalyticsPayload payload : failures) {
        mServerSend.send(payload);
      }
    }
  }
}