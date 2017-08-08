package com.familycircleapp.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.familycircleapp.repository.HasId;
import com.familycircleapp.repository.NotFoundException;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

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

  public static <T> Observable<T> observable(
      final @NonNull DatabaseReference reference, final @NonNull GenericTypeIndicator<T> type
  ) {
    return Observable.create(emitter -> {
      final ValueEventListener valueEventListener = reference.addValueEventListener(
          new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
              final Pair<Throwable, T> value = getValue(dataSnapshot, type);
              if (value.first == null) {
                emitter.onNext(value.second);
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

  public static <T> Single<T> single(
      @NonNull final DatabaseReference reference,
      @NonNull final GenericTypeIndicator<T> typeIndicator
  ) {
    return Single.create(emitter -> reference.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(final DataSnapshot dataSnapshot) {
            final Pair<Throwable, T> value = getValue(dataSnapshot, typeIndicator);
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

  private static <T> Pair<Throwable, T> getValue(
      final DataSnapshot dataSnapshot, final GenericTypeIndicator<T> typeIndicator
  ) {
    if (dataSnapshot.exists()) {
      final T value = dataSnapshot.getValue(typeIndicator);
      if (value instanceof HasId) {
        ((HasId) value).setId(dataSnapshot.getKey());
      }

      return new Pair<>(null, value);
    }

    return new Pair<>(new NotFoundException(dataSnapshot.getRef().toString()), null);
  }


  public static <T> LiveData<T> liveData(final @NonNull Observable<T> observable) {
    return new LiveData<T>() {

      private Disposable mDisposable;

      @Override
      protected void onActive() {
        mDisposable = observable.subscribe(this::postValue, Timber::e);
      }

      @Override
      protected void onInactive() {
        if (mDisposable != null) {
          mDisposable.dispose();
          mDisposable = null;
        }
      }
    };
  }

  public static <T> LiveData<T> liveData(
      final @NonNull DatabaseReference databaseReference, final @NonNull Class<T> clazz
  ) {
    return Rx.liveData(Rx.observable(databaseReference, clazz));
  }
}
