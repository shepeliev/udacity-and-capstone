package com.familycircleapp.battery;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

final class BatteryInfoListenerImpl implements BatteryInfoListener, LifecycleObserver {

  private final Context mContext;
  private BroadcastReceiver mBroadcastReceiver;

  BatteryInfoListenerImpl(final Context context) {
    mContext = context;
  }

  @Override
  public void start(final Lifecycle lifecycle) {
    if (lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)
        && mBroadcastReceiver == null) {
      start();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void start() {
    mBroadcastReceiver = new BatteryInfoReceiver();
    mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void stop() {
    mContext.unregisterReceiver(mBroadcastReceiver);
    mBroadcastReceiver = null;
  }
}
