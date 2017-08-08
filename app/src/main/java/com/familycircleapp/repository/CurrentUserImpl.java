package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Db;
import com.familycircleapp.utils.Rx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.familycircleapp.repository.CircleRepositoryImpl.MEMBERS_KEY;
import static com.familycircleapp.repository.UserRepositoryImpl.CURRENT_CIRCLE_KEY;

final class CurrentUserImpl implements CurrentUser {

  private final FirebaseAuth mFirebaseAuth;
  private final DatabaseReference mDatabaseReference;

  public CurrentUserImpl(
      @NonNull final FirebaseAuth firebaseAuth,
      @NonNull final FirebaseDatabase firebaseDatabase
  ) {
    mFirebaseAuth = firebaseAuth;
    mDatabaseReference = firebaseDatabase.getReference();
  }

  @Override
  public boolean isAuthenticated() {
    return mFirebaseAuth.getCurrentUser() != null;
  }

  @Nullable
  @Override
  public String getId() {
    final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    return firebaseUser != null ? firebaseUser.getUid() : null;
  }

  @Nullable
  @Override
  public String getDisplayName() {
    final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    return firebaseUser != null ? firebaseUser.getDisplayName() : null;
  }

  @Override
  public Single<String> joinCircle(final @NonNull String circleId) {
    final String userId = getId();
    if (userId == null) {
      throw new IllegalStateException("Current user has to be authenticated");
    }

    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CircleRepository.NAME + "/" + circleId,
          true);
    }};

    return Db.updateChildren(mDatabaseReference, update).map(o -> circleId);
  }

  @Override
  public Observable<List<String>> observeCirclesList() {
    final String userId = getId();
    if (userId == null) {
      throw new IllegalStateException("Current user has to be authenticated");
    }

    final GenericTypeIndicator<Map<String, Boolean>> type =
        new GenericTypeIndicator<Map<String, Boolean>>() {
        };

    final DatabaseReference circlesRef = mDatabaseReference
        .child(UserRepository.NAME)
        .child(userId)
        .child(CircleRepository.NAME);
    return Rx.observable(circlesRef, type).map(map -> new ArrayList<>(map.keySet()));
  }
}
