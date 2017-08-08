package com.familycircleapp.ui.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.repository.InviteRepository;

public class JoinCircleViewModel extends BackgroundTaskViewModel<String> {

  private final InviteRepository mInviteRepository;
  private final CurrentUser mCurrentUser;

  private String mInviteCode;

  public JoinCircleViewModel(
      @NonNull final ErrorTextResolver errorTextResolver,
      @NonNull final CurrentUser currentUser,
      @NonNull final InviteRepository inviteRepository
  ) {
    super(errorTextResolver);
    mInviteRepository = inviteRepository;
    mCurrentUser = currentUser;

  }

  @Override
  protected void startTask() {
    if (TextUtils.isEmpty(mInviteCode)) {
      throw new IllegalStateException("invite code should not be empty");
    }

    final String normalizedInviteCode = mInviteCode
        .replace("-", "")
        .toUpperCase();
    mInviteRepository.get(normalizedInviteCode).subscribe(this::handleInvite, this::fail);
  }

  public void setInviteCode(final String inviteCode) {
    mInviteCode = inviteCode;
  }

  private void handleInvite(final @NonNull Invite invite) {
    if (invite.getExpiration() < System.currentTimeMillis()) {
      fail(new JoinCircleErrorTextResolver.InviteCodeExpiredException());
    } else {
      mCurrentUser.joinCircle(invite.getCircleId()).subscribe(this::success, this::fail);
    }
  }
}
