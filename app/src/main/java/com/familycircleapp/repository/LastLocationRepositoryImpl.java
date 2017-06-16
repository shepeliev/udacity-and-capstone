package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

class LastLocationRepositoryImpl implements LastLocationRepository {

  private static final String LAST_KNOWN_LOCATION_KEY = "lastKnownLocation";
  private final FirebaseDatabase mFirebaseDatabase;

  LastLocationRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public LiveData<DeviceLocation> gtLastLocation(@NonNull final String userId) {
    final DatabaseReference reference = mFirebaseDatabase
        .getReference(UserRepository.NAME)
        .child(userId)
        .child(LAST_KNOWN_LOCATION_KEY);
    return new DatabaseReferenceLiveData<>(reference, DeviceLocation.class);
  }
}
