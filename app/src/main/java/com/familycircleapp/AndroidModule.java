package com.familycircleapp;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AndroidModule {

  private final Context mContext;

  public AndroidModule(final Context context) {
    mContext = context;
  }

  @Provides
  @Singleton
  Context provideContext() {
    return mContext;
  }
}
