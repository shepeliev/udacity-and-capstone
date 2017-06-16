package com.familycircleapp.ui.map;

import com.google.android.gms.maps.OnMapReadyCallback;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import java.util.List;

public interface GoogleMapService extends OnMapReadyCallback {

  void setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner);
  void putUserIds(@NonNull final List<String> userIds);
}
