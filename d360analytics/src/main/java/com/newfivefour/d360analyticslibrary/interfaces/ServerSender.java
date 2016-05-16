package com.newfivefour.d360analyticslibrary.interfaces;

public abstract class ServerSender implements ServerSend {
  private String mAnalyticsKey;
  private final PostFailedCallback mPostFailedCallback;

  public ServerSender(String analyticsKey, PostFailedCallback postFailedCallback) {
    mAnalyticsKey = analyticsKey;
    mPostFailedCallback = postFailedCallback;
  }

  public PostFailedCallback getPostFailedCallback() {
    return mPostFailedCallback;
  }

  public String getAnalyticsKey() {
    return mAnalyticsKey;
  }

}
