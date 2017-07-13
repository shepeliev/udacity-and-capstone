package com.familycircleapp.ui.newuser;

import android.support.annotation.StringRes;

import com.familycircleapp.utils.Consumer;

public class CreateJoinCircleResult {

  private final boolean mIsSuccess;
  private final int mErrorTextResourceId;

  private CreateJoinCircleResult(final boolean isSuccess, final int errorTextResourceId) {
    mIsSuccess = isSuccess;
    mErrorTextResourceId = errorTextResourceId;
  }

  public static CreateJoinCircleResult fail(@StringRes final int errorTextResourceId) {
    return new CreateJoinCircleResult(false, errorTextResourceId);
  }

  public static CreateJoinCircleResult success() {
    return new CreateJoinCircleResult(true, -1);
  }

  public void onSuccess(final Runnable onSuccess) {
    if (mIsSuccess) {
      onSuccess.run();
    }
  }

  public void onFail(final Consumer<Integer> onFail) {
    if (!mIsSuccess) {
      onFail.accept(mErrorTextResourceId);
    }
  }
}
