package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.Nullable;

final class CurrentUserImpl implements CurrentUser {

  private final FirebaseAuth mFirebaseAuth;

  public CurrentUserImpl(final FirebaseAuth firebaseAuth) {
    mFirebaseAuth = firebaseAuth;
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
}
