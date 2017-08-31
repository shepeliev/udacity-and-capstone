package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import io.reactivex.Single;

public interface InviteRepository {

  String NAME = "invites";

  Single<Invite> get(final @NonNull String id);

  Single<Invite> saveInvite(@NonNull final Invite invite);
}
