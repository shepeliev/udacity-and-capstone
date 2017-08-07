package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.utils.F;

import java.util.List;

public class CurrentCircleUserIdsViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final CurrentCircleRepository mCurrentCircleRepository;

  private LiveData<List<String>> mUserIdsLiveData;

  public CurrentCircleUserIdsViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final CurrentCircleRepository currentCircleRepository) {
    mCurrentUser = currentUser;
    mCurrentCircleRepository = currentCircleRepository;
  }

  public LiveData<List<String>> getUserIds() {
    if (mUserIdsLiveData == null) {
      mUserIdsLiveData = createUserIdsLiveData();
    }

    return mUserIdsLiveData;
  }

  private LiveData<List<String>> createUserIdsLiveData() {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("Current user is not authenticated");
    }

    return Transformations.map(
        mCurrentCircleRepository.getCurrentCircleLiveData(userId),
        circle -> F.map(circle.getMembers().keySet(), id -> id)
    );
  }
}
