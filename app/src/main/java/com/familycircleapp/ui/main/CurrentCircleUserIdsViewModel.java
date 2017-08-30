package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.List;

import io.reactivex.Observable;

public class CurrentCircleUserIdsViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final CurrentCircleRepository mCurrentCircleRepository;
  private final UserRepository mUserRepository;

  private LiveData<List<User>> mUserIdsLiveData;

  public CurrentCircleUserIdsViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final CurrentCircleRepository currentCircleRepository,
      @NonNull final UserRepository userRepository
  ) {
    mCurrentUser = currentUser;
    mCurrentCircleRepository = currentCircleRepository;
    mUserRepository = userRepository;
  }

  public LiveData<List<User>> getUserIds() {
    if (mUserIdsLiveData == null) {
      mUserIdsLiveData = createUserIdsLiveData();
    }

    return mUserIdsLiveData;
  }

  private LiveData<List<User>> createUserIdsLiveData() {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("Current user is not authenticated");
    }

    final Observable<List<User>> observable = mCurrentCircleRepository.observeCurrentCircle()
        .map(circle -> F.map(circle.getMembers().keySet(), id -> id))
        .flatMap(ids ->
          Observable.fromIterable(ids)
              .flatMap(id -> mUserRepository.getUser(id).toObservable())
              .toList()
              .toObservable()
        );

    return Rx.liveData(observable);
  }
}
