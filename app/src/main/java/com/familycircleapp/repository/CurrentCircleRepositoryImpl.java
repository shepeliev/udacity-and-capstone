package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

class CurrentCircleRepositoryImpl implements CurrentCircleRepository {

  private final UserRepository mUserRepository;
  private final CircleRepository mCircleRepository;

  CurrentCircleRepositoryImpl(
      @NonNull final UserRepository userRepository,
      @NonNull final CircleRepository circleRepository) {
    mUserRepository = userRepository;
    mCircleRepository = circleRepository;
  }

  @Override
  public LiveData<Circle> getCurrentCircle(@NonNull final String userId) {
    final LiveData<User> userLiveData = mUserRepository.getUser(userId);
    return Transformations.switchMap(userLiveData,
        user -> mCircleRepository.getCircle(user.getCurrentCircle()));
  }
}
