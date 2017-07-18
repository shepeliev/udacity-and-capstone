package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

class CurrentCircleRepositoryImpl implements CurrentCircleRepository {

  private final UserRepository mUserRepository;
  private final CircleRepository mCircleRepository;

  CurrentCircleRepositoryImpl(
      final @NonNull UserRepository userRepository,
      final @NonNull CircleRepository circleRepository
  ) {
    mUserRepository = userRepository;
    mCircleRepository = circleRepository;
  }

  @Override
  @NonNull
  public LiveData<Circle> getCurrentCircle(final @NonNull String userId) {
    return Transformations.switchMap(
        mUserRepository.getCurrentCircleIdLiveData(userId),
        mCircleRepository::getCircleLiveData
    );
  }
}
