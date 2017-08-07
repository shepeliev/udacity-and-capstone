package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CurrentCircleRepository {

  @NonNull
  @Deprecated
  LiveData<Circle> getCurrentCircleLiveData(final @NonNull String userId);

  @NonNull
  Observable<Circle> observeCurrentCircle();

  @NonNull
  Single<Circle> getCurrentCircle();
}
