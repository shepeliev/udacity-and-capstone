package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import io.reactivex.Single;

public interface CircleRepository {

  String NAME = "circles";

  @NonNull
  LiveData<Circle> getCircleLiveData(final @NonNull String id);

  @NonNull
  Single<String> createNewCircle(final @NonNull String userId, final @NonNull String circleName);
}
