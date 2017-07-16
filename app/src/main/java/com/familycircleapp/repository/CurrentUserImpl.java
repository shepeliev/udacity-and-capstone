package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Consumer;

import java.util.HashMap;
import java.util.Map;

import static com.familycircleapp.repository.CircleRepositoryImpl.MEMBERS_KEY;
import static com.familycircleapp.repository.UserRepositoryImpl.CURRENT_CIRCLE_KEY;

final class CurrentUserImpl implements CurrentUser {

  private final FirebaseAuth mFirebaseAuth;
  private final FirebaseDatabase mFirebaseDatabase;

  public CurrentUserImpl(
      @NonNull final FirebaseAuth firebaseAuth,
      @NonNull final FirebaseDatabase firebaseDatabase
  ) {
    mFirebaseAuth = firebaseAuth;
    mFirebaseDatabase = firebaseDatabase;
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
  public void joinCircle(
      @NonNull final String circleId,
      @Nullable final Consumer<Throwable> onComplete
  ) {
    final String userId = getId();
    assert userId != null;


    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
    }};

    mFirebaseDatabase.getReference().updateChildren(update, (databaseError, databaseReference) -> {
      if (onComplete != null) {
        onComplete.accept(databaseError != null ? databaseError.toException() : null);
      }
    });
  }
}
