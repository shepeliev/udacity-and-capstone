package com.familycircleapp.repository;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;

public interface InviteRepository {

  void get(@NonNull final String id, @NonNull Consumer<Invite> onResult);
}
