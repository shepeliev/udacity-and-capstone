package com.familycircleapp.ui.map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.familycircleapp.R;
import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.LastKnownLocationRepository;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GoogleMapServiceImpl implements GoogleMapService {

  private final Map<UserModel, LiveData<DeviceLocation>> mLocations = new HashMap<>();
  private final Map<UserModel, Marker> mMarkers = new HashMap<>();
  private final Map<UserModel, Circle> mCircles = new HashMap<>();

  private final Context mContext;
  private final LastKnownLocationRepository mLastKnownLocationRepository;

  private LifecycleOwner mLifecycleOwner;
  private GoogleMap mGoogleMap;
  private boolean mEnabled = false;
  private SingleShotContainer<UserCameraZoom> mUserCameraZoomCache = new SingleShotContainer<>(null);

  GoogleMapServiceImpl(
      @NonNull final Context context,
      @NonNull final LastKnownLocationRepository lastKnownLocationRepository
  ) {
    mContext = context;
    mLastKnownLocationRepository = lastKnownLocationRepository;
  }

  @Override
  public void setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner) {
    mLifecycleOwner = lifecycleOwner;
  }

  @Override
  public void putUsersOnMap(@NonNull final List<UserModel> users) {
    final List<UserModel> idsForDeleting = F.filter(
        mLocations.keySet(), user -> !users.contains(user)
    );
    F.foreach(idsForDeleting, this::removeUserMarker);

    final List<UserModel> newUsers = F.filter(users, id -> !mLocations.containsKey(id));
    F.foreach(
        newUsers,
        user -> putUserOnMap(
            user, Rx.liveData(mLastKnownLocationRepository.observeLastLocation(user.getId()))
        )
    );
  }

  @Override
  public void moveCameraToUser(@NonNull final UserModel user, final float zoom) {
    final Marker marker = mMarkers.get(user);
    if (marker == null) {
      mUserCameraZoomCache = new SingleShotContainer<>(new UserCameraZoom(user, zoom));
    } else {
      moveCamera(new UserCameraZoom(user, zoom));
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

  private void startObserveDeviceLocation(final Map.Entry<UserModel, LiveData<DeviceLocation>> entry) {
    final UserModel user = entry.getKey();
    final LiveData<DeviceLocation> liveData = entry.getValue();
    liveData.observe(mLifecycleOwner, deviceLocation -> updateUserMarker(user, deviceLocation));
  }

  private void stopObserveDeviceLocation(final LiveData<DeviceLocation> liveData) {
    liveData.removeObservers(mLifecycleOwner);
  }

  private void putUserOnMap(final UserModel user, final LiveData<DeviceLocation> deviceLocation) {
    if (mLocations.containsKey(user)) {
      throw new IllegalStateException("Device location for user ID: " +
          user + " is already on map");
    }

    mLocations.put(user, deviceLocation);

    if (mEnabled &&
        mLifecycleOwner
            .getLifecycle()
            .getCurrentState()
            .isAtLeast(Lifecycle.State.STARTED)) {
      startObserveDeviceLocation(new AbstractMap.SimpleEntry<>(user, deviceLocation));
    }
  }

  private void removeUserMarker(final UserModel user) {
    if (mLocations.containsKey(user)) {
      mLocations.get(user).removeObservers(mLifecycleOwner);
      mLocations.remove(user);
    }

    if (mMarkers.containsKey(user)) {
      mMarkers.get(user).remove();
      mMarkers.remove(user);
    }

    if (mCircles.containsKey(user)) {
      mCircles.get(user).remove();
      mCircles.remove(user);
    }
  }

  private void updateUserMarker(final UserModel user, final DeviceLocation deviceLocation) {
    if (mMarkers.containsKey(user)) {
      moveMarker(mMarkers.get(user), deviceLocation);
    } else {
      mMarkers.put(user, createMarker(deviceLocation, user));
    }

    if (mCircles.containsKey(user)) {
      moveCircle(mCircles.get(user), deviceLocation);
    } else {
      mCircles.put(user, createCircle(deviceLocation));
    }

    final UserCameraZoom userCameraZoom = mUserCameraZoomCache.getValue();
    if (userCameraZoom != null && user.equals(userCameraZoom.mUser)) {
      moveCamera(userCameraZoom);
    }
  }

  private void moveCamera(final UserCameraZoom userCameraZoom) {
    final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
        mMarkers.get(userCameraZoom.mUser).getPosition(), userCameraZoom.mZoom
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
    final int color = mContext.getResources().getColor(R.color.accuracyCircleColor);
    final CircleOptions options = new CircleOptions()
        .fillColor(color)
        .strokeWidth(0)
        .center(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()))
        .radius(deviceLocation.getAccuracy());
    return mGoogleMap.addCircle(options);
  }

  private Marker createMarker(final DeviceLocation deviceLocation, final UserModel user) {
    final MarkerOptions options = new MarkerOptions()
        .position(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude()))
        .icon(getAvatarIcon(user));
    return mGoogleMap.addMarker(options);
  }

  private BitmapDescriptor getAvatarIcon(final UserModel user) {
    final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    final IconGenerator iconGenerator = new IconGenerator(mContext);
    final int color = colorGenerator.getColor(user.getDisplayName());
    iconGenerator.setColor(color);
    iconGenerator.setTextAppearance(
        android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Subhead_Inverse
    );

    return BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(user.getDisplayName()));
  }

  private static class SingleShotContainer<T> {

    private T mValue;

    SingleShotContainer(final T value) {
      mValue = value;
    }

    public T getValue() {
      final T value = mValue;
      mValue = null;
      return value;
    }
  }

  private static class UserCameraZoom {

    private final UserModel mUser;
    private final float mZoom;

    private UserCameraZoom(final UserModel user, final float zoom) {
      mUser = user;
      mZoom = zoom;
    }
  }

}
