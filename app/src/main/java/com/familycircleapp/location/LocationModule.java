package com.familycircleapp.location;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class LocationModule {

  @Provides
  @Singleton
  LocationServiceManager provideLocationServiceManager() {
    return new LocationServiceManagerImpl();
  }
}
