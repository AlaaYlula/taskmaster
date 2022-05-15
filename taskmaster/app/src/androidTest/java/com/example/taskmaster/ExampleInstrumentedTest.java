package com.example.taskmaster;

import android.content.Context;

import androidx.test.espresso.Espresso;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
/////////////////


import androidx.test.rule.ActivityTestRule;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmaster", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<> (MainActivity.class);
    @Test
    public void changeText_Setting_newActivity() {

        openActionBarOverflowOrOptionsMenu(mainActivity.getActivity());
        onView(withText("setting")).perform(click());
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("Alaa"));
        onView(withId(R.id.submit)).perform(click());
        Espresso.onView(withText("Alaa")).check(matches(isDisplayed()));

    }
    @Test
    public void OnclickTask() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.title)).check(matches(withText("Read")));

    }

    @Test
    public void addingTask() {
        // Type text and then press the button.
        Espresso.onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("Task2"));
        onView(withId(R.id.doTask)).perform(clearText());
        onView(withId(R.id.doTask)).perform(typeText("Task2 Description"));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.button)).perform(click());
        Espresso.onView(withText("Task2")).check(matches(isDisplayed()));
    }


}