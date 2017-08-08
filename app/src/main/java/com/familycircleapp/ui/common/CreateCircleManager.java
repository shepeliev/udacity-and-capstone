package com.familycircleapp.ui.common;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.familycircleapp.utils.Consumer;

public class CreateCircleManager implements LifecycleObserver {

  private final CreateCircleViewModel mCreateCircleViewModel;

  public CreateCircleManager(
      final String defaultCircleName,
      @NonNull final FragmentActivity activity,
      @NonNull final ViewModelProvider.Factory viewModelFactory,
      @NonNull final Consumer<Boolean> onRun,
      @NonNull final Consumer<String> onSuccess,
      @NonNull final Consumer<String> onFail
      ) {
    if (activity instanceof LifecycleOwner) {
      mCreateCircleViewModel = ViewModelProviders
          .of(activity, viewModelFactory)
          .get(CreateCircleViewModel.class);


      final LifecycleOwner lifecycleOwner = (LifecycleOwner) activity;
      mCreateCircleViewModel.getRunningState().observe(lifecycleOwner, onRun::accept);
      mCreateCircleViewModel.getResult().observe(lifecycleOwner, onSuccess::accept);
      mCreateCircleViewModel.getError().observe(lifecycleOwner, onFail::accept);
      if (!TextUtils.isEmpty(defaultCircleName)) {
        mCreateCircleViewModel.setCircleName(defaultCircleName);
      }
    } else {
      throw new IllegalArgumentException("activity should be instance of LifecycleOwner");
    }
  }
}
