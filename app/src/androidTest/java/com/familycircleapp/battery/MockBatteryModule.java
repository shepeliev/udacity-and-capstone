package com.familycircleapp.battery;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockBatteryModule {

  @Provides
  @Singleton
  BatteryInfoListener provideBatteryInfoListener() {
    return mock(BatteryInfoListener.class);
  }
}
