package com.newfivefour.d360analyticslibrary.pojos;

import java.util.Date;
import java.util.HashMap;

public class D360AnalyticsPayload {

  public D360AnalyticsPayload(String name, HashMap<String, String> map) {
    getMeta().setLocalTimeStamp(new Date().getTime());
    getMeta().setName(name);
    data = map;
    //TODO is from WIFI
  }
  private HashMap<String, String> data = new HashMap<>();
  private Meta meta = new Meta();

  public HashMap<String, String> getData() {
    return data;
  }

  public void addData(String key, String value) {
    this.data.put(key, value);
  }

  public Meta getMeta() {
    return meta;
  }

  public static class Meta {
    private long eventNo;
    private long localTimeStamp;
    private String name;
    private String connectionInfo;

    public long getEventNo() {
      return eventNo;
    }

    public void setEventNo(long eventNo) {
      this.eventNo = eventNo;
    }

    public long getLocalTimeStamp() {
      return localTimeStamp;
    }

    public void setLocalTimeStamp(long localTimeStamp) {
      this.localTimeStamp = localTimeStamp;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getConnectionInfo() {
      return connectionInfo;
    }

    public void setFromWifi() {
      this.connectionInfo = "WIFI";
    }

    public void setFromMobile() {
      this.connectionInfo = "MOBILE";
    }

  }
}
