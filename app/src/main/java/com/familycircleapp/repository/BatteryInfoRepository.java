package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import com.familycircleapp.battery.BatteryInfo;

public interface BatteryInfoRepository {

  void saveBatteryInfo(@NonNull final String userId, final BatteryInfo batteryInfo);
}
