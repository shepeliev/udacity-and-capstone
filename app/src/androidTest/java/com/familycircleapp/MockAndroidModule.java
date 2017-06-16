package com.familycircleapp;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockAndroidModule {

  @Provides
  @Singleton
  PermissionManager providePermissionManager() {
    return mock(PermissionManager.class);
  }

  @Provides
  @Singleton
  SharedPreferences provideShatredPreferences() {
    return mock(SharedPreferences.class);
  }
}
