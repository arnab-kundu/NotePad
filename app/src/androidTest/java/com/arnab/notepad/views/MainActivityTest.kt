package com.arnab.notepad.views.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.arnab.notepad.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun createNewNoteTest() {
        delay()
        Espresso.onView(withId(R.id.bt_add_note)).perform(ViewActions.click())
        delay()
        Espresso.onView(withId(R.id.et_subject)).perform(ViewActions.clearText(), ViewActions.typeText("Title"), ViewActions.pressImeActionButton())
        delay()
        Espresso.onView(withId(R.id.et_description))
            .perform(ViewActions.clearText(), ViewActions.typeText("This is a really long description"), ViewActions.pressImeActionButton())
        delay()
        Espresso.onView(withId(R.id.floatingActionButton)).perform(ViewActions.click())
        delay()
    }

    @After
    fun tearDown() {

    }

    private fun delay() {
        try {
            Thread.sleep(300)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}