package com.familycircleapp.ui.newuser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class JoinCircleViewModel extends ViewModel {

  private final ObservableBoolean mIsLoaderScreenVisible = new ObservableBoolean(false);
  private final ObservableBoolean mButtonEnabled = new ObservableBoolean(true);
  private final ObservableField<String> mInviteCode = new ObservableField<>();

  private MutableLiveData<CreateJoinCircleResult> mCircleId;

  public void joinCircle() {
  }

  public MutableLiveData<CreateJoinCircleResult> getResult() {
    if (mCircleId == null) {
      mCircleId = new MutableLiveData<>();
    }

    return mCircleId;
  }

  public ObservableBoolean getIsLoaderScreenVisible() {
    return mIsLoaderScreenVisible;
  }

  public ObservableBoolean getButtonEnabled() {
    return mButtonEnabled;
  }

  public ObservableField<String> getInviteCode() {
    return mInviteCode;
  }

}
