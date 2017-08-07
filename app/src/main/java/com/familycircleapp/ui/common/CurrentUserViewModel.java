package com.familycircleapp.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;

public final class CurrentUserViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final UserRepository mUserRepository;
  private LiveData<User> mUser;

  public CurrentUserViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final UserRepository userRepository
  ) {
    mCurrentUser = currentUser;
    mUserRepository = userRepository;
  }

  public LiveData<User> getUser() {
    if (mUser == null) {
      mUser = loadCurrentUser();
    }

    return mUser;
  }

  private LiveData<User> loadCurrentUser() {
    if (mCurrentUser.getId() == null) {
      throw new IllegalStateException("user should be authenticated");
    }

    return mUserRepository.getUserLiveData(mCurrentUser.getId());
  }
}
