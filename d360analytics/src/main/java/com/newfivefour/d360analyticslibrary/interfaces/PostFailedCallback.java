package com.newfivefour.d360analyticslibrary.interfaces;

import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

/**
 * If we fail to send a payload, we should call this method.
 * The main analytics calls implements it, and calls the failure
 * database to store the failures.
 */
public interface PostFailedCallback {
    void onBadPost(int code, D360AnalyticsPayload payload);
  }
