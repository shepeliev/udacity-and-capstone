package com.familycircleapp.location;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

import android.content.Context;
import android.content.SharedPreferences;

import com.familycircleapp.PermissionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class LocationModule {

  @Provides
  @Singleton
  GoogleApiClientManager provideGoogleApiClientManager(final Context context) {
    return new GoogleApiClientManagerImpl(context);
  }

  @Provides
  @Singleton
  FusedLocationProviderApi provideFusedLocationProviderApi() {
    return LocationServices.FusedLocationApi;
  }

  @Provides
  @Singleton
  LocationUpdatesManager provideLocationUpdatesManager(
      final Context context,
      final PermissionManager permissionManager,
      final GoogleApiClientManager googleApiClientManager,
      final FusedLocationProviderApi fusedLocationProviderApi,
      final SharedPreferences sharedPreferences
  ) {
    return new LocationUpdatesManagerImpl(
        context,
        permissionManager,
        googleApiClientManager,
        fusedLocationProviderApi,
        sharedPreferences
    );
  }

  @Provides
  @Singleton
  GeocoderService provdeGocoderService() {
    return new GeocoderServiceImpl();
  }
}
