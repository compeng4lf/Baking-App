package com.android.bakingapp;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StepsActivityTest {



    @Rule
    public ActivityTestRule<RecipeStepsActivity> mActivityRule
            = new ActivityTestRule<>(RecipeStepsActivity.class);

    @Test
    public void checkToggle() {

        //Check to make sure with click on the textview that the ingredient list isdisplayed.
        onView(withId(R.id.textView)).perform(click());

        //check if textview is displayed after click
        onView(withId(R.id.tv_Ingredients)).check(matches(isDisplayed()));
    }

}
