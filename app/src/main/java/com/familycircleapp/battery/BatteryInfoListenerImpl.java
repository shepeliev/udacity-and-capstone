package com.familycircleapp.battery;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

final class BatteryInfoListenerImpl implements BatteryInfoListener {

  private final Context mContext;
  private boolean mEnabled = false;
  private LifecycleOwner mLifecycleOwner;
  private BroadcastReceiver mBroadcastReceiver;

  BatteryInfoListenerImpl(final Context context) {
    mContext = context;
  }

  @Override
  public void setLifecycleOwner(final LifecycleOwner lifecycleOwner) {
    mLifecycleOwner = lifecycleOwner;
  }

  @Override
  public void enable() {
    mEnabled = true;
    if (mLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
      registerBroadcastReceiver();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void start() {
    if (mEnabled) {
      registerBroadcastReceiver();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void stop() {
    unregisterBroadcastReceiver();
  }

  private void registerBroadcastReceiver() {
    if (mBroadcastReceiver == null) {
      mBroadcastReceiver = new BatteryInfoReceiver();
      mContext.registerReceiver(
          mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
      );
    }
  }

  private void unregisterBroadcastReceiver() {
    if (mBroadcastReceiver != null) {
      mContext.unregisterReceiver(mBroadcastReceiver);
      mBroadcastReceiver = null;
    }
  }
}
