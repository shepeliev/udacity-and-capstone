package com.familycircleapp.ui.common;

import com.google.firebase.database.DatabaseException;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.testutils.LiveDataUtil;
import com.familycircleapp.utils.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CreateCircleViewModelTest {

  @Rule
  public UiThreadTestRule mUiThread = new UiThreadTestRule();

  @Mock private BackgroundTaskViewModel.ErrorTextResolver mockErrorTextResolver;
  @Mock private CurrentUser mockCurrentUser;
  @Mock private CircleRepository mockCircleRepository;
  private CreateCircleViewModel mModel;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mModel = new CreateCircleViewModel(
        mockErrorTextResolver, mockCurrentUser, mockCircleRepository
    );
    mModel.getCircleName().set("Family");

    when(mockCurrentUser.getId()).thenReturn("user_1");
  }

  @Test
  public void start_databaseError_shouldFail() throws Exception {
    when(mockErrorTextResolver.getErrorText(any(DatabaseException.class))).thenReturn("error");
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Throwable>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockCircleRepository)
        .createNewCircle(eq("user_1"), eq("Family"), onResult.capture());
    onResult.getValue().accept(new DatabaseException(""));

    assertEquals("error", mModel.getErrorText().get());
  }


  @Test
  public void start_shouldSuccess() throws Throwable {
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Throwable>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockCircleRepository)
        .createNewCircle(eq("user_1"), eq("Family"), onResult.capture());
    onResult.getValue().accept(null);

    final boolean[] success = {false};
    mUiThread.runOnUiThread(() -> success[0] = LiveDataUtil.getValue(mModel.getResult()));

    assertTrue(success[0]);
  }
}
