package com.familycircleapp.location;

import com.google.android.gms.location.FusedLocationProviderApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockLocationModule {

  @Provides
  @Singleton
  GoogleApiClientManager provideGoogleApiClientManager() {
    return mock(GoogleApiClientManager.class);
  }

  @Provides
  @Singleton
  FusedLocationProviderApi provideFusedLocationProviderApi() {
    return mock(FusedLocationProviderApi.class);
  }

  @Provides
  @Singleton
  LocationUpdatesManager provideLocationUpdatesManager() {
    return mock(LocationUpdatesManager.class);
  }
}
