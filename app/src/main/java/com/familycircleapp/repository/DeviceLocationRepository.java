package com.familycircleapp.repository;

import android.support.annotation.NonNull;

public interface DeviceLocationRepository {

  String NAME = "locations";

  void saveDeviceLocation(
      @NonNull final String userId, @NonNull final DeviceLocation deviceLocation
  );
}
