package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Consumer;

public interface CircleRepository {

  String NAME = "circles";

  LiveData<Circle> getCircle(@NonNull final String id);

  void createNewCircle(
      @NonNull final String userId,
      @NonNull final String circleName,
      @Nullable final Consumer<Throwable> onComplete
      );
}
