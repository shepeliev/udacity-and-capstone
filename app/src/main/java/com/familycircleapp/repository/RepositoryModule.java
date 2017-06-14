package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class RepositoryModule {

  @Provides
  @Singleton
  CurrentUser provideCurrentUser(final FirebaseAuth firebaseAuth) {
    return new CurrentUserImpl(firebaseAuth);
  }

  @Provides
  @Singleton
  UserRepository provideUserRepository() {
    return new UserRepositoryImpl();
  }

  @Provides
  @Singleton
  CircleRepository provideCircleRepository() {
    return new CircleRepositoryImpl();
  }
}
