package com.familycircleapp.battery;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

final class BatteryInfoListenerImpl implements BatteryInfoListener {

  private final Context mContext;
  private BroadcastReceiver mBroadcastReceiver;

  BatteryInfoListenerImpl(final Context context) {
    mContext = context;
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void start() {
      registerBroadcastReceiver();
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
