package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

class UserRepositoryImpl implements UserRepository {
  @Override
  public LiveData<User> getUser(@NonNull final String id) {
    return null;
  }
}
