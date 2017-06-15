package com.familycircleapp;

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
}
