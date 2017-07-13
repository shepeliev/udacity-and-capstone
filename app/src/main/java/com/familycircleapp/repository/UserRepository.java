package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;

public interface UserRepository {

  String NAME = "users";

  LiveData<User> getUser(@NonNull final String id);

  LiveData<String> getCurrentCircleId(@NonNull final String userId);

  void saveBatteryInfo(@NonNull final String userId, @NonNull final BatteryInfo batteryInfo);

  void saveDisplayName(@NonNull String userId, @NonNull String displayName);
}
