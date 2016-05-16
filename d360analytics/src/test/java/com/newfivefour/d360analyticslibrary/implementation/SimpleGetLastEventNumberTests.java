package com.newfivefour.d360analyticslibrary.implementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SimpleGetLastEventNumberTests {

  SimpleLastEventNumber lastEventNumber;

  @Before
  public void before() {
    lastEventNumber = new SimpleLastEventNumber();
  }

  @Test
  public void run_getNumberAfterIncrease() throws Exception {
    assertThat(lastEventNumber.getLastEventNumber(), is(0));
    assertThat(lastEventNumber.incrementEventNumber(1), is(1));
    assertThat(lastEventNumber.getLastEventNumber(), is(1));
  }
}