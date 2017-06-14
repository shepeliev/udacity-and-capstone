package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface UserRepository {

  LiveData<User> getUser(@NonNull final String id);
}
