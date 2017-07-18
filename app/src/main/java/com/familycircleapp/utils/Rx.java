package com.familycircleapp.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.familycircleapp.repository.HasId;
import com.familycircleapp.repository.NotFoundException;

import io.reactivex.Observable;
import io.reactivex.Single;

public final class Rx {

  private Rx() {
    throw new UnsupportedOperationException();
  }

  public static <T> Observable<T> observable(
      final @NonNull DatabaseReference reference, final @NonNull Class<T> clazz
  ) {
    return Observable.create(emitter -> {
      final ValueEventListener valueEventListener = reference.addValueEventListener(
          new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
              final Pair<Throwable, T> value = getValue(dataSnapshot, clazz);
              if (value.first == null) {
                emitter.onNext(value.second);
              } else {
                emitter.onError(value.first);
              }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
              emitter.onError(databaseError.toException());
            }
          }
      );

      emitter.setCancellable(() -> reference.removeEventListener(valueEventListener));
    });
  }

  public static <T> Single<T> single(
      @NonNull final DatabaseReference reference, @NonNull final Class<T> clazz
  ) {
    return Single.create(emitter -> reference.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(final DataSnapshot dataSnapshot) {
            final Pair<Throwable, T> value = getValue(dataSnapshot, clazz);
            if (value.first == null) {
              emitter.onSuccess(value.second);
            } else {
              emitter.onError(value.first);
            }
          }

          @Override
          public void onCancelled(final DatabaseError databaseError) {
            emitter.onError(databaseError.toException());
          }
        }
    ));
  }

  private static <T> Pair<Throwable, T> getValue(
      final DataSnapshot dataSnapshot, final Class<T> clazz
  ) {
    if (dataSnapshot.exists()) {
      final T value = dataSnapshot.getValue(clazz);
      if (value instanceof HasId) {
        ((HasId) value).setId(dataSnapshot.getKey());
      }

      return new Pair<>(null, value);
    }

    return new Pair<>(new NotFoundException(dataSnapshot.getRef().toString()), null);
  }
}
