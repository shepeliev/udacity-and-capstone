package com.familycircleapp.ui.newuser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class CreateCircleViewModel extends ViewModel {

  private final ObservableBoolean mIsLoaderScreenVisible = new ObservableBoolean(false);
  private final ObservableBoolean mButtonEnabled = new ObservableBoolean(true);
  private final ObservableField<String> mCircleName = new ObservableField<>();

  private MutableLiveData<CreateJoinCircleResult> mResult;

  public void createCircle() {
  }

  public MutableLiveData<CreateJoinCircleResult> getResult() {
    if (mResult == null) {
      mResult = new MutableLiveData<>();
    }

    return mResult;
  }

  public ObservableBoolean getIsLoaderScreenVisible() {
    return mIsLoaderScreenVisible;
  }

  public ObservableBoolean getButtonEnabled() {
    return mButtonEnabled;
  }

  public ObservableField<String> getCircleName() {
    return mCircleName;
  }
}
