package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

final class CircleRepositoryImpl implements CircleRepository {
  @Override
  public LiveData<Circle> getCircle(@NonNull final String id) {
    return null;
  }
}
