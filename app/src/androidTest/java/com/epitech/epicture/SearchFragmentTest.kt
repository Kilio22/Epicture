package com.epitech.epicture

import android.view.KeyEvent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.epitech.epicture.model.GalleryImage
import com.epitech.epicture.model.ListDataResponse
import com.epitech.epicture.service.ImgurService
import com.epitech.epicture.ui.image_details.CommentListAdapter
import com.epitech.epicture.ui.search.SearchFragment
import com.epitech.epicture.ui.search.SearchFragmentDirections
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class SearchFragmentTest {
    private val imageList = listOf(
        GalleryImage("0", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 43, 3, null, "image/jpeg", null, 9, 9, true, null, true),
        GalleryImage("1", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 430, 13, null, "image/jpeg", null, 90, 1, true, null, true),
        GalleryImage("2", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 4300, 31, null, "image/jpeg", null, 19, 19, true, null, true),
        GalleryImage("3", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 4, 1, null, "image/jpeg", null, 91, 9, true, null, true)
    )

    @Test
    fun enableAdvancedSearch_AdvancedFieldsDisplayedInUi() {
        launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_Ouimgur)

        onView(withId(R.id.advanced_search_switch)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.qany_query_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.qexactly_query_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.file_type_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.sort_by_spinner)).check(matches(isDisplayed()))
    }

    @Test
    fun simpleSearch_CallMade() {
        mockkObject(ImgurService)
        coEvery { ImgurService.simpleSearch(0, "cats") } returns ListDataResponse(emptyList())
        launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_Ouimgur)

        onView(withId(R.id.base_query_input)).perform(typeText("cats"))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        coVerify(exactly = 1) {
            ImgurService.simpleSearch(0, "cats")
        }
    }

    @Test
    fun simpleSearch_ClickOnImage() {
        val iImage = 0
        val query = "cats"
        HomeActivityData.imgurCredentials = null
        mockkObject(ImgurService)
        val mockNavController = mockkClass(NavController::class)
        coEvery { ImgurService.simpleSearch(0, query) } returns ListDataResponse(imageList)
        coEvery { ImgurService.simpleSearch(1, query) } returns ListDataResponse(emptyList())
        coEvery { mockNavController.navigate(SearchFragmentDirections.actionNavigationSearchToImageDetailsFragment(imageList[iImage].id)) } returns Unit
        val searchScenario = launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_Ouimgur)
        searchScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.base_query_input)).perform(typeText(query))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.search_list)).perform(actionOnItemAtPosition<CommentListAdapter.CommentViewHolder>(iImage, clickChildViewWithId(R.id.imgur_image)))
        coVerify(exactly = 1) {
            ImgurService.simpleSearch(0, query)
        }
        coVerify(exactly = 1) {
            ImgurService.simpleSearch(1, query)
        }
        coVerify(exactly = 1) {
            mockNavController.navigate(SearchFragmentDirections.actionNavigationSearchToImageDetailsFragment(imageList[iImage].id))
        }
    }
}