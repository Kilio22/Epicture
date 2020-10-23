package com.epitech.epicture

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.epitech.epicture.ui.search.SearchFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class SearchFragmentTest {
    @Test
    fun enableAdvancedSearch_AdvancedFieldsDisplayedInUi() {
        launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_Ouimgur)

        onView(withId(R.id.advanced_search_switch)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.qany_query_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.qexactly_query_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.file_type_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.sort_by_spinner)).check(matches(isDisplayed()))
    }
}