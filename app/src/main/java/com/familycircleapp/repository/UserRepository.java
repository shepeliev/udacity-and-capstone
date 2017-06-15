package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;

public interface UserRepository {

  LiveData<User> getUser(@NonNull final String id);

  void saveBatteryInfo(@NonNull final String userId, @NonNull final BatteryInfo batteryInfo);
}
