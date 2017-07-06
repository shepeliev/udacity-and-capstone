package com.familycircleapp.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.familycircleapp.utils.F;

import java.util.List;

import timber.log.Timber;

final class ChildKeysDatabaseReferenceLiveData extends LiveData<List<String>>
    implements ValueEventListener {

  @VisibleForTesting final DatabaseReference mDatabaseReference;

  ChildKeysDatabaseReferenceLiveData(@NonNull  final DatabaseReference databaseReference) {
    mDatabaseReference = databaseReference;
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
    final List<String> keys = F.map(dataSnapshot.getChildren(), DataSnapshot::getKey);
    setValue(keys);
  }

  @Override
  public void onCancelled(final DatabaseError databaseError) {
    Timber.e("Firebase database error", databaseError);
  }
}
