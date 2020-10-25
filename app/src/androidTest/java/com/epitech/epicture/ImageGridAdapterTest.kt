package com.epitech.epicture

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.epitech.epicture.model.GalleryImage
import com.epitech.epicture.model.ListDataResponse
import com.epitech.epicture.service.ImgurService
import com.epitech.epicture.ui.favorites.FavoritesFragment
import com.epitech.epicture.ui.favorites.FavoritesFragmentDirections
import com.epitech.epicture.ui.image_details.CommentListAdapter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageGridAdapterTest {
    private val imageList = listOf(
            GalleryImage("0", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 43, 3, null, "image/jpeg", null, 9, 9, true, null, true),
            GalleryImage("1", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 430, 13, null, "image/jpeg", null, 90, 1, true, null, true),
            GalleryImage("2", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 4300, 31, null, "image/jpeg", null, 19, 19, true, null, true),
            GalleryImage("3", "Sad", null, false, "https://i.imgur.com/Sd1Tc3H.jpg", 4, 1, null, "image/jpeg", null, 91, 9, true, null, true)
    )

    @Test
    fun loadGrid_ClickOnImage() {
        val iImage = 0
        HomeActivityData.imgurCredentials = null
        mockkObject(ImgurService)
        val mockNavController = mockkClass(NavController::class)
        coEvery { ImgurService.getUserFavorites("", "", 0, "newest") } returns ListDataResponse(imageList)
        coEvery { ImgurService.getUserFavorites("", "", 1, "newest") } returns ListDataResponse(listOf())
        coEvery { mockNavController.navigate(FavoritesFragmentDirections.actionNavigationFavoritesToImageDetailsFragment(imageList[iImage].id)) } returns Unit
        val favoriteScenario = launchFragmentInContainer<FavoritesFragment>(themeResId = R.style.Theme_Ouimgur)
        favoriteScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        for (i in imageList.indices) {
            onView(withRecyclerView(R.id.favorites_list)
                    .atPositionOnView(i, R.id.favorite_tag))
                    .check(matches(isDisplayed()))
        }
        onView(withId(R.id.favorites_list)).perform(actionOnItemAtPosition<CommentListAdapter.CommentViewHolder>(iImage, clickChildViewWithId(R.id.imgur_image)))
        coVerify(exactly = 1) {
            ImgurService.getUserFavorites("", "", 0, "newest")
        }
        coVerify(exactly = 1) {
            ImgurService.getUserFavorites("", "", 1, "newest")
        }
        coVerify(exactly = 1) {
            mockNavController.navigate(FavoritesFragmentDirections.actionNavigationFavoritesToImageDetailsFragment(imageList[iImage].id))
        }
    }
}