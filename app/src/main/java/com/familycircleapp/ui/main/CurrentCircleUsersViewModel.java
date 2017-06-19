package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.utils.F;

import java.util.List;

public class CurrentCircleUsersViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final CurrentCircleRepository mCurrentCircleRepository;
  private final UserRepository mUserRepository;

  private LiveData<List<LiveData<CircleUser>>> mUsersLiveData;

  public CurrentCircleUsersViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final CurrentCircleRepository currentCircleRepository,
      @NonNull final UserRepository userRepository
  ) {
    mCurrentUser = currentUser;
    mCurrentCircleRepository = currentCircleRepository;
    mUserRepository = userRepository;
  }

  public LiveData<List<LiveData<CircleUser>>> getUsers() {
    if (mUsersLiveData == null) {
      mUsersLiveData = createUsersLiveData();
    }

    return mUsersLiveData;
  }

  private LiveData<List<LiveData<CircleUser>>> createUsersLiveData() {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("Current user is not authenticated");
    }

    return Transformations.map(
        mCurrentCircleRepository.getCurrentCircle(userId),
        circle -> F.map(
            circle.getMembers().keySet(),
            id -> Transformations.map(mUserRepository.getUser(id), CircleUser::fromUser
            )
        )
    );
  }
}
