package com.familycircleapp;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {

  private static Component sAppComponent;

  public static Component getComponent() {
    if (sAppComponent == null) {
      throw new IllegalStateException("The application has not been created yet.");
    }

    return sAppComponent;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    sAppComponent = buildAppComponent();
  }

  public Component buildAppComponent() {
    return DaggerAppComponent
        .builder()
        .androidModule(new AndroidModule(this))
        .build();
  }
}
