package com.familycircleapp.location;

import com.google.android.gms.location.LocationResult;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.DeviceLocationRepository;

import javax.inject.Inject;

import timber.log.Timber;

public class UpdatedLocationIntentService extends IntentService {

  @Inject SharedPreferences mSharedPreferences;
  @Inject CurrentUser mCurrentUser;
  @Inject GeocoderService mGeocoderService;
  @Inject DeviceLocationRepository mLocationRepository;

  public UpdatedLocationIntentService() {
    super(UpdatedLocationIntentService.class.getSimpleName());
  }

  @Override
  public void onCreate() {
    super.onCreate();
    App.getComponent().inject(this);
  }

  @Override
  protected void onHandleIntent(@Nullable final Intent intent) {
    final String userId = mCurrentUser.getId();
    if (TextUtils.isEmpty(userId)) {
      Timber.wtf("User ID is null or empty");
      return;
    }

    if (!mSharedPreferences.getBoolean(getString(R.string.pref_share_location), true)) {
      Timber.w("Sharing location is disabled");
      return;
    }

    if (!LocationResult.hasResult(intent)) {
      Timber.w("Intent does not contains LocationResult");
      return;
    }

    final Location location = LocationResult.extractResult(intent).getLastLocation();
    if (location == null) {
      Timber.w("Location result is null");
      return;
    }

    String address = null;
    String geocoderError = null;
    try {
      address = mGeocoderService.fetchAddress(
          location.getLatitude(),
          location.getLongitude()
      );
    } catch (GeocoderServiceException e) {
      geocoderError = e.getLocalizedMessage();
    }

    final DeviceLocation deviceLocation = new DeviceLocation.Builder()
        .setTime(location.getTime())
        .setLatitude(location.getLatitude())
        .setLongitude(location.getLongitude())
        .setAccuracy((double) location.getAccuracy())
        .setAddress(address)
        .setGeocoderError(geocoderError)
        .build();

    mLocationRepository.saveDeviceLocation(userId, deviceLocation);
  }
}
