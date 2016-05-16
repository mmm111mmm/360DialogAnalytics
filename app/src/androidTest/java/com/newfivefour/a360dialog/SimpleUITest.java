package com.newfivefour.a360dialog;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SimpleUITest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
          MainActivity.class);

  @Test
  public void test_checkWeDontCrash() {
    onView(withId(R.id.name)).perform(clearText(), typeText("even name 101"), closeSoftKeyboard());
    onView(withId(R.id.key)).perform(clearText(), typeText("k102"), closeSoftKeyboard());
    onView(withId(R.id.value)).perform(clearText(), typeText("v103"), closeSoftKeyboard());
    onView(withId(R.id.key1)).perform(clearText(), typeText("v104"), closeSoftKeyboard());
    onView(withId(R.id.value1)).perform(clearText(), typeText("v105"), closeSoftKeyboard());
    onView(withId(R.id.button)).perform(click());
    // If it ends well, there's not been a crash
  }

}