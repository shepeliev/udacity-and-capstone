package com.familycircleapp.ui.map;

import com.familycircleapp.repository.LastKnownLocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class MapModule {

  @Provides
  @Singleton
  GoogleMapService provideGoogleMapService(final LastKnownLocationRepository lastKnownLocationRepository) {
    return new GoogleMapServiceImpl(lastKnownLocationRepository);
  }
}
