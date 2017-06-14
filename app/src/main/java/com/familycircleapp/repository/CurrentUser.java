package com.familycircleapp.repository;

import android.support.annotation.Nullable;

public interface CurrentUser {

  boolean isAuthenticated();

  @Nullable String getId();

  @Nullable String getDisplayName();
}
