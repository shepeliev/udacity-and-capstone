package com.familycircleapp.mocks;

import com.familycircleapp.repository.CurrentUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
final class MockRepositoryModule {

  @Provides
  @Singleton
  CurrentUser provideCurrentUser() {
    return mock(CurrentUser.class);
  }
}
