package com.familycircleapp.ui.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.R;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.mocks.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.firebase.ui.auth.KickoffActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public IntentsTestRule<MainActivity> rule =
      new IntentsTestRule<MainActivity>(MainActivity.class, false, false) {
        @Override
        protected void beforeActivityLaunched() {
          super.beforeActivityLaunched();
          Intents.init();
          intending(hasComponent(KickoffActivity.class.getName()))
              .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null));
        }

        @Override
        protected void afterActivityLaunched() {
          rule.getActivity().runOnUiThread(() ->
              rule.getActivity().findViewById(R.id.loader_progress_bar).setVisibility(View.GONE)
          );
        }
      };

  @Inject ViewModelProvider.Factory mockFactory;
  @Inject CurrentUser mockCurrentUser;
  @Inject BatteryInfoListener mockBatteryInfoListener;
  @Mock CurrentCircleUsersViewModel mockCurrentCircleUsersViewModel;

  private MutableLiveData<List<LiveData<CircleUser>>> mUsersLiveData;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
    MockitoAnnotations.initMocks(this);

    mUsersLiveData = new MutableLiveData<>();

    when(mockCurrentUser.isAuthenticated()).thenReturn(true);
    when(mockFactory.create(CurrentCircleUsersViewModel.class))
        .thenReturn(mockCurrentCircleUsersViewModel);
    when(mockCurrentCircleUsersViewModel.getUsers()).thenReturn(mUsersLiveData);
  }

  @Test
  public void shouldShowLoaderScreen_whileDataIsLoading() throws Exception {
    rule.launchActivity(null);

    onView(withId(R.id.loader_screen)).check(matches(isDisplayed()));
  }

  @Test
  public void shouldHideLoaderScreen_whenDataIsLoaded() throws Exception {
    rule.launchActivity(null);

    mUsersLiveData.postValue(Collections.emptyList());

    onView(withId(R.id.loader_screen)).check(matches(not(isDisplayed())));
  }

  @Test
  public void shouldStartEntryPointActivityAndFinish_ifUserNotAuthenticated() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    intended(hasComponent(EntryPointActivity.class.getName()));
    assertTrue(rule.getActivity().isFinishing());
  }

  @Test
  public void shouldShowUser_inRecyclerView() throws Exception {
    final CircleUser user = new CircleUser(
        "John",
        77,
        "discharging",
        R.string.user_status_near,
        "1 Time Square"
    );
    final MutableLiveData<CircleUser> userLiveData = new MutableLiveData<>();
    userLiveData.postValue(user);
    final List<LiveData<CircleUser>> users = Collections.singletonList(userLiveData);
    rule.launchActivity(null);

    mUsersLiveData.postValue(users);

    onView(withId(R.id.tw_user_displayed_name)).check(matches(withText("John")));
    onView(withId(R.id.tw_user_status)).check(matches(withText("Near: 1 Time Square")));
    onView(withId(R.id.tw_battery_level)).check(matches(withText("77 %")));
  }

  @Test
  public void shouldStartBatteryInfoListener_ifUserAuthenticated() throws Exception {
    rule.launchActivity(null);

    verify(mockBatteryInfoListener).start(rule.getActivity().getLifecycle());
  }
}
