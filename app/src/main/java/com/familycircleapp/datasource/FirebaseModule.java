package com.familycircleapp.datasource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
  @Singleton
  public FirebaseDatabase provideFirebaseDatabase() {
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    firebaseDatabase.setPersistenceEnabled(true);
    return firebaseDatabase;
  }
}
