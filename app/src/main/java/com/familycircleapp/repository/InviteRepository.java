package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;

import io.reactivex.Single;

public interface InviteRepository {

  @Deprecated
  void get(@NonNull final String id, @NonNull Consumer<Invite> onResult);

  Single<Invite> get(final @NonNull String id);
}
