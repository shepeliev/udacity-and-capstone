package com.familycircleapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import android.support.annotation.Nullable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class FirebaseModule {

  @Provides
  @Singleton
  public FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  @Nullable
  public UserInfo provideUserInfo(final FirebaseAuth firebaseAuth) {
    return firebaseAuth.getCurrentUser();
  }
}
