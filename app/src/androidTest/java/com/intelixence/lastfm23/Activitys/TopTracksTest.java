package com.intelixence.lastfm23.Activitys;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.intelixence.lastfm23.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TopTracksTest {

    @Rule
    public ActivityTestRule<Core> mActivityTestRule = new ActivityTestRule<>(Core.class);

    @Test
    public void topTracksTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.ftt_et_search),
                        childAtPosition(
                                allOf(withId(R.id.ftt_cl_pagination),
                                        childAtPosition(
                                                withId(R.id.frameLayout2),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("do"), closeSoftKeyboard());

        ViewInteraction view = onView(
                allOf(withId(R.id.constraintLayout2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ftt_lv_top_tracks),
                                        0),
                                0),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.ftt_et_search), withText("do"),
                        childAtPosition(
                                allOf(withId(R.id.ftt_cl_pagination),
                                        childAtPosition(
                                                withId(R.id.frameLayout2),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(""));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.ftt_et_search),
                        childAtPosition(
                                allOf(withId(R.id.ftt_cl_pagination),
                                        childAtPosition(
                                                withId(R.id.frameLayout2),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction view2 = onView(
                allOf(withId(R.id.constraintLayout2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ftt_lv_top_tracks),
                                        0),
                                0),
                        isDisplayed()));
        view2.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
