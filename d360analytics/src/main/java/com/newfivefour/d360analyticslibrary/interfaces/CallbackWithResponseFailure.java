package com.newfivefour.d360analyticslibrary.interfaces;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class for retrofit to make dealing with server failures easier
 * @param <T>
 */
public abstract class CallbackWithResponseFailure<T> implements Callback<T> {
  public abstract void onResponseFailure(Call<T> call, Response<T> response);

  public boolean sendToResponseFailureInstead(Call<T> call, Response<T> response) {
    if(response!=null && !response.isSuccess()) {
      onResponseFailure(call, response);
      return true;
    } else {
      return false;
    }
  }
}
