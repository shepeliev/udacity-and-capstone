package com.familycircleapp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Consumer;

public interface CurrentUser {

  boolean isAuthenticated();

  @Nullable String getId();

  @Nullable String getDisplayName();

  void joinCircle(@NonNull final String circleId, @Nullable final Consumer<Throwable> onComplete);
}
