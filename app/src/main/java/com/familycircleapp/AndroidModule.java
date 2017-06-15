package com.familycircleapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

  @Provides
  @Singleton
  SharedPreferences provideSharedPreferences(final Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides
  @Singleton
  PermissionManager providePermissionManager(final Context context) {
    return new PermissionManagerImpl(context);
  }
}
