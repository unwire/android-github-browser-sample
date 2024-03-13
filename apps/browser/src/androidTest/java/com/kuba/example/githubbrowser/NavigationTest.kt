package com.kuba.example.githubbrowser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.impl.di.ServiceModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
@UninstallModules(ServiceModule::class)
class NavigationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun successfulNavigationFromSearchToContributors() {
        val fakeRepoName = FakeGithubService.fakeRepository.name

        // Fill in a query and click search
        onView(withId(com.kuba.example.projects.impl.R.id.edit_query))
            .perform(typeText(fakeRepoName))
        onView(withId(com.kuba.example.projects.impl.R.id.btn_search))
            .perform(click())

        // Perform a click on the repository card
        onView(allOf(withText(fakeRepoName), withId(com.kuba.example.projects.impl.R.id.lbl_name)))
            .perform(click())

        // Verify that we have navigated to contributors screen
        onView(withId(com.kuba.example.projects.impl.R.id.contributors_screen))
            .check(matches(isDisplayed()))
    }

    @Test
    fun successfulNavigationFromContributorsToUserRepository() {
        val fakeRepoName = FakeGithubService.fakeRepository.name

        // Fill in a query and click search
        onView(withId(com.kuba.example.projects.impl.R.id.edit_query))
            .perform(typeText(fakeRepoName))
        onView(withId(com.kuba.example.projects.impl.R.id.btn_search))
            .perform(click())

        // Perform a click on the repository card
        onView(allOf(withText(fakeRepoName), withId(com.kuba.example.projects.impl.R.id.lbl_name)))
            .perform(click())

        // Perform a click on the contributor
        val fakeLoginName = FakeGithubService.fakeUser.name
        onView(withText(fakeLoginName))
            .perform(click())

        // Verify we that have navigated to user repository screen
        onView(withId(com.kuba.example.users.impl.R.id.user_repositories_screen))
            .check(matches(isDisplayed()))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface TestModule {

        @Binds
        fun bindGithubService(githubService: FakeGithubService): GithubService
    }
}
