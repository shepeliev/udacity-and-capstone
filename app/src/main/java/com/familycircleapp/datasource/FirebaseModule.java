package com.familycircleapp.datasource;

import com.google.firebase.auth.FirebaseAuth;

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
}
