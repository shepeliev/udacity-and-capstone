package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface CurrentCircleRepository {

  @NonNull
  LiveData<Circle> getCurrentCircle(final @NonNull String userId);
}
