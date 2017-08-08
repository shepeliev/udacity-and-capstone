package com.familycircleapp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CurrentUser {

  boolean isAuthenticated();

  @Nullable
  String getId();

  @Nullable
  String getDisplayName();

  Single<String> joinCircle(final @NonNull String circleId);

  Observable<List<String>> observeCirclesList();
}
