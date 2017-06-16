package com.familycircleapp.map;

import com.familycircleapp.ui.map.GoogleMapService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockMapModule {

  @Provides
  @Singleton
  GoogleMapService provideGoogleMapService() {
    return mock(GoogleMapService.class);
  }
}
