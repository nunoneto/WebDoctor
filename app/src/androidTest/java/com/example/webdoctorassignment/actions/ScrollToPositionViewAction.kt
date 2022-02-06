package com.example.webdoctorassignment.actions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/**
 * Code from https://github.com/shauvik/espresso/blob/master/espresso/contrib/src/main/java/android/support/test/espresso/contrib/RecyclerViewActions.java
 */
class ScrollToPositionViewAction(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return "scroll RecyclerView to position: $position"
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.scrollToPosition(position)
    }
}