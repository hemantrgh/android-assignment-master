package com.kisanhub.test.view.activities


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.kisanhub.test.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class WeatherActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(WeatherActivity::class.java)

    @Test
    fun weatherActivityTest() {
        onView(withId(R.id.btnFetchWeather)).perform(click())
    }
}
