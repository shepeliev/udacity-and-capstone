package com.familycircleapp.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.utils.Rx;

public class UserViewModel extends ViewModel {

  private final UserRepository mUserRepository;
  private LiveData<User> mUser;

  public UserViewModel(@NonNull final UserRepository userRepository) {
    mUserRepository = userRepository;
  }

  public LiveData<User> getUser(@NonNull final String id) {
    if (mUser == null) {
      mUser = Rx.liveData(mUserRepository.getUser(id).toObservable());
    }

    return mUser;
  }
}
