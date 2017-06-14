package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface CircleRepository {

  LiveData<Circle> getCircle(@NonNull final String id);
}
