package com.familycircleapp.ui.invite;

import android.support.annotation.NonNull;

import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.repository.InviteRepository;
import com.familycircleapp.ui.common.BackgroundTaskViewModel;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public final class InviteViewModel extends BackgroundTaskViewModel<Invite> {

  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final long MIN_VALIDITY_DAYS = 2;
  private static final long MAX_VALIDITY_DAYS = 7;
  private static final int INVITE_CODE_LENGTH = 8;
  private final CurrentCircleRepository mCurrentCircleRepository;
  private final InviteRepository mInviteRepository;

  public InviteViewModel(
      @NonNull final CurrentCircleRepository currentCircleRepository,
      @NonNull final InviteRepository inviteRepository
  ) {
    mCurrentCircleRepository = currentCircleRepository;
    mInviteRepository = inviteRepository;
  }

  @Override
  protected void startTask() {
    mCurrentCircleRepository
        .getCurrentCircle()
        .flatMap(this::getInviteForCircle)
        .subscribe(this::success, this::fail);
  }

  private Single<Invite> getInviteForCircle(final Circle circle) {
    return circle.getInviteCode() != null ?
        getFromExistentInvite(circle).onErrorResumeNext(getNewInvite(circle)) :
        getNewInvite(circle);
  }

  private Single<Invite> getFromExistentInvite(final Circle circle) {
    return mInviteRepository
        .get(circle.getInviteCode())
        .flatMap(
            invite -> isInviteValid(invite.getExpiration()) ? Single.just(invite) : getNewInvite(circle)
        );
  }

  private boolean isInviteValid(final long expiration) {
    return System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MIN_VALIDITY_DAYS) < expiration;
  }

  private Single<Invite> getNewInvite(final Circle circle) {
    final Invite invite = new Invite(
        randomString(INVITE_CODE_LENGTH),
        circle.getId(),
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_VALIDITY_DAYS)
    );
    return mInviteRepository.saveInvite(invite);
  }

  private String randomString(final int length) {
    final Random random = new Random();
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
    }
    return sb.toString();
  }
}
