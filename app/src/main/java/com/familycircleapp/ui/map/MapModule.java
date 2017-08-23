package com.familycircleapp.ui.map;

import com.familycircleapp.repository.LastKnownLocationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public final class MapModule {

  @Provides
  GoogleMapService provideGoogleMapService(final LastKnownLocationRepository lastKnownLocationRepository) {
    return new GoogleMapServiceImpl(lastKnownLocationRepository);
  }
}
