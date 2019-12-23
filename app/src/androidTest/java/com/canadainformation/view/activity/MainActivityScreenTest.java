package com.canadainformation.view.activity;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.canadainformation.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created By Akash
 * on 23,Dec,2019 : 9:48 PM
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityScreenTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mNewsActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    // More complex tests should be added as app's complexity rises
    @Test
    public void displayItemsInList() {
        // check if the RecyclerView is visible
        onView(withId(R.id.rv_info_list)).check(matches(isDisplayed()));
    }

}