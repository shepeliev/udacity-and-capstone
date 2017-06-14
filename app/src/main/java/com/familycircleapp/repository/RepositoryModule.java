package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
  UserRepository provideUserRepository(final FirebaseDatabase firebaseDatabase) {
    return new UserRepositoryImpl(firebaseDatabase);
  }

  @Provides
  @Singleton
  CircleRepository provideCircleRepository(final FirebaseDatabase firebaseDatabase) {
    return new CircleRepositoryImpl(firebaseDatabase);
  }
}
