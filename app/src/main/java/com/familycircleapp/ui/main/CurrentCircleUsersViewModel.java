package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.FuncUtils;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;

import java.util.List;

public class CurrentCircleUsersViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final CircleRepository mCircleRepository;
  private final UserRepository mUserRepository;

  public CurrentCircleUsersViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final CircleRepository circleRepository,
      @NonNull final UserRepository userRepository
  ) {
    mCurrentUser = currentUser;
    mCircleRepository = circleRepository;
    mUserRepository = userRepository;
  }

  public LiveData<List<LiveData<CircleUser>>> getUsers() {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("Current user is not authenticated");
    }

    final LiveData<User> userLiveData = mUserRepository.getUser(userId);
    final LiveData<Circle> circleLiveData = Transformations.switchMap(
        userLiveData,
        user -> mCircleRepository.getCircle(user.getCurrentCircle())
    );

    return Transformations.map(
        circleLiveData,
        circle -> FuncUtils.map(
            circle.getMembers().keySet(),
            id -> Transformations.map(mUserRepository.getUser(id), CircleUser::fromUser
            )
        )
    );
  }
}
