package com.familycircleapp.ui.map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.familycircleapp.repository.LastKnownLocationRepository;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GoogleMapServiceImpl implements GoogleMapService {

  private final Map<String, LiveData<DeviceLocation>> mLocations = new HashMap<>();
  private final Map<String, Marker> mMarkers = new HashMap<>();
  private final Map<String, Circle> mCircles = new HashMap<>();

  private final LastKnownLocationRepository mLastKnownLocationRepository;

  private LifecycleOwner mLifecycleOwner;
  private GoogleMap mGoogleMap;
  private boolean mEnabled = false;
  private SingleShotContainer<UserCameraZoom> mUserCameraZoomCache = new SingleShotContainer<>(null);

  GoogleMapServiceImpl(final LastKnownLocationRepository lastKnownLocationRepository) {
    mLastKnownLocationRepository = lastKnownLocationRepository;
  }

  @Override
  public void setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner) {
    mLifecycleOwner = lifecycleOwner;
  }

  @Override
  public void putUsersOnMap(@NonNull final List<String> userIds) {
    final List<String> idsForDeleting = F.filter(mLocations.keySet(), id -> !userIds.contains(id));
    F.foreach(idsForDeleting, this::removeUserMarker);

    final List<String> newIds = F.filter(userIds, id -> !mLocations.containsKey(id));
    F.foreach(
        newIds,
        id -> putUserOnMap(id, Rx.liveData(mLastKnownLocationRepository.observeLastLocation(id)))
    );
  }

  @Override
  public void moveCameraToUser(@NonNull final String userId, final float zoom) {
    final Marker marker = mMarkers.get(userId);
    if (marker == null) {
      mUserCameraZoomCache = new SingleShotContainer<>(new UserCameraZoom(userId, zoom));
    } else {
      moveCamera(new UserCameraZoom(userId, zoom));
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
    if (!mEnabled) {
      return;
    }

    F.foreach(mLocations.entrySet(), this::startObserveDeviceLocation);
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void onStop() {
    F.foreach(F.map(mLocations.keySet(), mLocations::get), this::stopObserveDeviceLocation);
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void onDestroy() {
    removeAllUsers();
  }

  private void removeAllUsers() {
    mLocations.clear();

    F.foreach(mMarkers.values(), Marker::remove);
    mMarkers.clear();

    F.foreach(mCircles.values(), Circle::remove);
    mCircles.clear();
  }

  private void startObserveDeviceLocation(final Map.Entry<String, LiveData<DeviceLocation>> entry) {
    final String id = entry.getKey();
    final LiveData<DeviceLocation> liveData = entry.getValue();
    liveData.observe(mLifecycleOwner, deviceLocation -> updateUserMarker(id, deviceLocation));
  }

  private void stopObserveDeviceLocation(final LiveData<DeviceLocation> liveData) {
    liveData.removeObservers(mLifecycleOwner);
  }

  private void putUserOnMap(final String userId, final LiveData<DeviceLocation> deviceLocation) {
    if (mLocations.containsKey(userId)) {
      throw new IllegalStateException("Device location for user ID: " +
          userId + " is already on map");
    }

    mLocations.put(userId, deviceLocation);

    if (mEnabled &&
        mLifecycleOwner
            .getLifecycle()
            .getCurrentState()
            .isAtLeast(Lifecycle.State.STARTED)) {
      startObserveDeviceLocation(new AbstractMap.SimpleEntry<>(userId, deviceLocation));
    }
  }

  private void removeUserMarker(final String userId) {
    if (mLocations.containsKey(userId)) {
      stopObserveDeviceLocation(mLocations.get(userId));
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

    final UserCameraZoom userCameraZoom = mUserCameraZoomCache.getValue();
    if (userCameraZoom != null && userId.equals(userCameraZoom.mUserId)) {
      moveCamera(userCameraZoom);
    }
  }

  private void moveCamera(final UserCameraZoom userCameraZoom) {
    final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
        mMarkers.get(userCameraZoom.mUserId).getPosition(), userCameraZoom.zoom
    );
    mGoogleMap.moveCamera(cameraUpdate);
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

  private static class SingleShotContainer<T> {

    private T mValue;

    public SingleShotContainer(final T value) {
      mValue = value;
    }

    public T getValue() {
      final T value = mValue;
      mValue = null;
      return value;
    }
  }

  private static class UserCameraZoom {

    private final String mUserId;
    private final float zoom;

    private UserCameraZoom(final String userId, final float zoom) {
      mUserId = userId;
      this.zoom = zoom;
    }
  }
}
