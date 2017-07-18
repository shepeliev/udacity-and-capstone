package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;
import com.familycircleapp.utils.Rx;

import io.reactivex.Single;

final class UserRepositoryImpl implements UserRepository {

  static final String CURRENT_CIRCLE_KEY = "currentCircle";
  static final String DISPLAY_NAME_KEY = "displayName";

  private final DatabaseReference mUsersReference;

  UserRepositoryImpl(final @NonNull FirebaseDatabase firebaseDatabase) {
    mUsersReference = firebaseDatabase.getReference(UserRepository.NAME);
  }

  @Override
  public LiveData<User> getUserLiveData(@NonNull final String id) {
    final DatabaseReference reference = mUsersReference.child(id);
    return Rx.liveData(reference, User.class);
  }

  @Override
  public Single<User> getUser(@NonNull final String id) {
    return Rx.single(mUsersReference.child(id), User.class);
  }

  @Override
  public LiveData<String> getCurrentCircleIdLiveData(@NonNull final String userId) {
    return Rx.liveData(
        mUsersReference.child(userId).child(CURRENT_CIRCLE_KEY),
        String.class
    );
  }

  @Override
  public void saveBatteryInfo(
      @NonNull final String userId, @NonNull final BatteryInfo batteryInfo
  ) {
    final DatabaseReference reference = mUsersReference.child(userId);
    reference.updateChildren(batteryInfo.asMap());
  }

  @Override
  public void saveDisplayName(@NonNull final String userId, @NonNull final String displayName) {
    mUsersReference.child(userId).child(DISPLAY_NAME_KEY).setValue(displayName);
  }
}
