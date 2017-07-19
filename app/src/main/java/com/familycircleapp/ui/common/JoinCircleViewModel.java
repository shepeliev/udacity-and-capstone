package com.familycircleapp.ui.common;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.repository.InviteRepository;

public class JoinCircleViewModel extends BackgroundTaskViewModel<String> {

  private final ObservableField<String> mInviteCode = new ObservableField<>("");
  private final ObservableBoolean mIsEnabled = new ObservableBoolean(false);
  private final InviteRepository mInviteRepository;
  private final CurrentUser mCurrentUser;

  private final Observable.OnPropertyChangedCallback mInviteCodeChangeCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(final Observable observable, final int i) {
          mIsEnabled.set(!TextUtils.isEmpty(mInviteCode.get()));
        }
      };

  public JoinCircleViewModel(
      @NonNull final ErrorTextResolver errorTextResolver,
      @NonNull final CurrentUser currentUser,
      @NonNull final InviteRepository inviteRepository
  ) {
    super(errorTextResolver);
    mInviteRepository = inviteRepository;
    mCurrentUser = currentUser;

    mInviteCode.addOnPropertyChangedCallback(mInviteCodeChangeCallback);
  }

  public ObservableField<String> getInviteCode() {
    return mInviteCode;
  }

  public ObservableBoolean isEnabled() {
    return mIsEnabled;
  }

  @Override
  protected void startTask() {
    mInviteRepository.get(mInviteCode.get()).subscribe(this::handleInvite, this::fail);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    mInviteCode.removeOnPropertyChangedCallback(mInviteCodeChangeCallback);
  }

  private void handleInvite(final @NonNull Invite invite) {
    if (invite.getExpiration() < System.currentTimeMillis()) {
      fail(new JoinCircleErrorTextResolver.InviteCodeExpiredException());
    } else {
      mCurrentUser.joinCircle(invite.getCircleId()).subscribe(this::success, this::fail);
    }
  }
}
