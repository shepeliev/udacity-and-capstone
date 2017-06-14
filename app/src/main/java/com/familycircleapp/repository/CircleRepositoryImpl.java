package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

final class CircleRepositoryImpl implements CircleRepository {

  private static final String ENTITY_NAME = "circles";
  private final FirebaseDatabase mFirebaseDatabase;

  CircleRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public LiveData<Circle> getCircle(@NonNull final String id) {
    final DatabaseReference reference = mFirebaseDatabase.getReference(ENTITY_NAME).child(id);
    return new DatabaseReferenceLiveData<>(reference, Circle.class);
  }
}
