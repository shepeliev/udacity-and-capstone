package com.familycircleapp.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v7.app.AppCompatActivity;

public abstract class AppCompatLifecycleActivity extends AppCompatActivity
    implements LifecycleRegistryOwner {

  private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

  @Override
  public LifecycleRegistry getLifecycle() {
    return mLifecycleRegistry;
  }
}
