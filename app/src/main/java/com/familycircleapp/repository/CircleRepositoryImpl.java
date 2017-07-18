package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Consumer;
import com.familycircleapp.utils.Rx;

import java.util.HashMap;
import java.util.Map;

import static com.familycircleapp.repository.UserRepositoryImpl.CURRENT_CIRCLE_KEY;

final class CircleRepositoryImpl implements CircleRepository {

  static final String NAME_KEY = "name";
  static final String MEMBERS_KEY = "members";

  private final DatabaseReference mDatabaseReference;
  private final DatabaseReference mCirclesReference;

  CircleRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mCirclesReference = firebaseDatabase.getReference(CircleRepository.NAME);
    mDatabaseReference = firebaseDatabase.getReference();
  }

  @Override
  public LiveData<Circle> getCircleLiveData(@NonNull final String id) {
    final DatabaseReference reference = mCirclesReference.child(id);
    return Rx.liveData(reference, Circle.class);
  }

  @Override
  public void createNewCircle(
      @NonNull final String userId,
      @NonNull final String circleName,
      @Nullable final Consumer<Throwable> onComplete
  ) {
    final String circleId = mCirclesReference.push().getKey();
    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + NAME_KEY, circleName);
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
    }};

    mDatabaseReference.updateChildren(update, (error, ref) -> {
      if (onComplete != null) {
        onComplete.accept(error != null ? error.toException() : null);
      }
    });
  }
}
