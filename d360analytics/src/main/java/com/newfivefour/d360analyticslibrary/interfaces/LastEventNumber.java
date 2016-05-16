package com.newfivefour.d360analyticslibrary.interfaces;

/**
 * We need to send an event number to the analytics service. This is where we find it.
 */
public interface LastEventNumber {
  int getLastEventNumber();
  int incrementEventNumber(int num);
}
