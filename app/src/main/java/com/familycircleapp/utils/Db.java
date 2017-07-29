package com.familycircleapp.utils;

import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.Single;

public final class Db {

  private Db() {
    throw new UnsupportedOperationException();
  }

  public static Single<Object> updateChildren(
      final @NonNull DatabaseReference databaseReference,
      final @NonNull Map<String, Object> update
  ) {

    return Single.create(
        emitter -> databaseReference.updateChildren(
            update,
            (error, reference) -> {
              if (error != null) {
                emitter.onError(error.toException());
              } else {
                emitter.onSuccess(new Object());
              }
            }
        )
    );
  }
}
