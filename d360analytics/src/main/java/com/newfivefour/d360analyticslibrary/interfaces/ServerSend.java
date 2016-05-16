package com.newfivefour.d360analyticslibrary.interfaces;

import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

/**
 * Let's send our analytics payload to the server.
 */
public interface ServerSend {
    void send(D360AnalyticsPayload payload);
}