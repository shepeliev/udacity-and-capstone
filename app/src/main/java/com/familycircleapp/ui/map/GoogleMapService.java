package com.familycircleapp.ui.map;

import com.google.android.gms.maps.OnMapReadyCallback;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import java.util.List;

public interface GoogleMapService extends OnMapReadyCallback, LifecycleObserver {

  void setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner);
  void putUsersOnMap(@NonNull final List<String> userIds);
  void moveCameraToUser(@NonNull final String userId, final float zoom);
}
