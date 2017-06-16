package com.familycircleapp.repository;

import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.F;

import static java.util.Arrays.asList;

final class DeviceLocationRepositoryImpl implements DeviceLocationRepository {

  private final FirebaseDatabase mFirebaseDatabase;

  DeviceLocationRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public void saveDeviceLocation(
      @NonNull final String userId, @NonNull final DeviceLocation deviceLocation
  ) {
    final String newLocationKey = mFirebaseDatabase
        .getReference(DeviceLocationRepository.NAME)
        .child(userId)
        .push()
        .getKey();

    mFirebaseDatabase.getReference().updateChildren(
        F.mapOf(asList(
            F.mapEntry(
                "/" + DeviceLocationRepository.NAME + "/" + userId + "/" + newLocationKey,
                deviceLocation
            ),
            F.mapEntry(
                "/" + UserRepository.NAME + "/" + userId + "/currentAddress",
                deviceLocation.getAddress()
            ),
            F.mapEntry(
                "/" + UserRepository.NAME + "/" + userId + "/lastKnownLocation",
                deviceLocation
            )
        ))
    );
  }
}
