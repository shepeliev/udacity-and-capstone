package com.familycircleapp.ui.newuser;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.R;
import com.familycircleapp.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.NewUserActivity;
import com.familycircleapp.ui.common.CreateCircleViewModel;
import com.familycircleapp.ui.common.JoinCircleViewModel;
import com.familycircleapp.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.familycircleapp.matchers.CustomMatchers.toast;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewUserActivityTest {

  @Rule
  public IntentsTestRule<NewUserActivity> rule =
      new IntentsTestRule<>(NewUserActivity.class, false, false);

  @Inject CurrentUser mockCurrentUser;
  @Inject UserRepository mockUserRepository;
  @Inject ViewModelProvider.Factory mockViewModelFactory;
  @Mock private JoinCircleViewModel mockJoinCircleViewModel;
  @Mock private CreateCircleViewModel mockCreateCircleViewModel;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
    MockitoAnnotations.initMocks(this);

    when(mockViewModelFactory.create(JoinCircleViewModel.class))
        .thenReturn(mockJoinCircleViewModel);
    when(mockViewModelFactory.create(CreateCircleViewModel.class))
        .thenReturn(mockCreateCircleViewModel);

    final MutableLiveData<String> joinResult = new MutableLiveData<>();
    final MutableLiveData<String> createResult = new MutableLiveData<>();
    when(mockJoinCircleViewModel.getResult()).thenReturn(joinResult);
    when(mockCreateCircleViewModel.getResult()).thenReturn(createResult);


    final MutableLiveData<String> joinFail = new MutableLiveData<>();
    final MutableLiveData<String> createFail = new MutableLiveData<>();
    when(mockJoinCircleViewModel.getError()).thenReturn(joinFail);
    when(mockCreateCircleViewModel.getError()).thenReturn(createFail);
  }

  @Test
  public void shouldSaveDisplayName() throws Exception {
    when(mockCurrentUser.getId()).thenReturn("user_1");
    when(mockCurrentUser.getDisplayName()).thenReturn("John");

    rule.launchActivity(null);

    verify(mockUserRepository).saveDisplayName("user_1", "John");
  }

  @Test
  public void shouldSaveDisplayNameAsNA_ifCurrentUserDisplayNameIsNull() throws Exception {
    when(mockCurrentUser.getId()).thenReturn("user_1");
    when(mockCurrentUser.getDisplayName()).thenReturn(null);

    rule.launchActivity(null);

    verify(mockUserRepository).saveDisplayName("user_1", "N/A");
  }

  @Test
  public void testLoaderScreen() throws Exception {
    final ObservableBoolean isVisibleJoin = new ObservableBoolean();
    final ObservableBoolean isVisibleCreate = new ObservableBoolean();
    rule.launchActivity(null);

    isVisibleJoin.set(false);
    isVisibleCreate.set(false);
    onView(withId(R.id.loader_screen)).check(matches(not(isDisplayed())));
    onView(withId(R.id.btn_join_to_circle)).check(matches(isEnabled()));
    onView(withId(R.id.btn_create_new_circle)).check(matches(isEnabled()));

    isVisibleJoin.set(true);
    isVisibleCreate.set(false);
    TimeUnit.SECONDS.sleep(1);
    onView(withId(R.id.loader_screen)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_join_to_circle)).check(matches(not(isEnabled())));
    onView(withId(R.id.btn_create_new_circle)).check(matches(not(isEnabled())));

    isVisibleJoin.set(false);
    isVisibleCreate.set(true);
    TimeUnit.SECONDS.sleep(1);
    onView(withId(R.id.loader_screen)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_join_to_circle)).check(matches(not(isEnabled())));
    onView(withId(R.id.btn_create_new_circle)).check(matches(not(isEnabled())));

    isVisibleJoin.set(true);
    isVisibleCreate.set(true);
    TimeUnit.SECONDS.sleep(1);
    onView(withId(R.id.loader_screen)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_join_to_circle)).check(matches(not(isEnabled())));
    onView(withId(R.id.btn_create_new_circle)).check(matches(not(isEnabled())));
  }

  @Test
  public void testInviteCodeEditText() throws Exception {
    final ObservableField<String> inviteCode = new ObservableField<>();
    rule.launchActivity(null);

    onView(withId(R.id.et_invite_code)).perform(typeText("XXXXYYYY"));

    assertEquals("XXXXYYYY", inviteCode.get());
  }

  @Test
  public void testJoinCircleButtonEnablity() throws Exception {
    final ObservableBoolean joinEnabled = new ObservableBoolean();
    rule.launchActivity(null);

    joinEnabled.set(false);
    onView(withId(R.id.btn_join_to_circle)).check(matches(not(ViewMatchers.isEnabled())));

    joinEnabled.set(true);
    TimeUnit.SECONDS.sleep(1);
    onView(withId(R.id.btn_join_to_circle)).check(matches(ViewMatchers.isEnabled()));
  }

  @Test
  public void testJoinCircleButtonClick() throws Exception {
    rule.launchActivity(null);

    onView(withId(R.id.btn_join_to_circle)).perform(click());

    verify(mockJoinCircleViewModel).start();
  }

  @Test
  public void testCreateCircleButtonClick() throws Exception {
    rule.launchActivity(null);

    onView(withId(R.id.btn_create_new_circle)).perform(click());

    verify(mockCreateCircleViewModel).start();
  }

  @Test
  public void testJoinCircleFailed() throws Exception {
    TimeUnit.SECONDS.sleep(1);
    final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    when(mockJoinCircleViewModel.getError()).thenReturn(errorLiveData);
    rule.launchActivity(null);

    errorLiveData.postValue("error");
    TimeUnit.SECONDS.sleep(1);

    onView(withText("error")).inRoot(toast()).check(matches(isDisplayed()));
  }

  @Test
  public void testCreateCircleFailed() throws Exception {
    final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    when(mockCreateCircleViewModel.getError()).thenReturn(errorLiveData);
    rule.launchActivity(null);

    errorLiveData.postValue("error");
    TimeUnit.SECONDS.sleep(1);

    onView(withText("error")).inRoot(toast()).check(matches(isDisplayed()));
  }

  @Test
  public void testJoinCircleSuccess() throws Exception {
    final MutableLiveData<String> resultLiveData = new MutableLiveData<>();
    when(mockJoinCircleViewModel.getResult()).thenReturn(resultLiveData);
    rule.launchActivity(null);
    intending(hasComponent(MainActivity.class.getName())).respondWith(
        new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
    );

    resultLiveData.postValue("circle_1");
    TimeUnit.SECONDS.sleep(1);

    intended(hasComponent(MainActivity.class.getName()));
    assertTrue(rule.getActivity().isFinishing());
  }

  @Test
  public void testCreateCircleSuccess() throws Exception {
    final MutableLiveData<String> resultLiveData = new MutableLiveData<>();
    when(mockCreateCircleViewModel.getResult()).thenReturn(resultLiveData);
    rule.launchActivity(null);
    intending(hasComponent(MainActivity.class.getName())).respondWith(
        new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
    );

    resultLiveData.postValue("circle_1");
    TimeUnit.SECONDS.sleep(1);

    intended(hasComponent(MainActivity.class.getName()));
    assertTrue(rule.getActivity().isFinishing());
  }
}
