package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface LastLocationRepository {

  LiveData<DeviceLocation> gtLastLocation(@NonNull final String userId);
}
