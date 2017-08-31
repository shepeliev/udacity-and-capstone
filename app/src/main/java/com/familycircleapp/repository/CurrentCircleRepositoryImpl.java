package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;

class CurrentCircleRepositoryImpl implements CurrentCircleRepository {

  private final CurrentUser mCurrentUser;
  private final UserRepository mUserRepository;
  private final CircleRepository mCircleRepository;

  CurrentCircleRepositoryImpl(
      @NonNull final CurrentUser currentUser,
      @NonNull final UserRepository userRepository,
      @NonNull final CircleRepository circleRepository
  ) {
    mCurrentUser = currentUser;
    mUserRepository = userRepository;
    mCircleRepository = circleRepository;
  }

  @NonNull
  @Override
  public Observable<Circle> observeCurrentCircle() {
    if (mCurrentUser.getId() == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    return mUserRepository
        .observeCurrentCircleId(mCurrentUser.getId())
        .switchMap(mCircleRepository::observeCircle);
  }

  @NonNull
  @Override
  public Single<Circle> getCurrentCircle() {
    if (mCurrentUser.getId() == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    return mUserRepository
        .getUser(mCurrentUser.getId())
        .flatMap(user -> mCircleRepository.getCircle(user.getCurrentCircle()));
  }
}
