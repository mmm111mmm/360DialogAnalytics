package com.newfivefour.d360analyticslibrary.interfaces;

import com.newfivefour.d360analyticslibrary.exceptions.DatabaseFailureException;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

public interface FailureDatabase {
  /**
   * @param code One day, we could use this to select the type of analytics retries -- i.e. skip the badly formatted ones, or send them somewhere else.
   * @throws DatabaseFailureException
   */
    void add(int code, D360AnalyticsPayload payload) throws DatabaseFailureException;
  }