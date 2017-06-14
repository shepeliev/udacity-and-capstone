package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

class UserRepositoryImpl implements UserRepository {

  private static final String ENTITY_NAME = "users";
  private final FirebaseDatabase mFirebaseDatabase;

  UserRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public LiveData<User> getUser(@NonNull final String id) {
    final DatabaseReference reference = mFirebaseDatabase.getReference(ENTITY_NAME).child(id);
    return new DatabaseReferenceLiveData<>(reference, User.class);
  }
}
