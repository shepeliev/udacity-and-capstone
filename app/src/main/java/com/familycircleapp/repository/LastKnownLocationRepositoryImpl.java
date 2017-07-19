package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.utils.Rx;

class LastKnownLocationRepositoryImpl implements LastKnownLocationRepository {

  private static final String LAST_KNOWN_LOCATION_KEY = "lastKnownLocation";
  private final DatabaseReference mUsersReference;

  LastKnownLocationRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mUsersReference = firebaseDatabase.getReference(UserRepository.NAME);
  }

  @Override
  public LiveData<DeviceLocation> gtLastLocation(@NonNull final String userId) {
    final DatabaseReference reference = mUsersReference
        .child(userId)
        .child(LAST_KNOWN_LOCATION_KEY);
    return Rx.liveData(reference, DeviceLocation.class);
  }
}
