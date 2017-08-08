package com.familycircleapp.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.utils.Rx;

public final class CurrentCircleNameViewModel extends ViewModel {

  private final CurrentCircleRepository mCurrentCircleRepository;
  private LiveData<String> mName;
  private LiveData<Circle> mCircle;

  public CurrentCircleNameViewModel(
      @NonNull final CurrentCircleRepository currentCircleRepository
  ) {
    mCurrentCircleRepository = currentCircleRepository;
  }

  public LiveData<String> getCircleName() {
    if (mName == null) {
      mName = loadCircleName();
    }

    return mName;
  }

  public LiveData<Circle> getCircle() {
    if (mCircle == null) {
      mCircle = Rx.liveData(mCurrentCircleRepository.observeCurrentCircle());
    }

    return mCircle;
  }

  private LiveData<String> loadCircleName() {
    return Transformations.map(
        Rx.liveData(mCurrentCircleRepository.observeCurrentCircle()),
        Circle::getName
    );
  }
}
