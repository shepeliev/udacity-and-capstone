package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;

import io.reactivex.Single;

public interface InviteRepository {

  String NAME = "invites";

  @Deprecated
  void get(@NonNull final String id, @NonNull Consumer<Invite> onResult);

  Single<Invite> get(final @NonNull String id);

  Single<Invite> saveInvite(@NonNull final Invite invite);
}
