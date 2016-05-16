package com.newfivefour.d360analyticslibrary.interfaces;

import com.newfivefour.d360analyticslibrary.exceptions.DatabaseFailureException;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import java.util.List;

public interface RemoveAndRetrieveFailedSends {
  List<D360AnalyticsPayload> removeFailurePayloads() throws DatabaseFailureException;
}
