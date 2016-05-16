package com.newfivefour.d360analyticslibrary.implementation;

import com.newfivefour.d360analyticslibrary.interfaces.LastEventNumber;

/**
 * A tracked sequential event number for the payloads.
 *
 * This should be changed, but there are questions about the server handling of duplicate number,
 * since there's no way, relying on Android's HDD alone, that we can guarantee such.
 */
public class SimpleLastEventNumber implements LastEventNumber {
  private int number = 0;

  @Override public int getLastEventNumber() {
    return number;
  }

  @Override public int incrementEventNumber(int num) {
    return ++number;
  }
}
