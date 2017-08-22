package com.familycircleapp;

import android.support.multidex.MultiDexApplication;

import com.evernote.android.job.JobManager;

import javax.inject.Inject;

import timber.log.Timber;

public class App extends MultiDexApplication {

  private static Component sAppComponent;

  @Inject JobManager mJobManager;

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

    getComponent().inject(this);
  }

  public Component buildAppComponent() {
    return DaggerAppComponent
        .builder()
        .androidModule(new AndroidModule(this))
        .build();
  }
}
