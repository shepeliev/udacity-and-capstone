package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class CircleUserViewModel extends ViewModel {

  private final UserRepository mUserRepository;
  private Map<String, LiveData<CircleUser>> mCircleUserLiveData;

  public CircleUserViewModel(@NonNull final UserRepository userRepository) {
    mUserRepository = userRepository;
  }

  @NonNull
  public LiveData<CircleUser> getCircleUser(@NonNull final String userId) {
    if (mCircleUserLiveData == null) {
      mCircleUserLiveData = new HashMap<>();
    }

    if (!mCircleUserLiveData.containsKey(userId)) {
      mCircleUserLiveData.put(userId, createCircleUserLiveData(userId));
    }

    return mCircleUserLiveData.get(userId);
  }

  private LiveData<CircleUser> createCircleUserLiveData(final String userId) {
    return Transformations.map(mUserRepository.getUserLiveData(userId), CircleUser::fromUser);
  }
}
