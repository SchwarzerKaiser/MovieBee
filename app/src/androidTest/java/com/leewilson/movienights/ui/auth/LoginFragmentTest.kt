package com.leewilson.movienights.ui.auth

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.leewilson.movienights.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginFragmentTest {

    private val themeId = R.style.Theme_MaterialComponents

    @Test
    fun test_fragmentLaunched_viewIsShown() {
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = themeId)
        onView(withId(R.id.login_fragment_root_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_registerLinkClicked_navigatesToRegisterFragment() {

        val controller = TestNavHostController(ApplicationProvider.getApplicationContext())
        controller.setGraph(R.navigation.auth_nav_graph)
        val loginFragmentScenario = launchFragmentInContainer<LoginFragment>(themeResId = themeId)

        loginFragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), controller)
        }

        onView(withId(R.id.auth_register_text_link)).perform(click())
        assertEquals(controller.currentDestination?.id, R.id.registerFragment)
    }
}