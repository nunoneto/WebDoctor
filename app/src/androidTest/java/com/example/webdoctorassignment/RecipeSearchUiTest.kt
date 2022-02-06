package com.example.webdoctorassignment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.webdoctorassignment.actions.ScrollToPositionViewAction
import com.example.webdoctorassignment.core.MockServerDispatcher
import com.example.webdoctorassignment.di.BaseUrlModule
import com.example.webdoctorassignment.ui.RecipeSearchFragment
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(BaseUrlModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RecipeSearchUiTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var okHttp: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttp))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldSearchForRecipesAndShowResults() {
        launchFragmentInHiltContainer<RecipeSearchFragment>()

        onView(allOf(withId(R.id.query_edit_text), withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(typeText("potato"), pressImeActionButton())

        hasRecipeItemAtPosition(
            0,
            title = "Baked Potato Snack",
            author = "by Martha Stewart",
            calories = "195.4 calories",
            time = "0 minutes"
        )
        hasRecipeItemAtPosition(
            1,
            title = "Sweet potato crisps",
            author = "by BBC Good Food",
            calories = "80.6 calories",
            time = "30 minutes"
        )
        hasRecipeItemAtPosition(
            2,
            title = "Sweet Potato Smoothie",
            author = "by Food52",
            calories = "254.0 calories",
            time = "0 minutes"
        )
        hasRecipeItemAtPosition(
            3,
            title = "Crisp Potato Galettes",
            author = "by Epicurious",
            calories = "170.8 calories",
            time = "0 minutes"
        )
    }

    private fun hasRecipeItemAtPosition(
        position: Int,
        title: String,
        author: String,
        calories: String,
        time: String,
    ) {
        onView(withId(R.id.recipe_list)).apply {
            perform(ScrollToPositionViewAction(position))
            check(
                ViewAssertions.matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.title),
                            withText(title)
                        )
                    )
                )
            )
            check(
                ViewAssertions.matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.author),
                            withText(author)
                        )
                    )
                )
            )
            check(
                ViewAssertions.matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.calories),
                            withText(calories)
                        )
                    )
                )
            )
            check(
                ViewAssertions.matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.time),
                            withText(time)
                        )
                    )
                )
            )
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class FakeBaseUrlModule {

        @Provides
        fun providesBaseUrl(): String = "http://localhost:8080"
    }
}