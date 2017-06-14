package com.familycircleapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.matchers.ToastMatcher;
import com.familycircleapp.mocks.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.main.MainActivity;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.KickoffActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class EntryPointActivityTest {

  @Inject CurrentUser mockCurrentUser;
  private int mLoginResultCode = Activity.RESULT_CANCELED;
  private Intent mLoginResultIntent = null;

  @Rule
  public IntentsTestRule<EntryPointActivity> rule =
      new IntentsTestRule<EntryPointActivity>(EntryPointActivity.class, false, false) {
        @Override
        protected void beforeActivityLaunched() {
          super.beforeActivityLaunched();
          Intents.init();
          intending(hasComponent(KickoffActivity.class.getName())).respondWith(
              new Instrumentation.ActivityResult(mLoginResultCode, mLoginResultIntent)
          );
        }

        @Override
        protected void afterActivityLaunched() {
        }
      };

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
  }

  @Test
  public void shouldStartLoginActivity_ifUserNotAuthenticated() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    intended(hasComponent(KickoffActivity.class.getName()));
    assertTrue(rule.getActivity().isFinishing());
  }

  @Test
  public void shouldStartMainActivityAndFinish_ifUserAuthenticated() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(true);

    rule.launchActivity(null);

    intended(hasComponent(MainActivity.class.getName()));
  }

  @Test
  public void shouldStartMainActivityAndFinish_ifAuthenticationSucceed() throws Exception {
    mLoginResultCode = Activity.RESULT_OK;
    mLoginResultIntent = IdpResponse.getIntent(new IdpResponse("firebase", "user@email.com"));
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    intended(hasComponent(MainActivity.class.getName()));
    assertTrue(rule.getActivity().isFinishing());
  }

  @Test
  public void shouldShowToast_ifLoggingFailed_noNetwork() throws Exception {
    mLoginResultCode = Activity.RESULT_OK;
    mLoginResultIntent = IdpResponse.getErrorCodeIntent(ErrorCodes.NO_NETWORK);
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    TimeUnit.SECONDS.sleep(1);
    onView(withText(R.string.error_login_no_internet_connection))
        .inRoot(new ToastMatcher())
        .check(matches(isDisplayed()));
  }

  @Test
  public void shouldShowToast_ifLoggingFailed_unknownError() throws Exception {
    mLoginResultCode = Activity.RESULT_OK;
    mLoginResultIntent = IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR);
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    TimeUnit.SECONDS.sleep(1);
    onView(withText(R.string.error_login_unknown_error))
        .inRoot(new ToastMatcher())
        .check(matches(isDisplayed()));
  }

  @Test
  public void shouldFinish_ifSigningInCancelled() throws Exception {
    mLoginResultCode = Activity.RESULT_OK;
    mLoginResultIntent = null;

    rule.launchActivity(null);

    assertTrue(rule.getActivity().isFinishing());
  }

  @Test
  public void shouldFinish_ifSigningInActivityCancelled() throws Exception {
    mLoginResultCode = Activity.RESULT_CANCELED;
    mLoginResultIntent = null;

    rule.launchActivity(null);

    assertTrue(rule.getActivity().isFinishing());
  }
}
