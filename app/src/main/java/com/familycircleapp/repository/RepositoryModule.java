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
  CurrentUser provideCurrentUser(
      final FirebaseAuth firebaseAuth, final FirebaseDatabase firebaseDatabase
  ) {
    return new CurrentUserImpl(firebaseAuth, firebaseDatabase);
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

  @Provides
  @Singleton
  DeviceLocationRepository provideDeviceLocationRepository(final FirebaseDatabase firebaseDatabase) {
    return new DeviceLocationRepositoryImpl(firebaseDatabase);
  }

  @Provides
  @Singleton
  LastKnownLocationRepository provideLastLocationRepository(final FirebaseDatabase firebaseDatabase) {
    return new LastKnownLocationRepositoryImpl(firebaseDatabase);
  }

  @Provides
  @Singleton
  CurrentCircleRepository provideCurrentCircleRepository(
      final CurrentUser currentUser,
      final UserRepository userRepository,
      final CircleRepository circleRepository
  ) {
    return new CurrentCircleRepositoryImpl(currentUser, userRepository, circleRepository);
  }

  @Provides
  @Singleton
  InviteRepository provideInviteRepository(final FirebaseDatabase firebaseDatabase) {
    return new InviteRepositoryImpl(firebaseDatabase);
  }
}
