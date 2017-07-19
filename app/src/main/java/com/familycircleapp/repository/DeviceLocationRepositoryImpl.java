package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Db;

import java.util.HashMap;

final class DeviceLocationRepositoryImpl implements DeviceLocationRepository {

  private final DatabaseReference mDatabaseReference;
  private final DatabaseReference mLocationsReference;

  DeviceLocationRepositoryImpl(final @NonNull FirebaseDatabase firebaseDatabase) {
    mDatabaseReference = firebaseDatabase.getReference();
    mLocationsReference = firebaseDatabase.getReference(DeviceLocationRepository.NAME);
  }

  @Override
  public void saveDeviceLocation(
      @NonNull final String userId, @NonNull final DeviceLocation deviceLocation
  ) {
    final String newLocationKey = mLocationsReference.child(userId).push().getKey();

    final HashMap<String, Object> update = new HashMap<String, Object>() {{
      put("/" + DeviceLocationRepository.NAME + "/" + userId + "/" + newLocationKey, deviceLocation);
      put("/" + UserRepository.NAME + "/" + userId + "/currentAddress", deviceLocation.getAddress());
      put("/" + UserRepository.NAME + "/" + userId + "/lastKnownLocation", deviceLocation);
    }};

    Db.updateChildren(mDatabaseReference, update).subscribe();
  }
}
