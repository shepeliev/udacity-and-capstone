package com.familycircleapp.location;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.familycircleapp.PermissionManager;
import com.familycircleapp.R;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

class LocationUpdatesManagerImpl implements LocationUpdatesManager {

  private final Context mContext;
  private final PermissionManager mPermissionManager;
  private final GoogleApiClientManager mGoogleApiClientManager;
  private final FusedLocationProviderApi mFusedLocationProviderApi;
  private final SharedPreferences mSharedPreferences;

  LocationUpdatesManagerImpl(
      @NonNull final Context context,
      @NonNull final PermissionManager permissionManager,
      @NonNull final GoogleApiClientManager googleApiClientManager,
      @NonNull final FusedLocationProviderApi fusedLocationProviderApi,
      @NonNull final SharedPreferences sharedPreferences
  ) {
    mContext = context;
    mPermissionManager = permissionManager;
    mGoogleApiClientManager = googleApiClientManager;
    mFusedLocationProviderApi = fusedLocationProviderApi;
    mSharedPreferences = sharedPreferences;
  }

  @Override
  public void startLocationUpdates() {
    mGoogleApiClientManager.connect(this::requestLocationUpdates);
  }

  @Override
  public void startLocationUpdates(@NonNull final FragmentActivity activity) {
    mGoogleApiClientManager.connect(activity, this::requestLocationUpdates);
  }

  @Override
  public void stopLocationUpdates(@NonNull final FragmentActivity activity) {
    mGoogleApiClientManager.connect(activity, this::removeLocationUpdates);
  }

  private void removeLocationUpdates() {
    mFusedLocationProviderApi.removeLocationUpdates(
        mGoogleApiClientManager.getGoogleApiClient(), getPendingIntent()
    );
    mGoogleApiClientManager.disconnect();
  }

  private void requestLocationUpdates() {
    final int intervalMinutes = mSharedPreferences.getInt(
        mContext.getString(R.string.pref_update_interval),
        mContext.getResources().getInteger(R.integer.default_update_interval_minutes)
    );
    final LocationRequest request = new LocationRequest()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(TimeUnit.MINUTES.toMillis(intervalMinutes));

    mPermissionManager.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION, isGranted -> {
      if (isGranted) {
        mFusedLocationProviderApi.requestLocationUpdates(
            mGoogleApiClientManager.getGoogleApiClient(), request, getPendingIntent()
        );
      } else {
        Timber.w(Manifest.permission.ACCESS_FINE_LOCATION + " is not granted");
      }
    });

    mGoogleApiClientManager.disconnect();

    Timber.i(request.toString());
  }

  private PendingIntent getPendingIntent() {
    final Intent intent = new Intent(mContext, UpdatedLocationIntentService.class);
    return PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
