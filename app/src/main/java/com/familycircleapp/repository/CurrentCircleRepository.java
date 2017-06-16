package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface CurrentCircleRepository {

  LiveData<Circle> getCurrentCircle(@NonNull final String userId);
}
