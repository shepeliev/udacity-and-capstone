package com.familycircleapp.ui.map;

import android.content.Context;

import com.familycircleapp.repository.LastKnownLocationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public final class MapModule {

  @Provides
  GoogleMapService provideGoogleMapService(
      final Context context,
      final LastKnownLocationRepository lastKnownLocationRepository
  ) {
    return new GoogleMapServiceImpl(context, lastKnownLocationRepository);
  }
}
