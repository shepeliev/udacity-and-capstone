package com.familycircleapp.ui.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.LastLocationRepository;
import com.familycircleapp.utils.F;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GoogleMapServiceImpl implements GoogleMapService {

  private final Map<String, LiveData<DeviceLocation>> mLocations = new HashMap<>();
  private final Map<String, Marker> mMarkers = new HashMap<>();
  private final Map<String, Circle> mCircles = new HashMap<>();

  private final LastLocationRepository mLastLocationRepository;

  private LifecycleOwner mLifecycleOwner;
  private GoogleMap mGoogleMap;
  private boolean mEnabled = false;

  GoogleMapServiceImpl(final LastLocationRepository lastLocationRepository) {
    mLastLocationRepository = lastLocationRepository;
  }

  @Override
  public void setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner) {
    mLifecycleOwner = lifecycleOwner;
  }

  @Override
  public void putUserIds(@NonNull final List<String> userIds) {
    cleanup(userIds);
    for (final String userId : userIds) {
      if (mLocations.containsKey(userId)) {
        continue;
      }

      putUserMarker(userId, mLastLocationRepository.gtLastLocation(userId));
    }
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    mGoogleMap = googleMap;
    mEnabled = true;
    if (mLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
      onStart();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void onStart() {
    if (mEnabled) {
      for (final String userId : mLocations.keySet()) {
        mLocations.get(userId).observe(mLifecycleOwner,
            deviceLocation -> updateUserMarker(userId, deviceLocation));
      }
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void onStop() {
    for (final String userId : mLocations.keySet()) {
      mLocations.get(userId).removeObservers(mLifecycleOwner);
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void onDestroy() {
    mLocations.clear();

    for (final Marker marker : mMarkers.values()) {
      marker.remove();
    }
    mMarkers.clear();

    for (final Circle circle : mCircles.values()) {
      circle.remove();
    }
    mCircles.clear();
  }

  private void cleanup(final Collection<String> userIds) {
    F.foreach(userIds, this::removeUserMarker);
    for (final String userId : mLocations.keySet()) {
      if (!userIds.contains(userId)) {
        removeUserMarker(userId);
      }
    }
  }

  private void putUserMarker(final String userId, final LiveData<DeviceLocation> deviceLocation) {
    if (mLocations.containsKey(userId)) {
      mLocations.get(userId).removeObservers(mLifecycleOwner);
    }

    mLocations.put(userId, deviceLocation);
    if (mEnabled
        && mLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
      deviceLocation.observe(mLifecycleOwner,
          newDeviceLocation -> updateUserMarker(userId, newDeviceLocation));
    }
  }

  private void removeUserMarker(final String userId) {
    if (mLocations.containsKey(userId)) {
      mLocations.get(userId).removeObservers(mLifecycleOwner);
      mLocations.remove(userId);
    }

    if (mMarkers.containsKey(userId)) {
      mMarkers.get(userId).remove();
      mMarkers.remove(userId);
    }

    if (mCircles.containsKey(userId)) {
      mCircles.get(userId).remove();
      mCircles.remove(userId);
    }
  }

  private void updateUserMarker(final String userId, final DeviceLocation deviceLocation) {
    if (mMarkers.containsKey(userId)) {
      moveMarker(mMarkers.get(userId), deviceLocation);
    } else {
      mMarkers.put(userId, createMarker(deviceLocation));
    }

    if (mCircles.containsKey(userId)) {
      moveCircle(mCircles.get(userId), deviceLocation);
    } else {
      mCircles.put(userId, createCircle(deviceLocation));
    }
  }

  private void moveCircle(final Circle circle, final DeviceLocation deviceLocation) {
    circle.setCenter(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()));
    circle.setRadius(deviceLocation.getAccuracy());
  }

  private void moveMarker(final Marker marker, final DeviceLocation deviceLocation) {
    marker.setPosition(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()));
  }

  private Circle createCircle(final DeviceLocation deviceLocation) {
    final CircleOptions options = new CircleOptions()
        .center(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()))
        .radius(deviceLocation.getAccuracy());
    return mGoogleMap.addCircle(options);
  }

  private Marker createMarker(final DeviceLocation deviceLocation) {
    final MarkerOptions options = new MarkerOptions()
        .position(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()));
    return mGoogleMap.addMarker(options);
  }
}
