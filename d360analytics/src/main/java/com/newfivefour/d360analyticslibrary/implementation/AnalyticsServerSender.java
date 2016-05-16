package com.newfivefour.d360analyticslibrary.implementation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.newfivefour.d360analyticslibrary.endpoints.AnalyticsService;
import com.newfivefour.d360analyticslibrary.exceptions.AnalyticsInitFailureException;
import com.newfivefour.d360analyticslibrary.interfaces.CallbackWithResponseFailure;
import com.newfivefour.d360analyticslibrary.interfaces.PostFailedCallback;
import com.newfivefour.d360analyticslibrary.interfaces.ServerSender;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Sends the analytics payloads to the server
 */
public class AnalyticsServerSender extends ServerSender {

  private static String TAG = AnalyticsServerSender.class.getSimpleName();
  private final AnalyticsService.AnalyticsEndPoint mService;

  public AnalyticsServerSender(@NonNull AnalyticsService.AnalyticsEndPoint service,
                               @NonNull  String analyticsKey,
                               @NonNull PostFailedCallback postFailedCallback) {
    super(analyticsKey, postFailedCallback);
    if(analyticsKey==null || analyticsKey.trim().length()==0) {
      throw new AnalyticsInitFailureException("Did you pass a valid key?");
    } else {
      mService = service;
    }
  }

  @Override public void send(@NonNull final D360AnalyticsPayload payload) {
    if(payload==null) return;
    Log.d(TAG, "In server send(), sending: " + new Gson().toJson(payload));
    mService.postAnalytic(getAnalyticsKey(), payload).enqueue(getCallback(payload));
  }

  /**
   * The class's retrofit call back on network failure, server failure or success.
   * Sends the failures to the bad post callback on a failure.
   */
  @NonNull public CallbackWithResponseFailure<ResponseBody> getCallback(@NonNull final D360AnalyticsPayload payload) {
    return new CallbackWithResponseFailure<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(super.sendToResponseFailureInstead(call, response)) return;
        Log.d(TAG, "In server send() response: " + response.raw().code());
        Object body = response.body();
        if(body!=null) {
          Log.d(TAG, "Server response: " + body);
        }
      }

      @Override public void onResponseFailure(Call call, Response response) {
        Log.d(TAG, "In network server send() failure: " + response.code());
        getPostFailedCallback().onBadPost(response.code(), payload);
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        Log.d(TAG, "In network server send() failure: " + t);
        getPostFailedCallback().onBadPost(-1, payload);
      }
    };
  }
}
