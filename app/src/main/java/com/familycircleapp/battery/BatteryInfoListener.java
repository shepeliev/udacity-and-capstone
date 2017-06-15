package com.familycircleapp.battery;

import android.arch.lifecycle.Lifecycle;

public interface BatteryInfoListener {

  void start(final Lifecycle lifecycle);
}
