package com.newfivefour.d360analyticslibrary.implementation;

import com.newfivefour.d360analyticslibrary.endpoints.AnalyticsService;
import com.newfivefour.d360analyticslibrary.exceptions.AnalyticsInitFailureException;
import com.newfivefour.d360analyticslibrary.interfaces.PostFailedCallback;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsServerSenderTests {

  AnalyticsServerSender sender;
  @Mock PostFailedCallback postFailedCallback;
  @Mock AnalyticsService.AnalyticsEndPoint service;
  @Mock Call<ResponseBody> call;
  Response<ResponseBody> response500Error = Response.<ResponseBody>error(500, new ResponseBody() {
    @Override public MediaType contentType() {return null;}
    @Override public long contentLength() {return 0;}
    @Override public BufferedSource source() {return null;}
  });
  Response<ResponseBody> response200 = Response.<ResponseBody>success(new ResponseBody() {
    @Override public MediaType contentType() {return null;}
    @Override public long contentLength() {return 0;}
    @Override public BufferedSource source() {return null;}
  });

  @Before
  public void before() {
    sender = new AnalyticsServerSender(
            service, "key", postFailedCallback);
  }

  @Test
  public void run_throwExceptionOnBadKey() throws Exception {
    try {
      sender = new AnalyticsServerSender(
              service, "", postFailedCallback);
    } catch (AnalyticsInitFailureException e) {
      assertTrue("Found exception", true);
      return;
    }
    assertTrue("Wanted exception", false);
  }

  @Test
  public void run_exitOnNullPayload() throws Exception {
    sender.send(null);
    verify(service, times(0)).postAnalytic("key", null);
  }

  @Test
  public void run_sendToServerOnGoodPayload() throws Exception {
    D360AnalyticsPayload ob = new D360AnalyticsPayload("hi", null);
    when(service.postAnalytic("key", ob)).thenReturn(call);
    sender.send(ob);
    verify(service, times(1)).postAnalytic("key", ob);
  }

  @Test
  public void run_sendToFailureDatabaseOnServerUnknownHostFailure() throws Exception {
    final D360AnalyticsPayload ob = new D360AnalyticsPayload("hi", null);
    doAnswer(new Answer<Object>() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        sender.getCallback(ob).onFailure(call, new UnknownHostException());
        return null;
      }
    }).when(call).enqueue(any(Callback.class));
    when(service.postAnalytic("key", ob)).thenReturn(call);
    sender.send(ob);
    verify(postFailedCallback, times(1)).onBadPost(-1, ob);
  }

  @Test
  public void run_sendToFailureDatabaseOn500Failure() throws Exception {
    final D360AnalyticsPayload ob = new D360AnalyticsPayload("hi", null);
    doAnswer(new Answer<Object>() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        sender.getCallback(ob).onResponseFailure(call, response500Error);
        return null;
      }
    }).when(call).enqueue(any(Callback.class));
    when(service.postAnalytic("key", ob)).thenReturn(call);
    sender.send(ob);
    verify(postFailedCallback, times(1)).onBadPost(500, ob);
  }

  @Test
  public void run_parseBodyOnGoodResponse() throws Exception {
    final D360AnalyticsPayload ob = new D360AnalyticsPayload("hi", null);
    doAnswer(new Answer<Object>() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        sender.getCallback(ob).onResponse(call, response200);
        return null;
      }
    }).when(call).enqueue(any(Callback.class));
    when(service.postAnalytic("key", ob)).thenReturn(call);
    sender.send(ob);
  }

}