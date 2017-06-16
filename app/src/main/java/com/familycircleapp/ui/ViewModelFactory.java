package com.familycircleapp.ui;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Provider;

final public class ViewModelFactory implements ViewModelProvider.Factory {

  private final Map<Class<?>, Provider<ViewModel>> mModels;

  public ViewModelFactory(final Map<Class<?>, Provider<ViewModel>> models) {
    this.mModels = models;
  }

  @Override
  public <T extends ViewModel> T create(final Class<T> modelClass) {
    //noinspection unchecked
    return (T) mModels.get(modelClass).get();
  }
}
