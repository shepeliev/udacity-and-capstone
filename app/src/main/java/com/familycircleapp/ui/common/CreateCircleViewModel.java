package com.familycircleapp.ui.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;

public class CreateCircleViewModel extends BackgroundTaskViewModel<String> {

  private final CurrentUser mCurrentUser;
  private final CircleRepository mCircleRepository;

  private String mCircleName;

  public CreateCircleViewModel(
      @NonNull final ErrorTextResolver errorTextResolver,
      @NonNull final CurrentUser currentUser,
      @NonNull final CircleRepository circleRepository
  ) {
    super(errorTextResolver);
    mCurrentUser = currentUser;
    mCircleRepository = circleRepository;
  }

  public void setCircleName(final String circleName) {
    mCircleName = circleName;
  }

  @Override
  protected void startTask() {
    if (TextUtils.isEmpty(mCircleName)) {
      throw new IllegalStateException("circle name shouldn't be empty");
    }

    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    mCircleRepository.createNewCircle(userId, mCircleName).subscribe(this::success, this::fail);
  }
}
