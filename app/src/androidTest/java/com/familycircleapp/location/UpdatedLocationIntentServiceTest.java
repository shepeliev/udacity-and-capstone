package com.familycircleapp.location;

import com.google.android.gms.location.LocationResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.R;
import com.familycircleapp.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.DeviceLocationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class UpdatedLocationIntentServiceTest {

  private static final String EXTRA_LOCATION_RESULT = "com.google.android.gms.location.EXTRA_LOCATION_RESULT";
  private static final double LATITUDE = 34;
  private static final double LONGITUDE = 43;
  private static final double ACCURACY = 20;


  @Inject SharedPreferences mockSharedPreferences;
  @Inject CurrentUser mockCurrentUser;
  @Inject GeocoderService mockGeocoderService;
  @Inject DeviceLocationRepository mockDeviceLocationRepository;

  private Context mContext;
  private String mShareLocationPrefKey;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
    mContext = InstrumentationRegistry.getTargetContext();
    mShareLocationPrefKey = mContext.getString(R.string.pref_share_location);

    Mockito.reset(mockGeocoderService, mockDeviceLocationRepository);

    when(mockCurrentUser.getId()).thenReturn("user_1");
    when(mockGeocoderService.fetchAddress(
        LATITUDE, LONGITUDE, UpdatedLocationIntentService.MAX_ADDRESS_LINES
    )).thenReturn("address");
  }

  @Test
  public void onHandleIntent_shouldSaveLocation() throws Exception {
    final long time = System.currentTimeMillis();
    final Intent intent = getLocationResultIntent(time);
    when(mockSharedPreferences.getBoolean(mShareLocationPrefKey, true)).thenReturn(true);

    InstrumentationRegistry.getTargetContext().startService(intent);
    TimeUnit.SECONDS.sleep(2);

    final DeviceLocation expectedDeviceLocation = new DeviceLocation.Builder()
        .setTime(time)
        .setLatitude(LATITUDE)
        .setLongitude(LONGITUDE)
        .setAccuracy(ACCURACY)
        .setAddress("address")
        .build();
    verify(mockDeviceLocationRepository).saveDeviceLocation("user_1", expectedDeviceLocation);
  }

  @Test
  public void onHandleIntent_sharingLocationDisabled_doNothing() throws Exception {
    final long time = System.currentTimeMillis();
    final Intent intent = getLocationResultIntent(time);
    when(mockSharedPreferences.getBoolean(mShareLocationPrefKey, true)).thenReturn(false);

    InstrumentationRegistry.getTargetContext().startService(intent);
    TimeUnit.SECONDS.sleep(2);

    verify(mockGeocoderService, never()).fetchAddress(anyDouble(), anyDouble(), anyInt());
    verify(mockDeviceLocationRepository, never())
        .saveDeviceLocation(anyString(), any(DeviceLocation.class));
  }

  @Test
  public void onHandleIntent_noLocationResultInIntent_doNothing() throws Exception {
    final Intent intent = new Intent(mContext, UpdatedLocationIntentService.class);
    when(mockSharedPreferences.getBoolean(mShareLocationPrefKey, true)).thenReturn(true);

    InstrumentationRegistry.getTargetContext().startService(intent);
    TimeUnit.SECONDS.sleep(2);

    verify(mockGeocoderService, never()).fetchAddress(anyDouble(), anyDouble(), anyInt());
    verify(mockDeviceLocationRepository, never())
        .saveDeviceLocation(anyString(), any(DeviceLocation.class));
  }

  @Test
  public void onHandleIntent_userIdNull_doNothing() throws Exception {
    final long time = System.currentTimeMillis();
    final Intent intent = getLocationResultIntent(time);
    when(mockCurrentUser.getId()).thenReturn(null);
    when(mockSharedPreferences.getBoolean(mShareLocationPrefKey, true)).thenReturn(true);

    InstrumentationRegistry.getTargetContext().startService(intent);
    TimeUnit.SECONDS.sleep(2);

    verify(mockGeocoderService, never()).fetchAddress(anyDouble(), anyDouble(), anyInt());
    verify(mockDeviceLocationRepository, never())
        .saveDeviceLocation(anyString(), any(DeviceLocation.class));
  }

  @NonNull
  private Intent getLocationResultIntent(final long time) {
    final Location location = new Location("test");
    location.setTime(time);
    location.setLatitude(LATITUDE);
    location.setLongitude(LONGITUDE);
    location.setAccuracy((float) ACCURACY);
    final LocationResult locationResult = LocationResult.create(Collections.singletonList(location));
    final Intent intent = new Intent(
        InstrumentationRegistry.getTargetContext(), UpdatedLocationIntentService.class
    );
    intent.putExtra(EXTRA_LOCATION_RESULT, locationResult);
    return intent;
  }
}
