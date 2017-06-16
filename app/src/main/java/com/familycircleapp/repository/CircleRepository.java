package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public interface CircleRepository {

  String NAME = "circles";

  LiveData<Circle> getCircle(@NonNull final String id);
}
