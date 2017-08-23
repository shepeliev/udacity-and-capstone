package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;

public interface DeviceLocationRepository {

  String NAME = "locations";

  void saveDeviceLocation(
      @NonNull final String userId, @NonNull final DeviceLocation deviceLocation
  );

  Single<List<DeviceLocation>> getAllLocations(@NonNull final String userId);
}
