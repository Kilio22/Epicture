package com.epitech.epicture

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.epitech.epicture.model.BasicDataResponse
import com.epitech.epicture.model.Comment
import com.epitech.epicture.model.GalleryImage
import com.epitech.epicture.model.ListDataResponse
import com.epitech.epicture.service.ImgurService
import com.epitech.epicture.ui.image_details.CommentListAdapter
import com.epitech.epicture.ui.image_details.ImageDetailsFragment
import com.epitech.epicture.ui.image_details.SortTypes
import com.epitech.epicture.ui.image_details.VoteStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ImageDetailsFragmentTest {
    private val testCommentList = listOf(
            Comment(0, "I am a comment", "Michel", 20, 2, null),
            Comment(1, "I am another comment", "Michel", 200, 12, null),
            Comment(2, "I am not a comment", "Michel", 2000, 24, null),
            Comment(3, "I am maybe comment", "Michel", 240, 42, null),
            Comment(4, "I am.", "Michel", 2, 25, null)
    )

    private val testGalleryImage = GalleryImage(
            id = "TEST_ID",
            title = "Sad",
            description = null,
            isAlbum = false,
            link = "https://i.imgur.com/Sd1Tc3H.jpg",
            ups = 43,
            downs = 3,
            cover = null,
            type = "image/jpeg",
            vote = null,
            commentCount = testCommentList.size,
            favoriteCount = 19,
            isFavorite = false,
            images = null,
            inGallery = true,
    )

    @Before
    fun initImageDetailsFragmentTest() {
        HomeActivityData.imgurCredentials = null
        mockkObject(ImgurService)
        coEvery { ImgurService.getImageById("", testGalleryImage.id) } returns BasicDataResponse(testGalleryImage)
        coEvery { ImgurService.getComments("", testGalleryImage.id, SortTypes.BEST.value) } returns ListDataResponse(testCommentList)
    }

    @Test
    fun loadImage_CheckDataAndDrawables() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val commentCountFormat = appContext.resources.getString(R.string.comment_count)
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_title)).check(matches(withText(testGalleryImage.title)))
        // onView(withId(R.id.details_description)).check(doesNotExist())
        onView(withId(R.id.details_description)).check(matches(not(isDisplayed())))
        onView(withId(R.id.details_comment_msg)).check(matches(withText(commentCountFormat.format(testGalleryImage.commentCount))))
        onView(withId(R.id.details_favorite_ic)).check(matches(withDrawable(R.drawable.star_favorite_black)))
        onView(withId(R.id.details_favorite_count)).check(matches(withText(testGalleryImage.favoriteCount.toString())))
        onView(withId(R.id.details_upvote_ic)).check(matches(withDrawable(R.drawable.ic_baseline_arrow_upward_24)))
        onView(withId(R.id.details_upvote_count)).check(matches(withText(testGalleryImage.ups.toString())))
        onView(withId(R.id.details_downvote_ic)).check(matches(withDrawable(R.drawable.ic_baseline_arrow_downward_24)))
        onView(withId(R.id.details_downvote_count)).check(matches(withText(testGalleryImage.downs.toString())))
        coVerify(exactly = 1) {
            ImgurService.getImageById("", testGalleryImage.id)
        }
        coVerify(exactly = 1) {
            ImgurService.getComments("", testGalleryImage.id, SortTypes.BEST.value)
        }
    }

    @Test
    fun favImage_IconAndCountChanges() {
        coEvery { ImgurService.favImage("", testGalleryImage.id) } returns Unit
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_favorite_ic)).perform(click()).check(matches(withDrawable(R.drawable.star_favorite)))
        onView(withId(R.id.details_favorite_count)).check(matches(withText((testGalleryImage.favoriteCount + 1).toString())))
        coVerify(exactly = 1) {
            ImgurService.getImageById("", testGalleryImage.id)
        }
        coVerify(exactly = 1) {
            ImgurService.favImage("", testGalleryImage.id)
        }
    }

    @Test
    fun upvoteImage_IconAndCountChanges() {
        coEvery { ImgurService.vote("", testGalleryImage.id, VoteStatus.UP.value) } returns Unit
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_upvote_ic)).perform(click()).check(matches(withDrawable(R.drawable.ic_baseline_arrow_upward_highlight_24)))
        onView(withId(R.id.details_upvote_count)).check(matches(withText((testGalleryImage.ups + 1).toString())))
        coVerify(exactly = 1) {
            ImgurService.getImageById("", testGalleryImage.id)
        }
        coVerify(exactly = 1) {
            ImgurService.vote("", testGalleryImage.id, VoteStatus.UP.value)
        }
    }

    @Test
    fun downvoteImage_IconAndCountChanges() {
        coEvery { ImgurService.vote("", testGalleryImage.id, VoteStatus.DOWN.value) } returns Unit
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_downvote_ic)).perform(click())
                .check(matches(withDrawable(R.drawable.ic_baseline_arrow_downward_highlight_24)))
        onView(withId(R.id.details_downvote_count)).check(matches(withText((testGalleryImage.downs + 1).toString())))
        coVerify(exactly = 1) {
            ImgurService.getImageById("", testGalleryImage.id)
        }
        coVerify(exactly = 1) {
            ImgurService.vote("", testGalleryImage.id, VoteStatus.DOWN.value)
        }
    }

    @Test
    fun loadComments_CheckDataAndDrawables() {
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        for (i in testCommentList.indices) {
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_author))
                    .check(matches(withText(testCommentList[i].author)))
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_upvote_ic))
                    .check(matches(withDrawable(R.drawable.ic_baseline_arrow_upward_24)))
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_upvote_count))
                    .check(matches(withText(testCommentList[i].ups.toString())))
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_downvote_ic))
                    .check(matches(withDrawable(R.drawable.ic_baseline_arrow_downward_24)))
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_downvote_count))
                    .check(matches(withText(testCommentList[i].downs.toString())))
            onView(withRecyclerView(R.id.details_comments)
                    .atPositionOnView(i, R.id.comment_content))
                    .check(matches(withText(testCommentList[i].comment)))
        }
        coVerify(exactly = 1) {
            ImgurService.getComments("", testGalleryImage.id, SortTypes.BEST.value)
        }
    }

    @Test
    fun upvoteComment_IconAndCountChanges() {
        val iComment = 2
        val ups = testCommentList[iComment].ups
        coEvery { ImgurService.voteComment("", testCommentList[iComment].id.toString(), VoteStatus.UP.value) } returns Unit
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_comments)).perform(actionOnItemAtPosition<CommentListAdapter.CommentViewHolder>(iComment, clickChildViewWithId(R.id.comment_upvote_ic)))
        onView(withRecyclerView(R.id.details_comments)
                .atPositionOnView(iComment, R.id.comment_upvote_ic))
                .check(matches(withDrawable(R.drawable.ic_baseline_arrow_upward_highlight_24)))
        onView(withRecyclerView(R.id.details_comments)
                .atPositionOnView(iComment, R.id.comment_upvote_count))
                .check(matches(withText((ups + 1).toString())))
        coVerify(exactly = 1) {
            ImgurService.getComments("", testGalleryImage.id, SortTypes.BEST.value)
        }
        coVerify(exactly = 1) {
            ImgurService.voteComment("", testCommentList[iComment].id.toString(), VoteStatus.UP.value)
        }
    }

    @Test
    fun downvoteComment_IconAndCountChanges() {
        val iComment = 2
        val downs = testCommentList[iComment].downs
        coEvery { ImgurService.voteComment("", testCommentList[iComment].id.toString(), VoteStatus.DOWN.value) } returns Unit
        launchFragmentInContainer<ImageDetailsFragment>(themeResId = R.style.Theme_Ouimgur, fragmentArgs = bundleOf("imageId" to testGalleryImage.id))

        onView(withId(R.id.details_comments)).perform(actionOnItemAtPosition<CommentListAdapter.CommentViewHolder>(iComment, clickChildViewWithId(R.id.comment_downvote_ic)))
        onView(withRecyclerView(R.id.details_comments)
                .atPositionOnView(iComment, R.id.comment_downvote_ic))
                .check(matches(withDrawable(R.drawable.ic_baseline_arrow_downward_highlight_24)))
        onView(withRecyclerView(R.id.details_comments)
                .atPositionOnView(iComment, R.id.comment_downvote_count))
                .check(matches(withText((downs + 1).toString())))
        coVerify(exactly = 1) {
            ImgurService.getComments("", testGalleryImage.id, SortTypes.BEST.value)
        }
        coVerify(exactly = 1) {
            ImgurService.voteComment("", testCommentList[iComment].id.toString(), VoteStatus.DOWN.value)
        }
    }
}
