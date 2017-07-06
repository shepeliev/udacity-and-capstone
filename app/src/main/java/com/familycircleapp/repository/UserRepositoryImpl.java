package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;

final class UserRepositoryImpl implements UserRepository {

  static final String CURRENT_CIRCLE_KEY = "currentCircle";
  private final FirebaseDatabase mFirebaseDatabase;

  UserRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public LiveData<User> getUser(@NonNull final String id) {
    final DatabaseReference reference = mFirebaseDatabase
        .getReference(UserRepository.NAME)
        .child(id);
    return new DatabaseReferenceLiveData<>(reference, User.class);
  }

  @Override
  public LiveData<String> getCurrentCircleId(@NonNull final String userId) {
    final DatabaseReference currentCircleRef = mFirebaseDatabase
        .getReference(UserRepository.NAME)
        .child(userId)
        .child(CURRENT_CIRCLE_KEY);
    return new DatabaseReferenceLiveData<>(currentCircleRef, String.class);
  }

  @Override
  public void saveBatteryInfo(
      @NonNull final String userId, @NonNull final BatteryInfo batteryInfo
  ) {
    final DatabaseReference reference = mFirebaseDatabase
        .getReference(UserRepository.NAME)
        .child(userId);
    reference.updateChildren(batteryInfo.asMap());
  }
}
