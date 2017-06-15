package com.familycircleapp.battery;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class BatteryModule {

  @Provides
  @Singleton
  BatteryInfoListener provideBatteryInfoListener(final Context context) {
    return new BatteryInfoListenerImpl(context);
  }
}
