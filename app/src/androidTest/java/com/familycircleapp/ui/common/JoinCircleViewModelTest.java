package com.familycircleapp.ui.common;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.repository.InviteRepository;
import com.familycircleapp.testutils.LiveDataUtil;
import com.familycircleapp.utils.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
    mModel = new JoinCircleViewModel(
        mockErrorTextResolver, mockCurrentUser, mockInviteRepository
    );
    mModel.getInviteCode().set("XXXXYYYY");
  }

  @Test
  public void start_inviteCodeNotFound_shouldFail() throws Exception {
    when(
        mockErrorTextResolver.getErrorText(
            any(JoinCircleErrorTextResolver.InviteCodeNotFound.class))
    ).thenReturn("error");
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Invite>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockInviteRepository).get(eq("XXXXYYYY"), onResult.capture());
    onResult.getValue().accept(null);

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_inviteCodeExpired_shouldFail() throws Exception {
    when(
        mockErrorTextResolver.getErrorText(
            any(JoinCircleErrorTextResolver.InviteCodeExpired.class)
        )).thenReturn("error");
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Invite>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockInviteRepository).get(eq("XXXXYYYY"), onResult.capture());
    final Invite expiredInvite = new Invite();
    expiredInvite.setExpiration(System.currentTimeMillis() - 1);
    onResult.getValue().accept(expiredInvite);

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_databaseError_shouldFail() throws Exception {
    when(mockErrorTextResolver.getErrorText(any(DatabaseException.class))).thenReturn("error");
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Invite>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockInviteRepository).get(eq("XXXXYYYY"), onResult.capture());
    final Invite invite = new Invite();
    invite.setCircleId("circle_1");
    invite.setExpiration(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
    onResult.getValue().accept(invite);

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Throwable>> onComplete = ArgumentCaptor.forClass(Consumer.class);
    verify(mockCurrentUser).joinCircle(eq("circle_1"), onComplete.capture());
    onComplete.getValue().accept(DatabaseError.zzqv(DatabaseError.PERMISSION_DENIED).toException());

    assertEquals("error", mModel.getErrorText().get());
  }

  @Test
  public void start_shouldSuccess() throws Throwable {
    mModel.start();

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Invite>> onResult = ArgumentCaptor.forClass(Consumer.class);
    verify(mockInviteRepository).get(eq("XXXXYYYY"), onResult.capture());
    final Invite invite = new Invite();
    invite.setCircleId("circle_1");
    invite.setExpiration(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
    onResult.getValue().accept(invite);

    //noinspection unchecked
    final ArgumentCaptor<Consumer<Throwable>> onComplete = ArgumentCaptor.forClass(Consumer.class);
    verify(mockCurrentUser).joinCircle(eq("circle_1"), onComplete.capture());
    onComplete.getValue().accept(null);

    final String[] success = {null};
    mUiThread.runOnUiThread(() -> success[0] = LiveDataUtil.getValue(mModel.getResult()));

    assertEquals("circle_1", success[0]);
  }
}
