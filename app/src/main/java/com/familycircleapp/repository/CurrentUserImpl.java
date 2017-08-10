package com.familycircleapp.repository;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.familycircleapp.utils.Db;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.familycircleapp.repository.CircleRepositoryImpl.MEMBERS_KEY;
import static com.familycircleapp.repository.UserRepositoryImpl.CURRENT_CIRCLE_KEY;
import static java.util.Collections.emptySet;

final class CurrentUserImpl implements CurrentUser {

  private final FirebaseAuth mFirebaseAuth;
  private final DatabaseReference mDatabaseReference;
  private final UserRepository mUserRepository;

  public CurrentUserImpl(
      @NonNull final FirebaseAuth firebaseAuth,
      @NonNull final FirebaseDatabase firebaseDatabase,
      @NonNull final UserRepository userRepository
  ) {
    mFirebaseAuth = firebaseAuth;
    mDatabaseReference = firebaseDatabase.getReference();
    mUserRepository = userRepository;
  }

  @Override
  public boolean isAuthenticated() {
    return mFirebaseAuth.getCurrentUser() != null;
  }

  @Nullable
  @Override
  public String getId() {
    final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    return firebaseUser != null ? firebaseUser.getUid() : null;
  }

  @Nullable
  @Override
  public String getDisplayName() {
    final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    return firebaseUser != null ? firebaseUser.getDisplayName() : null;
  }

  @Override
  public Single<String> joinCircle(final @NonNull String circleId) {
    final String userId = getId();
    if (userId == null) {
      throw new IllegalStateException("Current user has to be authenticated");
    }

    final Map<String, Object> update = new HashMap<String, Object>() {{
      put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, true);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, circleId);
      put("/" + UserRepository.NAME + "/" + userId + "/" + CircleRepository.NAME + "/" + circleId,
          true);
    }};

    return Db.updateChildren(mDatabaseReference, update).map(o -> circleId);
  }

  @Override
  public Single<Object> leaveCurrentCircle(@NonNull final String newCircleId) {
    final String userId = getId();
    if (userId == null) {
      throw new IllegalStateException("Current user has to be authenticated");
    }

    return mUserRepository.getUser(userId)
        .flatMap(user -> {
          final HashMap<String, Object> update = new HashMap<String, Object>() {{
            final String circleId = user.getCurrentCircle();
            put("/" + CircleRepository.NAME + "/" + circleId + "/" + MEMBERS_KEY + "/" + userId, null);
            put("/" + UserRepository.NAME + "/" + userId + "/" + CircleRepository.NAME + "/" + circleId, null);
            put("/" + UserRepository.NAME + "/" + userId + "/" + CURRENT_CIRCLE_KEY, newCircleId);
          }};

          return Db.updateChildren(mDatabaseReference, update);
        });
  }

  @Override
  public Observable<List<String>> observeCirclesList() {
    final String userId = getId();
    if (userId == null) {
      throw new IllegalStateException("Current user has to be authenticated");
    }

    final GenericTypeIndicator<Map<String, Boolean>> type =
        new GenericTypeIndicator<Map<String, Boolean>>() {
        };

    final DatabaseReference circlesRef = mDatabaseReference
        .child(UserRepository.NAME)
        .child(userId)
        .child(CircleRepository.NAME);
    return Rx.observable(circlesRef, type).map(map -> new ArrayList<>(map.keySet()));
  }

  @Override
  public Single<Object> deleteAccount(@NonNull final String password) {
    //noinspection ConstantConditions
    return reauthenticate(password)
        .flatMap(aVoid -> mUserRepository.getUser(getId()))
        .map(user -> F.fold(
            user.getCircles() != null ? user.getCircles().keySet() : emptySet(),
            new HashMap<String, Object>() {{
              put("/" + UserRepository.NAME + "/" + user.getId(), null);
            }},
            (map, id) -> {
              map.put("/" + CircleRepository.NAME + "/" + id + "/" + MEMBERS_KEY + "/" + user.getId(), null);
              return map;
            }
        ))
        .flatMap(update -> Db.updateChildren(mDatabaseReference, update));
  }

  private Single<Object> reauthenticate(final String password) {
    final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    if (firebaseUser == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    //noinspection ConstantConditions
    final AuthCredential credential =
        EmailAuthProvider.getCredential(firebaseUser.getEmail(), password);

    return Single.create(
        emitter -> firebaseUser
            .reauthenticate(credential)
            .addOnSuccessListener(aVoid -> emitter.onSuccess(new Object()))
            .addOnFailureListener(emitter::onError)
    );
  }
}
