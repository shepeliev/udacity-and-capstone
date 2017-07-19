package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;

import io.reactivex.Single;

class InviteRepositoryImpl implements InviteRepository {
  @Override
  public void get(@NonNull final String id, @NonNull final Consumer<Invite> onResult) {
    throw new UnsupportedOperationException("not implemented yet");
  }

  @Override
  public Single<Invite> get(final @NonNull String id) {
    return null;
  }
}
