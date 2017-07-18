package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;

import io.reactivex.Single;

public interface UserRepository {

  String NAME = "users";

  LiveData<User> getUserLiveData(final @NonNull String id);

  Single<User> getUser(final @NonNull String id);

  LiveData<String> getCurrentCircleIdLiveData(final @NonNull String userId);

  void saveBatteryInfo(final @NonNull String userId, final @NonNull BatteryInfo batteryInfo);

  void saveDisplayName(final @NonNull String userId, final @NonNull String displayName);
}
