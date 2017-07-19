package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface LastKnownLocationRepository {

  LiveData<DeviceLocation> gtLastLocation(@NonNull final String userId);
}
