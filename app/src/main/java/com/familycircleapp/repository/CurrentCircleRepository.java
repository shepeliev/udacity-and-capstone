package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CurrentCircleRepository {

  @NonNull
  Observable<Circle> observeCurrentCircle();

  @NonNull
  Single<Circle> getCurrentCircle();
}
