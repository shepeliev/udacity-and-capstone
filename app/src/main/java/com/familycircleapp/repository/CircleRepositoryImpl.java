package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.familycircleapp.utils.Db;
import com.familycircleapp.utils.Rx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

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
  @NonNull
  public LiveData<Circle> getCircleLiveData(final @NonNull String id) {
    final DatabaseReference reference = mCirclesReference.child(id);
    return Rx.liveData(reference, Circle.class);
  }

  @Override
  public Observable<Circle> observeCircle(@NonNull final String id) {
    return Rx.observable(mCirclesReference.child(id), Circle.class);
  }

  @Override
  public Single<Circle> getCircle(@NonNull final String id) {
    return Rx.single(mCirclesReference.child(id), Circle.class);
  }

  @NonNull
  @Override
  public Single<String> createNewCircle(
      final @NonNull String userId, final @NonNull String circleName
  ) {
    final String circleId = mCirclesReference.push().getKey();
    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + NAME_KEY, circleName);
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
    }};

    return Db.updateChildren(mDatabaseReference, update).map(o -> circleId);
  }
}
