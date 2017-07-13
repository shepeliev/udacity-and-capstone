package com.familycircleapp.ui.newuser;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewUserActivityTest {

  @Rule
  public IntentsTestRule<NewUserActivity> rule =
      new IntentsTestRule<>(NewUserActivity.class, false, false);

  @Inject CurrentUser mockCurrentUser;
  @Inject UserRepository mockUserRepository;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
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
}
