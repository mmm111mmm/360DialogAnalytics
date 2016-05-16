package com.newfivefour.d360analyticslibrary.endpoints;

import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Endpoint for posting to the analytics end point
 */
public class AnalyticsService {

  public interface AnalyticsEndPoint {
    @POST("v2/events")
    Call<ResponseBody> postAnalytic(@Header("D360-Api-Key") String key, @Body D360AnalyticsPayload payload);
  }

  public AnalyticsEndPoint getService() {
    return new Retrofit.Builder() //http://api.dev.staging.crm.slace.me/
            .baseUrl("http://api.dev.staging.crm.slace.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient())
            .build()
            .create(AnalyticsEndPoint.class);
  }
}


