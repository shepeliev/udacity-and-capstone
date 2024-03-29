package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CircleRepository {

  String NAME = "circles";

  Observable<Circle> observeCircle(@NonNull final String id);

  Single<Circle> getCircle(@NonNull final String id);

  @NonNull
  Single<String> createNewCircle(final @NonNull String userId, final @NonNull String circleName);
}
