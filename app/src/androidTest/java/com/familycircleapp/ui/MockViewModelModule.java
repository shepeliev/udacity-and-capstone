package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public final class MockViewModelModule {

  @Provides
  @Singleton
  ViewModelProvider.Factory provideViewModelFactory() {
    return mock(ViewModelProvider.Factory.class);
  }
}
