package com.familycircleapp.repository;

import android.support.annotation.NonNull;

public interface HasId {

  void setId(@NonNull final String id);

  String getId();
}
