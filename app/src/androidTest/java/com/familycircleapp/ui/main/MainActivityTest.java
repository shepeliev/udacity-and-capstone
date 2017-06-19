package com.familycircleapp.ui.main;

import com.google.android.gms.maps.GoogleMap;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.pm.PackageManager;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.PermissionManager;
import com.familycircleapp.R;
import com.familycircleapp.TestApp;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.location.LocationUpdatesManager;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.map.GoogleMapService;
import com.firebase.ui.auth.KickoffActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static com.familycircleapp.matchers.CustomMatchers.toast;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public UiThreadTestRule uiThreadRule = new UiThreadTestRule();

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

  @Inject PermissionManager mockPermissionManager;
  @Inject ViewModelProvider.Factory mockFactory;
  @Inject CurrentUser mockCurrentUser;
  @Inject BatteryInfoListener mockBatteryInfoListener;
  @Inject LocationUpdatesManager mockLocationUpdatesManager;
  @Inject GoogleMapService mockGoogleMapService;
  @Mock CurrentCircleUserIdsViewModel mockCurrentCircleUserIdsViewModel;
  @Mock CircleUserViewModel mockCircleUserViewModel;

  private MutableLiveData<List<String>> mUserIdsLiveData;
  private MutableLiveData<CircleUser> mCircleUserLiveData;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
    MockitoAnnotations.initMocks(this);
    Mockito.reset(mockPermissionManager, mockFactory, mockCurrentUser, mockBatteryInfoListener,
        mockLocationUpdatesManager, mockGoogleMapService);

    mUserIdsLiveData = new MutableLiveData<>();
    mCircleUserLiveData = new MutableLiveData<>();

    when(mockCurrentUser.isAuthenticated()).thenReturn(true);
    when(mockFactory.create(CurrentCircleUserIdsViewModel.class))
        .thenReturn(mockCurrentCircleUserIdsViewModel);
    when(mockFactory.create(CircleUserViewModel.class))
        .thenReturn(mockCircleUserViewModel);
    when(mockCurrentCircleUserIdsViewModel.getUserIds()).thenReturn(mUserIdsLiveData);
    when(mockCircleUserViewModel.getCircleUser("user_1")).thenReturn(mCircleUserLiveData);
  }

  @Test
  public void shouldShowLoaderScreen_whileDataIsLoading() throws Exception {
    rule.launchActivity(null);

    onView(withId(R.id.loader_screen)).check(matches(isDisplayed()));
  }

  @Test
  public void shouldHideLoaderScreen_whenDataIsLoaded() throws Exception {
    rule.launchActivity(null);

    mUserIdsLiveData.postValue(Collections.emptyList());

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
  public void shouldShowUser_inRecyclerView() throws Throwable {
    final CircleUser user = new CircleUser(
        "John",
        77,
        "discharging",
        R.string.user_status_near,
        "1 Time Square"
    );
    mCircleUserLiveData.postValue(user);
    rule.launchActivity(null);

    uiThreadRule.runOnUiThread(() ->
        mUserIdsLiveData.setValue(Collections.singletonList("user_1")));

    onView(withId(R.id.tw_user_displayed_name)).check(matches(withText("John")));
    onView(withId(R.id.tw_user_status)).check(matches(withText("Near: 1 Time Square")));
    onView(withId(R.id.tw_battery_level)).check(matches(withText("77 %")));
  }

  @Test
  public void shouldStartBatteryInfoListener_ifUserAuthenticated() throws Exception {
    rule.launchActivity(null);

    verify(mockBatteryInfoListener).enable();
  }

  @Test
  public void shouldNotStartBatteryInfoListener_ifUserNotAuthenticated() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    verify(mockBatteryInfoListener, never()).enable();
  }

  @Test
  public void shouldStartLocationUpdates_ifUserAuthenticated_andPermissionGranted() throws Exception {
    rule.launchActivity(null);

    //noinspection unchecked
    final ArgumentCaptor<Runnable> argCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(mockPermissionManager).requestPermission(
        eq(rule.getActivity()),
        eq(Manifest.permission.ACCESS_FINE_LOCATION),
        eq(MainActivity.RC_LOCATION_PERMISSION),
        argCaptor.capture()
    );
    argCaptor.getValue().run();
    verify(mockLocationUpdatesManager).startLocationUpdates(rule.getActivity());
  }

  @Test
  public void shouldNotStartLocationUpdates_ifUserAuthenticated_butPermissionNotGranted() throws Exception {
    rule.launchActivity(null);

    verify(mockPermissionManager).requestPermission(
        eq(rule.getActivity()),
        eq(Manifest.permission.ACCESS_FINE_LOCATION),
        eq(MainActivity.RC_LOCATION_PERMISSION),
        any(Runnable.class)
    );

    verify(mockLocationUpdatesManager, never()).startLocationUpdates(any(FragmentActivity.class));
  }

  @Test
  public void onRequestPermissionResult_shouldStartLocationUpdates_ifPermissionGranted() throws Exception {
    rule.launchActivity(null);

    rule.getActivity().onRequestPermissionsResult(
        MainActivity.RC_LOCATION_PERMISSION,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        new int[]{PackageManager.PERMISSION_GRANTED}
    );

    verify(mockLocationUpdatesManager).startLocationUpdates(rule.getActivity());
  }

  @Test
  public void onRequestPermissionResult_shouldShowToast_ifPermissionNotGranted() throws Throwable {
    rule.launchActivity(null);

    uiThreadRule.runOnUiThread(() -> rule.getActivity().onRequestPermissionsResult(
        MainActivity.RC_LOCATION_PERMISSION,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        new int[]{PackageManager.PERMISSION_DENIED}
    ));

    verify(mockLocationUpdatesManager, never()).startLocationUpdates(any(FragmentActivity.class));
    onView(withText(R.string.error_location_permission_not_granted))
        .inRoot(toast())
        .check(matches(isDisplayed()));
  }

  @Test
  public void shouldNotStartLocationUpdates_ifUserNotAuthenticated() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);

    rule.launchActivity(null);

    verify(mockLocationUpdatesManager, never()).startLocationUpdates(any(FragmentActivity.class));
  }

  @Test
  public void shouldSetLifeCycleOwner_toGoogleMapService() throws Exception {
    rule.launchActivity(null);

    verify(mockGoogleMapService).setLifecycleOwner(rule.getActivity());
  }

  @Test
  public void shouldSetGoogleMap_toGoogleMapService() throws Exception {
    rule.launchActivity(null);

    verify(mockGoogleMapService).onMapReady(any(GoogleMap.class));
  }

  @Test
  public void shouldSetUserIds_toGoogleMapService() throws Throwable {
    final List<String> userIds = Collections.singletonList("user_1");
    rule.launchActivity(null);

    uiThreadRule.runOnUiThread(() -> mUserIdsLiveData.setValue(userIds));

    verify(mockGoogleMapService).putUserIds(Collections.singletonList("user_1"));
  }
}
