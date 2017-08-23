package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface LastKnownLocationRepository {

  Observable<DeviceLocation> observeLastLocation(@NonNull final String userId);
}
