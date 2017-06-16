package com.familycircleapp.ui.map;

import com.familycircleapp.repository.LastLocationRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class MapModule {

  @Provides
  @Singleton
  GoogleMapService provideGoogleMapService(final LastLocationRepository lastLocationRepository) {
    return new GoogleMapServiceImpl(lastLocationRepository);
  }
}
