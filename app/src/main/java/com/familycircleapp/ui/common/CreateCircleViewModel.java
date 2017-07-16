package com.familycircleapp.ui.common;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;

public class CreateCircleViewModel extends BackgroundTaskViewModel<Boolean> {

  private final ObservableField<String> mCircleName = new ObservableField<>("");

  private final CurrentUser mCurrentUser;
  private final CircleRepository mCircleRepository;

  public CreateCircleViewModel(
      @NonNull final ErrorTextResolver errorTextResolver,
      @NonNull final CurrentUser currentUser,
      @NonNull final CircleRepository circleRepository
  ) {
    super(errorTextResolver);
    mCurrentUser = currentUser;
    mCircleRepository = circleRepository;
  }

  public ObservableField<String> getCircleName() {
    return mCircleName;
  }

  @Override
  protected void startTask() {
    final String userId = mCurrentUser.getId();
    assert userId != null;
    mCircleRepository.createNewCircle(userId, mCircleName.get(), error -> {
      if (error != null) {
        fail(error);
      } else {
        success(true);
      }
    });
  }
}
