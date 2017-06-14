package com.familycircleapp.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.arch.lifecycle.LiveData;

import timber.log.Timber;

final class DatabaseReferenceLiveData<T> extends LiveData<T>
    implements ValueEventListener {

  private final DatabaseReference mDatabaseReference;
  private final Class<T> mClass;

  DatabaseReferenceLiveData(
      final DatabaseReference databaseReference,
      final Class<T> aClass
  ) {
    mDatabaseReference = databaseReference;
    mClass = aClass;
  }

  @Override
  protected void onActive() {
    mDatabaseReference.addValueEventListener(this);
  }

  @Override
  protected void onInactive() {
    mDatabaseReference.removeEventListener(this);
  }

  @Override
  public void onDataChange(final DataSnapshot dataSnapshot) {
    setValue(dataSnapshot.getValue(mClass));
  }

  @Override
  public void onCancelled(final DatabaseError databaseError) {
    Timber.e("Firebase database error", databaseError);
  }

}
