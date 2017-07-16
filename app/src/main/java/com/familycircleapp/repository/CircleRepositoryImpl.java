package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Consumer;

import java.util.HashMap;
import java.util.Map;

import static com.familycircleapp.repository.UserRepositoryImpl.CURRENT_CIRCLE_KEY;

final class CircleRepositoryImpl implements CircleRepository {

  static final String NAME_KEY = "name";
  static final String MEMBERS_KEY = "members";

  private final FirebaseDatabase mFirebaseDatabase;

  CircleRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mFirebaseDatabase = firebaseDatabase;
  }

  @Override
  public LiveData<Circle> getCircle(@NonNull final String id) {
    final DatabaseReference reference = mFirebaseDatabase
        .getReference(CircleRepository.NAME)
        .child(id);
    return new DatabaseReferenceLiveData<>(reference, Circle.class);
  }

  @Override
  public void createNewCircle(
      @NonNull final String userId,
      @NonNull final String circleName,
      @Nullable final Consumer<Throwable> onComplete
  ) {
    final DatabaseReference databaseReference = mFirebaseDatabase.getReference();
    final String circleId = databaseReference.child(CircleRepository.NAME).push().getKey();
    final Map<String, Object> update = new HashMap<String, Object>(){{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + NAME_KEY, circleName);
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
    }};

    databaseReference.updateChildren(update, (error, ref) -> {
      if (onComplete != null) {
        onComplete.accept(error != null ? error.toException() : null);
      }
    });
  }
}
