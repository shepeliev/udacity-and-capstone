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
}
