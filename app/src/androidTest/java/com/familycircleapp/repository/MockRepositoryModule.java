package com.familycircleapp.repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockRepositoryModule {

  @Provides
  @Singleton
  CurrentUser provideCurrentUser() {
    return mock(CurrentUser.class);
  }

  @Provides
  @Singleton
  UserRepository provideUserRepository() {
    return mock(UserRepository.class);
  }

  @Provides
  @Singleton
  DeviceLocationRepository provideDeviceLocationRepository() {
    return mock(DeviceLocationRepository.class);
  }

  @Provides
  @Singleton
  LastLocationRepository provideLastLocationRepository() {
    return mock(LastLocationRepository.class);
  }

  @Provides
  @Singleton
  CurrentCircleRepository provideCurrentCircleRepository() {
    return mock(CurrentCircleRepository.class);
  }

  @Provides
  @Singleton
  InviteRepository provideInviteRepository() {
    return mock(InviteRepository.class);
  }
}
