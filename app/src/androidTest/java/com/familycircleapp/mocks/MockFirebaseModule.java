package com.familycircleapp.mocks;

import com.google.firebase.auth.FirebaseAuth;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
final class MockFirebaseModule {

  @Provides
  @Singleton
  FirebaseAuth provideFirebaseAuth() {
    return Mockito.mock(FirebaseAuth.class);
  }
}
