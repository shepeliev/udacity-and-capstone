package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;
import com.familycircleapp.utils.Db;
import com.familycircleapp.utils.Rx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

class InviteRepositoryImpl implements InviteRepository {

  private static final String CIRCLE_ID_KEY = "circleId";
  private static final String EXPIRATION_KEY = "expiration";
  private static final String INVITE_CODE_KEY = "inviteCode";

  private final DatabaseReference mDatabaseReference;
  private final DatabaseReference mInvitesReference;

  InviteRepositoryImpl(final FirebaseDatabase firebaseDatabase) {
    mDatabaseReference = firebaseDatabase.getReference();
    mInvitesReference = firebaseDatabase.getReference(InviteRepository.NAME);
  }

  @Override
  public void get(@NonNull final String id, @NonNull final Consumer<Invite> onResult) {
    throw new UnsupportedOperationException("not implemented yet");
  }

  @Override
  public Single<Invite> get(@NonNull final String id) {
    return Rx.single(mInvitesReference.child(id), Invite.class);
  }

  @Override
  public Single<Invite> saveInvite(@NonNull final Invite invite) {
    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + invite.getCircleId() + "/" + INVITE_CODE_KEY, invite.getId());
      put("/" + InviteRepository.NAME + "/" + invite.getId() + "/" + CIRCLE_ID_KEY, invite.getCircleId());
      put("/" + InviteRepository.NAME + "/" + invite.getId() + "/" + EXPIRATION_KEY, invite.getExpiration());
    }};

    return Db.updateChildren(mDatabaseReference, update).map(o -> invite);
  }
}
