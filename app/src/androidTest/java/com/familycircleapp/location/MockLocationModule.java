package com.familycircleapp.location;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockLocationModule {

  @Provides
  @Singleton
  LocationServiceManager provideLocationServiceManager() {
    return mock(LocationServiceManager.class);
  }
}
