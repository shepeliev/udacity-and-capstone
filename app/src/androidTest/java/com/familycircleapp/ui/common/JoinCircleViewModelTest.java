package com.familycircleapp.ui.common;

import com.google.firebase.database.DatabaseException;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.repository.InviteRepository;
import com.familycircleapp.repository.NotFoundException;
import com.familycircleapp.testutils.LiveDataUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class JoinCircleViewModelTest {

  @Rule
  public UiThreadTestRule mUiThread = new UiThreadTestRule();

  @Mock private BackgroundTaskViewModel.ErrorTextResolver mockErrorTextResolver;
  @Mock private CurrentUser mockCurrentUser;
  @Mock private InviteRepository mockInviteRepository;
  private JoinCircleViewModel mModel;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mModel = new JoinCircleViewModel(mockErrorTextResolver, mockCurrentUser, mockInviteRepository);
    mModel.getInviteCode().set("XXXXYYYY");
  }

  @Test
  public void start_inviteCodeNotFound_shouldFail() throws Exception {
    when(mockErrorTextResolver.getErrorText(any(NotFoundException.class))).thenReturn("error");
    when(mockInviteRepository.get("XXXXYYYY")).thenReturn(Single.error(new NotFoundException()));

    mModel.start();

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_inviteCodeExpired_shouldFail() throws Exception {
    when(
        mockErrorTextResolver.getErrorText(
            any(JoinCircleErrorTextResolver.InviteCodeExpiredException.class)
        )).thenReturn("error");
    final Invite expiredInvite = new Invite();
    expiredInvite.setExpiration(System.currentTimeMillis() - 1);
    when(mockInviteRepository.get("XXXXYYYY")).thenReturn(Single.just(expiredInvite));

    mModel.start();

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_databaseError_shouldFail() throws Exception {
    when(mockErrorTextResolver.getErrorText(any(DatabaseException.class))).thenReturn("error");
    when(mockCurrentUser.joinCircle("circle_1"))
        .thenReturn(Single.error(new DatabaseException("error")));
    final Invite invite = new Invite();
    invite.setCircleId("circle_1");
    invite.setExpiration(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
    when(mockInviteRepository.get("XXXXYYYY")).thenReturn(Single.just(invite));

    mModel.start();

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_shouldSuccess() throws Throwable {
    when(mockCurrentUser.joinCircle("circle_1")).thenReturn(Single.just("circle_1"));
    final Invite invite = new Invite();
    invite.setCircleId("circle_1");
    invite.setExpiration(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
    when(mockInviteRepository.get("XXXXYYYY")).thenReturn(Single.just(invite));

    mModel.start();

    final String[] success = {null};
    mUiThread.runOnUiThread(() -> success[0] = LiveDataUtil.getValue(mModel.getResult()));

    assertEquals("circle_1", success[0]);
  }
}
