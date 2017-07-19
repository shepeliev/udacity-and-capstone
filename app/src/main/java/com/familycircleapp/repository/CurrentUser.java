package com.familycircleapp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Single;

public interface CurrentUser {

  boolean isAuthenticated();

  @Nullable
  String getId();

  @Nullable
  String getDisplayName();

  Single<String> joinCircle(final @NonNull String circleId);
}
