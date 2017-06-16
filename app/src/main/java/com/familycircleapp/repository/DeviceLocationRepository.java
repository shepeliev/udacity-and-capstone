package com.familycircleapp.repository;

import android.support.annotation.NonNull;

public interface DeviceLocationRepository {

  void saveDeviceLocation(
      @NonNull final String userId, @NonNull final DeviceLocation deviceLocation
  );
}
