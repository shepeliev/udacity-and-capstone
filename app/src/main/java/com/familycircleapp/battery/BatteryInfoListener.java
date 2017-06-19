package com.familycircleapp.battery;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;

public interface BatteryInfoListener  extends LifecycleObserver {

  void setLifecycleOwner(final LifecycleOwner lifecycleOwner);
  void enable();
}
